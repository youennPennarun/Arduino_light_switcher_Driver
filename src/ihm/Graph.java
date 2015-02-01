package ihm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import arduino.ArduinoConnector;

import communication.HASocket;

public class Graph extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel jp;
    JPanel jp2;
    Lampe l1 = new Lampe();
    Lampe l2 = new Lampe();
    Lampe l3 = new Lampe();
    
    public Graph() {
        super("Simulation maison");
        setSize(760, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        try {
			Image img = ImageIO.read(this.getClass().getResource(("/background.png")));
	        setContentPane(new JLabel(new ImageIcon(img)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        setLayout(new BorderLayout());
        
        l1.setOpaque(false);
        l2.setOpaque(false);
        l3.setOpaque(false);
        
		add(l1,BorderLayout.WEST);
		add(l3,BorderLayout.CENTER);
		add(l2,BorderLayout.EAST);
		l1.sety(40);
		l3.sety(130);
    }
    public void lightStateChange(int id,boolean isOn){
    	Lampe l = null;
    	if(id == 1){
    		l=l1;
    	}else if(id == 2){
    		l=l2;
    	}else if(id == 3){
    		l=l3;
    	}
    	if(isOn){
    		l.allumer();
    	}else{
    		l.eteindre();
    	}
    }
    public static void main(String[] args) {
    	
    	
		
    	
    	ArduinoConnector main = new ArduinoConnector();
		main.initialize();
		Thread t=new Thread() {
			public void run() {
				//the following line will keep this app alive for 1000 seconds,
				//waiting for events to occur and responding to them (printing incoming messages to console).
				try {Thread.sleep(1000000);} catch (InterruptedException ie) {}
			}
		};
		t.start();
		System.out.println("Started");
        
		final HASocket h = new HASocket(main);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
		    	Graph g1 = new Graph();
		    	h.connect(g1);
			}
		});
    }

    class Lampe extends JPanel {
    	
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int statut = 0; 												// 0 eteint , 1 allume
		private Image monimage = getToolkit().getImage("images\\eteinte.png");
		private int posx = 60;
		private int posy = 70;
    	
        public Lampe() {
            setPreferredSize(new Dimension(300, 300));
            
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);   
            g.drawImage(monimage, posx,posy,this);
          
        }
        public void setx(int s){
        	posx=s;
        }
        public void sety(int s){
        	posy=s;
        }
               
        public void allumer(){
        	this.statut=1;
        	try {
				this.monimage = ImageIO.read(this.getClass().getResource("/allumee.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	repaint();
        }
        public void eteindre(){
        	this.statut=0;
        	try {
				this.monimage = ImageIO.read(this.getClass().getResource(("/eteinte.png")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	repaint();
        }
        public boolean eteinte(){
        	return statut==0;
        }
        
        
        
    }

}