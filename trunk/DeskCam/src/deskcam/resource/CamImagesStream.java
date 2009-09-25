package deskcam.resource;

import java.awt.Dimension;
import java.awt.Image;
import java.util.Vector;

import javax.media.Buffer;
import javax.media.Player;
import javax.media.control.FrameGrabbingControl;
import javax.media.format.VideoFormat;
import javax.media.util.BufferToImage;

/**
 *
 * Esta clase utiliza una Player y la interfaz FrameGrabbingControl para generar imagenes estaticas desde
 * la webcam.
 *
 */
public class CamImagesStream implements SizedImagesStream {

	private FrameGrabbingControl control;
	
	private BufferToImage converter;
	
	private Vector<ImagesStreamListener> listeners = new Vector<ImagesStreamListener>();
	
	public CamImagesStream(Player player) {
		control = (FrameGrabbingControl) player.getControl("javax.media.control.FrameGrabbingControl");
		if(control == null) {
			throw new RuntimeException("Player "+player+" has no frame grabbing control.");
		}
	}

	@Override
	public void addListener(ImagesStreamListener listener) {
		listeners.add(listener);
	}

	@Override
	public Image obtainImage() {
		Buffer buff = control.grabFrame();
		if(buff == null || buff.getFormat() == null) {
			return null;
		}
		if(converter == null) {
			converter = new BufferToImage((VideoFormat) buff.getFormat());
		}
		Image img = converter.createImage(buff);
		if(img != null) {
			for (ImagesStreamListener l : listeners) {
				l.imageStreamed(img);
			}
		}
		return img;
	}

	@Override
	public boolean removeListener(ImagesStreamListener listener) {
		return listeners.remove(listener);
	}

	private Dimension size;
	
	@Override
	public Dimension getSize() {
		if(size == null) {
			Image img = obtainImage();
			size = new Dimension(img.getWidth(null), img.getHeight(null));
		}
		return new Dimension(size);
	}

	
	
}
