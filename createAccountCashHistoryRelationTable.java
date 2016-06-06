package classes;
import java.sql.*;

public class createAccountCashHistoryRelationTable {
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
			String sqlDropStatement = "DROP TABLE Account_Cash_History";
			try {
				stmt.executeUpdate(sqlDropStatement);
			}catch (Exception e1)	{
				System.out.println("Trying to drop Account_Cash_History Table...");
			}
			
			//create the account balance table
			String sqlCreateStatement = "CREATE TABLE Account_Cash_History " +
					"(CustomerId varchar(16) NOT NULL, CashDate int NOT NULL," +
					"CashAmount double,"+
					"PRIMARY KEY (CustomerId, CashDate)," +
					"FOREIGN KEY (CustomerId) REFERENCES Account(CustomerId))ENGINE=INNODB";
			stmt.executeUpdate(sqlCreateStatement);
						
			//insert the account holder information with initial balance  
			String sql = "insert into Account_Cash_History (CustomerId, CashDate, CashAmount) " +
					"values ('Zhen', 1,'0.00')";
			stmt.executeUpdate(sql);
			sql = "insert into Account_Cash_History (CustomerId, CashDate,CashAmount) " +
					"values ('Peter', 1,'2.00')";
			stmt.executeUpdate(sql);
			sql = "insert into Account_Cash_History (CustomerId, CashDate,CashAmount)" +
					" values ('Ting', 1,'5.50')";
			stmt.executeUpdate(sql);
			sql = "insert into Account_Cash_History (CustomerId, CashDate,CashAmount)" +
					" values ('John',1,'120.00')";
			stmt.executeUpdate(sql);
		
			//show the account holder information with current account balance
			sql = "Select * from Account_Cash_History";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())			{
				System.out.printf("CustomerId =%8s, CashDate=%4s, CashAmount= $%9.2f\n",
					rs.getString("CustomerId"), rs.getInt("CashDate"),rs.getDouble("CashAmount"));
			}
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
