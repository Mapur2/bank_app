package BankApp;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;

public class Close extends WindowAdapter {
    Connection con;
    Close(Connection con){
        this.con=con;
    }
    public void windowClosing(WindowEvent e){
        try {
            con.close();
            System.out.println("closed");
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        System.exit(1);
    }
}

