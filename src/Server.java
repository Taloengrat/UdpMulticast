import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Server implements Runnable{

	public TextField txfIp, txfPort, txfMes; // Textfield
    public JFrame f = new JFrame("Server"); // Frame

    public JPanel panelstart = new JPanel();
    public JPanel panelCenter = new JPanel();
    public JPanel panelPageEnd = new JPanel();
    public TextArea textArea = new TextArea(10, 60);
    public Label labelIp, labelPort, labelMes; // Label
    public Button btConnect = new Button("Connect");
    public Button btSend = new Button("Send");
    public Button btSendAll = new Button("Send All");
    public TextField txCountClient = new TextField();
    public String msgIn = "";
    public static int Countclient = 0;
    
    public DatagramSocket socket;
    public DatagramPacket packet;
    public DatagramPacket packetGet;

    public InetAddress address;
    public byte[] buf;
    
    public MulticastSocket socketGet;
    public String msgGet="";
    public byte[] bufGet = new byte[256];
    
    
    
	public Server() {
		// TODO Auto-generated constructor stub
		init();
	}
	
    private void init() {
        textArea.setEditable(false);
        textArea.setFocusable(false);

        btSend.setBackground(Color.blue);
        btSend.setForeground(Color.white);

        f.setSize(397, 364);
        f.getContentPane().setLayout(new BorderLayout(10,10));

        txfIp = new TextField(10);
        txfPort = new TextField(10);
        txfMes = new TextField(20);
      
        labelIp = new Label("IP");
        labelIp.setSize(18, 18);
        labelPort = new Label("PORT");
        labelPort.setSize(18, 18);
        labelMes = new Label("Message");
        labelMes.setSize(18, 18);
        labelMes.setForeground(Color.white);

        panelPageEnd.setBackground(Color.GREEN);

        panelCenter.setSize(1000, 500); // Set panel center
        panelCenter.setBackground(Color.black);

        panelPageEnd.setSize(1000, 500);
        panelPageEnd.setBackground(Color.BLACK);

        btConnect.setBackground(Color.GREEN);

        txfIp.setBounds(130, 100, 100, 20);

        txfPort.setBounds(130, 100, 100, 20);

        // Panel Start add componant
        panelstart.add(new Label("Client"));

        txCountClient.setText(String.valueOf(Countclient));
        txCountClient.setEditable(false);
        txCountClient.setFocusable(false);
        panelstart.add(txCountClient);

        // Panel Center add componant

        panelCenter.add(textArea);

        // Panel PageEnd add componant

        panelPageEnd.add(labelMes);
        panelPageEnd.add(txfMes);
        panelPageEnd.add(btSend);
        panelPageEnd.add(btSendAll);

        f.getContentPane().add(panelstart, BorderLayout.PAGE_START);
        f.getContentPane().add(panelCenter, BorderLayout.CENTER);
        f.getContentPane().add(panelPageEnd, BorderLayout.PAGE_END);

        f.setLocation(300, 300);
        f.pack();

        f.setVisible(true);
        /////////////////////////////////////////////////////////////////
        //////////////////// Event anoter in app ////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////

        txfMes.addActionListener(SendData);
        btSend.addActionListener(SendData);
        
//        btSendAll.addActionListener(SendAll);

    }
    
	ActionListener SendData = new ActionListener() {
		
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
			if (txfMes.getText().isEmpty()) {
			JOptionPane.showMessageDialog(panelPageEnd, "Please put message");
			}else {
				try {
				SendUDPmessage(txfMes.getText(), "230.0.0.0", 2712);
				} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				}
			}
		
		}	
	};

	public void SendUDPmessage(String msg, String ipAddress, int port) throws Exception{
		socket = new DatagramSocket();
		address = InetAddress.getByName(ipAddress);
		buf = ("Server : "+msg).getBytes();
		
		packet = new DatagramPacket(buf, buf.length, address, port);
		socket.send(packet);
		socket.close();
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			socketGet = new MulticastSocket(2712);
			address = InetAddress.getByName("230.0.0.0");
			socketGet.joinGroup(address);
			
			while(!msgGet.equals("exit")) {
				
				 packetGet = new DatagramPacket(bufGet, bufGet.length);
				
				socketGet.receive(packetGet);
				 msgGet = new String(packetGet.getData(), packetGet.getOffset(), packetGet.getLength());
				
				textArea.setText(textArea.getText()+"\n"+msgGet);
				
				System.out.println("Server Multicast UDP message received : "+ msgGet);
				
				
			}
			
			socketGet.leaveGroup(address);
			socketGet.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
