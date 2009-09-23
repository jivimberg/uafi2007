package deskcam.model.impl;

import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import deskcam.model.RGBConstants;
import deskcam.model.criteria.PixelEqualityCriteria;

public class Phantom extends AbstractStreamProcessor implements RGBConstants {

	private float alpha = .5f;
	
	public Phantom() {
		super("Phantom", "reference", "background");
	}

	@Override
	int[] obtainImage(int[] cam, Map<String, int[]> images,
			PixelEqualityCriteria criteria, int w, int h) {
		
		int[] ref = images.get("reference");
		int[] back = images.get("background");
		int[] output = new int[w*h];
		
		int ix, r, g, b;
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				ix = i * w + j;
				if(criteria.pxEquals(cam[ix], ref[ix])) {
					output[ix] = back[ix];
				} else {
					r = ((int)((back[ix] & RED) * (1-alpha)) + (int)((cam[ix] & RED) * alpha)) & RED;
					g = ((int)((back[ix] & GREEN) * (1-alpha)) + (int)((cam[ix] & GREEN) * alpha)) & GREEN;
					b = ((int)((back[ix] & BLUE) * (1-alpha)) + (int)((cam[ix] & BLUE) * alpha)) & BLUE;
					output[ix] = ALPHA | r | g | b;
				}
			}
		}
		
		return output;
	}

	@Override
	protected JComponent createConfigurationPanel() {
		JComponent panel = super.createConfigurationPanel();
		
		final JSlider alphaSelector = new JSlider(0, 100, 50);
		alphaSelector.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				alpha = alphaSelector.getValue() / 100f;
			}
		});
		alphaSelector.setMajorTickSpacing(20);
		alphaSelector.setMinorTickSpacing(5);
		alphaSelector.setPaintTicks(true);
		alphaSelector.setPaintLabels(true);		
		panel.add(alphaSelector);

		
		return panel;
	}
	
	
}
