package palettes.env;

import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel {

	private JLabel label;
	// private PalettePanel palette;
	
	public ImagePanel() {
		label = new JLabel();
		// palette = new PaelettePanel();
		add(label);
	}
	
	public void setImage(byte[] img, List<byte[]> palette) {
		if(img != null) {
			label.setIcon(new ImageIcon(img));
			// palette.setValues(palette);
		} else {
			label.setIcon(null);
			// palette.setValues(null);
		}
	}
}
