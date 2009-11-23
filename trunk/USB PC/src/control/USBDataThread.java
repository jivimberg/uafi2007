package control;

public class USBDataThread extends Thread {

	private Application app;
	
	public USBDataThread(Application app) {
		this.app = app;
	}
	
	@Override
	public void run() {
		for (;;) {
			app.getUSBTransmitter().receiveData();
			// The above method should block until data is recived & processed. No need to sleep.
		}		
	}
}
