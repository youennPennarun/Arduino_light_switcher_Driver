package arduino;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Enumeration;

import purejavacomm.CommPortIdentifier;
import purejavacomm.SerialPort;
import purejavacomm.SerialPortEvent;
import purejavacomm.SerialPortEventListener;

public class ArduinoConnector implements SerialPortEventListener {
    SerialPort port = null;
 
    private String appName = getClass().getName();
    private BufferedReader input = null;

	private BufferedWriter output = null;
     
    private static final int TIME_OUT = 1000; // Port open timeout
 
    private static final String PORT_NAMES[] = {  // PORTS
        "tty.usbmodem", // Mac OS X
        "usbdev", // Linux
        "tty", // Linux
        "serial", // Linux
        "COM3", // Windows
    };
     	
 
    public void initialize() {
        try {
            CommPortIdentifier portid = null;
            Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
            System.out.println(port);
            while (portid == null && portEnum.hasMoreElements()) {
                portid = (CommPortIdentifier)portEnum.nextElement();
                if ( portid == null ) 
                    continue;
                 
                System.out.println("Trying: " + portid.getName());
                for ( String portName: PORT_NAMES ) {
                    if ( portid.getName().equals(portName) 
                      || portid.getName().contains(portName)) {  // CONTAINS
                        port = (SerialPort) portid.open("ArduinoJavaComms", TIME_OUT);
                        port.setFlowControlMode(
                                SerialPort.FLOWCONTROL_XONXOFF_IN+ 
                                SerialPort.FLOWCONTROL_XONXOFF_OUT); // FLOW-CONTROL
                        input = new BufferedReader(
                                    new InputStreamReader( port.getInputStream() ));
                        output  = new BufferedWriter(new OutputStreamWriter(port.getOutputStream()));
 
                        System.out.println( "Connected on port: " + portid.getName() );
 
                        port.addEventListener(this);
                        port.notifyOnDataAvailable(true);
                    }
                }
            }
 /*
            while ( true) {
                try { Thread.sleep(100); } catch (Exception ex) { }
            }
            */
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    @Override
    public void serialEvent(SerialPortEvent event) {
        try {
            switch (event.getEventType() ) {
                case SerialPortEvent.DATA_AVAILABLE: 
                    String inputLine = input.readLine();
                    System.out.println(inputLine);
                    break;
 
                default:
                    break;
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void say(char valueOn){
    	if(output != null){
    		try {
    			System.out.println("sending "+valueOn);
				output.write(valueOn);
	    		output.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
}