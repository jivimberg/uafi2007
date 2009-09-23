package deskcam.model.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import deskcam.model.RGBConstants;
import deskcam.model.criteria.PixelEqualityCriteria;

public class Velocity extends AbstractStreamProcessor implements RGBConstants {

	private List<int[]> olds = new LinkedList<int[]>();
	private float lost = .2f;
	private float baseopac;
	private int qty = 4;
	
	private int framecount;
	private int interval = 15;
	
	public Velocity() {
		super("Velocity", "reference");
	}
	
	@Override
	int[] obtainImage(int[] cam, Map<String, int[]> images,
			PixelEqualityCriteria criteria, int w, int h) {
		
		int[] out = new int[w*h];

		
		while(olds.size() > qty + 1) {
			olds.remove(qty + 1);
			baseopac = lost;
		}
		
		int ix;
		
		int[] newimg = null;
		if(framecount-- == 0) {
			newimg = captureInstant(cam, images.get("reference"), w, h, criteria);
			framecount = interval;
		}

		baseopac -= lost / interval; 
		
		System.arraycopy(cam, 0, out, 0, w*h);
		float opacity = baseopac;
		int r, g, b;
		ListIterator<int[]> itt = olds.listIterator(olds.size());
		while(itt.hasPrevious()) {
			int[] old = itt.previous();
			for (int i = 0; i < h; i++) {
				for (int j = 0; j < w; j++) {
					ix = i * w + j;
					if(old[ix] != 0) {
						r = ((int)((out[ix] & RED) * (1-opacity)) + (int)((old[ix] & RED) * opacity)) & RED;
						g = ((int)((out[ix] & GREEN) * (1-opacity)) + (int)((old[ix] & GREEN) * opacity)) & GREEN;
						b = ((int)((out[ix] & BLUE) * (1-opacity)) + (int)((old[ix] & BLUE) * opacity)) & BLUE;
						out[ix] = ALPHA | r | g | b;
					}
				}
			}
			opacity += lost;
			if(opacity >= 1) {
				break;
			}
		}
		
		if(newimg != null) {
			olds.add(0, newimg);
		}
		
		return out;
	}
	
	protected int[] captureInstant(int[] cam, int[] ref, int w, int h, PixelEqualityCriteria criteria) {
		int[] newimg = new int[w*h];
		int ix;
		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				ix = j*w + i;
				newimg[ix] = criteria.pxEquals(cam[ix], ref[ix]) ? 0 : cam[ix]; 
			}
		}
		return newimg;
	}
	
	@Override
	protected JComponent createConfigurationPanel() {
		
		JComponent panel = super.createConfigurationPanel();
		
		final JSpinner spinner = new JSpinner(new SpinnerNumberModel(4, 0, 10, 1));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				qty = (Integer)(spinner.getValue());
				lost = 1f / (qty + 1);
			}
		});
		panel.add(spinner);
		
		final JSlider intervalSelector = new JSlider(0, 100, 45);
		intervalSelector.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				interval = intervalSelector.getValue() / 3;
				lost = 1f / (qty + 1);
			}
		});
		intervalSelector.setMajorTickSpacing(20);
		intervalSelector.setMinorTickSpacing(5);
		intervalSelector.setPaintTicks(true);
		intervalSelector.setPaintLabels(true);		
		panel.add(intervalSelector);
		
		return panel;
	}

}
