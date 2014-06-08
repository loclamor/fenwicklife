package com.m2dl.fenwicklife.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ControlPanel extends JPanel {

	private static final String PAUSE_TEXT = "Pause";
	private static final String NEXT_TEXT = "Next";
	private static final String SPEED_UP_TEXT = "Increase Speed";
	private static final String SPEED_DOWN_TEXT = "Decrease Speed";

	private JButton stopButton = new JButton(PAUSE_TEXT);
	private JButton nextButton = new JButton(NEXT_TEXT);
	private JButton speedUpButton = new JButton(SPEED_UP_TEXT);
	private JButton speedDownButton = new JButton(SPEED_DOWN_TEXT);

	public ControlPanel() {
		super();

		this.add(stopButton);
		stopButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				UIEngineProxy.getInstance().pause();
			}
		});
		
		
		
		this.add(nextButton);
		nextButton.setEnabled(false); // remove when working
		nextButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO
			}
		});
		
		this.add(speedUpButton);
		speedUpButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				UIEngineProxy.getInstance().speedUp();
			}
		});
		
		this.add(speedDownButton);
		speedDownButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				UIEngineProxy.getInstance().speedDown();
			}
		});
	}
}
