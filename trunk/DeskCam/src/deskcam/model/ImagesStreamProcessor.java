package deskcam.model;

import deskcam.resource.ImagesStream;
import deskcam.resource.InputsResponser;

/**
 * Las implementaciones de esta interfaz son las encargadas de transformar imagenes generando
 * un nuevo stream de salida.
 */
public interface ImagesStreamProcessor {

	/**
	 * Provee al processor un stream de imagenes de la webcam.
	 * @param stream stream de la webcam.
	 */
	void setInputStream(ImagesStream stream);
	
	/**
	 * Provee al processor una forma de interactuar con el usuario.
	 * Permite solicitarle imagenes y mostrarle un cuadro de configuración.
	 * @param responser responde a las solicitudes del processor.
	 */
	void setInputsResponser(InputsResponser responser);
	
	/**
	 * Devuelve el stream de imagenes generadas por el procesador.
	 * 
	 * <p><b><em>Sobre el método obtainImage del stream devuelto:</em></b></p>
	 * 
	 * <p>Al invocar obtainImage se debe verificar que el usuario haya provisto al procesador
	 * de todas las imagenes solicitadas, y que la configuración sea válida. De ser así
	 * se debe iniciar la generación de una nueva imagen que será devuelta.
	 *
	 * <blockquote><b>IMPORTANTE: De no disponer con los recursos necesarios para generar la imagen
	 * o en caso de ser inválida la configuración el método debe BLOQUEAR hasta que disponga
	 * de todos los recursos necesarios.</b>
	 * Bloquear significa que no retornará nada hasta tener los recursos necesarios.</blockquote> 
	 * 
	 * <p>Todas las llamadas a obtainImage serán realizadas por un mismo thread (por lo cual
	 * no habrá dos llamadas simultaneas, ni necesidad de sincrhonizar el método).</p> 
	 * 
	 * <p>El procesador no debe generar imagenes a menos que se haya invocado
	 * el metodo obtainImage del ImageStream devuelto a travez de este metodo.</p>
	 *  
	 * @return Un ImagesStream de imagenes generadas por el procesador.
	 */
	ImagesStream getOutputStream();
	
	String getProcessorName();

	/**
	 * Libera posibles recursos mantenidos por el procesador, debido a que este dejara de
	 * ejecutarse.
	 */
	void relaseResouces();
	
}
