import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Server_1 implements Runnable{														//Kentrikoipoiimenin o client stelnei mono query den exei prosvasi se arxeia
	private Socket client;																		//Edo mpainei to socket tou client
	private ObjectInputStream input;
	private ObjectOutputStream output;															//Kanalia epikoinonias me client
	Customer []customer;																		//Pinakas pou apothikeuetai o pinakas apo to Server_Listener
	Customer c;
	Server_1(Socket client,Customer []customer){												//Constructor
		this.client=client;																		//Socket Client
		this.customer=customer;																	//O pinakas pou kaname pass
		this.c=new Customer(1,1,1);																//Arxikopoiisi enos c,pou xrisimoipoiitai os lock
	}
	
	public void run() {
		System.out.println("Client Connected ....");
		try{
			output=new ObjectOutputStream(client.getOutputStream());
			output.flush();																		
			input=new ObjectInputStream(client.getInputStream());														//Arxikopoiisi kanalion
					try{
						for(int i=0;i<300000;i++){																		//Diavazei ola ta querys
							Query query=(Query)input.readObject();														//Diavasma enos Query kai dimiourgia antikeimenou
						synchronized(c){																				//Edo mpainei ena lock gia na min kanoun kai ta dio thread prosvasi ston pinaka
							if(query.getQuery()==1){
								output.writeObject(customer[query.getAccount()-1].getMoney());							//An einai tipou ena girna to ammount
							}else if (query.getQuery()==2){																//An einai tipou 2 allaxe to ammount
								if(query.getAction()==0)																//Add
									customer[query.getAccount()-1].setMoney(customer[query.getAccount()-1].getMoney()+query.getMoney());//Cusrrent money + the ammount
								else{
									if(customer[query.getAccount()-1].getMoney()-query.getMoney() > 0)					//Only if is possible sustruct the money
										customer[query.getAccount()-1].setMoney(customer[query.getAccount()-1].getMoney()-query.getMoney());//Cusrrent money - the ammount
								}
								output.writeObject(customer[query.getAccount()-1].getMoney());							//Sending back the new account
							}else if (query.getQuery()==3){																//An einai tipou 3
								if(customer[query.getAccount()-1].getMoney()-query.getMoney() > 0){						//Only if is possible sustruct the money
									customer[query.getAccount()-1].setMoney(customer[query.getAccount()-1].getMoney()- query.getMoney());			//Current money - ammount
									customer[query.getAccount_to()-1].setMoney(customer[query.getAccount_to()-1].getMoney()+ query.getMoney());		//Add the money to the second account
								}
								output.writeObject(customer[query.getAccount()-1].getMoney());														//Stelnei piso to neo ammount
							}else if(query.getQuery()==4){												//Ipologismos meso orou
								int N=0;double mesos=0, sum=0;											//Afou einai se sync bloclk den allazei kapio kata tin diarkeia tou ipologismou
								for(int j=0;j<1000;j++){
									if(customer[j].getDep()==query.getDep()){							//An anikei sti dep pou ekane to query,diladi ta misa accounts
										sum +=customer[j].getMoney();									//Adding his momey to the whole
										N++;															//+1 customer from the query dep
									}
								}
								if (N!=0) mesos=sum/N;
								output.writeObject(mesos);												//Send back the median
							}
						}																				//Edo apeleftheronaite i prosvasi ston pinaka
					}
					}catch(IOException e){System.out.println("Exception in response");}
			System.out.println("Client Disconnected.");													//Edo exei teliosei i epexergasia ton query
			input.close();
			output.close();																				//Klisimo kanalion 
			client.close();																				//Client disconnect
		}catch(Exception e){System.out.println("Server Exception");}
	}//end of run 
	
}
