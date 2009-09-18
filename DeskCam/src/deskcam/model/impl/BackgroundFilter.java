package deskcam.model.impl;

import deskcam.model.ImagesStreamProcessor;
import deskcam.resource.ImagesStream;
import deskcam.resource.InputsResponser;

/**
 * Procesador que cambia el fondo del stream de la webcam.
 * El procesador utiliza, ademas del stream de la webcam, una imagen para el background y una de referencia.
 * El procesamiento consiste en identificar los pixeles coincidentes entre una imagen obtenida desde
 * la webcam y otra de referencia, y reemplazar estos por los correspondientes al background.
 *
 */
//TODO: Implementar procesador de imagenes.
public class BackgroundFilter implements ImagesStreamProcessor {

	@Override
	public ImagesStream getOutputStream() {
		return null;
	}

	@Override
	public void setInputsResponser(InputsResponser res) {
	}

	@Override
	public void setInputStream(ImagesStream s) {
		
	}

}
