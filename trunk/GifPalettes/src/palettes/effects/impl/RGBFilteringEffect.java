package palettes.effects.impl;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JToggleButton;

import palettes.effects.GifEffect;
import palettes.effects.Refresher;

public class RGBFilteringEffect implements GifEffect {

	private boolean red;
	private boolean green;
	private boolean blue;
	
	private Refresher refresher;
	
	@Override
	public String getEffectName() {
		return "RGB Filtering";
	}

	@SuppressWarnings("serial")
	@Override
	public JComponent getEffectOptionsPanel() {
		JComponent panel = Box.createVerticalBox();
		panel.add(new JToggleButton(new AbstractAction("Red"){
			@Override
			public void actionPerformed(ActionEvent e) {
				red = ((JToggleButton)e.getSource()).isSelected();
				refresher.refresh();
			}
		}));
		panel.add(new JToggleButton(new AbstractAction("Green"){
			@Override
			public void actionPerformed(ActionEvent e) {
				green = ((JToggleButton)e.getSource()).isSelected();
				refresher.refresh();
			}
		}));
		panel.add(new JToggleButton(new AbstractAction("Blue"){
			@Override
			public void actionPerformed(ActionEvent e) {
				blue = ((JToggleButton)e.getSource()).isSelected();
				refresher.refresh();
			}
		}));
		return panel;
	}

	@Override
	public void setRefresher(Refresher refresher) {
		this.refresher = refresher;	
	}

	@Override
	public List<byte[]> transform(List<byte[]> palette) {
		List<byte[]> transformed = new ArrayList<byte[]>(palette.size());
		for (byte[] colour : palette) {
			transformed.add(new byte[] {red ? colour[0] : 0, green ? colour[1] : 0, blue ? colour[2] : 0});
		}
		return transformed;
	}
	
}
