import com.sun.jna.Native;


public class Tester {

	public static void main(String[] args) {
//		System.load("C:/Users/Juan Ignacio/Documents/Universidad Austral/Intro Com/MCHPUSB Custom Driver/Mpusbapi/Dll/Borland_C/mpusbapi.dll");
		System.setProperty("jna.library.path", "C:/Users/Juan Ignacio/Documents/Universidad Austral/Intro Com/MCHPUSB Custom Driver/Mpusbapi/Dll/Borland_C");
		MPUSBApi mpUSBApi = (MPUSBApi) Native.loadLibrary("mpusbapi", MPUSBApi.class);
		System.out.println( mpUSBApi.MPUSBGetDLLVersion());
//		System.out.println(mpUSBApi.MPUSBGetDeviceCount("VID_03F0&PID_3207"));
	}
}
