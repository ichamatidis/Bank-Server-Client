import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;


public class Fortos_Random {
	public static void main(String args[]){
		File f1;
		int from_dep=0;
		f1=new File("Fortos_Random_1.txt");
		int query=0;
		int account=1;
		int action=0;
		int account_to=0;
		double money=0;
		int i=0;
		Random random=new Random();
		
		try{
			FileWriter fw=new FileWriter(f1);
			BufferedWriter bw=new BufferedWriter(fw);
			System.out.println("Creating load for Department: "+from_dep);
			for(int j=0;j<300000;j++){										//300.000 Queries gia kathe dep
				i=random.nextInt(300000);									//Kanoume ena i=random,gia na vroume ta pososta gia kathe queri
				if(i<120000) {
					query=1;												//Query 1,emfanisi ammount 
					account=random.nextInt(100)+1;							//Random gia na vroume to pososto ama tha einai sto idio dep.
					if(account<=90) account=random.nextInt(500)+1;			//Me pithanotita 90% sto idio  
					else account=1000-random.nextInt(500);					//Allios tha paie sto allo Dep,me 10%
				}else if (i>=120000 && i<240000){
					query=2;												//Omoia gia query 2,add ot take
					account=random.nextInt(100)+1;		
					if(account<=90) account=random.nextInt(500)+1;			//Pithanotita 90% 
					else account=1000-random.nextInt(500);					//Pithanotita 10% na einai se allo dep dil apo 500 kai pano sto arxeio customers
					action=random.nextInt(2);								//if action=0 add,action=1 take
					money=random.nextInt(10000);
				}else if(i>=240000 && i<297000){
					query=3;
					account=random.nextInt(1000)+1;							//Account that we take the money
					account_to=random.nextInt(1000)+1;						//Account to add money
					money=random.nextInt(10000);							//Random money
				}else {
					query=4;												//Mesos oros pou anikei panta sto idio Dep
				}
				
																			//Write the commands to the file
				if(query==1){
					bw.write(Integer.toString(from_dep));					//Department that query comes from 
					bw.newLine();
					bw.write(Integer.toString(query));						//Type of query=1
					bw.newLine();
					bw.write(Integer.toString(account));					//Account to view the ammount of money
					bw.newLine();
				}else if(query==2){
					bw.write(Integer.toString(from_dep));
					bw.newLine();
					bw.write(Integer.toString(query));						//Add or take money
					bw.newLine();
					bw.write(Integer.toString(account));					//Account to add or take the money 
					bw.newLine();
					bw.write(Integer.toString(action));						//The action that we will do,ADD==0,TAKE==1
					bw.newLine();
					bw.write(Double.toString(money));						//Ammount of money
					bw.newLine();
				}else if(query==3){
					bw.write(Integer.toString(from_dep));					//Dep pou anikei to queri
					bw.newLine();
					bw.write(Integer.toString(query));						//Add or take money
					bw.newLine();
					bw.write(Integer.toString(account));					//Account to take the money 
					bw.newLine();
					bw.write(Integer.toString(account_to));					//Account to ass the money 
					bw.newLine();
					bw.write(Integer.toString(action));
					bw.newLine();
					bw.write(Double.toString(money));						//Ammount of money
					bw.newLine();
				}else{
					bw.write(Integer.toString(from_dep));					//Department that query comes from 
					bw.newLine();
					bw.write(Integer.toString(query));						//Type of query=4,median
					bw.newLine();
				}
			}//End of for
			System.out.println("O Fortos gia to Dep "+from_dep+" dimiourgithike...");
			bw.close();
		}catch(IOException e){System.out.println("Cant create file");}
	}//End of class

}
