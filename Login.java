package BankApp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Login extends Frame {
    String hidePass, showPass;
    Panel middlePanel;
    Label user_id, password;
    TextField nameField, passwordField, message;
    Button login, reg;
    ResultSet rs;
    Statement st;
    Connection con;
    Bank bank;
    Register register;
    Admin admin;

    Login() throws Exception {

        admin = null;
        register = null;
        bank = null;
        user_id = new Label("User ID : ");
        password = new Label("Password : ");
        nameField = new TextField(100);
        passwordField = new TextField(100);
        passwordField.setEchoChar('*');
        //passwordField.addKeyListener(new KeyListen());
        message = new TextField("Welcome. Enter user id and password");
        message.setEditable(false);
        login = new Button("Login");
        login.setSize(10, 10);
        login.addActionListener(new Listener());
        login.setBackground(Color.cyan);

        middlePanel = new Panel(new GridLayout(5, 2, 10, 10));
        middlePanel.add(user_id);
        middlePanel.add(nameField);
        middlePanel.add(password);
        middlePanel.add(passwordField);
        middlePanel.add(login);
        reg = new Button("Register");
        reg.addActionListener(new Listener());
        middlePanel.add(reg);
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "rupam123");
        this.addWindowListener(new Close(con));
        this.add(middlePanel, BorderLayout.CENTER);
        this.add(message, BorderLayout.SOUTH);

        this.setTitle("Login Page");
        this.setVisible(true);
        this.setSize(400, 300);
    }

    public static void main(String[] args) throws Exception {
        new Login();
    }

    @Override
    public Insets getInsets() {
        return new Insets(50, 20, 20, 20);
    }

    public void loginUser() {
        try {
            String userId, userPassword, userName;
            userId = nameField.getText().trim();
            userPassword = passwordField.getText().trim();
            if (userPassword.isEmpty() || userId.isEmpty()) {
                message.setText("Empty fields");
                return;
            }
            st = con.createStatement();
            System.out.println(userId+" "+userPassword);
            rs = st.executeQuery("select * from login where user_id=" + "'" + userId + "'and password=" + "'" + userPassword + "';");
            if (!rs.next()) {
                message.setText("Incorrect User ID or Password");
            } else {
                nameField.setText("");
                passwordField.setText("");
                userName = rs.getString(1);
                this.setVisible(false);
                if (rs.getString(3).equals("user")) {
                    if (bank == null)
                        bank = new Bank(userId, userName, con, this);
                    else {
                        bank.setVisible(true);
                        bank.getDetails(userId, userName);
                    }
                }
                else{
                    if (admin == null)
                        admin = new Admin(con, this,userId);
                    else {
                        admin.setVisible(true);
                        admin.updateID(userId);
                        admin.updateRequestBtn();
                    }
                }
                message.setText("");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void showRegister() {
        if (register == null)
            register = new Register(this, con);
        this.setVisible(false);
        register.setVisible(true);
    }

    class Listener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == login)
                loginUser();
            else if (e.getSource() == reg)
                showRegister();
        }
    }
}
/*create table bank(
	id int primary key auto_increment,
    user_id varchar(100) unique,
    name varchar(255),
    address varchar(255),
    email varchar(255),
    balance int not null
);*/