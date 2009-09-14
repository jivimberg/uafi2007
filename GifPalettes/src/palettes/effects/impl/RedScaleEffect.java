package palettes.effects.impl;


import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import palettes.effects.GifEffect;
import palettes.effects.Refresher;


public class RedScaleEffect implements GifEffect{
	
	static final int FPS_MIN = 0;
	static final int FPS_MAX = 100;
	static final int FPS_INIT = 50;  
	private Refresher refresher;
	private float luminance;
	@Override
	public String getEffectName() {
		return "Red Scale";
	}

	@Override
	public JPanel getEffectOptionsPanel() {
		JPanel panel = new JPanel();
		JSlider red = new JSlider(JSlider.VERTICAL,
                FPS_MIN, FPS_MAX, FPS_INIT);

		red.addChangeListener(new SliderListener());
		red.setMajorTickSpacing(40);
		red.setMinorTickSpacing(1);
		red.setPaintTicks(true);
		red.setPaintLabels(true);
		
		
		
		
		
		
		
		
		
		
	
		

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		panel.add(red);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

		return panel;
	}

	@Override
	public void setRefresher(Refresher refresher) {
		 this.refresher=refresher;
	}
	
	/*
	 * Los casteos a byte y los '&0xFF' son necesarios para trabajar con bytes en el rango [0-255] en lugar de [-128,127]. 
	 */
	@Override
	public List<byte[]> transform(List<byte[]> palette) {
		
		List<byte[]> transformed = new ArrayList<byte[]>(palette.size());
		for (byte[] colour : palette) {
			if(luminance > 0) {
				transformed.add(new byte[] {(byte)((int)(colour[0] + (255 - colour[0])*luminance)),(byte)((colour[1] + (255 - colour[1])*luminance)), (byte)((colour[2] + (255 - colour[2])*luminance))});
			} else {
				transformed.add(new byte[] {(byte)((int)(colour[0] + colour[0]*luminance)),(byte)((colour[1] + colour[1] * luminance)), (byte)((colour[2] + colour[2]*luminance))});
			}
		}
		
		return transformed;
	}
	
	
	
	
	class SliderListener implements ChangeListener {
	    public void stateChanged(ChangeEvent e) {
	        JSlider source = (JSlider)e.getSource();
	        if (!source.getValueIsAdjusting()) {
	         luminance = source.getValue() / 89f + 1;
	         refresher.refresh();
	            
		 
	        }    
	    }
	}
	
	
	
	
}
