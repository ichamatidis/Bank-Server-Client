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
	public static void main(String []args){//To department number dinetai os monadiko argument
		Socket client;
		ObjectInputStream input;
		ObjectOutputStream output;
		Customer lock=new Customer(1,1,1);//Variable for locking
		Customer []customer=new Customer[500];//Pinakas gia na diavasei topika tous customers
		int from_dep=Integer.parseInt(args[0]);
		int query,account,account_to,action,dep,id;double money;//O arithmos dep dinetai apo ta arguments
		try{
			int port=11111;			//Port gia sindesi me Main server
			System.out.println("Attemting Connection With Main Server");
			client=new Socket((Inet4Address)Inet4Address.getByName( "192.168.1.2"),port);			//Local Ip address,kai aitima sindesis
			System.out.println("Connected");														//Epitixeia sindesis
			output=new ObjectOutputStream(client.getOutputStream());								//Initializing the I/O Streams
			output.flush();
			input=new ObjectInputStream(client.getInputStream());
			long start_time=System.currentTimeMillis();
			File f=new File("customers.txt");														//Diavasms ton Customers pou anikoun sto dep=from_dep
			try{
				FileReader fr=new FileReader(f);
				BufferedReader br=new BufferedReader(fr);
				System.out.println("Opening Customer.txt...");										//cs=arithmos pou anikoun sto from_dep
				int cs=0;																			//Variable for the customers that belong to from_dep
				for(int i=0;i<1000;i++){
					id=Integer.parseInt(br.readLine());
					dep=Integer.parseInt(br.readLine());
					money=Double.parseDouble(br.readLine());
					if(dep==from_dep){																//Diavazei ta topika arxeia tou dep pou aforoun
						customer[cs]=new Customer(id,dep,money);
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
				if(from_dep==0) frt=new File("Fortos1.txt");	 									//Taxinomimena quries
				else frt=new File("Fortos2.txt");													//An einai to Dep_2 anoigei to 2
				//if(from_dep==0) frt=new File("Fortos_Random_1.txt");							//Antistoixa gia to anigma allou set fortou
				//else frt=new File("Fortos_Random_2.txt");												//Entelos tixaio arxeio me queries
				FileReader ft=new FileReader(frt);
				BufferedReader bt=new BufferedReader(ft);
				Customer []changed=new Customer[500];												//Stores the changes
				for(int i=0;i<500;i++)
					changed[i]=new Customer(1,1,1);
				boolean change=false;																//Means there are no changes
				int ch=0;																			//Holds the number of changed accounts	
				int displace;
				if(from_dep==0)displace=0;
				else displace=500;
			for(int i=0;i<300000;i++){
				dep=Integer.parseInt(bt.readLine());
				query=Integer.parseInt(bt.readLine());
				System.out.println("Dep: "+dep+" query "+query);
				if(query==1){
					account=Integer.parseInt(bt.readLine());
					Query q=new Query(dep,query,account);
					if(q.getAccount()-displace<=500 &&q.getAccount()-displace>0){														//If is a local query ,apantise topika...Edo den thelei enimerosi o server
						output.writeObject(new Query(0,0,0));
						System.out.println("O account: "+customer[q.getAccount()-displace-1].getId()+" ,exei: "+customer[q.getAccount()-displace-1].getMoney()+"  €,i= "+i+" state of query:(local)");
					}else{																			//Edo tha to steiloume ston kentriko........
						output.writeObject(q);														//Fainetai kai i diafora sta dio diavasmata ,sto 1 exoume prosvasi ston pinaka ,eno sto allo oxi
						if(change==true){															//Otan epikoinonisei me ton server ,ananeonei kai tis times tou
							output.writeObject(new Query(ch,ch,ch));								//Sends the number of changes to the server
							for(int k=0;k<ch;k++){
								output.writeObject(changed[k]);										//Sends the changed accounts
							}
							change=false; //Marks the dep as Up_to_date ,there are no changes
							ch=0;
						}
						double m1=(double)input.readObject();
						System.out.println("O account: "+q.getAccount()+" ,exei: "+m1+"  €,i= "+i+" state of query:(Main server)");
						synchronized (lock){														//locks and updates in case there is a change
								if(q.getAccount()-displace<=500 && q.getAccount()-displace>0)												//if the money changed from the other dep update it
									customer[q.getAccount()-displace-1].setMoney(m1);					
							}
					}
				}else if(query==2){
					account=Integer.parseInt(bt.readLine());
					action=Integer.parseInt(bt.readLine());
					money=Double.parseDouble(bt.readLine());
					Query q=new Query(dep,query,account,action,money);
					if(q.getAccount()-displace<=500 && q.getAccount()-displace>0){														//Means its local 
						output.writeObject(new Query(0,0,0));										//Senda a empty message to the server for update
						synchronized (lock){														//Klidonei ton pinaka
							if(q.getAction()==0)//Add
								customer[q.getAccount()-displace-1].setMoney(customer[q.getAccount()-displace-1].getMoney()+q.getMoney());//Cusrrent money + the new ammount
							else{
								if(customer[q.getAccount()-displace-1].getMoney()-q.getMoney() > 0)			//Only if is possible sustruct the money
									customer[q.getAccount()-displace-1].setMoney(customer[q.getAccount()-displace-1].getMoney()-q.getMoney());//changes localy
							}
						}
						change=true;
						changed[ch]=customer[q.getAccount()-displace-1];										//Puts the changed in a array
						ch++;																		//one more changed
						System.out.println("Account: "+customer[q.getAccount()-displace-1].getId()+" has,after the transaction: "+customer[q.getAccount()-displace-1].getMoney()+" €,i= "+i+" state of query:(local)");
					}else{//else send to server
						output.writeObject(q);
						if(change==true){															//Otan epikoinonisei me ton server ,ananeonei kai tis times tou
							output.writeObject(new Query(ch,ch,ch));								//Sends the number of changes to the server
							for(int k=0;k<ch;k++){
								output.writeObject(changed[k]);										//Sends the changed accounts
							}
							change=false; 															//Marks the dep as Up_to_date ,there are no changes
							ch=0;
						}
						double m2=(double)input.readObject();
						synchronized (lock){
							if(q.getAccount()-displace<=500&&q.getAccount()-displace>0)
								customer[q.getAccount()-displace-1].setMoney(m2);
						}
						System.out.println("Account: "+q.getAccount()+" has,after the transaction: "+m2+" €,i= "+i+" state of query:(Main server)");
					}
					
				}else if(query==3){
					account=Integer.parseInt(bt.readLine());
					account_to=Integer.parseInt(bt.readLine());
					action=Integer.parseInt(bt.readLine());
					money=Double.parseDouble(bt.readLine());
					System.out.println("Dep: "+dep+" query: "+query+" account: "+account+" account_to "+account_to);
					Query q=new Query(dep,query,account,account_to,action,money);
					if(q.getAccount()-displace<=500 && q.getAccount_to()-displace<=500&&q.getAccount()-displace>0&&q.getAccount_to()-displace>0){
						output.writeObject(new Query(0,0,0));															//Empty message to server
						synchronized (lock){
						if(customer[q.getAccount()-displace-1].getMoney()-q.getMoney() > 0){										//Only if is possible sustruct the money
							customer[q.getAccount()-displace-1].setMoney(customer[q.getAccount()-displace-1].getMoney()- q.getMoney());	//Current money - ammount
							customer[q.getAccount_to()-displace-1].setMoney(customer[q.getAccount_to()-displace-1].getMoney()+ q.getMoney());//Current money + the substructed
						}}
						change=true;																					//An account has been changed
						changed[ch]=customer[q.getAccount()-displace-1];															//Puts the changed in a array
						ch++;	
						System.out.println("Account: "+customer[q.getAccount()-displace-1].getId()+" has,after the transaction: "+customer[q.getAccount()-displace-1].getMoney()+" €,i= "+i+" state of query:(local)");
					}else {//else send to main server
						output.writeObject(q);
						if(change==true){															//Otan epikoinonisei me ton server ,ananeonei kai tis times tou
							output.writeObject(new Query(ch,ch,ch));								//Sends the number of changes to the server
							for(int k=0;k<ch;k++){
								output.writeObject(changed[k]);										//Sends the changed accounts
							}
							change=false; 															//Marks the dep as Up_to_date ,there are no changes
							ch=0;
						}
						double m3=(double)input.readObject();
						synchronized (lock){
							if(q.getAccount()-displace<=500&&q.getAccount()-displace>0)customer[q.getAccount()-displace-1].setMoney(m3);
							
							
							if(q.getAction()==0)
								if(q.getAccount_to()-displace<=500&&q.getAccount_to()-displace>0)customer[q.getAccount_to()-displace-1].setMoney(customer[q.getAccount_to()-displace-1].getMoney()+q.getMoney());
							else
								if(q.getAccount_to()-displace<=500&&q.getAccount_to()-displace>0)customer[q.getAccount_to()-displace-1].setMoney(customer[q.getAccount_to()-displace-1].getMoney()-q.getMoney());//If local update				
						}
						System.out.println("Account: "+q.getAccount()+" has,after the transaction: "+m3+" €,i= "+i+" state of query:(Main server)");
					}
				}else if(query==4){
					Query q4=new Query(dep,query);
					output.writeObject(q4);															//locks the array in the main server
					synchronized (lock){															//Exasfalizei oti den tha mpi kanis
						Query q=new Query(dep,query);
						int N=0;double mesos=0, sum=0;
						for(int j=0;j<500;j++){
							if(customer[j].getDep()==q.getDep()){
								sum +=customer[j].getMoney();//Adding his momey to the whole
								N++;//+1 customer from the query dep
							}
						}
						if (N!=0) mesos=sum/N;
						Date date=new Date();
						System.out.println("H metrisi tou mesou orou eginai stis: "+date);
						System.out.println("\tThe Department: "+(q.getDep()+1)+" has a median money value: "+mesos+" ,i= "+i+" state of query:(local)");
					}
					output.writeObject(new Query(0,0,0));//unlocks the array in the main server 
				}
				
				if(change==true){																	//sends to server signal that we have a change
					output.writeObject(new Query(5,5,5));											//5 is the signal of change
				}else output.writeObject(new Query(6,6,6));											//Signals that htere is no change
			}
			bt.close();
			System.out.println("My Job Is Done Disconnecting....");
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