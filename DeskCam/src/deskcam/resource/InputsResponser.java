package deskcam.resource;

import javax.swing.JComponent;

/**
 * 
 * Esta interfaz permite a un processor interactuar con el usuario.
 *
 */
public interface InputsResponser {
	
	/**
	 * Solicita al usuario una imagen est�tica. Puede obtenerse de un archivo o desde una captura con la webcam.
	 * @param resourceName el nombre de la imagen que se le indicar� al usuario que seleccione.
	 * @param listener recive la imagen una vez que el usuario la haya seleccionado, y cada vez que decida cambiarla.
	 */
	void requestImage(String resourceName, InputsListener listener);
	
	/**
	 * Muestra al usuario un panel de configuraci�n del processor.
	 * @param panel el panel de configuraci�n.
	 */
	void setConfigurationsPanel(JComponent panel);
	
}
