package deskcam.model.impl;

import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToggleButton;

import deskcam.model.RGBConstants;
import deskcam.model.criteria.PixelEqualityCriteria;

public class Composition extends AbstractStreamProcessor implements RGBConstants {

	private boolean doShot;
	private boolean clear = true;
	
	private boolean red = true;
	private boolean green = true;
	private boolean blue = true;
	
	private int[] output;
	
	public Composition() {
		super("Composition", "reference", "background", "shooter");
	}
	
	@Override
	int[] obtainImage(int[] camImage, Map<String, int[]> images,
			PixelEqualityCriteria criteria, int w, int h) {
		
		int ix;

		if(clear) {
			output = new int[w*h];
			System.arraycopy(images.get("background"), 0, output, 0, w*h);
			clear = false;
		}

		if(doShot) {
			int[] ref = images.get("reference");
			int[] shooter = images.get("shooter");
			int r, g, b;
			for (int j = 0; j < h; j++) {
				for (int i = 0; i < w; i++) {
					ix = j*w + i;
					if(!criteria.pxEquals(shooter[ix], ref[ix])) {
						r = red ? shooter[ix] & RED : 0;
						g = green ? shooter[ix] & GREEN : 0;
						b = blue ? shooter[ix] & BLUE : 0;
						output[ix] = ALPHA | r | g | b;
					}
				}
			}
			doShot = false;
		}

		return output;
	}
	
	@SuppressWarnings("serial")
	@Override
	protected JComponent createConfigurationPanel() {
		JComponent panel = super.createConfigurationPanel();

		panel.add(new JButton(new AbstractAction("Clear") {
			@Override
			public void actionPerformed(ActionEvent e) {
				clear = true;
			}
		}));

		panel.add(new JButton(new AbstractAction("Add Shot") {
			@Override
			public void actionPerformed(ActionEvent e) {
				doShot = true;
			}
		}));
		
		final JToggleButton redBtt = new JToggleButton();
		redBtt.setSelected(true);
		redBtt.setAction(new AbstractAction("Red") {
			@Override
			public void actionPerformed(ActionEvent e) {
				red = redBtt.isSelected(); 
			}
		});
		panel.add(redBtt);
		
		final JToggleButton greenBtt = new JToggleButton();
		greenBtt.setSelected(true);
		greenBtt.setAction(new AbstractAction("Green") {
			@Override
			public void actionPerformed(ActionEvent e) {
				green = greenBtt.isSelected(); 
			}
		});
		panel.add(greenBtt);

		final JToggleButton blueBtt = new JToggleButton();
		blueBtt.setSelected(true);
		blueBtt.setAction(new AbstractAction("Blue") {
			@Override
			public void actionPerformed(ActionEvent e) {
				blue = blueBtt.isSelected(); 
			}
		});
		panel.add(blueBtt);
		
		return panel;
	}

	@Override
	public void relaseResouces() {
		super.relaseResouces();
		output = null;
		clear = true;
	}
	
	

}
