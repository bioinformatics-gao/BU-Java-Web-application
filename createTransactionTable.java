package classes;
import java.sql.*;

public class createTransactionTable {
	private static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static String DB_URL = "jdbc:mysql://localhost:3306/cs565";
	private static String DB_Account_IDNAME = "cs";
	private static String DB_PASSWORD = "java";
	
	String sqlDropStatement;
	String sqlCreateStatement;
	String sql;	
	ResultSet rs;
	
	public static void main(String[] args) {
		try {
			Class.forName(JDBC_DRIVER);
			Connection con = DriverManager.getConnection(DB_URL, DB_Account_IDNAME, DB_PASSWORD);			
			Statement stmt = con.createStatement();
			
	        //drop the transaction table if it exist already
			String sqlDropStatement = "DROP TABLE TRANSACTIONS";
			try {
				stmt.executeUpdate(sqlDropStatement);
			}catch (Exception e1)	{
				System.out.println("Trying to drop TRANSACTIONs Table...");
			}
			
			//create the transaction table that refer to account holder information
			String sqlCreateStatement = "CREATE TABLE TRANSACTIONS " +	"(Transaction_ID integer auto_increment primary key," 
			    + "CustomerId varchar(16), FOREIGN KEY(CustomerId) REFERENCES Account(CustomerId), " 
			    + "Transaction_Date int, " 	
				+ "Transaction_Type varchar(10), " 
			    + "StockSymbol varchar(8), FOREIGN KEY(StockSymbol) REFERENCES StockQuotes(StockSymbol)," 
				+ "Quantity double, "
                + "PriceOrDepositAmount decimal(10, 2)) ENGINE=INNODB";
			stmt.executeUpdate(sqlCreateStatement);
			
			// create the trigger to allow only withdraw and deposit transaction type  //Deposit
			String ChkStatement ="CREATE TRIGGER type_check BEFORE INSERT ON TRANSACTIONS " 
			    +"FOR EACH ROW "
				+"BEGIN "
			        +"DECLARE msg varchar(255); "
		            +"IF (NEW.Transaction_Type)<>'Deposit' and NEW.Transaction_Type <> 'Buy' and NEW.Transaction_Type <> 'Sell' "
	                +"THEN "
	                    +"SET msg = concat(cast(new.Transaction_Type as char), 'is illegal, only Deposit/Buy/Sell allowed'); "
	                    +"SIGNAL sqlstate '45000' SET message_text = msg; "
		            +"END IF; "
			    +"END";
			stmt.executeUpdate(ChkStatement);
			
			// create the trigger to update the current account balance information
			String updateBalanceStatement ="CREATE TRIGGER Balance_update after INSERT ON TRANSACTIONS " 
			    +"FOR EACH ROW "
				+"BEGIN "
		            +"IF (NEW.Transaction_Type)='Deposit' "
	                +"THEN "
	                    +"update Account " 
	                    +"SET Accumulated_Balance = Accumulated_Balance + NEW.PriceOrDepositAmount where CustomerId = NEW.CustomerId; "
		            +"END IF; "
		            +"IF (NEW.Transaction_Type)='Sell' "
	                +"THEN "
	                    +"update Account " 
	                    +"SET Accumulated_Balance = Accumulated_Balance + NEW.PriceOrDepositAmount where CustomerId = NEW.CustomerId; "
		            +"END IF; "
		            +"IF (NEW.Transaction_Type)='Buy' "
	                +"THEN "
	                    +"update Account " 
	                    +"SET Accumulated_Balance = Accumulated_Balance - NEW.PriceOrDepositAmount where CustomerId = NEW.CustomerId; "
		            +"END IF; "
			    +"END"; 
			stmt.executeUpdate(updateBalanceStatement);
			
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
