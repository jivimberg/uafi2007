package deskcam;

import deskcam.env.EnvironmentManager;

/**
 *
 * La clase Applicacion permite acceder de forma est�tica a la unica instancia de la misma y de aqu� a 
 * los distintos componentes que la conforman, entre los que distinguimos:
 * <p>
 * <ul>
 * <li>ControlManager: Encargado del flujo de datos de la aplicacci�n.
 * <li>CommandsManager: Encargado de ejecutar las acciones desencadenadas por el usuario.
 * <li>EnvironmentManager: Punto de entrada a la interfaz gr�fica de la aplicaci�n.  
 * </ul>
 * <p>
 *
 */
public class Application {

	/**
	 * Inicializa la aplicaci�n.
	 */
	public static void start(){
		/* Este m�todo no realiza nada, pero llamarlo fuerza la ejecuci�n
		 * de todos los bloques est�ticos de la clase, inicializando asi
		 * la aplicaci�n
		 */
	}
	
	private static Application app = new Application();
	static {
		app.control.init();
		
	}
	
	public static Application getApplication() {
		return app;
	}

	private Application() {}
	
	private ControlManager control = new ControlManager();
	
	private EnvironmentManager env = new EnvironmentManager();
	
	private CommandsManager cmd = new CommandsManager();
	
	public ControlManager getControlManager() {
		return control;
	}

	public EnvironmentManager getEnvironmentManager() {
		return env;
	}
	
	public CommandsManager getCommandsManager() {
		return cmd;
	}
	
}
