package usb;

public class NoDevicesFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public NoDevicesFoundException() {
		super("No devices found.");
	}

}
