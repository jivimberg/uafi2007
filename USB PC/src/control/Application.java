package control;

import usb.USBTransmitter;
import view.MainWindow;

public class Application {

	private ActionsManager actionManager = new ActionsManager();
	private USBTransmitter transmitter = new USBTransmitter();
	private MainWindow gui = new MainWindow(this);
	
	public static void main(String args[]) {
		CommandsManager.getCM().setApp(new Application());
	}

	public ActionsManager getActionManager() {
		return actionManager;
	}

	public MainWindow getGui() {
		return gui;
	}
	
	public USBTransmitter getUSBTransmitter() {
		return transmitter;
	}
	
	public void exit() {
		System.exit(0);		
	}
}
