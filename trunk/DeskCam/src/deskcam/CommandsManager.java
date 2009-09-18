package deskcam;

import java.awt.Image;

/**
 *
 * Principalemnte responde a comandos iniciados por el usuario. 
 *
 */
// TODO: Implementar metodos del CommandsManager.
public class CommandsManager {

	/**
	 * Captura una imagen de la webcam con el nombre de recurso dado.
	 * @param resourceName
	 */
	public void captureImage(String resourceName) {
		// Para capturar las imagenes se puede pedir el CamImagesStream al ControlManager.
		Image img = null;
		Application.getApplication().getControlManager().imageObtained(img, resourceName);
	}
	
	/**
	 * Captura una imagen de la webcam con el nombre de recurso dado.
	 * @param resourceName
	 */
	public void loadImage(String resourceName) {
		// Debe mostrar un cuadro para abrir un archivo de imagen.
		Image img = null;
		Application.getApplication().getControlManager().imageObtained(img, resourceName);
	}
	
}
