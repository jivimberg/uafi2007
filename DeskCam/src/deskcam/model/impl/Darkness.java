package deskcam.model.impl;

import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import deskcam.model.RGBConstants;
import deskcam.model.criteria.PixelEqualityCriteria;

public class Darkness extends AbstractStreamProcessor implements RGBConstants {

	private float darkness;
	
	public Darkness() {
		super("The Darkside", "reference");
	}
	
	@Override
	int[] obtainImage(int[] cam, Map<String, int[]> images,
			PixelEqualityCriteria criteria, int w, int h) {
		int[] out = new int[w*h];
		int[] ref = images.get("reference");
		
		int ix, grey;
		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				ix = j*w + i;
				if(criteria.pxEquals(cam[ix], ref[ix])) {
					grey = (int)((((cam[ix] & RED) >> 16) + ((cam[ix] & GREEN) >> 8) + (cam[ix] & BLUE)) / 3 * darkness);
					out[ix] = ALPHA | (grey << 16) | (grey << 8) | grey;
				} else {
					out[ix] = cam[ix];
				}
			}
		}
		
		return out;
	}
	
	@Override
	protected JComponent createConfigurationPanel() {
		JComponent panel = super.createConfigurationPanel();
		
		final JSlider darkSelector = new JSlider(0, 100, 50);
		darkSelector.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				darkness = (100 - darkSelector.getValue()) / 100f;
			}
		});
		darkSelector.setMajorTickSpacing(20);
		darkSelector.setMinorTickSpacing(5);
		darkSelector.setPaintTicks(true);
		darkSelector.setPaintLabels(true);		
		panel.add(darkSelector);

		
		return panel;
	}	

}
