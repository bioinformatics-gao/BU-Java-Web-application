package classes;
import java.sql.*;

public class createAccountTable {
	private static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static String DB_URL = "jdbc:mysql://localhost:3306/cs565";
	private static String DB_Account_IDNAME = "cs";
	private static String DB_PASSWORD = "java";
	
	
	public static void main(String[] args) {
		try {
			Class.forName(JDBC_DRIVER);
			Connection con = DriverManager.getConnection(DB_URL, DB_Account_IDNAME, DB_PASSWORD);			
			Statement stmt = con.createStatement();
	        String sql;
	        
			//drop the account balance table if it exist already.
			String sqlDropStatement = "DROP TABLE Account";
			try {
				stmt.executeUpdate(sqlDropStatement);
			}catch (Exception e1)	{
				System.out.println("Trying to drop Account Table...");
			}
			
			//create the account balance table
			String sqlCreateStatement = "CREATE TABLE Account " + "(CustomerId varchar(16) primary key," + 
			    "CustomerName varchar(32), OpeningDate integer, OpeningBalance decimal(9, 2)," + 
				"Accumulated_Balance decimal(10, 2))ENGINE=INNODB";
			stmt.executeUpdate(sqlCreateStatement);
			
			//insert the account holder information with initial balance  
			sql = "insert into Account (CustomerId, CustomerName, OpeningDate, OpeningBalance,Accumulated_Balance) " +
					"values ('Zhen', 'Zhen_Gao','1','5000', '5000')";
			stmt.executeUpdate(sql);
			sql = "insert into Account (CustomerId, CustomerName, OpeningDate, OpeningBalance, Accumulated_Balance) " +
					"values ('Peter', 'Peter_Smith','2','8580.00','8580.00')";
			stmt.executeUpdate(sql);
			sql = "insert into Account (CustomerId, CustomerName, OpeningDate, OpeningBalance, Accumulated_Balance)" +
					" values ('Ting', 'Ting_Liu','5','17000.00','17000.00')";
			stmt.executeUpdate(sql);
			sql = "insert into Account (CustomerId, CustomerName, OpeningDate, OpeningBalance,Accumulated_Balance)" +
					" values ('John', 'John_Fisher','12','8830.00','8830.00')";
			stmt.executeUpdate(sql);
			
			//show the account holder information with current account balance
			sql = "Select * from Account";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())	{
				System.out.printf("CustomerId =%8s, CustomerName =%16s, OpeningDate=%4d, OpeningBalance = $%9.2f, Accumulated_Balance= $%9.2f\n",
						rs.getString("CustomerId"), rs.getString("CustomerName"),rs.getInt("OpeningDate"), 
						rs.getBigDecimal("OpeningBalance"),rs.getBigDecimal("Accumulated_Balance"));
			}
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
