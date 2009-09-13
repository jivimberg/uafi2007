package palettes;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import palettes.effects.GifEffect;
import palettes.gif.GifCodec;

public class CommandsManager {

	private byte[] loadedImage;
	private List<byte[]> loadedPalette;
	private byte[] modifiedImage;
	private List<byte[]> modifiedPalette;
	
	public void applyEffect(GifEffect effect) {
		
		modifiedPalette = effect.transform(loadedPalette);
		modifiedImage = GifCodec.setImagePalette(loadedImage, modifiedPalette);
		
		Application.getApplication().getEnv().setTransformedImage(modifiedImage, modifiedPalette);
		
	}

	public void loadImage(File file) {
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(file));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int r;
			while((r = in.read(b)) != -1) {
				out.write(b, 0, r);
			}
			in.close();
			loadedImage = out.toByteArray();
			loadedPalette = GifCodec.createPalette(loadedImage);
			Application.getApplication().getEnv().setOriginalImage(loadedImage, loadedPalette);
			Application.getApplication().getEnv().setTransformedImage(null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveImage(File file) {
		if(modifiedImage != null) {
			try {
				OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
				out.write(modifiedImage);
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
