package deskcam.model.impl;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Vector;

import deskcam.model.ImagesStreamProcessor;
import deskcam.resource.ImagesStream;
import deskcam.resource.ImagesStreamListener;
import deskcam.resource.InputsListener;
import deskcam.resource.InputsResponser;

/**
 * Procesador que cambia el fondo del stream de la webcam.
 * El procesador utiliza, ademas del stream de la webcam, una imagen para el background y una de referencia.
 * El procesamiento consiste en identificar los pixeles coincidentes entre una imagen obtenida desde
 * la webcam y otra de referencia, y reemplazar estos por los correspondientes al background.
 *
 */
public class BackgroundFilter implements ImagesStreamProcessor, InputsListener {

	private InputsResponser res;
	
	private ImagesStream cam;
	
	private Image back;
	
	@Override
	public ImagesStream getOutputStream() {
		return outputStream;
	}

	@Override
	public void setInputsResponser(InputsResponser res) {
		this.res = res;
		res.requestImage("background", this);
	}

	@Override
	public void setInputStream(ImagesStream s) {
		cam = s;
	}
	
	@Override
	public void imageObtained(String resourceName, Image img) {
		back = img;
	}

	private ImagesStream outputStream = new ImagesStream() {
		
		private boolean realized;
		
		public Image obtainImage() {
			waitForRealization();
			Image output = new BufferedImage(320, 240, BufferedImage.TYPE_INT_ARGB);
			Image input = cam.obtainImage();
			
			// TODO: Transformation code.
			
			if(output != null) {
				for (ImagesStreamListener l : listeners) {
					l.imageStreamed(output);
				}
			}
			return output;
		}
		
		private void waitForRealization() {
			if(!realized) {
				while(cam == null || back == null || cam.obtainImage() == null) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {}
				}
				realized = true;
			}
		}
		
		private Vector<ImagesStreamListener> listeners = new Vector<ImagesStreamListener>();
		public void addListener(ImagesStreamListener listener) {
			listeners.add(listener);
		}
		public boolean removeListener(ImagesStreamListener listener) {
			return listeners.remove(listener);
		}
	};
}
