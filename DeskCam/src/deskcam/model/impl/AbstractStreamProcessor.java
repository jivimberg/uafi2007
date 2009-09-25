package deskcam.model.impl;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import deskcam.model.ImagesStreamProcessor;
import deskcam.model.criteria.PixelEqualityCriteria;
import deskcam.resource.ImagesStream;
import deskcam.resource.ImagesStreamListener;
import deskcam.resource.InputsListener;
import deskcam.resource.InputsResponser;

public abstract class AbstractStreamProcessor implements ImagesStreamProcessor, InputsListener {

	private ImagesStream camstream;
	
	private String name;
	
	private volatile Map<String, int[]> images;
	
	private int tolerance = 20;
	private Class<? extends PixelEqualityCriteria> pxCriteria =
											PixelEqualityCriteria.CRITERIAS.values()[0].getCriteria();
	
	public AbstractStreamProcessor(String name, String... images) {
		this.images = new HashMap<String, int[]>(images.length);
		for (String imageName : images) {
			this.images.put(imageName, null);
		}
		this.name = name;
	}
	
	@Override
	public String getProcessorName() {
		return name;
	}
	
	
	@Override
	public void imageObtained(String resourceName, Image img) {
		images.put(resourceName, preprocessImage(img));
	}

	@Override
	public void setInputsResponser(InputsResponser res) {
		for (String resource : images.keySet()) {
			res.requestImage(resource, this);
		}

		res.setConfigurationsPanel(createConfigurationPanel());
	}
	
	protected JComponent createConfigurationPanel() {
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
		
		return panel;
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
	
	@Override
	public ImagesStream getOutputStream() {
		return outputStream;
	}

	private boolean realized;
	
	private ImagesStream outputStream = new ImagesStream() {
		
		public Image obtainImage() throws InterruptedException {
			waitForRealization();
			
			Image camimage = camstream.obtainImage();
			int[] input = preprocessImage(camimage);
			int w = camimage.getWidth(null);
			int h = camimage.getHeight(null);
			
			PixelEqualityCriteria criteria;
			try {
				criteria = pxCriteria.getConstructor(int.class).newInstance(tolerance);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			int[] obtained = AbstractStreamProcessor.this.obtainImage(input, images, criteria, w, h);
			
			Image output = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(w, h, obtained, 0, w));
			
			if(output != null) {
				for (ImagesStreamListener l : listeners) {
					l.imageStreamed(output);
				}
			}
			return output;
		}
		
		private void waitForRealization() throws InterruptedException {
			if(!realized) {
				while(camstream == null || images.containsValue(null) || camstream.obtainImage() == null) {
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
	
	abstract int[] obtainImage(int[] camImage, Map<String, int[]> images, PixelEqualityCriteria criteria, int w, int h);

	@Override
	public void relaseResouces() {
		realized = false;
		camstream = null;
		for (Map.Entry<String, int[]> img : images.entrySet()) {
			img.setValue(null);
		}
	}
}
