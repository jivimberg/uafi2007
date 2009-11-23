package view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import control.Application;

@SuppressWarnings("serial")
public class MainWindow extends JFrame{
	
	private OptionsPanel optionsPanel;
	private VoltageChart graph;
	private InfoPanel infoPanel;
	

	public MainWindow(Application app) {
		
		setLayout(new BorderLayout());
		optionsPanel = new OptionsPanel(app);
		infoPanel = new InfoPanel(app);
		graph = new VoltageChart();

		add(optionsPanel, BorderLayout.NORTH);
		add(graph, BorderLayout.CENTER);
		add(infoPanel, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(new Dimension(700,600));
		setVisible(true);
	}

	public JPanel getChart() {
		return graph;
	}

	public OptionsPanel getOptionsPanel() {
		return optionsPanel;
	}

	public VoltageChart getGraph() {
		return graph;
	}

	public InfoPanel getInfoPanel() {
		return infoPanel;
	}
	
}
