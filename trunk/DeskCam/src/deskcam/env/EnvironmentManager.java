package deskcam.env;

import javax.media.Player;
import javax.swing.JComponent;

import deskcam.resource.ImagesStream;

/**
 * �nico punto de acceso de toda la aplicaci�n a la interfaz gr�fica. 
 *
 */
// TODO: Crear interfaz gr�fica.
public class EnvironmentManager {

	/**
	 * Setea el panel de configuraci�n del processor para ser a�adido a la interfaz.
	 * @param panel
	 */
	public void setConfigurationsPanel(JComponent panel) {
		
	}

	/**
	 * A�ade a la interfaz un recuadro donde el usuario puede cargar/capturar
	 * una imagen para ser utilizada por el processor
	 * @param resourceName el nombre del recurso que ser� mostrado al usuario.
	 */
	public void addImageWindow(String resourceName) {
		
	}
	
	/**
	 * A�ade a la interfaz un recuadro donde se mostrara el streaming en vivo de la webcam.
	 * @param player asociado a la webcam
	 */
	public void setStreamPlayer(Player player) {
		// El mismo player provee una interfaz gr�fica a travez de uno de sus getters.
	}
	
	/**
	 * A�ade a la interfaz un recuadro donde se mostrar�n las imagenes que genere el images stream.
	 * @param name del recuadro.
	 * @param stream de imagenes que ser� escuchado para mostrar las imagenes que genere. 
	 */
	public void addImagesStreamWindow(String name, ImagesStream stream) {
		
	}

}
