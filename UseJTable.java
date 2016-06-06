package classes;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

@SuppressWarnings("serial")
public class UseJTable extends JFrame {

  String tabledata[][] = {{"John", "Sutherland", "Student"},
                     {"George", "Davies", "Student"},
                     {"Melissa", "Anderson", "Associate"},
                     {"Stergios", "Maglaras", "Developer"}, };

  String tablefields[] = {"Name", "Surname", "Status"};

public UseJTable(String title, String data[][], String fields[]  ) {
    super( title );
   // setSize( 150, 150 );
    addWindowListener( new WindowAdapter() {
      public void windowClosing( WindowEvent we ) {
        dispose();
        System.exit( 0 );
      }
    } );
    
    tabledata=data.clone();
    tablefields=fields.clone();
    init();
    pack();
    setVisible( true );
  }

public UseJTable(String title) {
    super( title );
    //setSize( 150, 150 );
    addWindowListener( new WindowAdapter() {
      public void windowClosing( WindowEvent we ) {
        dispose();
        System.exit( 0 );
      }
    } );
    
    
    init();
    pack();
    setVisible( true );
  }

@SuppressWarnings("unused")
public static void main( String[] argv ) {
	  UseJTable myTable = new UseJTable("JTable Example" );
  }

private void init() {
    JTable jt = new JTable( tabledata, tablefields );
    JScrollPane pane = new JScrollPane( jt );
    getContentPane().add( pane );
  }
}
