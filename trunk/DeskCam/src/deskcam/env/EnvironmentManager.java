package deskcam.env;

import javax.media.Player;
import javax.swing.JComponent;

import deskcam.resource.ImagesStream;

/**
 * Único punto de acceso de toda la aplicación a la interfaz gráfica. 
 *
 */
// TODO: Crear interfaz gráfica.
public class EnvironmentManager {

	/**
	 * Setea el panel de configuración del processor para ser añadido a la interfaz.
	 * @param panel
	 */
	public void setConfigurationsPanel(JComponent panel) {
		
	}

	/**
	 * Añade a la interfaz un recuadro donde el usuario puede cargar/capturar
	 * una imagen para ser utilizada por el processor
	 * @param resourceName el nombre del recurso que será mostrado al usuario.
	 */
	public void addImageWindow(String resourceName) {
		
	}
	
	/**
	 * Añade a la interfaz un recuadro donde se mostrara el streaming en vivo de la webcam.
	 * @param player asociado a la webcam
	 */
	public void setStreamPlayer(Player player) {
		// El mismo player provee una interfaz gráfica a travez de uno de sus getters.
	}
	
	/**
	 * Añade a la interfaz un recuadro donde se mostrarán las imagenes que genere el images stream.
	 * @param name del recuadro.
	 * @param stream de imagenes que será escuchado para mostrar las imagenes que genere. 
	 */
	public void addImagesStreamWindow(String name, ImagesStream stream) {
		
	}

}
