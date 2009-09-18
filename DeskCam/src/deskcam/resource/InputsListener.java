package deskcam.resource;

import java.awt.Image;

/**
 * Interfaz complementaria a InputsResponser que recibe las imagenes solicitadas al usuario. 
 */
public interface InputsListener {

	/**
	 * Metodo invocado cada vez que el usuario selecciona/captura una imagen bajo el resourceName dado.
	 * El usuario puede cambiar las imagenes, en cuyo caso se llamara nuevamente a este método con el mismo resourceName.
	 * @param resourceName el nombre de la imagen como fue pasado a 'requestImage'.
	 * @param img imagen que responde al recurso indicado por resourceName.
	 */
	void imageObtained(String resourceName, Image img);
	
}
