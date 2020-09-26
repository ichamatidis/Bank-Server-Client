import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;


public class file_creator {
	public static void main(String args[]){//Fortos gia to dep 1
		File f1;
		f1=new File("Fortos.txt");
		int account=1;
		int account_to=0;
		double money=0;
		Random random=new Random();
		try{
			FileWriter fw=new FileWriter(f1);
			BufferedWriter bw=new BufferedWriter(fw);
		
			for(int i=0;i<300000;i++){
					account=random.nextInt(1000)+1;					//Account pou pairnoume ta xrimata
					account_to=random.nextInt(1000)+1;				//Account pou vazoume ta xrimata
					money=random.nextInt(10000);
				
					bw.write(Integer.toString(account));			//Account that we take the money
					bw.newLine();
					bw.write(Integer.toString(account_to));			//Account to add the money 
					bw.newLine();
					bw.write(Double.toString(money));				//Ammount of money
					bw.newLine();
				
			}//End of for
			System.out.println("O Fortos gia to Dep  dimiourgithike...");
			bw.close();												//Closing File writer
		}catch(IOException e){System.out.println("Cant create file");}
	}//End of class

}
