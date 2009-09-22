package palettes.effects.impl;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import palettes.effects.GifEffect;
import palettes.effects.Refresher;

public class LightnessEffect implements GifEffect {

	private Refresher refresher;
	
	private JSlider slider;
	
	@Override
	public String getEffectName() {
		return "Lightness";
	}

	@Override
	public JComponent getEffectOptionsPanel() {
		JPanel panel = new JPanel();
		slider = new JSlider(JSlider.VERTICAL, -100, 100, 0);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				refresher.refresh();
			}
		});
		slider.setMajorTickSpacing(50);
		slider.setMinorTickSpacing(10);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);		
		panel.add(slider);

		return panel;
	}

	@Override
	public void setRefresher(Refresher refresher) {
		this.refresher = refresher;	
	}

	@Override
	public List<byte[]> transform(List<byte[]> palette) {
		List<byte[]> transformed = new ArrayList<byte[]>(palette.size());
		float lightness = slider.getValue() / 100f;
		if(lightness == 0) {
			transformed.addAll(palette);
		} else {
			int r, g, b;
			for (byte[] colour : palette) {
				r = (colour[0] & 0xFF);
				g = (colour[1] & 0xFF);
				b = (colour[2] & 0xFF);
				if(lightness > 0) {
					r = r + (int) ((255 - r) * lightness);
					g = g + (int) ((255 - g) * lightness);
					b = b + (int) ((255 - b) * lightness);
				} else {
					r = (int) (r * (1 + lightness));
					g = (int) (g * (1 + lightness));
					b = (int) (b * (1 + lightness));
				}
				transformed.add(new byte[] {(byte)r, (byte)g, (byte)b});
			}
		}
		return transformed;
	}

	
}
