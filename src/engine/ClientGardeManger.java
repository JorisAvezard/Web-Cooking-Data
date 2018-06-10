package engine;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientGardeManger {
	
	public String serverName = "192.168.137.166";
    public int serverPort = 8888;
    
    public ClientGardeManger() {
    	
    }
    
    public void envoi() throws UnknownHostException, IOException {
    	Socket socket;
		socket = new Socket(serverName, serverPort);
		System.out.println("Socket client: " + socket);
		socket.close();
    }
}
