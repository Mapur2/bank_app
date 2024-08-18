package BankApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Admin extends Frame {
    Button requests, statement, updateUser, deleteAC, logout, refreshRequest;
    Connection con;
    Login login;
    String userId;
    RequestForRegister requestForRegister;
    StatementOfUsers statementOfUsers;
    DeleteAC deleteAcc;
    UpdateAC updateAC;

    Admin(Connection con, Login login, String userID) {
        updateAC = null;
        requestForRegister = null;
        statementOfUsers = null;
        deleteAcc = null;
        this.login = login;
        this.con = con;
        this.userId = userID;
        this.setLayout(new GridLayout(5, 1, 10, 10));
        this.addWindowListener(new Close(con));
        Listener listener = new Listener();
        Panel req = new Panel(new GridLayout(1, 2, 2, 5));
        requests = new Button("Request for Register");
        requests.addActionListener(listener);
        req.add(requests);
        refreshRequest = new Button("Refresh Requests");
        refreshRequest.addActionListener(listener);
        req.add(refreshRequest);
        updateRequestBtn();
        statement = new Button("Bank Statement");
        statement.addActionListener(listener);
        updateUser = new Button("Update User");
        updateUser.addActionListener(listener);
        deleteAC = new Button("Delete A/C");
        deleteAC.addActionListener(listener);
        logout = new Button("Logout");
        logout.addActionListener(listener);
        logout.setBackground(Color.red);
        this.add(req);
        this.add(statement);
        this.add(updateUser);
        this.add(deleteAC);
        this.add(logout);

        this.setVisible(true);
        this.setSize(400, 300);
        this.setTitle("Admin-" + userId);
    }

    public void updateRequestBtn() {
        int n = count();
        if (n == 0)
            requests.setEnabled(false);
        else
            requests.setEnabled(true);
    }

    public int count() {
        int j = 0;
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from request;");
            while (rs.next()) {
                j++;
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return j;
    }

    public void updateID(String userId) {
        this.userId = userId;
        this.setTitle("Admin-" + userId);
    }

    private void showLogin() {
        this.setVisible(false);
        login.setVisible(true);
    }

    public Insets getInsets() {
        return new Insets(50, 20, 20, 20);
    }

    public void showRequests() {
        if (requestForRegister == null)
            requestForRegister = new RequestForRegister(con, this);
        else
            requestForRegister.updateDetails();
        this.setVisible(false);
        requestForRegister.setVisible(true);
    }

    public void showStatementOfUsers() {
        if (statementOfUsers == null)
            statementOfUsers = new StatementOfUsers(con, this, userId);
        else {
            statementOfUsers.showData();
            statementOfUsers.setVisible(true);
        }
        this.setVisible(false);
    }

    public void showDeleteAcc() {
        this.setVisible(false);
        if (deleteAcc == null)
            deleteAcc = new DeleteAC(con, this);
        else {
            deleteAcc.setVisible(true);
            deleteAcc.showData();
        }
    }

    public void showUpdateUser() {
        if (updateAC == null)

            updateAC = new UpdateAC(con, this);
        else
        {
            updateAC.showData();
            updateAC.setVisible(true);
        }
        this.setVisible(false);
    }

    class Listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == logout) {
                showLogin();
            } else if (e.getSource() == requests) {
                showRequests();
            } else if (e.getSource() == refreshRequest) {
                updateRequestBtn();
            } else if (e.getSource() == statement) {
                showStatementOfUsers();
            } else if (e.getSource() == deleteAC) {
                showDeleteAcc();
            } else if (e.getSource() == updateUser) {
                showUpdateUser();
            }
        }
    }

}
