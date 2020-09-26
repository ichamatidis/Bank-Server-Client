import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;


public class Server_Main_Listener {
	private ServerSocket server;
	private int port=11111;
	int from_dep;
	int dep,id;double money;
	
	public  Customer []customer=new Customer[1000];
	public Server_Main_Listener (){	}
	
	public void loop(){
		try{
			server=new ServerSocket(port);
			System.out.println("Accepting connections,for servers...");
			File f=new File("customers.txt");
			try{
				FileReader fr=new FileReader(f);
				BufferedReader br=new BufferedReader(fr);
				System.out.println("Opening Customer.txt...");
				id=0;dep=0;money=1.0;
				for(int i=0;i<1000;i++){
					id=Integer.parseInt(br.readLine());
					dep=Integer.parseInt(br.readLine());
					money=Double.parseDouble(br.readLine());
					customer[i]=new Customer(id,dep,money);	
					System.out.println(customer[i].toString());
				}
				System.out.println("Closing Customer.txt...");
				br.close();}catch(IOException e){System.out.println("Error in reading server");}
			System.out.println("Accepting Connections for servers");
			while(true){//Accepting connections and excecuting in a seperate thread
				Server_Main c=new Server_Main(server.accept(),customer);
				Thread t=new Thread(c);
				t.start();
			}
		}catch(Exception e){System.out.println("Server Listener Exception");}
}
}
