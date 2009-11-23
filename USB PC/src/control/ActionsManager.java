package control;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;

@SuppressWarnings("serial")
public class ActionsManager {

	private Action channel1Setted;
	private Action channel2Setted;
	private Action settedCh1AC;
	private Action settedCh1DC;
	private Action settedCh2AC;
	private Action settedCh2DC;
	
	private CommandsManager cm;
	
	public ActionsManager() {
		cm = CommandsManager.getCM();
		createViewChannel1Action();
		createViewChannel2Action();
		createSettedCh1AC();
		createSettedCh1DC();
		createSettedCh2AC();
		createSettedCh2DC();
	}
	
	private void createViewChannel1Action(){
		channel1Setted = new AbstractAction("CHANNEL 1"){
			public void actionPerformed(ActionEvent arg0) {
				cm.setChannel1(((JCheckBox)arg0.getSource()).isSelected());
			}
		};
	}
	
	private void createViewChannel2Action(){
		channel2Setted = new AbstractAction("CHANNEL 2"){
			public void actionPerformed(ActionEvent arg0) {
				cm.setChannel2(((JCheckBox)arg0.getSource()).isSelected());
			}
		};
	}
	
	private void createSettedCh1AC(){
		settedCh1AC = new AbstractAction("AC"){
			public void actionPerformed(ActionEvent arg0) {
				cm.setCh1AC(((JRadioButton)arg0.getSource()).isSelected());
			}
		};
	}
	
	private void createSettedCh1DC(){
		settedCh1DC = new AbstractAction("DC"){
			public void actionPerformed(ActionEvent arg0) {
				cm.setCh1DC(((JRadioButton)arg0.getSource()).isSelected());
			}
		};
	}
	
	private void createSettedCh2AC(){
		settedCh2AC = new AbstractAction("AC"){
			public void actionPerformed(ActionEvent arg0) {
				cm.setCh2AC(((JRadioButton)arg0.getSource()).isSelected());
			}
		};
	}
	
	private void createSettedCh2DC(){
		settedCh2DC = new AbstractAction("DC"){
			public void actionPerformed(ActionEvent arg0) {
				cm.setCh2DC(((JRadioButton)arg0.getSource()).isSelected());
			}
		};
	}
		
	public Action getSettedChannel1() {
		return channel1Setted;
	}


	public Action getSettedChannel2() {
		return channel2Setted;
	}

	public Action getSettedCh1AC() {
		return settedCh1AC;
	}

	public Action getSettedCh1DC() {
		return settedCh1DC;
	}

	public Action getSettedCh2AC() {
		return settedCh2AC;
	}

	public Action getSettedCh2DC() {
		return settedCh2DC;
	}
	
}
