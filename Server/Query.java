import java.io.Serializable;
public class Query implements Serializable{
	private static final long serialVersionUID = 1L;
	private  int dep;
	private int query;
	private int account;
	private int account_to;
	private int action;
	private double money;
																						//Gia kathe qureie arxikopoioume mono ta xrisima
	public Query(int dep,int query,int account){										//Constructor for query 1
		this.dep=dep;
		this.query=query;
		this.account=account;
		this.account_to=-1;
		this.action=-1;
		this.money=-1.0;
	}
	public Query(int dep,int query,int account,int action,double money){				//Constructor for 2
		this.dep=dep;
		this.query=query;
		this.account=account;
		this.account_to=-1;
		this.action=action;
		this.money=money;
	}
	public Query(int dep,int query,int account,int account_to,int action,double money){//Constructor for 3
		this.dep=dep;
		this.query=query;
		this.account=account;
		this.account_to=account_to;
		this.action=action;
		this.money=money;
	}
	public Query(int dep,int query){												//Constructor for 4
		this.dep=dep;
		this.query=query;
		this.account=-1;
		this.account_to=-1;
		this.action=-1;
		this.money=-1.0;
	}
																					//Get methods
	public int getDep(){
		return dep;
	}
	public int getQuery(){
		return query;
	}
	public int getAccount(){
		return account;
	}
	public int getAccount_to(){
		return account_to;
	}
	public int getAction(){
		return action;
	}
	public double getMoney(){
		return money;
	}
																					//Set methods
	public void setDep(int dep){
		this.dep=dep;
	}
	public void setQuery(int query){
		this.query=query;
	}
	public void setAccount(int account){
		this.account=account;
	}
	public void setAccount_to(int account_to){
		this.account_to=account_to;
	}
	public void setAction(int action){
		this.action=action;
	}
	public void setMoney(double money){
		this.money=money;
	}
	public String toString (){
		return (dep+" "+query+" "+account+" "+account_to+" "+action+" "+money);
	}
}
