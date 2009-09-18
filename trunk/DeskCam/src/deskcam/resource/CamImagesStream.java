package deskcam.resource;

import java.awt.Image;

import javax.media.Player;

/**
 *
 * Esta clase utiliza una Player y la interfaz FrameGrabbingControl para generar imagenes estaticas desde
 * la webcam.
 *
 */
// TODO implementar stream de imagenes a partir de un player.
public class CamImagesStream implements ImagesStream {

	public CamImagesStream(Player player) {

	}

	@Override
	public void addListener(ImagesStreamListener listener) {
	}

	@Override
	public Image obtainImage() {
		return null;
	}

	@Override
	public boolean removeListener(ImagesStreamListener listener) {
		return false;
	}

}
