package classes;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class Brokerage extends JFrame implements ActionListener {
	private static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static String DB_URL = "jdbc:mysql://localhost:3306/cs565";
	private static String DB_Account_IDNAME = "cs";
	private static String DB_PASSWORD = "java";
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;
    final JFrame frame = new JFrame("Centered");
    int date;
    final int commissions=10;
    
    public static void main(String[] args) {
    	Brokerage gui = new Brokerage( );
        gui.setVisible(true);
    }

    public Brokerage( )  {
        super("Brokerage functions");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenu Menu = new JMenu("Please choose the procedure you want to perform");

        JMenuItem LoadChoice = new JMenuItem("Load the initial data ï¬�le");
        LoadChoice.addActionListener(this);
        Menu.add(LoadChoice);
        JMenuItem whiteChoice = new JMenuItem("Add a New customer");
        whiteChoice.addActionListener(this);
        Menu.add(whiteChoice);
        JMenuItem blueChoice = new JMenuItem("List all Customers");
        blueChoice.addActionListener(this);
        Menu.add(blueChoice);
        JMenuItem redChoice = new JMenuItem("Save As");
        redChoice.addActionListener(this);
        Menu.add(redChoice);

        JMenuBar bar = new JMenuBar( );
        bar.add(Menu);
        setJMenuBar(bar);
    }

    @SuppressWarnings("unused")
	public void actionPerformed(ActionEvent e)   {
        String buttonString = e.getActionCommand( );

        if (buttonString.equals("Load the initial data ï¬�le"))
        	createFileChooser(frame);
        else if (buttonString.equals("Add a New customer")){
        	CatchInput information = new CatchInput();
        	information.run();
        }
        else if (buttonString.equals("List all Customers")){
        	Customers cc = new Customers();
			
        	String tabledata[][] = {{"Elsa", "Elsa_Rossa", "101", "55800.5", "85000.54" },
                    {"Zhen", "Zhen", "1", "78888" , "558878.58"},
                    {"Ting", "Ting_Liu", "11", "58369.78","8855.35"},
                    {"Peter", "Peter_Smith", "2", "8580.21","8360.71"}, };

         String tablefields[] = {"ID", "Name", "OpenDate", "Innitial b", "current Balance"};
           UseJTable myJ= new UseJTable(buttonString, tabledata,tablefields);
        }
        else if (buttonString.equals("Save As"))
        {
        	try {
      	    Class.forName(JDBC_DRIVER);
  		    Connection con = DriverManager.getConnection(DB_URL, DB_Account_IDNAME, DB_PASSWORD);			
  		    Statement stmt = con.createStatement();
            java.lang.String sql;
            /*//sql= "select * into outfile 'd:/data1.csv' from Account";
            //stmt.executeQuery(sql);
            saved information = new saved();
        	//information.run();*/ 
               } catch (Exception e2) {  
           System.out.println("mistakes in saving");
        } 
        }
        else
            System.out.println("Unexpected error.");
    }
    
 
  
    private static void createFileChooser(final JFrame frame)  {
    	
		String filename = File.separator+"tmp";
		JFileChooser fileChooser = new JFileChooser(new File(filename));
		// pop up an "Open File" file chooser dialog
		fileChooser.showOpenDialog(frame);
		System.out.println("File to open: " + fileChooser.getSelectedFile());
	    File file=fileChooser.getSelectedFile(); 
	    
	    try   { 
	    	 BufferedReader in = new BufferedReader(new FileReader(file));
	    	 String str;
	    	 while ((str = in.readLine()) != null && !str.trim().isEmpty()){
	    		 String line = str; 
	    		 processTransaction(line);
	    	 }
	    	 in.close();
	    }catch(IOException e) { 
	      System.out.println("I/O Error: " + e); 
	    } 
		
	}
    
	// get the line of Transaction 
	//Use Hashtable or HashMap to do an efficient lookup if a Customer exists or not
	public static void processTransaction(String line) {
   		 String str = line; //line reading
   		 String[] arr;
	   	 arr = str.split(" ",-1);
	   	 System.out.print(arr[0]);
	   	 String type= arr[0];
         String Id = arr[1];
	   	 String date = arr[2]; 
	   	 
	   	 if(type.equals("Deposit")){ 
	   		 System.out.println("xxxx");
	   		 String money = arr[3];
	       	 System.out.println(Id);
	       	 try {
	              Class.forName(JDBC_DRIVER);
	  			  Connection con = DriverManager.getConnection(DB_URL, DB_Account_IDNAME, DB_PASSWORD);			
	  			  Statement stmt = con.createStatement();
	              
	              String sql = "insert into TRANSACTIONS (CustomerId, Transaction_Date,Transaction_Type,PriceOrDepositAmount)" +
	              		" values ('"+Id+"', '"+date+"', 'Deposit', '"+money+"')";
	  			  stmt.executeUpdate(sql);
	  			  stmt.close();
			 } catch (Exception e) {
					e.printStackTrace();
			 }
 	      } else if (type.equals("Buy") ||type.equals("Sell")){
  	          System.out.println("yyyy");
 	    	  String symbol = arr[3];
 	    	  String quantity = arr[4];
 	    	  System.out.println(Id);
 	    	  try {
	              Class.forName(JDBC_DRIVER);
	  		      Connection con = DriverManager.getConnection(DB_URL, DB_Account_IDNAME, DB_PASSWORD);			
	  			  Statement stmt = con.createStatement();
	              
	              String sql = "insert into TRANSACTIONS (CustomerId,Transaction_Date,Transaction_Type,StockSymbol,Quantity) " +
	            		" values ('"+Id+"', '"+date+"', '"+type+"', '"+symbol+"','"+quantity+"')";
  				  stmt.executeUpdate(sql);
                  stmt.close();
			 } catch (Exception e) {
				  e.printStackTrace();
			 }
        } 
	}
	
  	// get the line of Transaction 
	//Use Hashtable or HashMap to do an efficient lookup if a Customer exists or not
	public double computeRevenue(int day) throws ClassNotFoundException {
		this.date= day;
		double quantity = 0;
		try {
			Class.forName(JDBC_DRIVER);
			Connection con = DriverManager.getConnection(DB_URL, DB_Account_IDNAME, DB_PASSWORD);			
			Statement stmt = con.createStatement();
	        String sql;
			//show the account holder information with current stock 
			sql = "Select * from Account_Stockhisory where CustomerId ='"+date+"'))";
			ResultSet rs = stmt.executeQuery(sql);
			sql= "SELECT COUNT(*) FROM Account_Stockhisory";
			stmt.executeQuery(sql);
			rs.next();			
				quantity=rs.getInt(1);
			
			stmt.close();
			con.close(); 
			return quantity*10;
		  } catch (SQLException e) {
	        e.printStackTrace();
	      }
		return quantity*10.0;	
	   }
	
	public void addANewCustomer(String line) {
		 String str = line; //line reading
   		 String[] arr;
   		 try {
		   	 arr = str.split(" ",-1);
		   	 System.out.print(arr[0]);
		   	 String Id= arr[0];
	         String name = arr[1];
		   	 String date = arr[2];
		   	 Integer Innidate = Integer.parseInt(date);
	   		 String money = arr[3];
	   		 Double Innimoney=Double.parseDouble(money);
	       	 System.out.println(Id);
	     
	    	 Account newAcct= new Account(Id, name, Innidate, Innimoney);
	    	 newAcct.addToAccountTable();
		 } catch (Exception e) {
					e.printStackTrace();
		 }
	}
}
@SuppressWarnings("serial")//catch input
class CatchInput extends JFrame implements ActionListener {
	private JTextField Information;
	String line;
	public CatchInput( )	{
		super("catch the input of new customer");
		setSize(600, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout(2, 1));
		
		JPanel InformationPanel = new JPanel( ); 
		InformationPanel.setLayout(new BorderLayout( ));
		InformationPanel.setBackground(Color.WHITE); 
		
		Information = new JTextField(50);
		
		InformationPanel.add(Information, BorderLayout.SOUTH);
        JLabel nameLabel = new JLabel("Enter your name here:");
        InformationPanel.add(nameLabel, BorderLayout.CENTER);
        add(InformationPanel);
		
		JPanel buttonPanel = new JPanel( );
		buttonPanel.setLayout(new FlowLayout( ));
		JButton actionButton = new JButton("Send input"); 
		actionButton.addActionListener(this);
		buttonPanel.add(actionButton);
		JButton clearButton = new JButton("Clear"); 
		clearButton.addActionListener(this);
		buttonPanel.add(clearButton); 
		add(buttonPanel);
	}
	public void actionPerformed(ActionEvent e) 	{
		String actionCommand = e.getActionCommand( );
		if (actionCommand.equals("Send input")){
		    line =Information.getText( );
		    System.out.println(line);
		    Brokerage B= new Brokerage();
		    B.addANewCustomer(line);
		    Information.setText("You send the information:   " + Information.getText( ));
		}
		else if (actionCommand.equals("Clear"))
		Information.setText("");
		else
		Information.setText("Unexpected error.");
	} 
	
	public String run(){
		CatchInput gui = new CatchInput( );
		gui.setVisible(true);
		return line;
	}

}

@SuppressWarnings("serial")
class saved extends JFrame 	{
		// Constructor		
		public saved() 		{
			super("Strings");  // Window title
			setVisible(true);
		}
		// Drawing strings in the graphics context
		public void paint( Graphics g ) 		{
			super.paint(g);	
			int x = 20, y = 50;
			Font newFont	=	new Font("Arial", Font.PLAIN, 18);
			g.setFont(newFont);
			g.drawString("A string in Arial and Plain of size 18", x, y);
		}
		
		/*public String run(){
			
		}*/
		
	}