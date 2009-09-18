package deskcam;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import javax.media.Player;
import javax.swing.JComponent;

import deskcam.model.ImagesStreamProcessor;
import deskcam.model.ProcessorsFactory;
import deskcam.resource.CamImagesStream;
import deskcam.resource.ImagesStream;
import deskcam.resource.InputsListener;
import deskcam.resource.InputsResponser;

/**
 * 
 * Controla el procesamiento de imagenes y flujo de datos.
 * Establece interrelaciones entre las distintas partes de la aplicación. 
 *
 */
public class ControlManager implements InputsResponser {

	private ImagesStreamProcessor processor;
	
	private ImagesStream camImagesStream;

	/** Stream de imagenes generadas por el procesador */
	private ImagesStream outputStream;
	
	private Map<String, InputsListener> listeners = new HashMap<String, InputsListener>();
	
	public void init() {
		
		/* Se obtiene el procesador por defecto */
		processor = ProcessorsFactory.getInstance().getDefaultProcessor();
		
		/* Se le setea un InputsResponser encargado de proveerlo de las imagenes que necesite (ej: background image) */
		processor.setInputsResponser(this);
		
		/* Se obtiene un player asociado a la webcam para proveer de imagenes al programa */
		// TODO: Crear un player asociado a la webcam.
		Player player = null;
		
		/* Se crea un stream de imagenes que genere imagenes desde la webcam */
		camImagesStream = new CamImagesStream(player);
		
		/* Se le asigna dicho stream al processador */
		processor.setInputStream(camImagesStream);
		
		/* Se obtiene un stream con las imagenes generadas por el procesador */
		outputStream = processor.getOutputStream();
		
		/* Se crea un recuadro en la interfaz de usuario asociado al player */
		Application.getApplication().getEnvironmentManager().setStreamPlayer(player);
		
		/* Se crea un recuadro en la interfaz de usuario que muestra las imagenes de la webcam capturadas por el procesador */
		Application.getApplication().getEnvironmentManager().addImagesStreamWindow("Input", camImagesStream);
		
		/* Se crea un recuadro en la interfaz de usuario asociado a dicho stream */
		Application.getApplication().getEnvironmentManager().addImagesStreamWindow("Output", outputStream);
		
		/* Se inicia un thread que fuerza al procesador a generar nuevas imagenes */
		Thread th = new Thread("Image Processing Thread") {
			public void run() {
				outputStream.obtainImage();
			};
		};
		th.setDaemon(true); // El thread muere al terminar el programa.
		th.start();
	}

	@Override
	public void requestImage(String resourceName, InputsListener listener) {
		listeners.put(resourceName, listener);
		Application.getApplication().getEnvironmentManager().addImageWindow(resourceName);
	}

	@Override
	public void setConfigurationsPanel(JComponent panel) {
		Application.getApplication().getEnvironmentManager().setConfigurationsPanel(panel);
	}

	public void imageObtained(Image img, String resourceName) {
		listeners.get(resourceName).imageObtained(resourceName, img);
	}

	public ImagesStream getCamImagesStream() {
		return camImagesStream;
	}
	
}
