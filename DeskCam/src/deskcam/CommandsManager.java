package deskcam;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import deskcam.model.ImagesStreamProcessor;

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
		Image img = null;
		try {
			img = Application.getApplication().getControlManager().getCamImagesStream().obtainImage();
		} catch (InterruptedException e) {}
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
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "bmp", "gif"));
		int ans = chooser.showOpenDialog(null);
		File f = chooser.getSelectedFile();
		if(ans != JFileChooser.APPROVE_OPTION || f == null) {
			return;
		}
		Image img = Toolkit.getDefaultToolkit().getImage(f.getAbsolutePath());
		Application.getApplication().getEnvironmentManager().setImageResource(img, resourceName);
		
		Dimension camdim = Application.getApplication().getControlManager().getCamImagesStream().getSize();
		
		if(img.getWidth(null) != camdim.width || img.getHeight(null) != camdim.height) {
			img = img.getScaledInstance(camdim.width, camdim.height, Image.SCALE_SMOOTH);
	        Application.getApplication().getEnvironmentManager().loadImage(img);
		}
		
		Application.getApplication().getControlManager().imageObtained(img, resourceName);
	}

	public void loadProcessor(ImagesStreamProcessor processor) {
		Application.getApplication().getControlManager().initProcessor(processor);
	}
	
}
