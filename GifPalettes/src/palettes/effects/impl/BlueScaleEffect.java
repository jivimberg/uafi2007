package palettes.effects.impl;


import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import palettes.effects.GifEffect;
import palettes.effects.Refresher;


public class BlueScaleEffect implements GifEffect{
	
	static final int FPS_MIN = 0;
	static final int FPS_MAX = 100;
	static final int FPS_INIT = 50;  
		
	@Override
	public String getEffectName() {
		return "Blue Scale";
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
			byte R = (byte)(0);
			byte G = (byte)(0);
			byte B = (byte)(colour[2]);
			transformed.add(new byte[] {R, G, B});
		}
		return transformed;
	}
	
	
}