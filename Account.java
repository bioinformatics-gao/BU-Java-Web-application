package classes;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class Account {
	private static final String CustomrtId = null;
	String CustomerName,CustomerId;
	String stockSymbol, StockItemSymbol[];
	int date;
	double InitialDeposit;
	double quantity; 
	double stockPrice;
	double Accumulated_Balance;
	double amount; 
	double cashAmount;
	double totalStockValue;
	double StockItemQuantity[], StockItemUnitValue[],StockItemCashValue[];
	
	private static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static String DB_URL = "jdbc:mysql://localhost:3306/cs565";
	private static String DB_Account_IDNAME = "cs";
	private static String DB_PASSWORD = "java";
	
	public Account(String CustomerId, String CustomerName, int OpeningDate, double InitialDeposit){
		this.CustomerId = CustomerId;
		this.CustomerName = CustomerName;
		this.InitialDeposit = InitialDeposit;
		this.Accumulated_Balance = InitialDeposit;
		this.date=OpeningDate;
	}

	public void addToAccountTable(){
		try {
			Class.forName(JDBC_DRIVER);
			Connection con = DriverManager.getConnection(DB_URL, DB_Account_IDNAME, DB_PASSWORD);			
			Statement stmt = con.createStatement();
	
			//insert the account holder information with initial balance  
			String sql = "insert into Account (CustomerId, CustomerName, OpeningDate, OpeningBalance) " +
					"values ('"+CustomerId+"', '"+CustomerName+"','"+date+"','"+InitialDeposit+"')";
			stmt.executeUpdate(sql);
			stmt.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	//check if enough funds are available to buy
	public boolean isValidBuy (String stockSymbol, double quant) {
		this.stockSymbol=stockSymbol;
		this.quantity=quant; 
		Stock m1 = new Stock();
		stockPrice=m1.getStockPrice(stockSymbol); 
		try {
			Class.forName(JDBC_DRIVER);
			Connection con = DriverManager.getConnection(DB_URL, DB_Account_IDNAME, DB_PASSWORD);			
			Statement stmt = con.createStatement();
	        String sql;
		
			//show the account holder information with current account balance
			sql = "Select * from Account  where CustomerId = '"+CustomerId+"' ";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())			{
				this.Accumulated_Balance=rs.getDouble("Accumulated_Balance");
			}
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Accumulated_Balance>quantity*stockPrice);
	}
	
	public void buy(String stockSymbol, double quantity) {
		this.stockSymbol=stockSymbol; 
		this.quantity=quantity;
		if(isValidBuy (stockSymbol, quantity)){
			try {
				Class.forName(JDBC_DRIVER);
				Connection con = DriverManager.getConnection(DB_URL, DB_Account_IDNAME, DB_PASSWORD);			
				Statement stmt = con.createStatement();
		        String sql;
		        sql = "insert into TRANSACTIONS (CustomerId,Transaction_Type,StockSymbol, Quantity) " +
						"values ('"+CustomerId+"', 'Buy', '"+stockSymbol+"', '"+quantity+"')";
				try {
					stmt.executeUpdate(sql);
				}catch (Exception e1)	{
					System.out.println("mistakes, check the name and/or transaction type");
				}
				
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
					
		}else{
			System.out.println("NO enough fund to buy");
		}
	}
    
	//check if the customer has that many shares of that stock to sell.  
	public boolean isValidSell (String stockSymbol, double quant) {
		this.quantity=quant; 
		double AvailableQuantity=0; 
		this.stockSymbol=stockSymbol;
		try {
			Class.forName(JDBC_DRIVER);
			Connection con = DriverManager.getConnection(DB_URL, DB_Account_IDNAME, DB_PASSWORD);			
			Statement stmt = con.createStatement();
	        String sql;
		
			//show the account holder information with current stock 
			sql = "Select * from Account_Stock where CustomerId ='"+CustomerId+"' and StockSymbol = '"+stockSymbol+"' ";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())			{
				AvailableQuantity=rs.getDouble("StockQuantity");
			}
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (AvailableQuantity>quantity);
	}
		
	public void sell(String stockSymbol, double quantity) {
		this.stockSymbol=stockSymbol; 
		this.quantity=quantity;
		if(isValidSell (stockSymbol, quantity)){
			try {
				Class.forName(JDBC_DRIVER);
				Connection con = DriverManager.getConnection(DB_URL, DB_Account_IDNAME, DB_PASSWORD);			
				Statement stmt = con.createStatement();
		        String sql;
		        sql = "insert into TRANSACTIONS (CustomerId,Transaction_Type,StockSymbol, Quantity) " +
						"values ('"+CustomerId+"', 'Sell', '"+stockSymbol+"', '"+quantity+"')";
				try {
					stmt.executeUpdate(sql);
				}catch (Exception e1)	{
					System.out.println("mistakes, check the name and/or transaction type");
				}
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
					
		}else{
			System.out.println("NO enough stock to sell");
		}
	}   
	
	public void deposit(double money) {
    	this.amount =money; 
    	try {
			Class.forName(JDBC_DRIVER);
			Connection con = DriverManager.getConnection(DB_URL, DB_Account_IDNAME, DB_PASSWORD);			
			Statement stmt = con.createStatement();
			String sql;
			
			//try to insert new transactions information (check the transaction type and the account holder name)
			sql = "insert into TRANSACTIONS (CustomerId,Transaction_Type,PriceOrDepositAmount)" +
					"values ('"+CustomerId+"', 'Deposit', '"+amount+"')";
			try {
				stmt.executeUpdate(sql);
			}catch (Exception e1)	{
				System.out.println("mistakes in the information, check the name and/or transaction type");
			}
									
			//fetch transaction information and print it
			System.out.println("Now printting the transactions of this time:\n");
			sql = "Select * from TRANSACTIONS";
			ResultSet rs1 = stmt.executeQuery(sql);
			while (rs1.next())			{
				System.out.printf("Transaction_ID =%2d, CustomerId =%8s, Transaction_Type =%8s, PriceOrDepositAmount = $%9.2f\n",	
					rs1.getInt("Transaction_ID"), rs1.getString("CustomerId"), rs1.getString("Transaction_Type"), rs1.getBigDecimal("PriceOrDepositAmount") );
			}
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    public double getCashValue(int day) {
       	this.date =day; 
    	try {
			Class.forName(JDBC_DRIVER);
			Connection con = DriverManager.getConnection(DB_URL, DB_Account_IDNAME, DB_PASSWORD);			
			Statement stmt = con.createStatement();
			String sql;
											
			//fetch transaction information and print it
			System.out.println("Now printting the cash of this accont houlder in this date:\n");
			sql = "Select * from Account_Cash_History where CustomerId ='"+CustomerId+"' and CashDate = '"+date+"'";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())			{
				cashAmount=rs.getDouble("CashAmount");
			}
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return cashAmount;
  	}
    
    public double getStockValue(int day) {
       	this.date =day; 
       	int i=0;
    	try {
			Class.forName(JDBC_DRIVER);
			Connection con = DriverManager.getConnection(DB_URL, DB_Account_IDNAME, DB_PASSWORD);			
			Statement stmt = con.createStatement();
			String sql;
			/*//Stock m1 = new Stock();*/
							
			//fetch transaction information and print it
			System.out.println("Now printting the cash of this accont houlder in this date:\n");
		
			//show the account holder information with current account balance
			sql = "Select * from Account_StockHistory where CustomerId ='"+CustomerId+"' and date = '"+date+"'";
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println(rs.next());
			//SELECT  COUNT(*) 	FROM  TABLE_NAME
			while (rs.next())			{
				StockItemSymbol[i] = rs.getString("StockSymbol");
				System.out.println(StockItemSymbol[i]);
				StockItemUnitValue[i]=Stock.getStockPrice(StockItemSymbol[i], date); 	
				System.out.println("dadad2");
				StockItemQuantity[i] = rs.getDouble("StockQuantity");
				System.out.println("dadad3");
				StockItemCashValue[i]=StockItemUnitValue[i]*StockItemQuantity[i];
				System.out.println("dadad4");
				totalStockValue += StockItemCashValue[i];
				System.out.println("dadad5");
				i++;
			}
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	System.out.println("dadad6");
    	return totalStockValue;
	}
    
    public double getCashValue() {
       	this.date =101; 
    	try {
			Class.forName(JDBC_DRIVER);
			Connection con = DriverManager.getConnection(DB_URL, DB_Account_IDNAME, DB_PASSWORD);			
			Statement stmt = con.createStatement();
			String sql;
											
			//fetch transaction information and print it
			System.out.println("Now printting the cash of this accont houlder in this date:\n");
			sql = "Select * from Account_Cash_History where CustomerId ='"+CustomerId+"' and CashDate = '"+date+"'";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())			{
				cashAmount=rs.getDouble("CashAmount");
			}
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return cashAmount;
  	}
    
    
public void current_protfolia(int date, String ID) {
this.date=date;
		try {
			Class.forName(JDBC_DRIVER);
			Connection con = DriverManager.getConnection(DB_URL, DB_Account_IDNAME, DB_PASSWORD);			
			Statement stmt = con.createStatement();
	
			//drop the account balance table if it exist already.
			String sqlDropStatement = "DROP TABLE Account_Stock";
			try {
				stmt.executeUpdate(sqlDropStatement);
			}catch (Exception e1)	{
				System.out.println("Trying to drop Account_Stock Table...");
			}
			
			String [] protf = null;
			//show the account holder information with current account balance
			String sql = "Select * from Account_Stock where Transcation_date= '"+date+"'";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())			{
				System.out.printf("CustomerId =%8s, StockSymbol=%4s, StockQuantity= $%9.2f\n",
					rs.getString("CustomerId"), rs.getString("StockSymbol"),rs.getDouble("StockQuantity"));
			}
			stmt.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
public void current_protfolia(String ID) {
    int date =101;
	try {
		Class.forName(JDBC_DRIVER);
		Connection con = DriverManager.getConnection(DB_URL, DB_Account_IDNAME, DB_PASSWORD);			
		Statement stmt = con.createStatement();

		//drop the account balance table if it exist already.
		String sqlDropStatement = "DROP TABLE Account_Stock";
		try {
			stmt.executeUpdate(sqlDropStatement);
		}catch (Exception e1)	{
			System.out.println("Trying to drop Account_Stock Table...");
		}
		
		String [] protf = null;
		//show the account holder information with current account balance
		String sql = "Select * from Account_Stock where Transcation_date= '"+date+"'";
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next())			{
			System.out.printf("CustomerId =%8s, StockSymbol=%4s, StockQuantity= $%9.2f\n",
				rs.getString("CustomerId"), rs.getString("StockSymbol"),rs.getDouble("StockQuantity"));
		}
		stmt.close();
		
	} catch (Exception e) {
		e.printStackTrace();
	}
}

public void Transcation_history(String ID) {
    int date =101;
    this.CustomerId=ID; 
	try {
		Class.forName(JDBC_DRIVER);
		Connection con = DriverManager.getConnection(DB_URL, DB_Account_IDNAME, DB_PASSWORD);			
		Statement stmt = con.createStatement();

		//drop the account balance table if it exist already.
		String sqlDropStatement = "DROP TABLE Account_Stock";
		try {
			stmt.executeUpdate(sqlDropStatement);
		}catch (Exception e1)	{
			System.out.println("Trying to drop Account_Stock Table...");
		}
		
		String [] protf = null;
		//show the account holder information with current account balance
		String sql = "Select * from Account_Stock_history where CostomId= '"+CustomrtId+"'";
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next())			{
			System.out.printf("CustomerId =%8s, StockSymbol=%4s, StockQuantity= $%9.2f\n",
				rs.getString("CustomerId"), rs.getString("StockSymbol"),rs.getDouble("StockQuantity"));
		}
		
		sql = "Select * from Account_Stock_history where CostomId= '"+CustomrtId+"'and Transaction_Type='Sell'";
		ResultSet rs2 = stmt.executeQuery(sql);
		while (rs2.next())			{
			System.out.printf("CustomerId =%8s, StockSymbol=%4s, StockQuantity= $%9.2f\n",
				rs2.getString("CustomerId"), rs2.getString("StockSymbol"),rs2.getDouble("StockQuantity"));
		}
		
		sql = "Select * from Account_Stock_history where CostomId= '"+CustomrtId+"'and Transaction_Type='Buy'";
		ResultSet rs3 = stmt.executeQuery(sql);
		while (rs3.next())			{
			System.out.printf("CustomerId =%8s, StockSymbol=%4s, StockQuantity= $%9.2f\n",
				rs3.getString("CustomerId"), rs3.getString("StockSymbol"),rs3.getDouble("StockQuantity"));
		}
		
		sql = "Select * from Account_Stock_history where CostomId= '"+CustomrtId+"'and Transaction_Type='Deposot'";
		ResultSet rs4 = stmt.executeQuery(sql);
		while (rs4.next())			{
			System.out.printf("CustomerId =%8s, StockSymbol=%4s, StockQuantity= $%9.2f\n",
				rs4.getString("CustomerId"), rs4.getString("StockSymbol"),rs4.getDouble("StockQuantity"));
		}
		stmt.close();
		
		sql = "Select * from Account_Stock_history where CostomId= '"+CustomrtId+"";
		ResultSet rs41 = stmt.executeQuery(sql);
		while (rs41.next())			{
			System.out.printf("CustomerId =%8s, StockSymbol=%4s, StockQuantity= $%9.2f\n",
				rs41.getString("CustomerId"), rs41.getString("StockSymbol"),rs41.getDouble("StockQuantity"));
		}
		stmt.close();
		
	} catch (Exception e) {
		e.printStackTrace();
	}
}


}
