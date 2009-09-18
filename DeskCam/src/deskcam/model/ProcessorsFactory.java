package deskcam.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Mangeja los procesadores de imagenes registrados en la aplicación. 
 */
public class ProcessorsFactory {

	private static List<Class<? extends ImagesStreamProcessor>> processors;
	static {
		loadProcessors();
	}
	
	private ProcessorsFactory() {}

	private static ProcessorsFactory factory = new ProcessorsFactory();
	
	public static ProcessorsFactory getInstance() {
		return factory;
	}
	
	public ImagesStreamProcessor getDefaultProcessor() {
		if(processors != null && !processors.isEmpty()) {
			try {
				return processors.get(0).newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private static void loadProcessors() {
		try {
			BufferedReader buf = new BufferedReader(new InputStreamReader(ProcessorsFactory.class.getResourceAsStream("processors.lst")));
			String line;
			processors = new ArrayList<Class<? extends ImagesStreamProcessor>>();
			try {
				while((line = buf.readLine()) != null) {
					try {
						Class<?> processor = Class.forName(line);
						processors.add((Class<? extends ImagesStreamProcessor>) processor);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
}
