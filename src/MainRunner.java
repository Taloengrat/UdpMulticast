
public class MainRunner {

	public static void main(String[] args) {
		
		new Thread(new Client()).start();
		new Thread(new Client()).start();
		new Thread(new Client()).start();
		new Thread(new Client()).start();
		new Thread(new Client()).start();
		new Thread(new Client()).start();
		
		new Thread(new Server()).start();
		
	}
}
