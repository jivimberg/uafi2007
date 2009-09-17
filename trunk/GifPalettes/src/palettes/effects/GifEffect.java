package palettes.effects;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;


/**
 * 
 * Cada implementaci�n de esta clase permite la aplicaci�n de un efecto sobre la paleta de colores de una imagen.
 * Esta clase provee un m�todo que se invoca para realizar la transformaci�n de la paleta de acuerdo al
 * algoritmo que implemente, y otros 3 m�todos que permiten integrarlo de forma completa a una interf�z gr�fica.
 * 
 * Toda clase que implemente esta interfaz debe proveer un constructor sin par�metros para ser instanciada. 
 * 
 */
public interface GifEffect {

	/**
	 * @param palette lista inmodificable de arreglos de 3 bytes ([r, g, b]), representando la paleta de colores de la imagen original ([23,45,12], [54,158,255], ...)
	 * @return la nueva paleta generada tras la aplicaci�n de la transformaci�n.
	 */
	List<byte[]> transform(List<byte[]> palette);
	
	/**
	 * @return nombre del efecto para mostrar en el combo box.
	 */
	String getEffectName();
	
	/**
	 * @return panel con opciones de configuracion del efecto, o null si no es configurable.
	 */
	JComponent getEffectOptionsPanel();
	
	/**
	 * Provee al efecto de un refresher para que este desencadene la recalculaci�n de la transformaci�n al modificar sus parametros.
	 * @param refresher desencadena la regeneraci�n de la imagen transformada.
	 */
	void setRefresher(Refresher refresher);
	
}
