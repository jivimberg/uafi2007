package deskcam.model.impl;

import java.util.Map;

import deskcam.model.RGBConstants;
import deskcam.model.criteria.PixelEqualityCriteria;

public class Oldie extends AbstractStreamProcessor implements RGBConstants {

	public Oldie() {
		super("The Oldie", "reference");
	}
	
	@Override
	int[] obtainImage(int[] cam, Map<String, int[]> images,
			PixelEqualityCriteria criteria, int w, int h) {
		
		int[] out = new int[w*h];
		int[] ref = images.get("reference");
		
		int ix, grey;
		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				ix = j*w + i;
				if(criteria.pxEquals(cam[ix], ref[ix])) {
					out[ix] = cam[ix];
				} else {
					grey = (((cam[ix] & RED) >> 16) + ((cam[ix] & GREEN) >> 8) + (cam[ix] & BLUE)) / 3;
					out[ix] = ALPHA | (grey << 16) | (grey << 8) | grey;
				}
			}
		}
		
		return out;
	}

}
