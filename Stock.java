package classes;
import java.sql.*;

public class Stock {
	private static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static String DB_URL = "jdbc:mysql://localhost:3306/cs565";
	private static String DB_Account_IDNAME = "cs";
	private static String DB_PASSWORD = "java";
	private static String stockSymbol;

	public static void main(String[] args) {
		try {
			Class.forName(JDBC_DRIVER);
			Connection con = DriverManager.getConnection(DB_URL, DB_Account_IDNAME, DB_PASSWORD);			
			Statement stmt = con.createStatement();
			String sqlDropStatement;
			String sqlCreateStatement;
			String sql;	
			ResultSet rs;
			
			//drop the StockHistory table if it exist already.
			sqlDropStatement = "DROP TABLE StockHistory";
			try {
				stmt.executeUpdate(sqlDropStatement);
			}catch (Exception e1)	{
				System.out.println("Trying to drop StockHistory Table...");
			}
			
			//create the StockHistory table
			sqlCreateStatement = "CREATE TABLE StockHistory " + 
					"(StockSymbol varchar(8),  SDate integer, EndTheDayPrice decimal(6, 2), " +
					"PRIMARY KEY (StockSymbol, SDate) )ENGINE=INNODB";
			stmt.executeUpdate(sqlCreateStatement);
			
			for (int i =1; i<=100; i++){
				double factor=Math.random()*2.0;
 			    sql= "insert into StockHistory (StockSymbol, SDate, EndTheDayPrice) " +
  			 	     "values ('AAA', '"+i+"', 100.00*'"+factor+"')"; //insert with random double between 0 to 200% of the original price
			    stmt.executeUpdate(sql);
			}
		
			for (int i =1; i<=100; i++){
				double factor=Math.random()*2.0;
 			    sql= "insert into StockHistory (StockSymbol, SDate, EndTheDayPrice) " +
  			 	     "values ('BBB', '"+i+"', 18.00*'"+factor+"')"; //insert with random double between 0 to 200% of the original price
			    stmt.executeUpdate(sql);
			}
			
			for (int i =1; i<=100; i++){
				double factor=Math.random()*2.0;
 			    sql= "insert into StockHistory (StockSymbol, SDate, EndTheDayPrice) " +
  			 	     "values ('CCC', '"+i+"', 218.09*'"+factor+"')";//insert random double between 0 to 200% of the original price
 			    stmt.executeUpdate(sql);
			}
			
			for (int i =1; i<=100; i++){
				double factor=Math.random()*2.0;
 			    sql= "insert into StockHistory (StockSymbol, SDate, EndTheDayPrice) " +
  			 	     "values ('DDD', '"+i+"', 650.00*'"+factor+"')";//insert random double between 0 to 200% of the original price
			    stmt.executeUpdate(sql);
			}

			//show theStockHistory holder information with currentStockHistory balance
			sql = "Select * from StockHistory";
			rs = stmt.executeQuery(sql);
			while (rs.next())			{
				System.out.printf("StockSymbol =%8s, SDate = %4d, EndTheDayPrice = $%6.2f\n",
						rs.getString("StockSymbol"), rs.getInt("SDate"), rs.getBigDecimal("EndTheDayPrice"));
			}
			
			//create the StockQuotes table
			String sqlClearStatement = "delete from StockQuotes ";
			stmt.executeUpdate(sqlClearStatement);
					
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
	
	// get the current price of the given stock
	public double getStockPrice(String sSymbol) {
		
		stockSymbol=sSymbol;
		String sql;	
		ResultSet rs;
		double currentPrice = 0;
		try {
			Class.forName(JDBC_DRIVER);
			Connection con = DriverManager.getConnection(DB_URL, DB_Account_IDNAME, DB_PASSWORD);			
			Statement stmt = con.createStatement();
			
			String clearTableStatement = "delete from StockQuotes";
			try {
				stmt.executeUpdate(clearTableStatement);
			}catch (Exception e1)	{
				System.out.println("Clearing old transaction information in memory");
			}
							
			//insert data to the StockQuotes  
			double factor;
			factor=Math.random()*2.0;
			sql = "insert into StockQuotes (StockSymbol, CurrentStockPrice) values ('"+stockSymbol+"', 100.00*'"+factor+"')";
			stmt.executeUpdate(sql);
			
			sql = "Select * from StockQuotes where stockSymbol = '"+stockSymbol+"' ";
			rs = stmt.executeQuery(sql);
			
			while (rs.next())			{
				currentPrice= rs.getDouble("CurrentStockPrice");
			}
			stmt.close();
      				
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return currentPrice;	
	}  
		
  	// get the price of the given stock in the end of the date 
	public static double getStockPrice(String sSymbol, int day) {
		stockSymbol=sSymbol;
		String sql;	
		ResultSet rs;
		double PriceAtTheEndOfThatDay = 0;
		try {
			Class.forName(JDBC_DRIVER);
			Connection con = DriverManager.getConnection(DB_URL, DB_Account_IDNAME, DB_PASSWORD);			
			Statement stmt = con.createStatement();
		
			sql = "Select * from StockHistory where stockSymbol = '"+stockSymbol+"' and SDate = '"+day+"' ";
			rs = stmt.executeQuery(sql);
			while (rs.next())			{
				PriceAtTheEndOfThatDay= rs.getDouble("CurrentStockPrice");
			}
			stmt.close();
      				
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return PriceAtTheEndOfThatDay;	
	}
	
}
