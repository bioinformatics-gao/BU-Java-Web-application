package classes;
import java.sql.*;

public class createAccountStockRelationTable {
	private static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static String DB_URL = "jdbc:mysql://localhost:3306/cs565";
	private static String DB_Account_IDNAME = "cs";
	private static String DB_PASSWORD = "java";

	public static void main(String[] args) {
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
			
			//create the account balance table
			String sqlCreateStatement = "CREATE TABLE Account_Stock " +
					"(CustomerId varchar(16) NOT NULL, StockSymbol varchar(8) NOT NULL," +
					"StockQuantity double,"+
					"PRIMARY KEY (CustomerId, StockSymbol)," +
					"FOREIGN KEY (CustomerId) REFERENCES Account(CustomerId)," +
					"FOREIGN KEY (StockSymbol) REFERENCES StockQuotes(StockSymbol))ENGINE=INNODB";
			stmt.executeUpdate(sqlCreateStatement);
						
			//insert the account holder information with initial balance  
			String sql = "insert into Account_Stock (CustomerId, StockSymbol, StockQuantity) " +
					"values ('Zhen', 'AAA','0.00')";
			stmt.executeUpdate(sql);
			sql = "insert into Account_Stock (CustomerId, StockSymbol,StockQuantity) " +
					"values ('Peter', 'AAA','2.00')";
			stmt.executeUpdate(sql);
			sql = "insert into Account_Stock (CustomerId, StockSymbol,StockQuantity)" +
					" values ('Ting', 'AAA','5.50')";
			stmt.executeUpdate(sql);
			sql = "insert into Account_Stock (CustomerId, StockSymbol,StockQuantity)" +
					" values ('John','BBB','120.00')";
			stmt.executeUpdate(sql);
		
			//show the account holder information with current account balance
			sql = "Select * from Account_Stock";
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
}
