package com.m2dl.fenwicklife.ui;

import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import com.m2dl.fenwicklife.engine.service.IGlobalStatus;
import com.m2dl.fenwicklife.ui.drawables.BasicDrawable;
import com.m2dl.fenwicklife.ui.drawables.Drawable;
import com.m2dl.fenwicklife.ui.drawables.object.BoxDrawable;
import com.m2dl.fenwicklife.ui.drawables.object.FenwickDrawable;
import com.m2dl.fenwicklife.ui.drawables.object.WallDrawable;
import com.m2dl.fenwicklife.xmlrpc.consumer.ConsumerImpl;
import com.m2dl.fenwicklife.xmlrpc.consumer.IConsumer;
/**
 * Main of the UI
 *
 */
public class UIMain {
	// Displayed panel
	private static ScenePanel panel;
	// Timer to refresh informations 
	private static Timer timer;
	// Engine server adress
	private static String serverAdress = "127.0.0.1";
	// engine server port
	private static Integer serverPort = 8081;
	// consumer to do xml-rpc request
	private static IConsumer consumer;
	
	public static void main(String[] args) {
		// Get params for 
		if (args.length == 1) {
			serverAdress = args[0];
		}
		if (args.length >= 2) {
			serverAdress = args[0];
			serverPort = new Integer(args[1]).intValue();
		}

		// Initialize the consumer
		consumer = new ConsumerImpl(serverAdress, serverPort, IGlobalStatus.class);
		
		// Initialize the windows
		JFrame window = new JFrame();
	    window.setVisible(true);
	    window.setSize(600, 400);
	    window.setTitle("Fenwick Life UI");
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    // Init the panel to display drawables
	    panel = new ScenePanel(1, 1);
	    window.add(panel);
	    
	    // Launch the timer to get current state from engine server
	    refreshdisplay();   
	    TimerTask task = new TimerTask()
		{
			@Override
			public void run() 
			{
				// Refresh the UI
				refreshdisplay();
			}	
		};
		timer = new Timer();
		timer.schedule(task, 0, 100);//AtFixedRate
	}
	
	/**
	 * Launch a request to the engine server and refresh display
	 */
	public static void refreshdisplay() {
		// Retrieve scene grid size
		Object[] paramsIntern = { 0 };

		String engineData = (String) consumer.consumeService( "getSize", paramsIntern );
		String[] sizeArray = engineData.split(":");
		panel.setVirtualSize(Integer.parseInt(sizeArray[0]), Integer.parseInt(sizeArray[1]));
		// Retrieve all object position and display them 
		engineData = (String) consumer.consumeService( "getAllPositions", paramsIntern );
	    if(engineData == null) {
	    	return;
	    }
		String[] engineDataLines = engineData.split("\n");
	    if(engineDataLines == null) {
	    	return;
	    }
	    panel.reset();
	    for(String line : engineDataLines) {
	    	if(line.matches("[0-9]*:[0-9]*:[a-zA-Z]*:[0-9]:[0-9]")) {
	    		String[] lineInformations = line.split(":");
		    	int x = Integer.parseInt(lineInformations[0]);
		    	int y = Integer.parseInt(lineInformations[1]);
		    	String type = lineInformations[2];
		    	boolean hasAgent = Integer.parseInt(lineInformations[3]) == 1;
		    	boolean hasBox = Integer.parseInt(lineInformations[4]) == 1;
		    			
		    	switch(type) {
		    	case "Wall" :
		    		panel.addGraphics(new WallDrawable(x, y));
		    		break;
		    	case "Home" :
		    		panel.addGraphics(new Drawable(x, y, BasicDrawable.RECTANGLE, Color.decode("0x40FF87")));
		    		break;
		    	case "Storage" :
		    		panel.addGraphics(new Drawable(x, y, BasicDrawable.RECTANGLE, Color.decode("0xCC6D14")));
		    		break;
		    	}
		    	if( hasAgent ) {
		    		panel.addGraphics(new FenwickDrawable(x, y, !hasBox));
		    	}
		    	else if( hasBox ) {
		    		panel.addGraphics(new BoxDrawable(x, y));
		    	}
	    	}
	    }
	    panel.repaint();
	}

}
