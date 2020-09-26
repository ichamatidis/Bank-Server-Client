


public class query_server implements Runnable {
	Customer []customer;
	boolean isFree[];
	int isTakenFrom[];
	int afr,ato;
	double m;
	query_server (Customer []customer,boolean []isFree,int []isTakenFrom,int afr,int ato,double m){																														
		this.customer=customer;	
		this.isFree=isFree;
		this.isTakenFrom=isTakenFrom;
		this.afr=afr;
		this.ato=ato;
		this.m=m;
	}																
	
	public void run(){
		if(isFree[afr-1]==true){							//Prospathei na desmeusi to ena account	 
			isFree[afr-1]=false;
			isTakenFrom[afr-1]=afr;
			
		}else{
			while(isFree[afr-1]==false)						//Allios perimenei na eleutherothei
				{ /*wait*/}
			isFree[afr-1]=false;
			isTakenFrom[afr-1]=afr;
		}
		
		if(isFree[ato-1]==true){							//Prospathei na desmeusei to allo account
			isFree[ato-1]=false;
			isTakenFrom[ato-1]=ato;
			
		}else{
			while(isFree[ato-1]==false){/*wait*/}			//Allios perimenei na eleutherothei
			isFree[ato-1]=false;
			isTakenFrom[ato-1]=ato;
			
		}
		
		if(isTakenFrom[afr-1]==afr&&isTakenFrom[ato-1]==ato){
			customer[afr-1].setMoney(customer[afr-1].getMoney()-m);				//Make exchange
			customer[ato-1].setMoney(customer[ato-1].getMoney()+m);
			isTakenFrom[afr-1]=0;
			isTakenFrom[ato-1]=0;
			isFree[afr-1]=true;													//And free them
			isFree[ato-1]=true;
			System.out.println(Thread.currentThread().getName());
			System.out.println(" \tAccount_from: "+afr+" money: "+customer[afr-1].getMoney()+" and \n"+" \tAccount_to: "+ato+" money: "+customer[ato-1].getMoney());
		}
	}
}
