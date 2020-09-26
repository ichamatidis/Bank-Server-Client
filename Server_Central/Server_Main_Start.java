
public class Server_Main_Start {
	public static void main(String args[]){
		Server_Main_Listener s= new Server_Main_Listener(); //Starts the loop ,for accepting connection
		s.loop();
		
	}
}
