package deskcam.model.impl;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import deskcam.model.ImagesStreamProcessor;
import deskcam.model.RGBConstants;
import deskcam.model.criteria.PixelEqualityCriteria;
import deskcam.resource.ImagesStream;
import deskcam.resource.ImagesStreamListener;
import deskcam.resource.InputsListener;
import deskcam.resource.InputsResponser;

/**
 * Procesador que cambia el fondo del stream de la webcam.
 * El procesador utiliza, ademas del stream de la webcam, una imagen para el background y una de referencia.
 * El procesamiento consiste en identificar los pixeles coincidentes entre una imagen obtenida desde
 * la webcam y otra de referencia, y reemplazar estos por los correspondientes al background.
 *
 */
public class BackgroundFilter implements ImagesStreamProcessor, RGBConstants {

	private ImagesStream camstream;
	private volatile int[] reference;
	private volatile int[] background;
	
	private int tolerance = 20;
	private Class<? extends PixelEqualityCriteria> pxCriteria =
											PixelEqualityCriteria.CRITERIAS.values()[0].getCriteria();
	
	@Override
	public ImagesStream getOutputStream() {
		return outputStream;
	}

	@Override
	public void setInputsResponser(InputsResponser res) {
		res.requestImage("reference", new InputsListener() {
			public void imageObtained(String resourceName, Image img) {
				reference = preprocessImage(img);
			}
		});
		res.requestImage("background", new InputsListener() {
			public void imageObtained(String resourceName, Image img) {
				background = preprocessImage(img);
			}
		});
		
		JComponent panel = Box.createVerticalBox();
		
		final JComboBox criteriaSelector = new JComboBox(PixelEqualityCriteria.CRITERIAS.values());
		criteriaSelector.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				pxCriteria = ((PixelEqualityCriteria.CRITERIAS)criteriaSelector.getSelectedItem()).getCriteria();
			}
		});
		criteriaSelector.setSelectedItem(pxCriteria);
		panel.add(criteriaSelector);
		
		final JSlider toleranceSelector = new JSlider(JSlider.VERTICAL, 0, 100, 20);
		toleranceSelector.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				tolerance = toleranceSelector.getValue();
			}
		});
		toleranceSelector.setMajorTickSpacing(50);
		toleranceSelector.setMinorTickSpacing(10);
		toleranceSelector.setPaintTicks(true);
		toleranceSelector.setPaintLabels(true);		
		panel.add(toleranceSelector);

		res.setConfigurationsPanel(panel);
	}

	public void setInputStream(ImagesStream s) {
		camstream = s;
	}
	
	public int[] preprocessImage(Image img) {
		int w = img.getWidth(null);
		int h = img.getHeight(null);
		
		int[] pixels = new int[w*h];
		PixelGrabber grabber = new PixelGrabber(img, 0, 0, w, h, pixels, 0, w);
		try {
			grabber.grabPixels(0);
		} catch (InterruptedException e) {}

		return pixels;
	}

	private boolean realized;
	
	private ImagesStream outputStream = new ImagesStream() {
		
		public Image obtainImage() throws InterruptedException {
			waitForRealization();
			
			Image input = camstream.obtainImage();
			
			int w = input.getWidth(null);
			int h = input.getHeight(null);
			
			int[] campixels = new int[w*h];
			PixelGrabber grabber = new PixelGrabber(input, 0, 0, w, h, campixels, 0, w);
			try {
				grabber.grabPixels(0);
			} catch (InterruptedException e) {}
			
			int[] outpixels = new int[w*h];
			
			PixelEqualityCriteria criteria;
			try {
				criteria = pxCriteria.getConstructor(int.class).newInstance(tolerance);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			int ix;
			for (int j = 0; j < h; j++) {
				for (int i = 0; i < w; i++) {
					ix = j*w + i;
					outpixels[ix] = criteria.pxEquals(campixels[ix], reference[ix]) ? background[ix] : campixels[ix]; 
				}
			}
			
			Image output = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(w, h, outpixels, 0, w));
			
			if(output != null) {
				for (ImagesStreamListener l : listeners) {
					l.imageStreamed(output);
				}
			}
			return output;
		}
		
		private void waitForRealization() throws InterruptedException {
			if(!realized) {
				while(camstream == null || background == null || reference == null || camstream.obtainImage() == null) {
					Thread.sleep(500);
				}
				realized = true;
			}
		}
		
		private Vector<ImagesStreamListener> listeners = new Vector<ImagesStreamListener>();
		public void addListener(ImagesStreamListener listener) {
			listeners.add(listener);
		}
		public boolean removeListener(ImagesStreamListener listener) {
			return listeners.remove(listener);
		}
	};
	
	@Override
	public String getProcessorName() {
		return "Scenes Swap";
	}

	@Override
	public void relaseResouces() {
		realized = false;
		camstream = null;
		reference = null;
		background = null;
	};
	
}
