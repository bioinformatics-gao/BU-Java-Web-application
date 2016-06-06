package classes;
import java.sql.*;

public class createStockHistoryTable {
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
			
			stmt.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
