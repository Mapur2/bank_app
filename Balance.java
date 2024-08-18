package BankApp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Balance extends Frame {
    Label bln;
    Bank b;
    Button back;
    String userId;
    int balance;
    Connection con;
    Statement st;
    ResultSet rs;
    Balance(String userId, Bank b, Connection con){
        this.addWindowListener(new CloseComp());
        this.con=con;
        this.userId=userId;
        this.setSize(500,150);
        this.b = b;

        this.setTitle("Balance");
        this.setVisible(false);
        this.setLayout(new GridLayout(2,2,10,10));
        bln=new Label();
        getDetails();
        bln.setBackground(Color.gray);
        
        this.add(new Label("Current Balance"));
        add(bln);
        back =new Button("Back");
        back.addActionListener(new Listener());
        add(back);
    }

    protected   void getDetails(){
        try {
            st = con.createStatement();
            rs = st.executeQuery("select balance from details where user_id=" + "'" + userId + "';");
            if(rs.next()) {
                balance = rs.getInt(1);
                bln.setText(balance+"");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    @Override
    public Insets getInsets() {
        return new Insets(50, 20, 20, 20);
    }
    void closeBalance(){
        b.setVisible(true);
        this.setVisible(false);
    }
    class Listener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            closeBalance();
        }
    }
    class CloseComp extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            closeBalance();
        }

    }
}