import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.Date;


public class Server_Dep {
	public static void main(String []args){											//To department number dinetai os monadiko argument
		Socket client;
		ObjectInputStream input;
		ObjectOutputStream output;
		Customer lock=new Customer(1,1,1);											//Variable for locking
		Customer []customer=new Customer[500];										//Pinakas gia na diavasei topika tous customers
		int from_dep=Integer.parseInt(args[0]);										//To Dep tou local_server os argument
		//int from_dep=0;
		int query,account,account_to,action,dep,id;double money;					//O arithmos dep dinetai apo ta arguments
		try{
			int port=11111;															//Port gia sindesi me Main server
			System.out.println("Attemting Connection With Main Server");
			client=new Socket((Inet4Address)Inet4Address.getByName( "192.168.1.2"),port);		//Local Ip address,kai aitima sindesis
			System.out.println("Connected");													//Epitixeia sindesis
			output=new ObjectOutputStream(client.getOutputStream());							//Initializing the I/O Streams
			output.flush();
			input=new ObjectInputStream(client.getInputStream());
			long start_time=System.currentTimeMillis();
			File f=new File("customers.txt");													//Diavasma ton Customers pou anikoun sto dep=from_dep
			try{
				FileReader fr=new FileReader(f);
				BufferedReader br=new BufferedReader(fr);
				System.out.println("Opening Customer.txt...");									//cs=arithmos pou anikoun sto from_dep
				int cs=0;																		//Variable for the customers that belong to from_dep
				for(int i=0;i<1000;i++){
					id=Integer.parseInt(br.readLine());
					dep=Integer.parseInt(br.readLine());
					money=Double.parseDouble(br.readLine());
					if(dep==from_dep){															//Diavazei ta topika arxeia tou dep pou aforoun
						customer[cs]=new Customer(id,dep,money);								//Mono gia from_dep=dep
						System.out.println(customer[cs].toString());
						cs++;
					}
					
				}
				System.out.println("Closing Customer.txt...");
				br.close();
				fr.close();
			}catch(IOException e2){System.out.println("Error in reading the customer file");}
																								//Afou diavase ton pinaka tora diavazei ton forto kai exipiretei
			try{
				File frt; 												
				if(from_dep==0) frt=new File("Fortos1.txt");									//An einai to dep 1 anixe to forto 1
				else frt=new File("Fortos2.txt");												//Allios ton forto gia to deutero
				//if(from_dep==0) frt=new File("Fortos_Random_1.txt");							//Antistoixa gia to anigma allou set fortou
				//else frt=new File("Fortos_Random_2.txt");							
				FileReader ft=new FileReader(frt);
				BufferedReader bt=new BufferedReader(ft);
				int displace;
				if(from_dep==0)displace=0;
				else displace=500;
			for(int i=0;i<300000;i++){															//Exipiretisi ton queries ,oses ginetai topika 
				dep=Integer.parseInt(bt.readLine());											//Allios ston Main server
				query=Integer.parseInt(bt.readLine());
				System.out.println("Dep: "+dep+" query "+query);
				if(query==1){
					account=Integer.parseInt(bt.readLine());
					Query q=new Query(dep,query,account);
					if(q.getAccount()-displace<=500&&q.getAccount()-displace>0){												//If is a local query ,apantise topika...Edo den thelei enimerosi o server
						System.out.println("O account: "+customer[q.getAccount()-displace-1].getId()+" ,exei: "+customer[q.getAccount()-displace-1].getMoney()+"  €,i= "+i+" state of query:(local)");
						output.writeObject(q);														//Fainetai kai i diafora sta dio diavasmata ,																			
						double m1=(double)input.readObject();										//sto 1 exoume prosvasi ston pinaka ,eno sto allo oxi
						synchronized (lock){														//Klidonei ton topiko pinaka kai allazei tin timi tou
							customer[q.getAccount()-displace-1].setMoney(m1);						//Allaxe tin topiki timi,pou simainei oti allaxe apo to Dep 2
						}
					}else{																			//Edo tha to steiloume ston kentriko........
						output.writeObject(q);														//Fainetai kai i diafora sta dio diavasmata ,																			
						double m1=(double)input.readObject();										//sto 1 exoume prosvasi ston pinaka ,eno sto allo oxi
						System.out.println("O account: "+q.getAccount()+" ,exei: "+m1+"  €,i= "+i+" state of query:(Main server)");
					}
				}else if(query==2){
					account=Integer.parseInt(bt.readLine());
					action=Integer.parseInt(bt.readLine());
					money=Double.parseDouble(bt.readLine());
					Query q=new Query(dep,query,account,action,money);															//Query gia Prosthesi h' afairesi apo ena account
					if(q.getAccount()-displace<=500 && q.getAccount()-displace>0){																					//Means its local 
						synchronized (lock){																					//Klidonei ton pinaka
							if(q.getAction()==0)																				//Add Money
								customer[q.getAccount()-displace-1].setMoney(customer[q.getAccount()-displace-1].getMoney()+q.getMoney());		//Cusrrent money + the new ammount
							else{
								if(customer[q.getAccount()-displace-1].getMoney()-q.getMoney() > 0)										//Only if is possible sustruct the money
									customer[q.getAccount()-displace-1].setMoney(customer[q.getAccount()-displace-1].getMoney()-q.getMoney());	//Changes localy
							}
						}
						output.writeObject(q);																	//It will add the money to server,and send the answer
						double m2=(double)input.readObject();													//reads the pesponse and do nothing,just for sync
						if(customer[q.getAccount()-displace-1].getMoney() !=m2)											//An h timi tou server einai diafori tis dikia mas 
							synchronized (lock){customer[q.getAccount()-displace-1].setMoney(m2);}						//Simainei pos allaxe ston server opote to neo ammou+money einai to m2 				
						System.out.println("Account: "+customer[q.getAccount()-displace-1].getId()+" has,after the transaction: "+customer[q.getAccount()-displace-1].getMoney()+" €,i= "+i+" state of query:(local)");
					}else{																						//To account den se auto ton server
						output.writeObject(q);
						double m2=(double)input.readObject();													//Diavazei to ammount pou mas esteile o server kai emfanizeo
						System.out.println("Account: "+q.getAccount()+" has,after the transaction: "+m2+" €,i= "+i+" state of query:(Main server)");
					}
					
				}else if(query==3){																				//Take from one account and add to an other
					account=Integer.parseInt(bt.readLine());
					account_to=Integer.parseInt(bt.readLine());
					action=Integer.parseInt(bt.readLine());
					money=Double.parseDouble(bt.readLine());
					System.out.println("Dep: "+dep+" query: "+query+" account: "+account+" account_to "+account_to);
					Query q=new Query(dep,query,account,account_to,action,money);
					if(q.getAccount()-displace<=500 && q.getAccount()-displace>0&& q.getAccount_to()-displace<=500&& q.getAccount_to()-displace>0){  //If both are local																//Local Query,both accounts are here
						synchronized (lock){
							if(customer[q.getAccount()-displace-1].getMoney()-q.getMoney() > 0){												//Only if is possible sustruct the money
								customer[q.getAccount()-displace-1].setMoney(customer[q.getAccount()-displace-1].getMoney()- q.getMoney());			//Current money - ammount
								customer[q.getAccount_to()-displace-1].setMoney(customer[q.getAccount_to()-displace-1].getMoney()+ q.getMoney());		//Current money + the substructed
							}
						}
						output.writeObject(q);																						//sends the query for update to the server
						double m2=(double)input.readObject();																		//reads the pesponse and do nothing
						synchronized (lock){//Edo elegxei an anikei sto dep tou
							if(q.getAccount()-displace<=500 && q.getAccount()-displace>0&& customer[q.getAccount()-displace-1].getMoney() !=m2 )customer[q.getAccount()-displace-1].setMoney(m2);					//If there was a change in the server
							if(q.getAction()==0 && customer[q.getAccount()-1].getMoney() !=m2)																//if the account is changed
								if(customer[q.getAccount()-displace-1].getDep()==from_dep)customer[q.getAccount_to()-displace-1].setMoney(customer[q.getAccount_to()-displace-1].getMoney()+q.getMoney());
							else
								if(customer[q.getAccount()-displace-1].getDep()==from_dep)customer[q.getAccount_to()-displace-1].setMoney(customer[q.getAccount_to()-displace-1].getMoney()-q.getMoney());
						}																																	//Updates both accounts
						System.out.println("Account: "+customer[q.getAccount()-displace-1].getId()+" has,after the transaction: "+customer[q.getAccount()-displace-1].getMoney()+" €,i= "+i+" state of query:(local)");
					}else {																
						output.writeObject(q);															//else send to main server
						double m3=(double)input.readObject();											//Sends to server 
						synchronized (lock){															//Locks the array localy and 
							if(q.getAccount()-displace<=500&& q.getAccount()-displace>0)customer[q.getAccount()-displace-1].setMoney(m3);			//If local update one account
							if(q.getAction()==0)														//If the other is local too,update it too
								if(q.getAccount_to()-displace<=500&& q.getAccount_to()-displace>0)customer[q.getAccount_to()-displace-1].setMoney(customer[q.getAccount_to()-displace-1].getMoney()+q.getMoney());
							else																		//Otan ta steiloume sto server ginetai update ekei
								if(q.getAccount_to()-displace<=500&& q.getAccount_to()-displace>0)customer[q.getAccount_to()-displace-1].setMoney(customer[q.getAccount_to()-displace-1].getMoney()-q.getMoney());				
						}
						System.out.println("Account: "+q.getAccount()+" has,after the transaction: "+m3+" €,i= "+i+" state of query:(Main server)");	//That was a main server query
					}
				}else if(query==4){												//Mesos oros
					Query q4=new Query(dep,query);
					output.writeObject(q4);										//Locks the array in the Main server,opote den tha allaxei i timi tou ekei
					synchronized (lock){										//Exasfalizei oti den tha mpi kanis
						Query q=new Query(dep,query);
						int N=0;double mesos=0, sum=0;
						for(int j=0;j<500;j++){
							if(customer[j].getDep()==q.getDep()){
								sum +=customer[j].getMoney();					//Adding his money to the whole
								N++;											//+1 customer from the query dep
							}
						}
						if (N!=0) mesos=sum/N;
						Date date=new Date();
						System.out.println("H metrisi tou mesou orou eginai stis: "+date);
						System.out.println("\tThe Department: "+(q.getDep()+1)+" has a median money value: "+mesos+" ,i= "+i+" state of query:(local)");
					}
					output.writeObject(new Query(0,0,0));						//Stelnei ena "keno" query kai xeklidonei o server
				}
			}
			bt.close();
			long end_time=System.currentTimeMillis();
			long duration=end_time-start_time;
			double secs=duration/1000.0;
			System.out.println("Done in: "+duration+"="+secs+" seconds");
			System.out.println("Disconnecting now");
			}catch(Exception e3){System.out.println("Error in fortos reading");}
			input.close();
			output.close();
			client.close();
		}catch(IOException e1){System.out.println("Error in connection");}
	}
}