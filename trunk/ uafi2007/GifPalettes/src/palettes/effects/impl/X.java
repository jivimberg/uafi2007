package palettes.effects.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.JPanel;

import palettes.effects.GifEffect;
import palettes.effects.Refresher;

public class X implements GifEffect {

	public X() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getEffectName() {
		return "X";
	}

	@Override
	public JPanel getEffectOptionsPanel() {
		return null;
	}

	@Override
	public void setRefresher(Refresher refresher) {}

	@Override
	public List<byte[]> transform(List<byte[]> palette) {
		List<byte[]> transformed = new ArrayList<byte[]>();
		for (int i = palette.size() - 1; i >= 0; i--) {
			transformed.add(palette.get(i));
		}
		return transformed;
	}

}
