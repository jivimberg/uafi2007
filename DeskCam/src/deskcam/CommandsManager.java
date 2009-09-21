package deskcam;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * Principalemnte responde a comandos iniciados por el usuario. 
 *
 */
public class CommandsManager {

	/**
	 * Captura una imagen de la webcam con el nombre de recurso dado.
	 * @param resourceName
	 */
	public void captureImage(String resourceName) {
		// Para capturar las imagenes se puede pedir el CamImagesStream al ControlManager.
		Image img = Application.getApplication().getControlManager().getCamImagesStream().obtainImage();
		if(img != null) {
			Application.getApplication().getEnvironmentManager().setImageResource(img, resourceName);
			Application.getApplication().getControlManager().imageObtained(img, resourceName);
		}
	}
	
	/**
	 * Captura una imagen de la webcam con el nombre de recurso dado.
	 * @param resourceName
	 */
	public void loadImage(String resourceName) {
		// Debe mostrar un cuadro para abrir un archivo de imagen.
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "bmp", "gif"));
		chooser.setVisible(true);
		File f = chooser.getSelectedFile();
		if(f == null) {
			return;
		}
		Image img = Toolkit.getDefaultToolkit().getImage(f.getAbsolutePath());
		Application.getApplication().getEnvironmentManager().setImageResource(img, resourceName);
		Application.getApplication().getControlManager().imageObtained(img, resourceName);
	}
	
}
