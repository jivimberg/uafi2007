package deskcam;

import javax.swing.UIManager;

/**
 * Punto de entrada de la aplicación.
 */
public class Main {

	/**
	 * Punto de entrada de la aplicación.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		
		Application.start();
	}
	
}
