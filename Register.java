package BankApp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Register extends Frame {
    TextField name, id, email, address, password, cpassword,amount;
    Button registerBtn,loginBtn;
    Panel panel;
    Connection con;
    Login log;
    Label msg;
    ResultSet rs;
    Statement st;
    Register(Login log, Connection con){
        this.log=log;
        this.con=con;
        this.setTitle("Register");
        this.setLayout(new BorderLayout(10,10));
        panel=new Panel(new GridLayout(9,2,10,10));

        name=new TextField("");
        email=new TextField("");
        address=new TextField("");
        amount=new TextField("2000");
        id=new TextField("");
        password=new TextField("");
        password.setEchoChar('*');
        cpassword=new TextField("");
        cpassword.setEchoChar('*');

        Listener listener=new Listener();
        registerBtn=new Button("Register");
        registerBtn.addActionListener(listener);
        loginBtn=new Button("Login");
        loginBtn.addActionListener(listener);
        registerBtn.setBackground(Color.cyan);

        panel.add(new Label("Name"));
        panel.add(name);
        panel.add(new Label("Email"));
        panel.add(email);
        panel.add(new Label("Address"));
        panel.add(address);
        panel.add(new Label("Opening Balance"));
        panel.add(amount);
        panel.add(new Label("User ID"));
        panel.add(id);
        panel.add(new Label("Password"));
        panel.add(password);
        panel.add(new Label("Confirm Password"));
        panel.add(cpassword);

        panel.add(registerBtn);
        panel.add(loginBtn);

        msg=new Label();
        msg.setBackground(Color.lightGray);
        this.add(msg,BorderLayout.SOUTH);

        this.add(panel,BorderLayout.CENTER);
        this.setSize(400,400);
        this.setVisible(true);
        this.addWindowListener(new Close(con));
    }

    public Insets getInsets(){
        return new Insets(50,20,20,20);
    }
    private void showLogin(){
        msg.setText("");
        name.setText("");
        id.setText("");
        address.setText("");
        cpassword.setText("");
        password.setText("");
        email.setText("");
        this.setVisible(false);
        log.setVisible(true);
    }

    private void registerUser(){
        try {
            String userName, userId, userEmail, userAddress, userPassword, userCPassword,userBalance;
            int balance;
            userBalance=amount.getText().trim();
            userName=name.getText().trim();
            userId=id.getText().trim();
            userEmail=email.getText().trim();
            userAddress=address.getText().trim();
            userPassword=password.getText().trim();
            userCPassword=cpassword.getText().trim();
            if(userAddress.isEmpty() || userEmail.isEmpty() || userCPassword.isEmpty() ||
                    userPassword.isEmpty() || userId.isEmpty() || userName.isEmpty() || userBalance.isEmpty())
                throw new RegisterException("Empty Field(s)");
            if(!userCPassword.equals(userPassword))
                throw new RegisterException("Confirm Password does not match with Password");
            balance=Integer.parseInt(userBalance);
            if(balance<2000)
                throw new RegisterException("Minimum balance must be 2000");
            st=con.createStatement();
            rs=st.executeQuery("select * from login where user_id='"+userId+"';");
            if(rs.next())
                throw new RegisterException("User Id already present. Try some different id");
            st=con.createStatement();
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String strDate= formatter.format(date);
            st.executeUpdate("insert into request(user_id,name,address,email,balance,password,odata) value('"+userId+"','"+userName+
                    "','"+ userAddress+"','"+userEmail+"',"+balance+",'"+userPassword+"','"+strDate+"');");
            log.message.setText("Requested for Register");
            amount.setText("2000");
            showLogin();
        }
        catch (RegisterException e){
            msg.setText(e.toString());
        }
        catch (Exception e){
            msg.setText("Something went wrong");
            System.out.println(e);
        }
    }

    private class Listener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==loginBtn){
                showLogin();
            }
            else if (e.getSource()==registerBtn) {
                registerUser();
            }
        }
    }
    class RegisterException extends Exception{
        String msg;
        RegisterException(String m){
            msg=m;
        }
        public String toString(){
            return  msg;
        }
    }
}
