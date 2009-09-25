package deskcam.env;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import deskcam.Application;

/**
 * Muestra una imagen y permite desencadenar acciones para cargar la misma desde un archivo o capturarla
 * desde la webcam.
 *
 */
@SuppressWarnings("serial")
public class ImagePanel extends JPanel {
	
	private JLabel image;
	
	private String resourceName;
	
	public ImagePanel(String name) {
		super(new BorderLayout());
		
		this.resourceName = name;
		
		JLabel label = new JLabel(resourceName);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(label.getFont().deriveFont(12f));
		add(label, BorderLayout.NORTH);
		
		add(image = new JLabel());
		
		JPanel btts = new JPanel();
		
		btts.add(new JButton(capture));
		btts.add(new JButton(load));
		
		add(btts, BorderLayout.SOUTH);
		
		validate();
		
	}
	
	private Action capture = new AbstractAction("Capture") {
		@Override
		public void actionPerformed(ActionEvent e) {
			Application.getApplication().getCommandsManager().captureImage(resourceName);
		}
	};

	private Action load = new AbstractAction("Load") {
		@Override
		public void actionPerformed(ActionEvent e) {
			Application.getApplication().getCommandsManager().loadImage(resourceName);
		}
	};
	
	public void setImage(Image img) {
		image.setIcon(new ImageIcon(img));
	}
}
