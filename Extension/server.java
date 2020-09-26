import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class server {
	public Customer customer[]=new Customer[1000];
	public boolean isFree[]=new boolean[1000];
	public int isTakenFrom[]=new int[1000];
	private int account_from, account_to;
	private double money;
	public server(){}
	
	public void loop(){
		File f=new File("customers.txt");														//Anoigma tou arxeiou me tous pelates
		try{
			FileReader fr=new FileReader(f);
			BufferedReader br=new BufferedReader(fr);
			System.out.println("Opening Customer.txt...");
			for(int i=0;i<1000;i++){
				account_from=Integer.parseInt(br.readLine());
				money=Double.parseDouble(br.readLine());
				customer[i]=new Customer(account_from,account_to,money);	
				isFree[i]=true;
				isTakenFrom[i]=0;
				//System.out.println(customer[i].toString());										//Diavasma kai emfanisi
			}
			System.out.println("Closing Customer.txt...");
			br.close();
		}catch(IOException e){System.out.println("Error in reading server");}
		
		File q=new File("Fortos.txt");														//Anoigma tou arxeiou me tous pelates
		try{
			FileReader fr=new FileReader(q);
			BufferedReader br=new BufferedReader(fr);
			System.out.println("Opening Fortos.txt...");
			int afr,ato;double m;
			ExecutorService executor = Executors.newFixedThreadPool(20);  					//Thread pool
			for(int i=0;i<2999;i++){
				afr=Integer.parseInt(br.readLine());
				ato=Integer.parseInt(br.readLine());
				m=Double.parseDouble(br.readLine());										
				query_server query = new query_server(customer,isFree,isTakenFrom,afr,ato,m);
	            executor.execute(query);
			}
			executor.shutdown();
			while (!executor.isTerminated()) {/**/}
			System.out.println("Done");
			br.close();
		}catch(IOException e){System.out.println("Error in reading server");}
		
	}

}
