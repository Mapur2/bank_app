package BankApp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ProfilePage extends Frame {
    TextField name, id, eml, add, bln;
    Bank b;
    Button back;
    Connection con;
    String userName, userId, email, address;
    int balance;
    Statement st;
    ResultSet rs;

    ProfilePage(String userId, Bank b, Connection con) {
        this.addWindowListener(new CloseComp());
        this.userId=userId;
        this.con = con;
        this.b = b;

        this.setTitle("Profile");
        this.setSize(500, 300);
//        this.setVisible(false);


        this.setLayout(new GridLayout(6, 2, 10, 10));
        name = new TextField();
        name.setEditable(false);
        id = new TextField(userId);
        id.setEditable(false);
        eml = new TextField();
        eml.setEditable(false);
        add = new TextField();
        add.setEditable(false);
        bln = new TextField();
        bln.setEditable(false);
        getDetails();

        this.add(new Label("ID"));
        add(id);
        this.add(new Label("Name"));
        this.add(name);
        this.add(new Label("Email"));
        add(eml);
        this.add(new Label("Address"));
        add(add);
        this.add(new Label("Balance"));
        add(bln);
        back = new Button("Back");
        back.addActionListener(new Listener());
        add(back);
    }

    protected void getDetails() {
        try {
            st = con.createStatement();
            rs = st.executeQuery("select * from details where user_id=" + "'" + userId + "';");
            if(rs.next()) {
                userName = rs.getString(2);
                name.setText(userName);
                balance = rs.getInt(5);
                bln.setText(balance+"");
                email = rs.getString(4);
                eml.setText(email);
                address = rs.getString(3);
                add.setText(address);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public Insets getInsets() {
        return new Insets(50, 20, 20, 20);
    }

    void closeProfile() {
        b.setVisible(true);
        this.setVisible(false);
    }

    class Listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            closeProfile();
        }
    }
    class CloseComp extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            closeProfile();
        }

    }
}