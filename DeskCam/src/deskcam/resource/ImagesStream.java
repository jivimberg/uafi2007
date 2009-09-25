package deskcam.resource;

import java.awt.Image;

/**
 *
 * Un ImagesStream es un generador de imagenes a pedido.
 * Al invocar el metodo 'obtainImage' el stream generara una nueva imagen.
 * Ademas, tdoas las nuevas imagenes generadas serán enviadas a los listeners del stream.
 * 
 * En nuestro programa encontramos básicamente 2 tipos de implementaciones de esta clase:
 * - Streams generados a partir de un recurso (por ejemplo desde un Player asociado a una webcam).
 * - Streams generados por procesadores de imagenes.
 * 
 */
public interface ImagesStream {
	
	/**
	 * Genera una nueva imagen, la devuelve, y notifica a todos los listeners que dicha imagen fue generada.
	 * @return una nueva imagen generada
	 * @throws InterruptedException if image obtaining process is interruped by any means
	 */
	Image obtainImage() throws InterruptedException;
	
	/**
	 * Agrega un listener para ser notificado de las imagenes que genere el stream.
	 * @param listener
	 */
	void addListener(ImagesStreamListener listener);

	/**
	 * Quita un listener de este stream.
	 * @param listener
	 * @return si el listener estaba asociado o no al stream.
	 */
	boolean removeListener(ImagesStreamListener listener);

}
