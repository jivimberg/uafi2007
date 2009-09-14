package palettes.effects.impl;


import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import palettes.effects.GifEffect;
import palettes.effects.Refresher;


public class Y implements GifEffect, ChangeListener{


	private static final int FPS_MAX = 100;
	private static final int FPS_INIT = 100;
	private static final int FPS_MIN = 0;
	
	JSlider slider;
	double value;
	
	Refresher refresher;
	
	@Override
	public String getEffectName() {
		return "Y";
	}

	@Override
	public JPanel getEffectOptionsPanel() {
		JPanel panel = new JPanel();
		slider = new JSlider(JSlider.VERTICAL,
                FPS_MIN, FPS_MAX, FPS_INIT);

		slider.addChangeListener(this);
		slider.setMajorTickSpacing(40);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);		
		panel.add(slider);

		return panel;
	}

	@Override
	public void setRefresher(Refresher refresher) {
		this.refresher = refresher;
	}
	
	/*
	 * Los casteos a byte y los '&0xFF' son necesarios para trabajar con bytes en el rango [0-255] en lugar de [-128,127]. 
	 */
	@Override
	public List<byte[]> transform(List<byte[]> palette) {
		List<byte[]> transformed = new ArrayList<byte[]>(palette.size());
		for (byte[] colour : palette) {
			byte R = (byte)(colour[0]*value);
			byte G = (byte)(colour[1]*value);
			byte B = (byte)(colour[2]*value);
			transformed.add(new byte[] {R, G, B});
		}
		return transformed;
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		value = slider.getValue() / 100;
        refresher.refresh();
	}
}
