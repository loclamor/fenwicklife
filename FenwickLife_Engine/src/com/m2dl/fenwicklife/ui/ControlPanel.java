package com.m2dl.fenwicklife.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ControlPanel extends JPanel {

	private static final String PAUSE_TEXT = "Pause";

	private JButton stopButton = new JButton(PAUSE_TEXT);

	public ControlPanel() {
		super();

		this.add(stopButton);
		stopButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("button pushed");
				UIEngineProxy.getInstance().pause();
			}
		});
	}
}
