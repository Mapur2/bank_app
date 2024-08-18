package BankApp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Bank extends Frame {
    String userId,userName;
    Panel optionsPanel;
    Button profileBtn, withdrawBtn, balanceBtn, depositBtn, logoutBtn,transactionBtn;
    Connection con;
    Listener listener;

    ProfilePage profilePage;
    Balance balancePage;
    WithDraw withDrawPage;
    DepositPage depositPage;
    Login login;
    Transactions trans;
    Bank(String userId,String userName, Connection con,Login login) throws Exception {
        this.login=login;
        this.setVisible(true);
        this.setSize(400,300);
        this.userId = userId;
        this.userName=userName;

        listener = new Listener();
        this.setTitle(userName);
        this.con=con;
        this.addWindowListener(new Close(con));

        optionsPanel = new Panel(new GridLayout(6, 1, 10, 10));
        profileBtn = new Button("Profile");
        profileBtn.addActionListener(listener);
        withdrawBtn = new Button("Withdraw");
        withdrawBtn.addActionListener(listener);
        balanceBtn = new Button("Balance");
        balanceBtn.addActionListener(listener);
        depositBtn = new Button("Deposit");
        depositBtn.addActionListener(listener);
        logoutBtn = new Button("Logout");
        logoutBtn.addActionListener(listener);
        logoutBtn.setBackground(Color.red);
        transactionBtn=new Button("Transactions");
        transactionBtn.addActionListener(listener);

        optionsPanel.add(profileBtn);
        optionsPanel.add(balanceBtn);
        optionsPanel.add(withdrawBtn);
        optionsPanel.add(depositBtn);
        optionsPanel.add(transactionBtn);
        optionsPanel.add(logoutBtn);


        this.add(new Label("Welcome, "+userName, 1),BorderLayout.NORTH);
        this.add(optionsPanel, BorderLayout.CENTER);

        profilePage = null;
        balancePage = null;
        withDrawPage = null;
        trans=null;
    }
    public void getDetails(String userId,String userName)
    {
        this.userName=userName;
        this.userId=userId;
    }
    @Override
    public Insets getInsets() {
        return new Insets(50, 20, 20, 20);
    }

//    public void loginUser() {
//        try {
//            if (userId == null)
//                userId = nameField.getText().trim();
//            if (userPassword == null)
//                userPassword = passwordField.getText().trim();
//            if (userPassword.isEmpty() || userId.isEmpty()) {
//                message.setText("Empty fields");
//                return;
//            }
//            st = con.createStatement();
//            rs = st.executeQuery("select * from bank where user_id=" + "'" + userId + "'and password=" + "'" + userPassword + "';");
//            if (!rs.next()) {
//                message.setText("Incorrect User ID or Password");
//            } else {
//                userName = rs.getString(3);
//                balance = rs.getInt(6);
//                email = rs.getString(5);
//                address = rs.getString(4);
//                message.setText("Welcome, " + userName);
//                this.remove(loginPanel);
//                this.add(optionsPanel);
//                this.setTitle(userName);
//                this.setSize(400, 400);
//            }
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }

    void showProfile() {
        if(profilePage==null)
            profilePage = new ProfilePage(userId, this,con);
        else
            profilePage.getDetails();
        profilePage.setVisible(true);
        this.setVisible(false);
    }

    void showBalance() {
        if(balancePage==null)
            balancePage = new Balance(userId,this,con);
        else
            balancePage.getDetails();
        balancePage.setVisible(true);
        this.setVisible(false);
    }
    public void showTransaction(){
        if(trans==null)
            trans=new Transactions(con,this,userId);
        else
            trans.updateDate();
        this.setVisible(false);
        trans.setVisible(true);
    }
    void showWithdraw() {
        if(withDrawPage==null)
            withDrawPage = new WithDraw(this, con, userId);
        else
            withDrawPage.getDetails();
        withDrawPage.setVisible(true);
        this.setVisible(false);
    }

    void showDeposit(){
        if(depositPage==null)
            depositPage = new DepositPage(this, con, userId);
        else
            depositPage.getDetails();
        depositPage.setVisible(true);
        this.setVisible(false);
    }
    void showLogin(){
        login.setVisible(true);
        this.setVisible(false);
    }
    class Listener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == profileBtn) {
                showProfile();
            } else if (e.getSource() == balanceBtn) {
                showBalance();
            } else if (e.getSource() == withdrawBtn) {
                showWithdraw();
            }
            else if(e.getSource()==depositBtn){
                showDeposit();
            }
            else if(e.getSource()==logoutBtn)
            {
                showLogin();
            } else if (e.getSource()==transactionBtn) {
                showTransaction();
            }
        }
    }
}
