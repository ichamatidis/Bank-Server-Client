import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Server_Main implements Runnable{
	private Socket client;//For connecting with the deps socket
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
			boolean change=false;
			Query t=new Query(1,1,1);
			while(true){//while the server of dep is connected
				Query query=(Query)input.readObject();
				if(query.getQuery()==1){
					if(change==true){
						Query update=(Query)input.readObject();
						Customer temp=new Customer(1,1,1);
						for(int k=0;k<update.getAccount();k++){
							temp=(Customer)input.readObject();
							customer[temp.getId()-1].setMoney(temp.getMoney());
						}
						change=false;
					}
					output.writeObject(customer[query.getAccount()-1].getMoney());//
				}else if(query.getQuery()==2){
					if(change==true){
						Query update=(Query)input.readObject();
						Customer temp=new Customer(1,1,1);
						for(int k=0;k<update.getAccount();k++){
							temp=(Customer)input.readObject();
							customer[temp.getId()-1].setMoney(temp.getMoney());
						}
						change=false;
					}
					synchronized (lock){
					if(query.getAction()==0)//Add
						customer[query.getAccount()-1].setMoney(customer[query.getAccount()-1].getMoney()+query.getMoney());//Cusrrent money + the new ammount
					else{
						if(customer[query.getAccount()-1].getMoney()-query.getMoney() > 0)//Only if is possible sustruct the money
							customer[query.getAccount()-1].setMoney(customer[query.getAccount()-1].getMoney()-query.getMoney());
					}
					output.writeObject(customer[query.getAccount()-1].getMoney());//Sending back the new account//
					}
				}else if(query.getQuery()==3){
					if(change==true){
						Query update=(Query)input.readObject();
						Customer temp=new Customer(1,1,1);
						for(int k=0;k<update.getAccount();k++){
							temp=(Customer)input.readObject();
							customer[temp.getId()-1].setMoney(temp.getMoney());
						}
						change=false;
					}//Asks for update
					synchronized (lock){
						if(customer[query.getAccount()-1].getMoney()-query.getMoney() > 0){//Only if is possible sustruct the money
							customer[query.getAccount()-1].setMoney(customer[query.getAccount()-1].getMoney()- query.getMoney());//Current money - ammount
							customer[query.getAccount_to()-1].setMoney(customer[query.getAccount_to()-1].getMoney()+ query.getMoney());
						}
						output.writeObject(customer[query.getAccount()-1].getMoney());//Sends back the new value 
					}
				}else if(query.getQuery()==4){
					Query qlock=new Query(1,1,1);
					synchronized(lock){while(qlock.getQuery()!=0){qlock=(Query)input.readObject();}}//locks the array until the median is calculated in the dep 
				}else if(query.getQuery()==0){//Auto to minima stelnetai gia na ginei sixrinismos ton minimaton
					//Do nothing
				}
				t=(Query)input.readObject();
				if(t.getAccount()==5) change=true;
				else change=false;
			if(client.isClosed())break;
			}
			System.out.println("Client Disconnected.");
			input.close();
			output.close();
			client.close();
		}catch(Exception e){System.out.println("Client Disconnected...");}
	}
}
