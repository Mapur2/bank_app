package BankApp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class UpdateUser extends Frame {
    TextField name, id, eml, add, bln;
    TextArea msg;
    UpdateAC updateAC;
    Button back, update;
    Connection con;
    String userName, userId, email, address;
    int balance;
    Statement st;
    ResultSet rs;
    boolean changed;
    UpdateUser(UpdateAC ua, Connection con) {
        this.addWindowListener(new CloseComp());
        this.con = con;
        this.updateAC = ua;

        this.setLayout(new GridLayout(7, 2, 10, 10));
        name = new TextField();
        id = new TextField();
        id.setEditable(false);
        eml = new TextField();
        add = new TextField();
        bln = new TextField();

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
        update = new Button("Update");
        update.addActionListener(new Listener());
        add(update);
        msg = new TextArea(10,20);
        add(msg);
        msg.setEditable(false);
        this.setVisible(true);
        this.setSize(500, 500);
    }

    protected void getDetails(String id) {
        try {
            userId = id;
            changed=false;
            this.setTitle("Update - "+userId);
            this.id.setText(userId);
            st = con.createStatement();
            rs = st.executeQuery("select * from details where user_id=" + "'" + userId + "';");
            if (rs.next()) {
                userName = rs.getString(2);
                name.setText(userName);
                balance = rs.getInt(5);
                bln.setText(balance + "");
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
        if(changed)
            updateAC.showData();
        updateAC.setVisible(true);
        this.setVisible(false);
    }

    public void updateUser() {
        try {
            String tempName, tempAddress, tempEmail, tempBaln;
            tempName = name.getText().trim();
            tempAddress = add.getText().trim();
            tempEmail = eml.getText().trim();
            tempBaln = bln.getText().trim();
            if (tempName.equals(userName) && tempAddress.equals(address) && tempBaln.equals(balance + "") && tempEmail.equals(email)) {
                msg.setText("Please make some change to Update details");
            } else if (tempName.isEmpty() || tempBaln.isEmpty() || tempAddress.isEmpty() || tempEmail.isEmpty()) {
                msg.setText("Empty Fields");
            } else {
                st = con.createStatement();
                st.executeUpdate("update details set name='" + tempName + "',address='" + tempAddress +
                        "', email='" + tempEmail + "', balance='" + Integer.parseInt(tempBaln) + "' where user_id='" + userId + "';");

                name.setText(tempName);
                bln.setText(tempBaln);
                eml.setText(tempEmail);
                add.setText(tempAddress);
                userName = tempName;
                email = tempEmail;
                address = tempAddress;
                balance = Integer.parseInt(tempBaln);
                changed=true;
                msg.setText("Successfully Updated");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    class Listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == back)
                closeProfile();
            else if (e.getSource() == update) {
                updateUser();
            }
        }
    }

    class CloseComp extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            closeProfile();
        }

    }
}