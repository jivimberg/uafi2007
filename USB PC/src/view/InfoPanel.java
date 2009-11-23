package view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import control.Application;
import control.CommandsManager;

@SuppressWarnings("serial")
public class InfoPanel extends JPanel {

	private JTextField vValueCH1;
	private JTextField vValueCH2;
	
	public InfoPanel(Application app) {
		JLabel ch1L = new JLabel("Channel 1:");
		vValueCH1 = new JTextField();
		vValueCH1.setPreferredSize(new Dimension(100,20));
		vValueCH1.setEnabled(false);
		
		JLabel ch2L = new JLabel("Channel 2:");
		vValueCH2 = new JTextField();
		vValueCH2.setPreferredSize(new Dimension(100,20));
		vValueCH2.setEnabled(false);
		
		JButton addV = new JButton(new AbstractAction("Add Values"){
			
			public void actionPerformed(ActionEvent arg0) {
					
				new TestThread().start();
				
			}
		});
		
		add(ch1L);
		add(vValueCH1);
		add(ch2L);
		add(vValueCH2);
		add(addV);
	}

	public void setvValue1Text(float v){
		vValueCH1.setText(v+" v.");
	}
	
	public void setvValue2Text(float v){
		vValueCH2.setText(v+" v.");
	}
	
	class TestThread extends Thread {
        public void run() {
        	Double a = 1.0;
        	Double v1;
        	Double v2;
        	while (true) {
        		v1 = 3 + Math.sin(Math.toRadians(a));
        		v2 = 3 + Math.cos(Math.toRadians(a));
				try {
					CommandsManager.getCM().addValueToCH1(v1.floatValue());
					CommandsManager.getCM().addValueToCH2(v2.floatValue());
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				a += 2.0;
			}
        }
    }
	
}
