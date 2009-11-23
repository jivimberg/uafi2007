package view;

import java.awt.BorderLayout;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import control.Application;

@SuppressWarnings("serial")
public class OptionsPanel extends JPanel {

	private JCheckBox channel1;
	private JCheckBox channel2;
	private ButtonGroup currentSelection1, currentSelection2;
	private JRadioButton acRB1, dcRB1;
	private JRadioButton acRB2,dcRB2;
	private JSlider freqSlider;
	
	public OptionsPanel(Application app) {
		
		/*CHANNEL 1*/
		JPanel ch1Panel = new JPanel();
		Border border = new TitledBorder("Channel 1");
		ch1Panel.setBorder(border);
		channel1 = new JCheckBox(app.getActionManager().getSettedChannel1());
		channel1.setSelected(true);
		currentSelection1 = new ButtonGroup();
		acRB1 = new JRadioButton(app.getActionManager().getSettedCh1AC());
		dcRB1 = new JRadioButton(app.getActionManager().getSettedCh1DC());
		dcRB1.setSelected(true);
		currentSelection1.add(acRB1);
		currentSelection1.add(dcRB1);
		ch1Panel.add(channel1);
		ch1Panel.add(acRB1);
		ch1Panel.add(dcRB1);
		
		/*CHANNEL 2*/
		JPanel ch2Panel = new JPanel();
		border = new TitledBorder("Channel 2");
		ch2Panel.setBorder(border);
		channel2 = new JCheckBox(app.getActionManager().getSettedChannel2());
		channel2.setSelected(true);
		acRB2 = new JRadioButton(app.getActionManager().getSettedCh2AC());
		dcRB2 = new JRadioButton(app.getActionManager().getSettedCh2DC());
		dcRB2.setSelected(true);
		currentSelection2 = new ButtonGroup();
		currentSelection2.add(acRB2);
		currentSelection2.add(dcRB2);
		ch2Panel.add(channel2);
		ch2Panel.add(acRB2);
		ch2Panel.add(dcRB2);
		
		
		JPanel freqPanel = new JPanel(new BorderLayout());
		freqPanel.add(new JLabel("	Sampling Frequency (KHz)"), BorderLayout.NORTH);
		freqSlider = new JSlider(0,45);
		freqSlider.setOrientation(JSlider.HORIZONTAL);
		freqSlider.setMajorTickSpacing(5);
		freqSlider.setMinorTickSpacing(1);
		freqSlider.setPaintTicks(true);
		freqSlider.setPaintLabels(true);
		//freqSlider.addChangeListener(new SliderListener());
		freqSlider.setEnabled(false);
		freqPanel.add(freqSlider,BorderLayout.CENTER);
		
		add(ch1Panel);
		add(ch2Panel);
		add(freqPanel);
	}
	
/*	class SliderListener implements ChangeListener{

		@Override
		public void stateChanged(ChangeEvent e) {
			CommandsManager.getCM().setFrequency(((JSlider)e.getSource()).getValue());
		}
		
	}
*/	
	
	public void setSliderFreq (int freqInKHz){
		freqSlider.setValue(freqInKHz);
		repaint();
	}
	
}
