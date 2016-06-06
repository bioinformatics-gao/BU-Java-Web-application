package classes;
import java.sql.*;

public class createStockQuotesTable {
	private static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static String DB_URL = "jdbc:mysql://localhost:3306/cs565";
	private static String DB_Account_IDNAME = "cs";
	private static String DB_PASSWORD = "java";

	public static void main(String[] args) {
		try {
			Class.forName(JDBC_DRIVER);
			Connection con = DriverManager.getConnection(DB_URL, DB_Account_IDNAME, DB_PASSWORD);			
			Statement stmt = con.createStatement();
			String sqlDropStatement;
			String sqlCreateStatement;
			String sql;	
			
			//drop the StockQuotes table if it exist already.
			sqlDropStatement = "DROP TABLE StockQuotes";
			try {
				stmt.executeUpdate(sqlDropStatement);
			}catch (Exception e1)	{
				System.out.println("Trying to drop StockQuotes Table...");
			}
			
			//create the StockQuotes table
			sqlCreateStatement = "CREATE TABLE StockQuotes " + "(StockSymbol varchar(8) primary key," 
			+ " CurrentStockPrice decimal(6, 2))ENGINE=INNODB";
			stmt.executeUpdate(sqlCreateStatement);
			/*
			String clearTableStatement = "delete from StockQuotes";
			try {
				stmt.executeUpdate(clearTableStatement);
			}catch (Exception e1)	{
				System.out.println("Clearing old transaction information in memory");
			}*/
			
			//insert data to the StockQuotes  
			double factor;
			factor=Math.random()*2.0;
			sql = "insert into StockQuotes (StockSymbol, CurrentStockPrice) values ('AAA', 100.00*'"+factor+"')";
			stmt.executeUpdate(sql);
			sql = "insert into StockQuotes (StockSymbol,  CurrentStockPrice) values ('BBB', 18.00*'"+factor+"')";
			stmt.executeUpdate(sql);
			sql = "insert into StockQuotes (StockSymbol,  CurrentStockPrice) values ('CCC', 218.09*'"+factor+"')";
			stmt.executeUpdate(sql);
			sql = "insert into StockQuotes (StockSymbol,  CurrentStockPrice) values ('DDD', 650.00*'"+factor+"')";
			stmt.executeUpdate(sql);
			stmt.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	
}
