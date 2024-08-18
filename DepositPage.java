package BankApp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.Statement;

public class DepositPage extends Frame {
    Label bln, msg;
    TextField amt;
    Bank b;
    Button back;
    Connection con;
    Button wd;
    Statement st;
    ResultSet rs;
    int baln;
    String userId;

    DepositPage(Bank b, Connection con, String userId) {
        this.addWindowListener(new CloseComp());
        this.userId = userId;
        this.con = con;
        this.setSize(500, 200);
        this.b = b;

        this.setTitle("Deposit");
//        this.setVisible(false);
        this.setLayout(new GridLayout(4, 2, 10, 10));
        msg = new Label();
        bln = new Label();
        getDetails();

        bln.setBackground(Color.gray);
        this.add(new Label("Current Balance"));
        add(bln);
        this.add(new Label("Amount"));
        amt = new TextField();
        add(amt);
        wd = new Button("Deposit");
        wd.addActionListener(new Listener());
        add(wd);
        back = new Button("Back");
        back.addActionListener(new Listener());
        add(back);
        add(msg);
    }

    protected void getDetails() {
        try {
            st = con.createStatement();
            rs = st.executeQuery("select balance from details where user_id=" + "'" + userId + "';");
            if (rs.next()) {
                baln = rs.getInt(1);
                bln.setText(baln + "");
            }
            msg.setText("");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public Insets getInsets() {
        return new Insets(50, 20, 20, 20);
    }

    void closeDeposit() {
        b.setVisible(true);
        this.setVisible(false);
    }

    private void deposit() {
        try {
            if (amt.getText().trim().isEmpty()) {
                msg.setText("Empty field");
                return;
            }
            int eb = Integer.parseInt(amt.getText().trim());
            if (eb <= 0) {
                msg.setText("Amount should be greater than 0");
            } else {
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String strDate= formatter.format(date);
                Statement tst=con.createStatement();
                tst.executeUpdate("insert into transaction value('"+userId+"','"+strDate+"','d',"+eb+");");
                st = con.createStatement();
                baln = baln + eb;
                st.executeUpdate("UPDATE details SET balance=" + baln + " WHERE user_id= '" + userId + "' ;");
                Statement temp = con.createStatement();
                temp.executeUpdate("commit");
                msg.setText(eb + "/- successfully deposit");
                bln.setText(baln + "");
            }
            amt.setText("");
        }
        catch (NumberFormatException e) {
            amt.setText("");
            msg.setText("Invalid Input");
        }
        catch (Exception e) {
            amt.setText("");
            msg.setText("Cannot deposit amount");
            System.out.println(e);
        }
    }

    class Listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == back) {
                closeDeposit();

            } else if (e.getSource() == wd) {
                deposit();

            }
        }
    }

    class CloseComp extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            closeDeposit();
        }

    }
}