import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.Date;

public class Client_test {
	public static void main(String args[]){
		int dep,query,account,account_to,action,from_dep=0;										//Variables for FileReading
		double money,m;
		Socket client;
		ObjectInputStream input;																//Input kai output gia epikoinonia ton server
		ObjectOutputStream output;
		try{
			int port=11111;
			System.out.println("Attemting Connection");											//Sto ergastirio allazei		
			client=new Socket((Inet4Address)Inet4Address.getByName( "192.168.1.2"),port);		//Local Ip address
			System.out.println("Connected");													//Initiallizing connection
			output=new ObjectOutputStream(client.getOutputStream());							//Initializing  the I/O Streams
			output.flush();
			input=new ObjectInputStream(client.getInputStream());					
			
			long start_time=System.currentTimeMillis();											//Snapshot of the current time
			
			try{
				File f;	 																		//Anigoume to Queri file gia diavasma
				
				from_dep=Integer.parseInt(args[0]);												//Arithmos tou dep pou tha anikei o client
				if(from_dep==0) f=new File("Fortos1.txt");										//An einai to dep 1 anixe to forto 1
				else f=new File("Fortos2.txt");													//Alios ton allo
				//if(from_dep==0)f =new File("Fortos_Random_1.txt");							//Antistoixa gia to anigma allou set fortou
				//else f=new File("Fortos_Random_2.txt");
		
				FileReader fr=new FileReader(f);
				BufferedReader br=new BufferedReader(fr);										//BufferReader gia diavasma ton Querie		
				for(int i=0;i<300000;i++){
								dep=Integer.parseInt(br.readLine());							//Diavazoume ta stoixeia line-by-line
								query=Integer.parseInt(br.readLine());
								System.out.println("Dep: "+dep+" Query:"+query);				//Emfanizei to Dep kai to Query pou tha ektelesoume
								if(query==1){													//An einai tipou 1,diavase mono account,ta alla den xreiazontai
									account=Integer.parseInt(br.readLine());
									Query q=new Query(dep,query,account);						//Dimiourgoume ena Query antikeimeno
									output.writeObject(q);										//To stelnoume sto server 
									double m1=(double)input.readObject();						//Diavazoume tin apantisi
									System.out.println("Account:  "+q.getAccount()+"  has: "+ m1 +"  €,i= "+i);
								}else if(query==2){												//An einai tipou 2 theloume na diavasoume kai alla stoixeia
									account=Integer.parseInt(br.readLine());
									action=Integer.parseInt(br.readLine());
									money=Double.parseDouble(br.readLine());
									Query q=new Query(dep,query,account,action,money);
									output.writeObject(q);										//Stelnoume to Query kai diavazoume to neo poso,meta tin metafora xrimaton
									m=(double)input.readObject();
									System.out.println("Account: "+q.getAccount()+" has,after the transaction: "+m+" €,i= "+i);
								}else if(query==3){												//An einai tipou 3,diavazoume pali ta aparaitita
									account=Integer.parseInt(br.readLine());
									account_to=Integer.parseInt(br.readLine());
									action=Integer.parseInt(br.readLine());
									money=Double.parseDouble(br.readLine());
									Query q=new Query(dep,query,account,account_to,action,money);
									output.writeObject(q);																		//Stelnoume to Query
									m=(double)input.readObject();																//Diavazoume to poso mono tou account kai to emfanizoume
									System.out.println("Account: "+q.getAccount()+" has,after the transaction: "+m+" €,i= "+i);
								}else if(query==4){																				//An einai tipou 4 den xreiazetai na diavasoume kati
									Query q=new Query(dep,query);																//Metavliti gia tin ora
									Date date=new Date();																		//H ora tou ipologismou einai prin steiloume to query,kai oxi meta
									System.out.println("H metrisi tou mesou orou eginai stis: "+date);
									output.writeObject(q);																		//Stelnoume to aitima gia meso oro
									m=(double)input.readObject();																//Diavazoume tin apantisi pou einai o mesos oros
									System.out.println("The Department: "+(q.getDep()+1)+" has a median money value: "+m+" ,i= "+i);
								}
				}
				br.close();																										//Kleisimo tou BufferReader
			}catch(IOException e){System.out.println("Error in client reading");}
			long end_time=System.currentTimeMillis();																			//Snapshot of the current time 
			long duration=end_time-start_time;																					//Calculate the executuon time
			double secs=duration/1000.0;
			System.out.println("Done in: "+ duration +" msec="+secs+"  secs");
			System.out.println("Disconecting now");
			input.close();																										//Closing Streams,Socket
			output.close();
			client.close();
			System.out.println("Disconnected.");																				//Disconnected
		}catch (Exception e2){System.out.println("Client exception");}	
	}
}
