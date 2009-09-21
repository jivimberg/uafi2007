package deskcam.env;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import javax.media.Player;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import deskcam.resource.ImagesStream;

/**
 * Único punto de acceso de toda la aplicación a la interfaz gráfica. 
 *
 */
public class EnvironmentManager {

	private JPanel windowsPanel;
	
	private JPanel optionsPanel;
	
	private Map<String, ImagePanel> resources = new HashMap<String, ImagePanel>();
	
	public EnvironmentManager() {
		
		JFrame frame = new JFrame("DeskCam");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel mainP = new JPanel(new BorderLayout());
		
		windowsPanel = new JPanel(new GridLayout(2, 3));
		
		mainP.add(windowsPanel);
		
		optionsPanel = new JPanel(new BorderLayout());
		
		mainP.add(optionsPanel, BorderLayout.EAST);
		
		frame.add(mainP);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setVisible(true);
		
	}
	
	/**
	 * Setea el panel de configuración del processor para ser añadido a la interfaz.
	 * @param panel
	 */
	public void setConfigurationsPanel(JComponent panel) {
		if(optionsPanel.getComponentCount() != 0) {
			optionsPanel.remove(0);
		}
		optionsPanel.add(panel);
	}

	/**
	 * Añade a la interfaz un recuadro donde el usuario puede cargar/capturar
	 * una imagen para ser utilizada por el processor
	 * @param resourceName el nombre del recurso que será mostrado al usuario.
	 */
	public void addImageWindow(String resourceName) {
		ImagePanel panel = new ImagePanel(resourceName);
		
		resources.put(resourceName, panel);
		
		windowsPanel.add(panel);
	}
	
	/**
	 * Añade a la interfaz un recuadro donde se mostrara el streaming en vivo de la webcam.
	 * @param player asociado a la webcam
	 */
	public void setStreamPlayer(Player player) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(player.getVisualComponent());
		panel.add(player.getControlPanelComponent(), BorderLayout.SOUTH);
		windowsPanel.add(panel);
		windowsPanel.validate();
	}
	
	/**
	 * Añade a la interfaz un recuadro donde se mostrarán las imagenes que genere el images stream.
	 * @param name del recuadro.
	 * @param stream de imagenes que será escuchado para mostrar las imagenes que genere. 
	 */
	public void addImagesStreamWindow(String name, ImagesStream stream) {
		windowsPanel.add(new StreamPanel(name, stream));
		windowsPanel.validate();
	}

	public void setImageResource(Image img, String resourceName) {
		resources.get(resourceName).setImage(img);
	}

}
