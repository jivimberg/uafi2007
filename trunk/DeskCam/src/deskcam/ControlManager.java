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
import deskcam.resource.SizedImagesStream;

/**
 * 
 * Controla el procesamiento de imagenes y flujo de datos.
 * Establece interrelaciones entre las distintas partes de la aplicación. 
 *
 */
public class ControlManager implements InputsResponser {

	private ImagesStreamProcessor actualProcessor;
	
	private SizedImagesStream camImagesStream;
	
	

	/** Stream de imagenes generadas por el procesador */
	private volatile ImagesStream outputStream;
	
	private Map<String, InputsListener> listeners = new HashMap<String, InputsListener>();
	
	public void init() {
		Application.getApplication().getEnvironmentManager().setProcessors(ProcessorsFactory.getInstance().getProcessors());

		/* Se obtiene un player asociado a la webcam para proveer de imagenes al programa */
		Player player = WebcamAccesPoint.getWebcamPlayer();
		
		/* Se crea un stream de imagenes que genere imagenes desde la webcam */
		camImagesStream = new CamImagesStream(player);
		
		/* Se crea un recuadro en la interfaz de usuario asociado al player */
		Application.getApplication().getEnvironmentManager().setStreamPlayer(player);
		
		/* Se crea inicia el programa con un procesador ya cargado */
		initProcessor(ProcessorsFactory.getInstance().getDefaultProcessor());

		/* Se inicia un thread que fuerza al procesador a generar nuevas imagenes */
		imageProcessingThread = new Thread("Image Processing Thread") {
			public void run() {
				for(;;) {
					synchronized (ControlManager.this) {
						try {
							if(outputStream != null) {
								outputStream.obtainImage();
							} else {
								Thread.sleep(100);
							}
						} catch (InterruptedException e) {}
					}
					while(synchronizing) {
						Thread.yield();
					}
				}
			};
		};
		imageProcessingThread.setDaemon(true); // El thread muere al terminar el programa.
		imageProcessingThread.start();
	}
	
	private Thread imageProcessingThread; 
	
	private volatile boolean synchronizing;
	
	public void initProcessor(ImagesStreamProcessor processor) {
	
		synchronizing = true;
		
		if(imageProcessingThread != null) {
			imageProcessingThread.interrupt();
		}
		
		synchronized (this) {

			synchronizing = false;

			if(actualProcessor != null) {
				
				outputStream = null;
				
				actualProcessor.relaseResouces();
				
				Application.getApplication().getEnvironmentManager().clearPanels();
				
			}
			
			/* Se obtiene el procesador por defecto */
			actualProcessor = processor;
			
			/* Se le setea un InputsResponser encargado de proveerlo de las imagenes que necesite (ej: background image) */
			actualProcessor.setInputsResponser(this);
			
			/* Se le asigna dicho stream al processador */
			actualProcessor.setInputStream(camImagesStream);
			
			/* Se obtiene un stream con las imagenes generadas por el procesador */
			outputStream = actualProcessor.getOutputStream();
			
			/* Se crea un recuadro en la interfaz de usuario que muestra las imagenes de la webcam capturadas por el procesador */
			Application.getApplication().getEnvironmentManager().addImagesStreamWindow("Input", camImagesStream);
			
			/* Se crea un recuadro en la interfaz de usuario asociado a dicho stream */
			Application.getApplication().getEnvironmentManager().addImagesStreamWindow("Output", outputStream);
			
		}
		
		
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

	public SizedImagesStream getCamImagesStream() {
		return camImagesStream;
	}
}
