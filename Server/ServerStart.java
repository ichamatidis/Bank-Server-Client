
public class ServerStart {
	public static void main(String args[]){
		ServerListener s= new ServerListener(); 			//Makes a server that listens for connection
		s.loop();											//Start the listening for connections
		
	}
}
