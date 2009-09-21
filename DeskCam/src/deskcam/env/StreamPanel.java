package deskcam.env;

import java.awt.BorderLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import deskcam.resource.ImagesStream;
import deskcam.resource.ImagesStreamListener;

/**
 * Muestra las imagenes generadas por un ImageStream.
 */
@SuppressWarnings("serial")
public class StreamPanel extends JPanel implements ImagesStreamListener {

	private JLabel image; 
	
	public StreamPanel(String name, ImagesStream stream) {
		super(new BorderLayout());
		
		add(new JLabel(name), BorderLayout.NORTH);
		
		add(image = new JLabel());
		
		validate();
		
		stream.addListener(this);
		
	}

	@Override
	public void imageStreamed(Image img) {
		image.setIcon(new ImageIcon(img));
	}
}
