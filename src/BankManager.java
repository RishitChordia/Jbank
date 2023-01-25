import java.sql.*;
import javax.swing.*;
import javax.swing.Timer;
import java.util.*;
import java.awt.event.*;

public class BankManager extends User {
    int managerID;

    public BankManager(String username, String password, int managerID) {
        super(username, password);
        this.managerID = managerID;
    }

    public static boolean checkIfAccountExists(int managerID) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "admin");
            Statement stmt = conn.createStatement();
            String sqlQuery = "use jbank;";
            stmt.executeUpdate(sqlQuery);
            sqlQuery = "select username from bankmanager where managerid = " + String.valueOf(managerID)
                    + ";";
            ResultSet resultSet = stmt.executeQuery(sqlQuery);
            if (resultSet.next()) {
                return true;
            }
            ;
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void bankManagerWindow(Hashtable<String, ImageIcon> iconTable) {
        FourButtonGUI bankManagerDashboard = new FourButtonGUI();

        bankManagerDashboard.topLeftButton.setIcon(iconTable.get("ChangeAccountType"));
        bankManagerDashboard.topLeftButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeAccountTypeWindow(iconTable);
            }
        });

        bankManagerDashboard.topRightButton.setIcon(iconTable.get("UnblockAccount"));
        bankManagerDashboard.topRightButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                unblockAccountWindow(iconTable);
            }
        });

        bankManagerDashboard.bottomLeftButton.setIcon(iconTable.get("ViewAccounts"));
        bankManagerDashboard.bottomLeftButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewAccountsOptionsWindow(iconTable);
            }
        });

        bankManagerDashboard.bottomRightButton.setIcon(iconTable.get("ViewTransactions"));
        bankManagerDashboard.bottomRightButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewTransactionsWindow(iconTable); // display gui not put in yet.
            }
        });

        bankManagerDashboard.exitButton.setIcon(iconTable.get("Logout"));

        bankManagerDashboard.setVisible(true);

    }

    public void changeAccountTypeWindow(Hashtable<String, ImageIcon> iconTable) {
        TwoFieldBaseGUI changeAccountTypeScreen = new TwoFieldBaseGUI();
        changeAccountTypeScreen.topIcon.setIcon(iconTable.get("UsernameAccountNumber"));
        changeAccountTypeScreen.bottomIcon.setIcon(iconTable.get("AuthenticateWithPassword"));
        changeAccountTypeScreen.submit.setIcon(iconTable.get("Submit"));
        changeAccountTypeScreen.exitButton.setIcon(iconTable.get("Logout"));

        changeAccountTypeScreen.submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeAccountTypeScreen.topError.setIcon(null);
                changeAccountTypeScreen.bottomError.setIcon(null);
                String accountUsername = "";
                String sqlQuery;
                boolean foundAccount = false;
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "admin");
                        Statement stmt = conn.createStatement();) {
                    sqlQuery = "use jbank;";
                    stmt.executeUpdate(sqlQuery);
                    try {
                        int accountNumber = Integer
                                .parseInt(changeAccountTypeScreen.topInput.getText().trim());
                        if (AccountHolder.checkIfAccountExists(accountNumber)) {
                            sqlQuery = "select username from accountholder where accountnumber = "
                                    + accountNumber + ";";
                            ResultSet resultSet = stmt.executeQuery(sqlQuery);
                            resultSet.next();
                            accountUsername = resultSet.getString(1);
                            foundAccount = true;
                        }
                    } catch (Exception exception) {
                    }
                    if (!foundAccount && checkIfAccountExists(changeAccountTypeScreen.topInput.getText())) {
                        sqlQuery = "select username from accountholder where username = '"
                                + changeAccountTypeScreen.topInput.getText().trim() + "';";
                        ResultSet resultSet = stmt.executeQuery(sqlQuery);
                        resultSet.next();
                        accountUsername = resultSet.getString(1);
                        foundAccount = true;
                    }
                    if (!foundAccount) {
                        changeAccountTypeScreen.topError.setIcon(iconTable.get("UserDoesntExist"));
                        return;
                    }

                    if (!passwordHash(changeAccountTypeScreen.bottomInput.getText().trim()).equals(password)) {
                        changeAccountTypeScreen.bottomError.setIcon(iconTable.get("IncorrectPassword"));
                        return;
                    } else {
                        sqlQuery = "update accountholder set accounttype = 'Indian' where username = '"
                                + accountUsername + "';";
                        stmt.executeUpdate(sqlQuery);
                        changeAccountTypeScreen.submit.setIcon(iconTable.get("Success"));
                        javax.swing.Timer timer = new Timer(2000, callBack -> {
                            changeAccountTypeScreen.dispose();
                        });
                        timer.setRepeats(false);
                        timer.start();
                    }

                } catch (Exception exception) {
                    changeAccountTypeScreen.topError.setIcon(iconTable.get("UserDoesntExist"));
                }
            }
        });

        changeAccountTypeScreen.setVisible(true);

    }

    public void unblockAccountWindow(Hashtable<String, ImageIcon> iconTable) {

        TwoFieldBaseGUI unblockAccountScreen = new TwoFieldBaseGUI();
        unblockAccountScreen.topIcon.setIcon(iconTable.get("UsernameAccountNumber"));
        unblockAccountScreen.bottomIcon.setIcon(iconTable.get("AuthenticateWithPassword"));
        unblockAccountScreen.submit.setIcon(iconTable.get("Submit"));
        unblockAccountScreen.exitButton.setIcon(iconTable.get("Logout"));

        unblockAccountScreen.submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                unblockAccountScreen.topError.setIcon(null);
                unblockAccountScreen.bottomError.setIcon(null);
                String accountUsername = "";
                String sqlQuery;
                boolean foundAccount = false;
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "admin");
                        Statement stmt = conn.createStatement();) {
                    sqlQuery = "use jbank;";
                    stmt.executeUpdate(sqlQuery);
                    try {
                        int accountNumber = Integer
                                .parseInt(unblockAccountScreen.topInput.getText().trim());
                        if (AccountHolder.checkIfAccountExists(accountNumber)) {
                            sqlQuery = "select username from accountholder where accountnumber = "
                                    + accountNumber + ";";
                            ResultSet resultSet = stmt.executeQuery(sqlQuery);
                            resultSet.next();
                            accountUsername = resultSet.getString(1);
                            foundAccount = true;
                        }
                    } catch (Exception exception) {
                    }
                    if (!foundAccount && checkIfAccountExists(unblockAccountScreen.topInput.getText())) {
                        sqlQuery = "select username from accountholder where username = '"
                                + unblockAccountScreen.topInput.getText().trim() + "';";
                        ResultSet resultSet = stmt.executeQuery(sqlQuery);
                        resultSet.next();
                        accountUsername = resultSet.getString(1);
                        foundAccount = true;
                    }
                    if (!foundAccount) {
                        unblockAccountScreen.topError.setIcon(iconTable.get("UserDoesntExist"));
                        return;
                    }

                    if (!passwordHash(unblockAccountScreen.bottomInput.getText().trim()).equals(password)) {
                        unblockAccountScreen.bottomError.setIcon(iconTable.get("IncorrectPassword"));
                        return;
                    } else {
                        sqlQuery = "update accountholder set passwordattempts = 0 where username = '" + accountUsername
                                + "';";
                        stmt.executeUpdate(sqlQuery);
                        unblockAccountScreen.submit.setIcon(iconTable.get("Success"));
                        javax.swing.Timer timer = new Timer(2000, callBack -> {
                            unblockAccountScreen.dispose();
                        });
                        timer.setRepeats(false);
                        timer.start();
                    }

                } catch (Exception exception) {
                    unblockAccountScreen.topError.setIcon(iconTable.get("UserDoesntExist"));
                }
            }
        });

        unblockAccountScreen.setVisible(true);

    }

    public void viewAccountsOptionsWindow(Hashtable<String, ImageIcon> iconTable) {
        FourButtonGUI accountTypesDashboard = new FourButtonGUI();
        accountTypesDashboard.topLeftButton.setIcon(iconTable.get("ViewAllAccounts"));
        accountTypesDashboard.topLeftButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String sqlQuery = "select username,accountnumber,balance from accountholder;";
                viewAccountsWindow(iconTable, sqlQuery);
            }
        });

        accountTypesDashboard.topRightButton.setIcon(iconTable.get("ViewBlockedAccounts"));
        accountTypesDashboard.topRightButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String sqlQuery = "select username,accountnumber,balance from accountholder where passwordattempts >= 5;";
                viewAccountsWindow(iconTable, sqlQuery);
            }
        });

        accountTypesDashboard.bottomLeftButton.setIcon(iconTable.get("ViewIndianAccounts"));
        accountTypesDashboard.bottomLeftButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String sqlQuery = "select username,accountnumber,balance from accountholder where accounttype = 'Indian';";
                viewAccountsWindow(iconTable, sqlQuery);
            }
        });

        accountTypesDashboard.bottomRightButton.setIcon(iconTable.get("ViewNRIAccounts"));
        accountTypesDashboard.bottomRightButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String sqlQuery = "select username,accountnumber,balance from accountholder where accounttype = 'NRI';";
                viewAccountsWindow(iconTable, sqlQuery);
            }
        });

        accountTypesDashboard.exitButton.setIcon(iconTable.get("Logout"));
        accountTypesDashboard.setVisible(true);

    }

    public void viewAccountsWindow(Hashtable<String, ImageIcon> iconTable, String viewQuery) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "admin");
                Statement stmt = conn.createStatement();) {
            String sqlQuery = "use jbank;";
            stmt.executeUpdate(sqlQuery);
            ResultSet rs = stmt.executeQuery(viewQuery);
            DisplayGUI printstatementScreen = new DisplayGUI(rs, "Username,Account Number,Balance".split(","));
            printstatementScreen.exitButton.setIcon(iconTable.get("Logout"));
            printstatementScreen.setVisible(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewTransactionsWindow(Hashtable<String, ImageIcon> iconTable) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "admin");
                Statement stmt = conn.createStatement();) {
            String sqlQuery = "use jbank;";
            stmt.executeUpdate(sqlQuery);
            sqlQuery = "select sender,receiver,amount from transaction order by transactionid desc;";
            ResultSet rs = stmt.executeQuery(sqlQuery);

            DisplayGUI printstatementScreen = new DisplayGUI(rs, "Sender,Receiver,Amount".split(","));
            printstatementScreen.exitButton.setIcon(iconTable.get("Logout"));
            printstatementScreen.setVisible(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
