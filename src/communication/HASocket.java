package communication;

import ihm.Graph;

import java.net.URISyntaxException;

import javax.swing.JOptionPane;

import arduino.ArduinoConnector;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

public class HASocket {
	private final static String url = "https://home-automation-server.herokuapp.com/";
	//private final static String url = "http://localhost:8080/";

	private Socket socket;
	private boolean justConnected = true;
	
	private static HASocket haSocket;

	private static Graph mainFrame;
	private static LightEmiter lightEmiter;
	public static ArduinoConnector driver;
	
	public HASocket(ArduinoConnector main) {
		driver = main;
	}
	public void connect(Graph mainFrame){
		this.setMainFrame(mainFrame);
		HASocket.haSocket = this;
		try {
			socket = IO.socket(HASocket.url);
			socket.on(
					Socket.EVENT_CONNECT, new Emitter.Listener() {
						@Override
						public void call(Object... args) {
							System.out.println("connected");
							getLightEmiter().getLightStates();
						}
					}).on("newClientConnected" ,new Emitter.Listener() {
						@Override
						public void call(Object... args) {
							System.out.println("new Client!");
						}
					}).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
						@Override
						public void call(Object... args) {
							JOptionPane.showMessageDialog(null,
								    "Disconnected from server",
								    "Socket error",
								    JOptionPane.ERROR_MESSAGE);
							System.exit(0);
						}
					}).on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
						@Override
						public void call(Object... args) {
							JOptionPane.showMessageDialog(null,
								    "Disconnected from server",
								    "Socket error",
								    JOptionPane.ERROR_MESSAGE);
							System.exit(0);
						}
					}).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
						@Override
						public void call(Object... args) {
							JOptionPane.showMessageDialog(null,
								    "Connection failed",
								    "Socket error",
								    JOptionPane.ERROR_MESSAGE);
							System.exit(0);
						}
					}).on(Socket.EVENT_ERROR, new Emitter.Listener() {
						@Override
						public void call(Object... args) {
							JOptionPane.showMessageDialog(null,
								    "Connection failed",
								    "Socket error",
								    JOptionPane.ERROR_MESSAGE);
							System.exit(0);
						}
					}).on(Socket.EVENT_RECONNECT_ERROR, new Emitter.Listener() {
						@Override
						public void call(Object... args) {
							JOptionPane.showMessageDialog(null,
								    "Connection failed",
								    "Socket error",
								    JOptionPane.ERROR_MESSAGE);
							System.exit(0);
						}
					});

			setLightEmiter(new LightEmiter(this));
			socket.connect();
		} catch (URISyntaxException e) {
			System.exit(0);
			e.printStackTrace();
		}
	}


	public void setFrame(Graph mainFrame) {
		this.setMainFrame(mainFrame);

	}
	public Socket getSocket() {
		return socket;
		
	}
	
	public static HASocket getHaSocket() {
		return haSocket;
	}
	public static void setHaSocket(HASocket haSocket) {
		HASocket.haSocket = haSocket;
	}
	public static Graph getMainFrame() {
		return mainFrame;
	}
	public void setMainFrame(Graph mainFrame) {
		this.mainFrame = mainFrame;
	}
	public boolean isJustConnected() {
		return justConnected;
	}
	public void setJustConnected(boolean justConnected) {
		this.justConnected = justConnected;
	}
	public static LightEmiter getLightEmiter() {
		return lightEmiter;
	}
	public static void setLightEmiter(LightEmiter lightEmiter) {
		HASocket.lightEmiter = lightEmiter;
	}
	
	
}