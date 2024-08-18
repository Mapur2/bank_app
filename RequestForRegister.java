package BankApp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class RequestForRegister extends Frame {
    Button confirm[],reject[];
    String users[];
    Connection con;
    Statement st;
    ResultSet rs;
    Admin admin;
    Panel reqs;
    Listener listener;
    int countReq;
    ErrorMessage errorMessage;
    RequestForRegister(Connection con,Admin admin){
        listener=new Listener();
        this.addWindowListener(new CloseComp());
        errorMessage=new ErrorMessage("");
        this.setLayout(new BorderLayout(10,10));
        this.con=con;
        this.admin=admin;
        Panel headings=new Panel(new GridLayout(1,8,10,10));
        headings.add(new Label("User Id"));
        headings.add(new Label("Name"));
        headings.add(new Label("Address"));
        headings.add(new Label("Email"));
        headings.add(new Label("Balance"));
        headings.add(new Label("Opening Date"));
        headings.add(new Label("Confirm"));
        headings.add(new Label("Reject"));
        showData();
        this.add(headings,BorderLayout.NORTH);
        this.setVisible(true);
        this.setSize(600,200+20*countReq);
    }
    public void updateDetails(){
        countReq= count();
        this.setSize(600,200+15*countReq);
        this.remove(reqs);
        reqs=new Panel(new GridLayout(countReq,8,5,5));
        confirm=new Button[countReq];
        reject=new Button[countReq];
        users=new String[countReq];
        int i=0;
        try {
            st = con.createStatement();
            rs = st.executeQuery("select * from request;");
            while(rs.next())
            {
                reqs.add(new Label(rs.getString(1)));
                reqs.add(new Label(rs.getString(3)));
                reqs.add(new Label(rs.getString(4)));
                reqs.add(new Label(rs.getString(5)));
                reqs.add(new Label(rs.getString(6)));
                reqs.add(new Label(rs.getString(7)));
                confirm[i]=new Button("Confirm");
                confirm[i].setSize(50,20);
                confirm[i].addActionListener(listener);
                reject[i]=new Button("Reject");
                reject[i].setSize(50,20);
                reject[i].addActionListener(listener);
                users[i]=rs.getString(1);
                reqs.add(confirm[i]);
                reqs.add(reject[i]);
                i++;
            }
            this.add(reqs,BorderLayout.CENTER);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
    private void showData(){
        countReq=count();
        if(countReq==0)
            return;
        reqs=new Panel(new GridLayout(countReq,8,5,5));
        confirm=new Button[countReq];
        reject=new Button[countReq];
        users=new String[countReq];
        int i=0;
        try {
            st = con.createStatement();
            rs = st.executeQuery("select * from request;");
            while(rs.next())
            {
                reqs.add(new Label(rs.getString(1)));
                reqs.add(new Label(rs.getString(3)));
                reqs.add(new Label(rs.getString(4)));
                reqs.add(new Label(rs.getString(5)));
                reqs.add(new Label(rs.getString(6)));
                reqs.add(new Label(rs.getString(7)));
                confirm[i]=new Button("Confirm");
                confirm[i].setSize(50,20);
                confirm[i].addActionListener(listener);
                reject[i]=new Button("Reject");
                reject[i].setSize(50,20);
                reject[i].addActionListener(listener);
                users[i]=rs.getString(1);
                reqs.add(confirm[i]);
                reqs.add(reject[i]);
                i++;
            }
            this.add(reqs,BorderLayout.CENTER);
        }
        catch (Exception e)
        {
            System.out.println(e);
            showError("Error in retrieving data");
        }
    }
    private void showError(String msg){
        errorMessage.setVisible(true);
        errorMessage.label.setText(msg);
    }
    int count() {
        int j = 0;
        try {
            st = con.createStatement();
            rs = st.executeQuery("select * from request;");
            while (rs.next()) {
                j++;
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return j;
    }
    public Insets getInsets(){
        return new Insets(50,20,20,20);
    }
    private void registerUser(int i)
    {
        try {
            int balance;
            st = con.createStatement();
            rs = st.executeQuery("select * from request where user_id='"+users[i]+"';");
            rs.next();
            st=con.createStatement();
            balance=rs.getInt(6);
            st.executeUpdate("insert into login value('"+rs.getString(1)+"','"+rs.getString(2)+"','user');");
            st=con.createStatement();
            st.executeUpdate("insert into details value('"+users[i]+"','"+rs.getString(3)+"','"+
                    rs.getString(4)+"','"+rs.getString(5)+"',"+balance+");");

            deleteData(i);
        }
        catch (Exception e)
        {
            System.out.println(e);
            showError("Cannot confirm/reject");
        }
    }
    private void deleteData(int i){
        try {
            st=con.createStatement();
            st.executeUpdate("delete from request where user_id='"+users[i]+"';");
            st=con.createStatement();
            st.executeUpdate("commit;");
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
    class Listener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            int i=0;
            boolean confirmed=false;
            for(i=0;i<countReq;i++)
            {
                if(e.getSource()==confirm[i])
                {
                    confirmed=true;
                    break;
                }
                if(e.getSource()==reject[i])
                    break;
            }
            if(confirmed)
            {
                registerUser(i);
                deleteData(i);
            }
            else{
                deleteData(i);
            }
            confirm[i].setEnabled(false);
            reject[i].setEnabled(false);
        }
    }
    void closeProfile() {
        this.setVisible(false);
        admin.setVisible(true);
        admin.updateRequestBtn();
    }
    class CloseComp extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            closeProfile();
        }

    }
}
