package control;

import view.CurrentMode;
import view.VoltageChart;

public class CommandsManager {

	private Application app;
	private static CommandsManager cm;

	private CommandsManager() {}

	public void initSampling() {
		new USBDataThread(app).start();
	}
	
	public static CommandsManager getCM() {
		if (cm == null) {
			cm = new CommandsManager();
		}
		return cm;
	}

	public void setChannel1(boolean selected) {
		app.getUSBTransmitter().setChannel1(selected);
		((VoltageChart)app.getGui().getChart()).setCh1Active(selected);
		calcFrequency();
	}

	public void setChannel2(boolean selected) {
		app.getUSBTransmitter().setChannel2(selected);
		((VoltageChart)app.getGui().getChart()).setCh2Active(selected);
		calcFrequency();
	}
	
	public void calcFrequency() {
		int freq = app.getUSBTransmitter().calculateFreq();
		app.getGui().getOptionsPanel().setSliderFreq(freq);
	}

	public void setApp(Application app) {
		this.app = app;
	}
	
	public void addValueToCH1(float v){
		((VoltageChart)app.getGui().getChart()).addVtoCh1(v);
	}
	
	public void addValueToCH2(float v){
		((VoltageChart)app.getGui().getChart()).addVtoCh2(v);
	}

	public void setCh1AC(boolean selected) {
		if(selected)
			((VoltageChart)app.getGui().getChart()).setCh1Mode(CurrentMode.AC);
		else
			((VoltageChart)app.getGui().getChart()).setCh1Mode(CurrentMode.DC);		
	}

	public void setCh1DC(boolean selected) {
		if(selected)
			((VoltageChart)app.getGui().getChart()).setCh1Mode(CurrentMode.DC);
		else
			((VoltageChart)app.getGui().getChart()).setCh1Mode(CurrentMode.AC);
			
	}

	public void setCh2AC(boolean selected) {
		if(selected)
			((VoltageChart)app.getGui().getChart()).setCh2Mode(CurrentMode.AC);
		else
			((VoltageChart)app.getGui().getChart()).setCh2Mode(CurrentMode.DC);
			
	}

	public void setCh2DC(boolean selected) {
		if(selected){
			((VoltageChart)app.getGui().getChart()).setCh2Mode(CurrentMode.DC);
		}else{
			((VoltageChart)app.getGui().getChart()).setCh2Mode(CurrentMode.AC);
		}
	}
	
	public void setvValue1Text(float v){
		app.getGui().getInfoPanel().setvValue1Text(v);
	}
	
	public void setvValue2Text(float v){
		app.getGui().getInfoPanel().setvValue2Text(v);
	}
	
}
