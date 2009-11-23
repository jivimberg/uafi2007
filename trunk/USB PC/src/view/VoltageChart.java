package view;

import java.awt.Dimension;

import javax.swing.JPanel;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;


import control.CommandsManager;

@SuppressWarnings("serial")
public class VoltageChart extends JPanel {

	/** The time series data. */
	private TimeSeries channel1;
	private TimeSeries channel2;

	private boolean ch1Active = true;
	private boolean ch2Active = true;
	
	private CurrentMode ch1Mode = CurrentMode.DC;
	private CurrentMode ch2Mode = CurrentMode.DC;

	private TimeSeriesCollection dataset;


	public VoltageChart() {
		super();
		channel1 = new TimeSeries("Channel 1");
		channel2 = new TimeSeries("Channel 2");
		channel1.setMaximumItemCount(150);
		channel2.setMaximumItemCount(150);
		
		dataset = new TimeSeriesCollection(channel1);
		dataset.addSeries(channel2);

		final JFreeChart chart = createChart(dataset);
		final ChartPanel chartPanel = new ChartPanel(chart);
		add(chartPanel);
		setPreferredSize(new Dimension(500, 270));

	}

	private JFreeChart createChart(final XYDataset dataset) {
		final JFreeChart result = ChartFactory.createTimeSeriesChart(
				"Oscillator", "Time", "Voltage", dataset, true, true, false);
		final XYPlot plot = result.getXYPlot();
		ValueAxis axis = plot.getDomainAxis();
		axis = plot.getRangeAxis();
		axis.setRange(0.0, 5.5);
		return result;
	}

	public void addVtoCh1(float v) {
		if (ch1Active) {
			if (ch1Mode == CurrentMode.DC) {
				if (v < 0)
					channel1.addOrUpdate(new Millisecond(), 0);
				else if (v > 5)
					channel1.addOrUpdate(new Millisecond(), 5);
				else
					channel1.addOrUpdate(new Millisecond(), v);
			} else {
				channel1.addOrUpdate(new Millisecond(),
						getAvgMagnitude(channel1));
			}
			channel1.fireSeriesChanged();
			CommandsManager.getCM().setvValue1Text(v);
		}
	}
	
	private float getAvgMagnitude(TimeSeries ts){
		float result = 0;
		for (int i = 0; i < ts.getItemCount(); i++) {
			result += ts.getValue(i).floatValue();
		}
		return result / ts.getItemCount();
	}

	public void addVtoCh2(float v) {
		if (ch2Active) {
			if (ch2Mode == CurrentMode.DC) {
				if (v < 0)
					channel2.addOrUpdate(new Millisecond(), 0);
				else if (v > 5)
					channel2.addOrUpdate(new Millisecond(), 5);
				else
					channel2.addOrUpdate(new Millisecond(), v);
			} else {
				channel2.addOrUpdate(new Millisecond(),
						getAvgMagnitude(channel2));
			}
			channel2.fireSeriesChanged();
			CommandsManager.getCM().setvValue2Text(v);
		}
	}

	private void setChannel1(boolean selected) {
		if (!selected) {
			dataset.removeSeries(channel1);
		} else {
			dataset.addSeries(channel1);
		}

	}

	private void setChannel2(boolean selected) {
		if (!selected) {
			dataset.removeSeries(channel2);
		} else {
			dataset.addSeries(channel2);
		}
	}

	public TimeSeries getSeries() {
		return channel1;
	}

	public void setCh1Mode(CurrentMode ch1Mode) {
		this.ch1Mode = ch1Mode;
	}

	public void setCh2Mode(CurrentMode ch2Mode) {
		this.ch2Mode = ch2Mode;
	}

	public void setCh1Active(boolean ch1Active) {
		setChannel1(ch1Active);
		this.ch1Active = ch1Active;
	}

	public void setCh2Active(boolean ch2Active) {
		setChannel2(ch2Active);
		this.ch2Active = ch2Active;
	}
	
	public boolean isChannel1Active(){
		return ch1Active;
	}
	
	public boolean isChannel2Active(){
		return ch2Active;
	}

}