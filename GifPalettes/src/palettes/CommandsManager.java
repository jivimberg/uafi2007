package palettes;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
		if(loadedImage != null) {
			modifiedPalette = effect.transform(loadedPalette);
			modifiedImage = GifCodec.setImagePalette(loadedImage, modifiedPalette);
			Application.getApplication().getEnv().setTransformedImage(modifiedImage, modifiedPalette);
		}
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
			if(!file.getName().endsWith(".gif")) {
				file = new File(file.getParentFile(), file.getName() + ".gif");
			}
			OutputStream out = null;
			try {
				out = new BufferedOutputStream(new FileOutputStream(file));
				out.write(modifiedImage);
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(out != null) {
					try {
						out.close();
					} catch (IOException e) {}
				}
			}
		}
	}
}
