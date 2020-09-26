import java.io.*;
import java.util.Random;
public class Fortos {
	public static void main(String args[]){//Fortos gia to dep 1
		File f1;
		int from_dep=0;
		f1=new File("Fortos1.txt");
		int query=0;
		int account=1;
		int action=0;
		int account_to=0;
		double money=0;
		Random random=new Random();
		try{
			FileWriter fw=new FileWriter(f1);
			BufferedWriter bw=new BufferedWriter(fw);
			System.out.println("Creating load for Department "+from_dep+ "...");
			for(int i=0;i<300000;i++){
				if(i<120000) {
					query=1;										//Query 1 ,emfanisi
					account=random.nextInt(100)+1;					//Random gia na vroume to pososto ama tha einai sto idio dep.
					if(account<=90) account=random.nextInt(500)+1;	//Me pithanotita 90% sto idio  ,10% sto allo
					else account=1000-random.nextInt(500);			//Allios tha paie sto dep 2
				}else if (i>=120000 && i<240000){
					query=2;										//Omoia gia query 2,prosthesi-afairesi xrimaton
					account=random.nextInt(100)+1;		
					if(account<=90) account=random.nextInt(500)+1;	//Pithanotita 90% ,sto idio
					else account=1000-random.nextInt(500);			//Pithanotita 10% na einai se allo dep dil apo 500 kai pano sto arxeio customers
					action=random.nextInt(2);						//if action=0 add,action=1 take
					money=random.nextInt(10000);
				}else if(i>=240000 && i<297000){
					query=3;
					account=random.nextInt(1000)+1;					//Account pou pairnoume ta xrimata
					account_to=random.nextInt(1000)+1;				//Account pou vazoume ta xrimata
					money=random.nextInt(10000);
				}else {
					query=4;										//Gia ton meso oro apla vazoume to query=4 ,dioti eiani panta sto Dep pou 
																	// ton kalese
				}
				
																	//Write the commands to the file
				if(query==1){
					bw.write(Integer.toString(from_dep));			//Department pou anikei o fortos 
					bw.newLine();
					bw.write(Integer.toString(query));				//Type of query=1,emfanisi xrimaton
					bw.newLine();
					bw.write(Integer.toString(account));			//Account to view
					bw.newLine();
				}else if(query==2){
					bw.write(Integer.toString(from_dep));
					bw.newLine();
					bw.write(Integer.toString(query));				//Add or take money
					bw.newLine();
					bw.write(Integer.toString(account));			//Account from ,to substruct
					bw.newLine();
					bw.write(Integer.toString(action));				//If is 0 ADD,if is 1 TAKE
					bw.newLine();
					bw.write(Double.toString(money));				//Ammount to add or take
					bw.newLine();
				}else if(query==3){
					bw.write(Integer.toString(from_dep));
					bw.newLine();
					bw.write(Integer.toString(query));				//Take from account ,the ammount money ,and add it to account to
					bw.newLine();
					bw.write(Integer.toString(account));			//Account that we take the money
					bw.newLine();
					bw.write(Integer.toString(account_to));			//Account to add the money 
					bw.newLine();
					bw.write(Integer.toString(action));
					bw.newLine();
					bw.write(Double.toString(money));				//Ammount of money
					bw.newLine();
				}else{
					bw.write(Integer.toString(from_dep));			//Department that query comes from 
					bw.newLine();
					bw.write(Integer.toString(query));				//Type of query=4,means the median
					bw.newLine();
				}
			}//End of for
			System.out.println("O Fortos gia to Dep "+from_dep+" dimiourgithike...");
			bw.close();												//Closing File writer
		}catch(IOException e){System.out.println("Cant create file");}
	}//End of class
}//End of class
