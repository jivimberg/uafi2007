package usb;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import control.CommandsManager;

/**
 *
 * Clase que controla las transferencia y procesamiento de información desde y hacia el PIC.
 *
 */
public class USBTransmitter {

	private static final String PVID_PID = "p04D8_0204"; //Microchip's VID & PID.
	private static final String PEP_OUT = "\\MCHP_EP1";
	private static final String PEP_IN = "\\MCHP_EP2";

	private static final int INSTANCE = 1;

	private MPUSBApi mpUSBApi;
	
	private boolean channel1On;
	private boolean channel2On;
	private boolean adcOn;

	private Pointer ep1Handler;
	private Pointer ep2Handler;

	private long lastTimestamp;
	private volatile int frequence;
	
	/**
	 * Crea una nueva instancia de la clase, adquiriendo el dll necesario para interactuar
	 * por usb con el PIC.
	 */
	public USBTransmitter() {
		mpUSBApi = MPUSBApi.INSTANCE;
	}

	/**
	 * Este método verifica que el dispositivo se encuentra conectado, y de ser así inicializa los handles
	 * para transferir información a travez los endpoints del mismo.
	 *
	 * @throws NoDevicesFoundException si el dispositivo no se encuentra conectado.
	 */
	public void initDevice() throws NoDevicesFoundException {
		int devicesCount = mpUSBApi._MPUSBGetDeviceCount(PVID_PID.toCharArray());
		if(devicesCount == 0){
			throw new NoDevicesFoundException();
		}
		ep1Handler = mpUSBApi._MPUSBOpen(INSTANCE, PVID_PID, PEP_OUT,MPUSBApi.MP_WRITE, 0);
		ep2Handler = mpUSBApi._MPUSBOpen(INSTANCE, PVID_PID, PEP_IN, MPUSBApi.MP_READ, 0);
	}

	/**
	 * Cierra el endpoint pasado como parametro.
	 */
	public void closeDevicePort(Pointer ep) {
		mpUSBApi._MPUSBClose(ep);
	}
	
	/**
	 * Retorna la frecuencia a la que el ADC esta muestreando actualmente.
	 */
	public int calculateFreq() {
		return frequence;
	}

	/**
	 * IMPORTANTE: Este método bloquea hasta que nuevas muestras sean recibidas desde el PIC.
	 * 
	 * El método aloca un espacio en la memoria para recibir las muestras del PIC, y solicita
	 * a la dll la lectura de la información proveniente del EP2. 
	 * Una vez con las muestras en memoria, se verifica que sea una cantidad par de bytes y se
	 * procede a procesarlos en el siguiente formato:
	 * AAAAAAAA|AABBBBBC
	 * (i)		A (0-9):	Muestra de 10 bytes obtenida por el ADC.
	 * (ii)		B (10-14)	Sin uso (en 0)
	 * (iii)	C (15)		Canal de medición. 0: Channel 1, 1: Channel 2.
	 * Finalmente se indica al commands manager que agregue cada una de las muestras al graficador.
	 * Esta comentado el setear los textos del graficador por cuestiones de performance. Una vez con el
	 * programa funcionando se deberían descomentar esas lineas para ver como se ve afectada
	 * la velocidad de ejecución del programa.
	 */
	public void receiveData() {
		Pointer data = new Memory(300);
		IntByReference readed = new IntByReference();
		
		mpUSBApi._MPUSBRead(ep2Handler, data, 300, readed, 0);

		int len = readed.getValue();
		
		if(len % 2 != 0) {
			throw new RuntimeException("Half a sample recived");
		}
		
		refreshFrequence(len/2);
		
		Sample[] samples = new Sample[len/2];
		
		for(int i = 0; i < samples.length; i++) {
			char sample = data.getChar(i*2);
			samples[i] = new Sample(sample & 0x3FF, sample > 0 ? 1 : 2);
		}
		CommandsManager cm = CommandsManager.getCM();
		for(Sample sample : samples) {
			if(sample.channel == 1) {
				cm.addValueToCH1(sample.value);
				//cm.setvValue1Text(sample.value);
			}else{
				cm.addValueToCH2(sample.value);
				//cm.setvValue2Text(sample.value);
			}
		}
	}

	/**
	 * Recalcula la frecuencia de muestreo a partir de una cantidad de muestras nuevas
	 * que han llegado desde la última llamada a este método.
	 * Dado que la medición de millisegundos no siempre es precisa, se estiman 2ms en caso
	 * de que el valor no haya sido actualizado desde la última llamada al método. Se estiman
	 * 2ms ya que este es el intervalo cada el cual el host usb pedirá al device que envíe los
	 * datos.
	 * En caso de perderse paquetes (posible en modo isócrono) la frecuencia estará mal calculada
	 * por un instante, hasta que arriven los 2 paquetes siguientes. 
	 * @param samples
	 */
	public void refreshFrequence(int samples) {
		long newTimestamp = System.currentTimeMillis();
		if(newTimestamp - lastTimestamp == 0) {
			lastTimestamp -= 2; // No se reciben muestras cada menos que 2ms.
		}
		frequence = (int) (samples / (newTimestamp - lastTimestamp));
	}

	/**
	 * La clase Sample representa una medión de voltaje en un canal determinado, y es utilizada a los
	 * fines de ir almacenando ordenadamente el parseo de la información proveniente del USB.
	 */
	class Sample {
		float value;
		int channel;
		Sample(int relative, int channel) {
			this.value = toAbsoluteValue(relative);
			this.channel = channel;
		}
		private float toAbsoluteValue(int relative) {
			return (relative / 1024f) * 5;
		}
	}
	
	/**
	 * Este método debe invocarse siempre que cambie alguno de los 3 flags de configuración del PIC
	 * (adcON, channel1On y channel2On) para enviar una cadena de 3 bytes, con los 3 flags, a travez
	 * del EP1 del PIC. Este método aloca un buffer de 3 bytes en memoria, y tras escribir los flags
	 * solicita a la dll el envío de este. Si por algún motivo no se logran enviar los 3 bytes al PIC
	 * una excepción es lanzada. 
	 */
	private void configureADC() {
		Pointer buff = new Memory(3); // 3 bytes.
		buff.setByte(0, (byte) (adcOn ? 1 : 0));
		buff.setByte(1, (byte) (channel1On ? 1 : 0));			
		buff.setByte(2, (byte) (channel2On ? 1 : 0));			
		
		IntByReference writed = new IntByReference();

		mpUSBApi._MPUSBWrite(ep1Handler, buff, 3, writed, 0);
		
		if(writed.getValue() != 3) {
			throw new RuntimeException("couldn't send full configuration command");
		}
	}

	/**
	 * Setea el flag que indica al PIC si se pretenden hacer mediciones por el canal 1 del
	 * osciloscopio, y se solicita el envió de estos flags al PIC.
	 */
	public void setChannel1(boolean active) {
		channel1On = active;
		configureADC();
	}

	/**
	 * Setea el flag que indica al PIC si se pretenden hacer mediciones por el canal 2 del
	 * osciloscopio, y se solicita el envió de estos flags al PIC.
	 */
	public void setChannel2(boolean active) {
		channel2On = active;
		configureADC();
	}
	
	/**
	 * Setea el flag que indica al PIC si el osciloscopio debe estar activado o no.
	 * En caso de no estar activado, no se haran mediciones por ningun canal independientemenete
	 * del estado de los flags de los canales.
	 * Es necesario enviar este flag en true al PIC para que se inicie el módulo ADC tras la
	 * correcta enumeración y configuración del dispositivo. 
	 */
	public void setADCOn(boolean on) {
		adcOn = on;
		configureADC();
	}
}
