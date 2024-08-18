package BankApp;



import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class UpdateAC extends Frame {
    Button update[];
    String users[];
    Connection con;
    Statement st;
    ResultSet rs;
    Admin admin;
    Panel reqs;
    Listener listener;
    int countReq;
    ErrorMessage errorMessage;
    UpdateUser updateUser;
    UpdateAC(Connection con,Admin admin){
        updateUser = null;
        listener=new Listener();
        this.addWindowListener(new CloseComp());
        this.setLayout(new BorderLayout(10,10));
        this.con=con;
        this.admin=admin;
        errorMessage=new ErrorMessage("");
        Panel headings=new Panel(new GridLayout(1,5,10,10));
        headings.add(new Label("Name"));
        headings.add(new Label("Address"));
        headings.add(new Label("Email"));
        headings.add(new Label("Balance"));
        headings.add(new Label("Update"));
        showData();
        this.add(headings,BorderLayout.NORTH);
        this.setVisible(true);
        this.setSize(600,200+20*countReq);
    }
    public void showData(){
        countReq=count();
        if(countReq==0)
            return;
        if(reqs!=null)
        {
            this.remove(reqs);
        }
        reqs=new Panel(new GridLayout(countReq,5,5,5));

        update=new Button[countReq];
        users=new String[countReq];
        int i=0;
        try {
            st = con.createStatement();
            rs = st.executeQuery("select * from details;");
            while(rs.next())
            {
                reqs.add(new Label(rs.getString(2)));
                reqs.add(new Label(rs.getString(3)));
                reqs.add(new Label(rs.getString(4)));
                reqs.add(new Label(rs.getString(5)));
                update[i]=new Button("Update");
                update[i].setSize(50,20);
                update[i].addActionListener(listener);
                users[i]=rs.getString(1);
                reqs.add(update[i]);
                i++;
            }
            this.add(reqs,BorderLayout.CENTER);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
    private void showError(String msg){
        this.setVisible(false);
        errorMessage.setVisible(true);
        errorMessage.label.setText(msg);
    }
    int count() {
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
    public Insets getInsets(){
        return new Insets(50,20,20,20);
    }

    public void showUpdate(int i){
        if(updateUser==null){
            updateUser=new UpdateUser(this,con);
        }
        else{
            this.setVisible(true);
            updateUser.setVisible(true);
        }
        this.setVisible(false);
        updateUser.getDetails(users[i]);
    }
    class Listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int i;
            for(i=0;i<countReq;i++)
            {
                if(update[i]==e.getSource())
                    break;
            }
            showUpdate(i);
        }
    }
    void closeProfile() {
        admin.setVisible(true);
        admin.updateRequestBtn();
        this.setVisible(false);
    }
    class CloseComp extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            closeProfile();
        }

    }
}
