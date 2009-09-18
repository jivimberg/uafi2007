package deskcam.resource;

import java.awt.Image;

/**
 * Interfaz para escuchar eventos generados por un ImagesStream.
 */
public interface ImagesStreamListener {

	/**
	 * Una nueva imagen fue generada por el ImageStream.
	 * @param img la nueva imagen generada.
	 */
	void imageStreamed(Image img);
	
}
