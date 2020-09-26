import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;


public class ServerListener {
	private ServerSocket server;										//Socket pou ginontai ta aitimata
	private int port=11111;												//Port pou "alouei" to socket
	int dep=0,id=0;
	double money=1.0;
	public  Customer []customer=new Customer[1000];						//Pinakas pou tha diavasoume tous customer
	public ServerListener (){ }											//O constructor einai kenos
	
	public void loop(){
		
		try{
			server=new ServerSocket(port);															//Arxikopoiisi tou socket sto port 
			System.out.println("Accepting connections,for clients...");
			File f=new File("customers.txt");														//Anoigma tou arxeiou me tous pelates
			try{
				FileReader fr=new FileReader(f);
				BufferedReader br=new BufferedReader(fr);
				System.out.println("Opening Customer.txt...");
				for(int i=0;i<1000;i++){
					id=Integer.parseInt(br.readLine());
					dep=Integer.parseInt(br.readLine());
					money=Double.parseDouble(br.readLine());
					customer[i]=new Customer(id,dep,money);	
					System.out.println(customer[i].toString());										//Diavasma kai emfanisi
				}
				System.out.println("Closing Customer.txt...");
				System.out.println("Accepting connections,for clients...");
				br.close();
			}catch(IOException e){System.out.println("Error in reading server");}

			while(true){																		//Accepting connections and excecuting in a seperate thread
				Server_1 c=new Server_1(server.accept(),customer);								//if a connection is has been accepted,it is passed in the server_1 class and the-
				Thread t=new Thread(c);															//thread that will ecommunicate with the client is started
				t.start();
			}
		}catch(Exception e){System.out.println("Server Listener Exception");}	
	}//end of loop 
	
}
