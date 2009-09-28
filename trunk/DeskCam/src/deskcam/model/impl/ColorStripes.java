package deskcam.model.impl;

import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import deskcam.model.RGBConstants;
import deskcam.model.criteria.PixelEqualityCriteria;

public class ColorStripes extends AbstractStreamProcessor implements RGBConstants {

	public ColorStripes() {
		super("Color Stripes", "reference");
	}
	
	private int mov;
	
	private int vel;
	
	private int stripesWidth = 10;
	
	@Override
	int[] obtainImage(int[] cam, Map<String, int[]> images,
			PixelEqualityCriteria criteria, int w, int h) {
		
		int[] out = new int[w*h];
		int[] ref = images.get("reference");
		
		mov += vel;
		
		int ix, j2;
		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				ix = j*w + i;
				if(criteria.pxEquals(cam[ix], ref[ix])) {
					out[ix] = cam[ix];
				} else {
					j2 = (j+mov)/stripesWidth;
					out[ix] = ALPHA | (0xFF << ((j2%3)*8));
					if((j2%6) > 2) {
						out[ix] |= (0xFF << (((j2+1)%3)*8));
					}
				}
			}
		}
		
		return out;
	}
	
	@Override
	protected JComponent createConfigurationPanel() {
		JComponent panel = super.createConfigurationPanel();
		
		final JSlider velSelector = new JSlider(0, 100, 0);
		velSelector.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				vel = velSelector.getValue() / 10;
			}
		});
		velSelector.setMajorTickSpacing(20);
		velSelector.setMinorTickSpacing(5);
		velSelector.setPaintTicks(true);
		velSelector.setPaintLabels(true);		
		panel.add(velSelector);

		final JSlider widthSelector = new JSlider(0, 100, 10);
		widthSelector.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				stripesWidth = (widthSelector.getValue()/3) + 1;
			}
		});
		widthSelector.setMajorTickSpacing(20);
		widthSelector.setMinorTickSpacing(5);
		widthSelector.setPaintTicks(true);
		widthSelector.setPaintLabels(true);		
		panel.add(widthSelector);
		
		return panel;
	}	

}
