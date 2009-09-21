package deskcam;

import deskcam.env.EnvironmentManager;

/**
 *
 * La clase Applicacion permite acceder de forma estática a la unica instancia de la misma y de aquí a 
 * los distintos componentes que la conforman, entre los que distinguimos:
 * <p>
 * <ul>
 * <li>ControlManager: Encargado del flujo de datos de la aplicacción.
 * <li>CommandsManager: Encargado de ejecutar las acciones desencadenadas por el usuario.
 * <li>EnvironmentManager: Punto de entrada a la interfaz gráfica de la aplicación.  
 * </ul>
 * <p>
 *
 */
public class Application {

	/**
	 * Inicializa la aplicación.
	 */
	public static void start(){
		/* Este método no realiza nada, pero llamarlo fuerza la ejecución
		 * de todos los bloques estáticos de la clase, inicializando asi
		 * la aplicación
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
