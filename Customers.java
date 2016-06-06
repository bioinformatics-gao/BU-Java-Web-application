package classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.*;  
import java.util.*;  

import com.mysql.jdbc.ResultSetMetaData;
  
@SuppressWarnings("unused")
public class Customers {  
	private static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static String DB_URL = "jdbc:mysql://localhost:3306/cs565";
	private static String DB_Account_IDNAME = "cs";
	private static String DB_PASSWORD = "java";
	
    public ArrayList<String> getFileContent() {  
        ArrayList<String> LineList = new ArrayList<String>();  
     
            String item[][];  
        	try {
            	Class.forName(JDBC_DRIVER);
        		Connection con = DriverManager.getConnection(DB_URL, DB_Account_IDNAME, DB_PASSWORD);			
        		Statement stmt = con.createStatement();
                String sql;
        		
                
                sql="select * into outfile 'd:/test.xls' from Account";
                stmt.executeQuery(sql);
                
                sql = "Select * from Account";
            		ResultSet rs = stmt.executeQuery(sql);
            		while (rs.next()) {
            			String line = "  " + rs.getString("CustomerId") + " " +rs.getString("CustomerName") + 
            				  "	 " + rs.getInt("OpeningDate") + "  " + rs.getDouble("OpeningBalance")  +
            				   " " + rs.getDouble("Accumulated_Balance");
                    LineList.add(line);  
                    }
            	} catch (Exception e) {  
                     e.printStackTrace();  
                } 
        	        	
        return LineList;  
    }

	
}


