package deskcam.env;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.media.Player;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;

import deskcam.Application;
import deskcam.model.ImagesStreamProcessor;
import deskcam.resource.ImagesStream;

/**
 * Único punto de acceso de toda la aplicación a la interfaz gráfica. 
 *
 */
public class EnvironmentManager {

	private JPanel windowsPanel;
	
	private JPanel optionsPanel;
	
	private JPanel liveStreamPanel;
	
	private JMenu effects;
	
	private Map<String, ImagePanel> resources = new HashMap<String, ImagePanel>();
	
	@SuppressWarnings("serial")
	public EnvironmentManager() {
		
		JFrame frame = new JFrame("DeskCam");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel mainP = new JPanel(new BorderLayout());
		
		windowsPanel = new JPanel(new GridLayout(2, 3));
		
		mainP.add(windowsPanel);
		
		optionsPanel = new JPanel();
		
		mainP.add(optionsPanel, BorderLayout.EAST);
		
		JMenuBar menubar = new JMenuBar();
		JMenu file = new JMenu("File");
		file.add(new JMenuItem(new AbstractAction("Quit") {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		}));
		menubar.add(file);

		effects = new JMenu("Effects");
		menubar.add(effects);
		
		frame.add(mainP);
		frame.setJMenuBar(menubar);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setVisible(true);
		
	}
	
	@SuppressWarnings("serial")
	public void setProcessors(List<ImagesStreamProcessor> processors) {
		int first = effects.getPopupMenu().getComponentCount();
		for (final ImagesStreamProcessor processor : processors) {
			effects.add(new JRadioButtonMenuItem(new AbstractAction(processor.getProcessorName()) {
				@Override
				public void actionPerformed(ActionEvent e) {
					Application.getApplication().getCommandsManager().loadProcessor(processor);
				}
			}));
		}
		if(!processors.isEmpty()) {
			((JRadioButtonMenuItem)effects.getPopupMenu().getComponent(first)).getAction().putValue(Action.SELECTED_KEY, true);
		}
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
		SwingUtilities.getWindowAncestor(optionsPanel).validate();
	}

	/**
	 * Añade a la interfaz un recuadro donde el usuario puede cargar/capturar
	 * una imagen para ser utilizada por el processor
	 * @param resourceName el nombre del recurso que será mostrado al usuario.
	 */
	public void addImageWindow(String resourceName) {
		ImagePanel panel = new ImagePanel(resourceName);
		panel.setAlignmentX(.5f);
		panel.setAlignmentY(.5f);
		resources.put(resourceName, panel);
		
		windowsPanel.add(panel);
		windowsPanel.validate();
	}
	
	/**
	 * Añade a la interfaz un recuadro donde se mostrara el streaming en vivo de la webcam.
	 * @param player asociado a la webcam
	 */
	public void setStreamPlayer(Player player) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(player.getVisualComponent());
		panel.add(player.getControlPanelComponent(), BorderLayout.SOUTH);
		liveStreamPanel = new JPanel();
		liveStreamPanel.add(panel);
		windowsPanel.add(liveStreamPanel);
		windowsPanel.validate();
	}
	
	/**
	 * Añade a la interfaz un recuadro donde se mostrarán las imagenes que genere el images stream.
	 * @param name del recuadro.
	 * @param stream de imagenes que será escuchado para mostrar las imagenes que genere. 
	 */
	public void addImagesStreamWindow(String name, ImagesStream stream) {
		JPanel panel = new StreamPanel(name, stream);
		panel.setAlignmentX(.5f);
		panel.setAlignmentY(.5f);
		windowsPanel.add(panel);
		windowsPanel.validate();
	}

	/**
	 * Muestra la imagen dada en el cuadro correspondiente al resourceName dado.
	 */
	public void setImageResource(Image img, String resourceName) {
		resources.get(resourceName).setImage(img);
	}

	/**
	 * Quita de entorno gráfico cualquier elemento visual agregado mediante los metodos
	 * addImagesStreamWindow, addImagesWindow y setConfigurationPanel.
	 */
	public void clearPanels() {
		for (Component component : windowsPanel.getComponents()) {
			if(component != liveStreamPanel) {
				windowsPanel.remove(component);
			}
		}
	}

	private MediaTracker tracker ;
	private int mediaID;

	
	public void loadImage(Image img) {
		if(tracker == null) {
			tracker = new MediaTracker(windowsPanel);
		}
		tracker.addImage(img, ++mediaID);
		try {
			tracker.waitForID(mediaID);
		} catch (InterruptedException e) {}
	}
}
