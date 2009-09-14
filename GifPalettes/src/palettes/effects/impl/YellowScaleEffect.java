package palettes.effects.impl;


import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import palettes.effects.GifEffect;
import palettes.effects.Refresher;


public class YellowScaleEffect implements GifEffect{




	@Override
	public String getEffectName() {
		return "Yellow Scale";
	}

	@Override
	public JPanel getEffectOptionsPanel() {
		return null;
	}

	@Override
	public void setRefresher(Refresher refresher) {}
	
	/*
	 * Los casteos a byte y los '&0xFF' son necesarios para trabajar con bytes en el rango [0-255] en lugar de [-128,127]. 
	 */
	@Override
	public List<byte[]> transform(List<byte[]> palette) {
		List<byte[]> transformed = new ArrayList<byte[]>(palette.size());
		for (byte[] colour : palette) {
			transformed.add(new byte[] {(byte)(255),(byte)(255), (byte)(colour[2])});
		}
		return transformed;
	}
}
