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
	 * Permite solicitarle imagenes y mostrarle un cuadro de configuraci�n.
	 * @param responser responde a las solicitudes del processor.
	 */
	void setInputsResponser(InputsResponser responser);
	
	/**
	 * Devuelve el stream de imagenes generadas por el procesador.
	 * 
	 * <p><b><em>Sobre el m�todo obtainImage del stream devuelto:</em></b></p>
	 * 
	 * <p>Al invocar obtainImage se debe verificar que el usuario haya provisto al procesador
	 * de todas las imagenes solicitadas, y que la configuraci�n sea v�lida. De ser as�
	 * se debe iniciar la generaci�n de una nueva imagen que ser� devuelta.
	 *
	 * <blockquote><b>IMPORTANTE: De no disponer con los recursos necesarios para generar la imagen
	 * o en caso de ser inv�lida la configuraci�n el m�todo debe BLOQUEAR hasta que disponga
	 * de todos los recursos necesarios.</b>
	 * Bloquear significa que no retornar� nada hasta tener los recursos necesarios.</blockquote> 
	 * 
	 * <p>Todas las llamadas a obtainImage ser�n realizadas por un mismo thread (por lo cual
	 * no habr� dos llamadas simultaneas, ni necesidad de sincrhonizar el m�todo).</p> 
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
