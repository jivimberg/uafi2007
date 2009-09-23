package deskcam.model.impl;

import deskcam.model.criteria.PixelEqualityCriteria;

public class Fire extends Velocity {

	@Override
	protected int[] captureInstant(int[] cam, int[] ref, int w, int h,
			PixelEqualityCriteria criteria) {
		int[] newimg = new int[w*h];
		int ix;
		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				ix = j*w + i;
				if(!criteria.pxEquals(cam[ix], ref[ix])) {
					newimg[ix] = (cam[ix] & RED) | ALPHA;
				}
			}
		}
		return newimg;
	}
	
}
