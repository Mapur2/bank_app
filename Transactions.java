package BankApp;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class Transactions extends Frame {
    Label date, type, amount;
    Panel  table;
    Connection con;
    ResultSet rs, rtemp;
    Statement st;
    Bank bank;
    String userId;
    Bank b;
    Transactions(Connection con, Bank bank, String userID) {
        b=bank;
        this.userId=userID;
        this.setLayout(new BorderLayout(10, 10));
        this.addWindowListener(new CloseComp());
        date = new Label("Date");
        type = new Label("Type");
        amount = new Label("Amount");
        date.setBackground(Color.black);
        date.setForeground(Color.white);
        date.setAlignment(Label.CENTER);
        type.setForeground(Color.white);
        type.setBackground(Color.black);
        type.setAlignment(Label.CENTER);
        amount.setForeground(Color.white);
        amount.setBackground(Color.black);
        amount.setAlignment(Label.CENTER);
        Panel upper=new Panel(new GridLayout(1,3,5,5));
        upper.add(date);
        upper.add(type);
        upper.add(amount);
        this.add(upper,BorderLayout.NORTH);
        this.con=con;
        showData();
        this.add(new Label(), BorderLayout.WEST);
        this.add(new Label(), BorderLayout.EAST);
        this.setTitle("Transactions");
        this.setVisible(true);
        this.setSize(400, 400);
    }

    private int count() {
        int j = 0;
        try {
            st = con.createStatement();
            rs = st.executeQuery("select * from transaction where user_id='"+userId+"';");
            while (rs.next()) {
                j++;
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return j;
    }

    private void showData() {
        try {
            int n = count();
            table = new Panel(new GridLayout(n, 3, 8, 8));
            st = con.createStatement();
            rs = st.executeQuery("select * from transaction where user_id='"+userId+"';");
            while (rs.next()) {
                date = new Label(rs.getString(2));
                String tp=rs.getString(3);
                type = new Label();
                amount = new Label(rs.getString(4));
                if(tp.equals("d"))
                {
                    type.setText("Deposit");
                    amount.setBackground(Color.green);
                }
                else{
                    type.setText("Withdraw");
                    amount.setBackground(Color.red);
                }
                date.setAlignment(Label.CENTER);
                type.setAlignment(Label.CENTER);
                amount.setAlignment(Label.CENTER);
                table.add(date);
                table.add(type);
                table.add(amount);
            }
            this.add(table, BorderLayout.CENTER);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    void updateDate(){
        try {
            int n = count();
            this.remove(table);
            table=new Panel(new GridLayout(n, 3, 8, 8));
            st = con.createStatement();
            rs = st.executeQuery("select * from transaction where user_id='"+userId+"';");
            while (rs.next()) {
                date = new Label(rs.getString(2));
                String tp=rs.getString(3);
                type = new Label();
                amount = new Label(rs.getString(4));
                if(tp.equals("d"))
                {
                    type.setText("Deposit");
                    amount.setBackground(Color.green);
                }
                else{
                    type.setText("Withdraw");
                    amount.setBackground(Color.red);
                }
                date.setAlignment(Label.CENTER);
                date.setBackground(Color.white);
                type.setBackground(Color.white);
                type.setAlignment(Label.CENTER);
                amount.setAlignment(Label.CENTER);
                table.add(date);
                table.add(type);
                table.add(amount);
            }
            this.add(table, BorderLayout.CENTER);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    @Override
    public Insets getInsets() {
        return new Insets(50, 20, 20, 20);
    }
    void closeWithdraw() {

        b.setVisible(true);
        this.setVisible(false);
    }
    class CloseComp extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            closeWithdraw();
        }

    }

}
