import java.io.*;
import java.util.Random;
public class Customer_Files {
	public static void main(String args[]){
		File f=new File("customers.txt");					//Onoma arxeio pou tha dimiourgisoume
		try{
			FileWriter fw=new FileWriter(f);
			BufferedWriter bw=new BufferedWriter(fw);
			Random random=new Random();
			
			int Account;
			int Dep;
			double money;
			
			for(int i=0;i<1000;i++){
				Account=i+1;								//Aritmos logariasmou,Id:1->1000
				Dep=(i<500)?0:1;							//Department=500 eggrafes gia to kathe Dep.
				money=random.nextInt(1000000);				//Vazei tixaia lefta
				bw.write(Integer.toString(Account));		//Writing the data
				bw.newLine();								//To newLine mpainei gia pio eukoli anagnosi
				bw.write(Integer.toString(Dep));
				bw.newLine();
				bw.write(Double.toString(money));
				bw.newLine();								//Writes the info for 1 customer
			}
			System.out.println("File for customers succesfully created...");
			bw.close();										//Close the Writing Stream
		}catch(IOException e){System.out.println("Can't open the file");}
	}
}
