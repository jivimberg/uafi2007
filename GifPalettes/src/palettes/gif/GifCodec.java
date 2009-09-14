package palettes.gif;

import java.util.ArrayList;
import java.util.List;

public class GifCodec {
	
	private final static int PALETTE_BEGIN = 13;
	private final static int PALLETE_LENGHT = 256*3;

	public static List<byte[]> createPalette(byte[] loadedImage) {
		List<byte[]> palette = new ArrayList<byte[]>(256);
		for(int i = PALETTE_BEGIN; i < PALETTE_BEGIN + PALLETE_LENGHT; i += 3) {
			palette.add( new byte[] { loadedImage[i] , loadedImage[i+1], loadedImage[i+2] } );
		}
		return palette;
	}

	public static byte[] setImagePalette(byte[] loadedImage,
			List<byte[]> modifiedPalette) {
		byte[] modifiedImage = new byte[loadedImage.length];
		System.arraycopy(loadedImage, 0, modifiedImage, 0, loadedImage.length);
		byte[] paletteArray = new byte[modifiedPalette.size()*3];
		for (int i = 0; i < modifiedPalette.size(); i++) {
			byte[] colour = modifiedPalette.get(i);
			paletteArray[i*3] = colour[0];
			paletteArray[i*3 + 1] = colour[1];
			paletteArray[i*3 + 2] = colour[2];
		}
		System.arraycopy(paletteArray, 0, modifiedImage, PALETTE_BEGIN, paletteArray.length);
		return modifiedImage;
	}
}
