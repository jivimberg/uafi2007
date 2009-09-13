package palettes.effects;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class EffectsManager {

	private List<GifEffect> effects = new ArrayList<GifEffect>();
	
	public List<GifEffect> getEffects() {
		return effects;
	}

	public void loadEffects(Reader effectsStream) {
		BufferedReader buf = new BufferedReader(effectsStream);
		String line;
		try {
			while((line = buf.readLine()) != null) {
				try {
					Class<?> effect = Class.forName(line);
					effects.add((GifEffect)effect.newInstance());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
