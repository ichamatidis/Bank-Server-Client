import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Server_Main implements Runnable{
	private Socket client;												//For connecting with the deps socket
	ObjectOutputStream output;
	ObjectInputStream  input;
	int dep=0,id=0;
	double money=1.0;
	public  Customer []customer;
	
	public Server_Main (Socket client,Customer []customer){
		this.client=client;
		this.customer=customer;
	}
	
	public void run(){
		System.out.println("Client connected ....");
		try{
			output=new ObjectOutputStream(client.getOutputStream());
			output.flush();
			input=new ObjectInputStream(client.getInputStream());
			Customer lock =new Customer(1,1,1);
			while(true){												//while the server of dep is connected
				Query query=(Query)input.readObject();
				if(query.getQuery()==1){
					output.writeObject(customer[query.getAccount()-1].getMoney());//
				}else if(query.getQuery()==2){
					synchronized (lock){																										//Lock the Main array
					if(query.getAction()==0)																									//Add
						customer[query.getAccount()-1].setMoney(customer[query.getAccount()-1].getMoney()+query.getMoney());					//Cusrrent money + the new ammount
					else{
						if(customer[query.getAccount()-1].getMoney()-query.getMoney() > 0)														//Only if is possible sustruct the money
							customer[query.getAccount()-1].setMoney(customer[query.getAccount()-1].getMoney()-query.getMoney());
					}
					output.writeObject(customer[query.getAccount()-1].getMoney());																//Sending back the new account
					}
				}else if(query.getQuery()==3){																									//Iparxei periptosi ena account na allaxeo apo to allo dep sto poio den anikei 
					synchronized (lock){																										//Gia auto elegxetai apo to Dep_Server
						if(customer[query.getAccount()-1].getMoney()-query.getMoney() > 0){														//Only if is possible sustruct the money
							customer[query.getAccount()-1].setMoney(customer[query.getAccount()-1].getMoney()- query.getMoney());				//Current money - ammount
							customer[query.getAccount_to()-1].setMoney(customer[query.getAccount_to()-1].getMoney()+ query.getMoney());
						}
						output.writeObject(customer[query.getAccount()-1].getMoney());															//Sends back the new value 
					}
				}else if(query.getQuery()==4){
					Query qlock=new Query(1,1,1);										//Otan erthei sima gia type=4
					synchronized(lock){													//Klidonei ton Pinaka Customer tou Main server mexri na erthi sima gia xeklidoma
						while(qlock.getQuery()!=0){
							qlock=(Query)input.readObject();							//Sima gia xeklidoma
						}
					} 
				}	
			if(client.isClosed())break;													//Epidei den xeroume posa aitimata tha erthoun sto Main einai se While
			}
			System.out.println("Client Disconnected.");
			input.close();
			output.close();
			client.close();
		}catch(Exception e){System.out.println("Client Disconnected...");}
	}
}
