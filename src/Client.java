import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
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
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.plaf.SliderUI;

public class Client implements Runnable{
	public TextField txfIp, txfPort, txfName, txfMes; // Textfield
    public JFrame f;
    public JPanel panelPageStart;
    public JPanel panelCenter;
    public JPanel panelPageEnd;
    public TextArea textArea;
    public Label labelIp, labelPort, labelName, labelMes;
    public Button btConnect;
    public Button btSend;
    public String msg="";
    
    byte[] buf = new byte[256];
    public MulticastSocket socket;
    public InetAddress address;
    
    
    byte[] bufSend ;
    public DatagramSocket socketSend;
    public DatagramPacket packet;
    
	public Client() {
		f = new JFrame("Client");
        panelPageStart = new JPanel(); // Panel
        panelCenter = new JPanel();
        panelPageEnd = new JPanel();
        textArea = new TextArea(10, 60);
        btConnect = new Button("Connect");
        btSend = new Button("Send");
        msg = "";
        
		init();
	}

	@Override
	public void run() {
		
		// TODO Auto-generated method stub
		System.out.println("Client Running");
		
		try {
			socket = new MulticastSocket(2712);
			address = InetAddress.getByName("230.0.0.0");
			socket.joinGroup(address);
			
			while(!msg.equals("exit")) {
				
				 packet = new DatagramPacket(buf, buf.length);
				
				socket.receive(packet);
				 msg = new String(packet.getData(), packet.getOffset(), packet.getLength());
				
				textArea.setText(textArea.getText()+"\n"+msg);
				
				System.out.println("Multicast UDP message received : "+ msg);
				
				
			}
			
			socket.leaveGroup(address);
			socket.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
    public void init() {

        textArea.setEditable(false);
        textArea.setFocusable(false);

        btSend.setBackground(Color.blue);
        btSend.setForeground(Color.white);

        f.setSize(500, 364);
        f.getContentPane().setLayout(new BorderLayout(10,10));


        txfIp = new TextField(10);
        txfPort = new TextField(10);

        txfName = new TextField(5);
        txfMes = new TextField(20);


        labelIp = new Label("IP");
        labelIp.setSize(18, 18);
        labelPort = new Label("PORT");
        labelPort.setSize(18, 18);
        labelName = new Label("Name");
        labelName.setSize(18, 18);
        labelMes = new Label("Message");
        labelMes.setSize(18, 18);

        panelPageStart.setLayout(new FlowLayout()); //Set panel start
        panelPageStart.setSize(1000, 500);
        panelCenter.setSize(1000, 500); // Set panel center
        panelCenter.setBackground(Color.blue);
        panelPageEnd.setSize(1000, 500);
        panelPageEnd.setBackground(Color.white);
        btConnect.setBackground(Color.GREEN);

        txfIp.setBounds(130, 100, 100, 20);
        txfPort.setBounds(130, 100, 100, 20);

        // Panel Start add componant
        panelPageStart.add(labelIp);
        panelPageStart.add(txfIp);
        panelPageStart.add(labelPort);
        panelPageStart.add(txfPort);
        panelPageStart.add(labelName);
        panelPageStart.add(txfName);
        panelPageStart.add(btConnect);
        
       

        // Panel Center add componant

        panelCenter.add(textArea);

        // Panel PageEnd add componant
        panelPageEnd.add(labelMes);
        panelPageEnd.add(txfMes);
        panelPageEnd.add(btSend);
        panelPageEnd.setVisible(false);

        f.getContentPane().add(panelPageStart, BorderLayout.PAGE_START);
        f.getContentPane().add(panelCenter, BorderLayout.CENTER);
        f.getContentPane().add(panelPageEnd, BorderLayout.PAGE_END);

        f.setLocation(800, 300);
   

        txfIp.setText("localhost");
        txfPort.setText("2712");
       
        f.setVisible(true);

        /////////////////////////////////////////////////////////////////
        //////////////////// Event anoter in app ////////////////////////
////////////////////////////////////////////////////////////////////////////////////

        
        txfName.addActionListener(ConnectServerAction);
        txfMes.addActionListener(sendAll);
//        
//        
        btSend.addActionListener(sendAll);
        btConnect.addActionListener(ConnectServerAction);

    }

    ActionListener ConnectServerAction = new ActionListener() {		
 		@Override
 		public void actionPerformed(ActionEvent e) {
 			// TODO Auto-generated method stub
 			
 			if (txfIp.getText().isEmpty()) {
 				JOptionPane.showMessageDialog(panelPageStart, "Please put IP!!!");
 			}else if (txfPort.getText().isEmpty()) {
 				JOptionPane.showMessageDialog(panelPageStart, "Please put Port!!!");
 			}else if (txfName.getText().isEmpty()) {
 				JOptionPane.showMessageDialog(panelPageStart, "Please put Name!!!");
 			}else {
 				SetConnectedStyle();
 			}
 			
 		}
 	};
 	
 	 public void SetConnectedStyle() {
 			// TODO Auto-generated method stub
 			
 	    	panelPageEnd.setVisible(true);
 	    	panelPageStart.setBackground(Color.GREEN);
 	    	btConnect.setLabel("Disconnect");
 	    	btConnect.setBackground(Color.gray);
 	    	txfIp.setEditable(false);
 	    	txfPort.setEditable(false);
 	    	txfName.setEditable(false);
 	    	
 	    	txfIp.setFocusable(false);
 	    	txfPort.setFocusable(false);
 	    	txfName.setFocusable(false);
 		}
 	
 	 
    	ActionListener sendAll = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			try {
				SendUDPmessage();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	};
	
	public void SendUDPmessage() throws IOException {
		socketSend = new DatagramSocket();
		address = InetAddress.getByName("230.0.0.0");
		buf = (txfName.getText()+" : "+txfMes.getText()).getBytes();
		
		packet = new DatagramPacket(buf, buf.length, address, 2712);
		socketSend.send(packet);
		socketSend.close();
	}
	
}
