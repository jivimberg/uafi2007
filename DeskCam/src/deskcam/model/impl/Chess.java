package deskcam.model.impl;

import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import deskcam.model.RGBConstants;
import deskcam.model.criteria.PixelEqualityCriteria;

public class Chess extends AbstractStreamProcessor implements RGBConstants {

	public Chess() {
		super("Chess", "reference");
	}
	
	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;
	
	private int size = 20;
	
	@Override
	int[] obtainImage(int[] cam, Map<String, int[]> images,
			PixelEqualityCriteria criteria, int w, int h) {
		
		int[] out = new int[w*h];
		int[] ref = images.get("reference");
		
		int ix;
		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				ix = j*w + i;
				out[ix] = (j/size + i/size + (criteria.pxEquals(cam[ix], ref[ix]) ? 1 : 0)) % 2 == 0 ? BLACK : WHITE;
			}
		}
		
		return out;
	}
	
	@Override
	protected JComponent createConfigurationPanel() {
		JComponent panel = super.createConfigurationPanel();
		
		final JSlider sizeSelector = new JSlider(0, 100, 20);
		sizeSelector.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				size = (sizeSelector.getValue() / 2) + 1;
			}
		});
		sizeSelector.setMajorTickSpacing(20);
		sizeSelector.setMinorTickSpacing(5);
		sizeSelector.setPaintTicks(true);
		sizeSelector.setPaintLabels(true);		
		panel.add(sizeSelector);
		
		return panel;
	}

	
	
}
