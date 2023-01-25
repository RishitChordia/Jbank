import javax.swing.*;
import java.util.*;
import java.awt.event.*;

public class BankingPortal {
    public static void main(String[] args) {

        Hashtable<String, ImageIcon> iconTable = User.createIconTable();

        TwoButtonGUI homeScreen = new TwoButtonGUI();

        homeScreen.leftButton.setIcon(iconTable.get("AccountHolder"));
        homeScreen.rightButton.setIcon(iconTable.get("BankManager"));

        homeScreen.leftButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                User.accountHolderLoginWindow(iconTable);
            }
        });
        homeScreen.rightButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                User.bankManagerLoginWindow(iconTable);
            }
        });
        homeScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        homeScreen.setVisible(true);
    }
}
