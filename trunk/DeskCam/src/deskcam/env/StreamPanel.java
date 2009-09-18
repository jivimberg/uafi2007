package deskcam.env;

import java.awt.Image;

import javax.swing.JPanel;

import deskcam.resource.ImagesStream;
import deskcam.resource.ImagesStreamListener;

/**
 * Muestra las imagenes generadas por un ImageStream.
 */
// TODO: Implementar StreamPanel
@SuppressWarnings("serial")
public class StreamPanel extends JPanel implements ImagesStreamListener {

	public StreamPanel(String name, ImagesStream stream) {
		
	}

	@Override
	public void imageStreamed(Image img) {}
}
