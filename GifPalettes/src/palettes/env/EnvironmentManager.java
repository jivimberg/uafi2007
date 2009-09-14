package palettes.env;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import palettes.Application;
import palettes.effects.GifEffect;
import palettes.effects.Refresher;

@SuppressWarnings("serial")
public class EnvironmentManager implements Refresher {

	private ImagePanel originalP;
	private ImagePanel modifiedP;
	
	private JComboBox combo;
	private JPanel effectP;
	private JButton okBtt;

	public EnvironmentManager() {
		
		JFrame frame = new JFrame("Gif Palettes");
		
		JMenuBar menubar = new JMenuBar();
		menubar.add(new JButton(loadImage));
		menubar.add(new JButton(saveImage));
		menubar.add(new JButton(exit));
		frame.setJMenuBar(menubar);
		
		JPanel mainP = new JPanel(new BorderLayout());
		
		JPanel imgsP = new JPanel(new BorderLayout());
		
		originalP = new ImagePanel();
		modifiedP = new ImagePanel();
		imgsP.add(originalP, BorderLayout.WEST);
		imgsP.add(modifiedP, BorderLayout.EAST);
		
		JPanel leftP = new JPanel(new BorderLayout());
		
		combo = new JComboBox();
		combo.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				loadEffect(((EffectAdapterForCombo)combo.getSelectedItem()).getEffect());
				apply.actionPerformed(null);
			}
		});
		effectP = new JPanel(new BorderLayout());
		okBtt = new JButton(apply);
		
		leftP.add(combo, BorderLayout.NORTH);
		Box box = Box.createVerticalBox();
		box.add(okBtt);
		box.add(effectP);
		leftP.add(box);
		
		mainP.add(imgsP, BorderLayout.WEST);
		mainP.add(leftP, BorderLayout.EAST);
		
		frame.add(mainP);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setVisible(true);
		
	}
	
	public void setEffects(List<GifEffect> effects) {
		EffectAdapterForCombo[] comboEffects = new EffectAdapterForCombo[effects.size()];
		for(int i = 0; i < effects.size(); i++) {
			comboEffects[i] = new EffectAdapterForCombo(effects.get(i));
			
		}
		combo.setModel(new DefaultComboBoxModel(comboEffects));
		if(!effects.isEmpty()) {
			combo.setSelectedIndex(0);
			loadEffect(((EffectAdapterForCombo)combo.getSelectedItem()).getEffect());
		}
	}
	
	public void setOriginalImage(byte[] img, List<byte[]> palette) {
		originalP.setImage(img, palette);
		
	}
	
	public void setTransformedImage(byte[] img, List<byte[]> palette) {
		modifiedP.setImage(img, palette);
	}
	
	public void loadEffect(GifEffect effect) {
		if(effectP.getComponentCount() != 0) {
			effectP.remove(0);
		}
		JPanel options = effect.getEffectOptionsPanel();
		if(options != null) {
			effectP.add(options);
		}
		effect.setRefresher(this);
		effectP.validate();
	}
	
	private Action loadImage = new AbstractAction("Open") {
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int ans = chooser.showOpenDialog(null);
			if(ans == JFileChooser.APPROVE_OPTION) {
				Application.getApplication().getCmd().loadImage(chooser.getSelectedFile());
			}
		}
	};
	
	private Action saveImage = new AbstractAction("Save") {
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int ans = chooser.showSaveDialog(null);
			System.out.println(ans);
			if(ans == JFileChooser.APPROVE_OPTION) {
				Application.getApplication().getCmd().saveImage(chooser.getSelectedFile());
			}
		}
	};
	
	private Action exit = new AbstractAction("Exit") {
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	};
	
	private Action apply = new AbstractAction("Apply") {
		public void actionPerformed(ActionEvent e) {
			if(combo.getSelectedItem() != null) {
				if(Application.getApplication() != null){
					Application.getApplication().getCmd().applyEffect(((EffectAdapterForCombo)combo.getSelectedItem()).getEffect());
				}
			}
		}
	};
	
	private class EffectAdapterForCombo {
		private GifEffect effect;
		private EffectAdapterForCombo(GifEffect e) {
			this.effect = e;
		}
		public GifEffect getEffect() {
			return effect;
		}
		@Override
		public String toString() {
			return effect.getEffectName();
		}
	}
	
	@Override
	public void refresh() {
		apply.actionPerformed(null);
	}

}
