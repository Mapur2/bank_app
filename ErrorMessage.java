package BankApp;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ErrorMessage extends Frame {
    Label label;
    ErrorMessage(String msg){
        this.setVisible(false);
        label=new Label(msg);
        label.setAlignment(1);
        this.add(label);
        this.addWindowListener(new CloseComp());
        this.setSize(300,150);
    }
    void closeProfile() {
        this.setVisible(false);
    }
    class CloseComp extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            closeProfile();
        }

    }
}
