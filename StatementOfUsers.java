package BankApp;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class StatementOfUsers extends Frame {
    Label users,totalBalance,name,address,email,balance;
    Panel  table;
    Connection con;
    ResultSet rs, rtemp;
    Statement st;
    Admin admin;
    String userId;
    StatementOfUsers(Connection con, Admin admin, String userID) {
        this.admin=admin;
        this.userId=userID;
        this.setLayout(new BorderLayout(10, 10));
        this.addWindowListener(new CloseComp());
        name = new Label("Name");
        address = new Label("Address");
        email = new Label("Email");
        balance=new Label("Balance");
        name.setBackground(Color.black);
        name.setForeground(Color.white);
        name.setAlignment(Label.CENTER);
        address.setForeground(Color.white);
        address.setBackground(Color.black);
        address.setAlignment(Label.CENTER);
        email.setForeground(Color.white);
        email.setBackground(Color.black);
        email.setAlignment(Label.CENTER);
        balance.setForeground(Color.white);
        balance.setBackground(Color.black);
        balance.setAlignment(Label.CENTER);
        Panel upperComp=new Panel(new BorderLayout(5,5));
        users=new Label();
        totalBalance=new Label();
        Panel info=new Panel(new GridLayout(2,2));
        info.add(new Label("Total number of users"));
        info.add(users);
        info.add(new Label("Total balance"));
        info.add(totalBalance);
        Panel header=new Panel(new GridLayout(1,4,5,5));
        header.add(name);
        header.add(address);
        header.add(email);
        header.add(balance);
        upperComp.add(header,BorderLayout.SOUTH);
        upperComp.add(info,BorderLayout.CENTER);

        this.add(upperComp,BorderLayout.NORTH);
        this.con=con;
        showData();
        this.setTitle("Statement Of Users");
        this.setVisible(true);
        this.setSize(400, 400);
    }

    private int count() {
        int j = 0;
        try {
            st = con.createStatement();
            rs = st.executeQuery("select * from details;");
            while (rs.next()) {
                j++;
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return j;
    }

    public void showData()
    {
        try {
            int n = count();
            users.setText(n+"");
            int total=0;
            if(table!=null)
                this.remove(table);
            table=new Panel(new GridLayout(n,4,10,5));
            st = con.createStatement();
            rs = st.executeQuery("select * from details;");
            while (rs.next()) {
                table.add(new Label(rs.getString(2)));
                table.add(new Label(rs.getString(3)));
                table.add(new Label(rs.getString(4)));
                table.add(new Label(rs.getString(5)));
                total=total+rs.getInt(5);
            }
            this.add(table, BorderLayout.CENTER);
            totalBalance.setText(total+"");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public Insets getInsets() {
        return new Insets(50, 20, 20, 20);
    }
    void closeWithdraw() {

        admin.setVisible(true);
        this.setVisible(false);
    }
    class CloseComp extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            closeWithdraw();
        }

    }

}

