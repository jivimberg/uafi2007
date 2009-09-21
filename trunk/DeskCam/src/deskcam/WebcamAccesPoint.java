package deskcam;

import java.util.Vector;

import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Manager;
import javax.media.Player;

public class WebcamAccesPoint {

	private WebcamAccesPoint() {}
	
	private static Player player;

	@SuppressWarnings("unchecked")
	public static Player getWebcamPlayer()  {
		
		if(player == null) {
			
			try {
				CaptureDeviceInfo camInfo = null;
				Vector<CaptureDeviceInfo> v = CaptureDeviceManager.getDeviceList(null);
				for (CaptureDeviceInfo dev : v) {
					if(dev.getName().startsWith("vfw")) {
						camInfo = dev;
					}
				}
				if(camInfo != null) {
					player = Manager.createRealizedPlayer(camInfo.getLocator());
					player.start();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return player;
	}
}
