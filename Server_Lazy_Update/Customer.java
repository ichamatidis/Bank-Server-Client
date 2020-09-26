import java.io.*;
public class Customer implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;			//1-1000 
	private int dep;		//0 'h 1
	private double money;	
		public Customer (int id,int dep,double money){
		this.id=id;
		this.dep=dep;
		this.money=money;
		}
		public String toString(){				//Ektiposi
			return id+" "+dep+" "+money;
		}

		//Get functions
		public int getId(){
			return id;
		}
		public int getDep(){
			return dep;
		}
		public double getMoney(){
			return money;
		}

		//Set Functions
		public void setId(int id){
			this.id=id;
		}
		public void setDep(int dep){
			this.dep=dep;
		}
		public void setMoney(double money){
			this.money=money;
		}

}//End of class
