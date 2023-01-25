import java.sql.*;
import java.text.DecimalFormat;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

class AccountHolder extends User {
    int accountNumber;
    Double balance;
    int passwordAttempts;
    Double minimumBalance;
    Double transactionFees;

    public AccountHolder(String username, String password, int accountNumber, Double balance, int passwordAttempts) {
        super(username, password);
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.passwordAttempts = passwordAttempts;
    }

    public static boolean checkIfAccountExists(int accountNumber) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "admin");
            Statement stmt = conn.createStatement();
            String sqlQuery = "use jbank;";
            stmt.executeUpdate(sqlQuery);
            sqlQuery = "select username from accountholder where accountnumber = " + String.valueOf(accountNumber)
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

    public Double calculateTransactionFees(Double fundToTransfer) {
        return fundToTransfer * this.transactionFees;
    }

    public boolean checkTransactionValidity(Double fundToTransfer) {
        return ((this.balance - fundToTransfer - calculateTransactionFees(fundToTransfer)) >= this.minimumBalance);
    }

    public void accountHolderWindow(Hashtable<String, ImageIcon> iconTable) {
        FourButtonGUI accountHolderDashboard = new FourButtonGUI();
        accountHolderDashboard.topLeftButton.setIcon(iconTable.get("TransferFunds"));
        accountHolderDashboard.topLeftButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                transferFundsWindow(iconTable);
            }
        });
        accountHolderDashboard.topRightButton.setIcon(iconTable.get("CheckBalance"));
        accountHolderDashboard.topRightButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                DecimalFormat df = new DecimalFormat("0.00");
                accountHolderDashboard.topRightButton.setIcon(null);
                accountHolderDashboard.topRightButton.setText(df.format(balance));
                accountHolderDashboard.topRightButton
                        .setBorder(BorderFactory.createLineBorder(Color.decode("#181C3C"), 4));
                accountHolderDashboard.topRightButton.setFont(new Font("Poppins Medium", Font.BOLD, 40));
                accountHolderDashboard.topRightButton.setBackground(Color.decode("#B3E4E8"));
                accountHolderDashboard.topRightButton.setHorizontalAlignment(JTextField.CENTER);
                accountHolderDashboard.topRightButton.setForeground(Color.decode("#30346c"));
                javax.swing.Timer timer = new Timer(5000, callBack -> {
                    accountHolderDashboard.topRightButton.setText("");
                    accountHolderDashboard.topRightButton.setIcon(iconTable.get("CheckBalance"));
                });
                timer.setRepeats(false);
                timer.start();
            }
        });

        accountHolderDashboard.bottomLeftButton.setIcon(iconTable.get("PrintStatement"));
        accountHolderDashboard.bottomLeftButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                printStatementWindow(iconTable);
            }
        });
        accountHolderDashboard.bottomRightButton.setIcon(iconTable.get("ChangePassword"));
        accountHolderDashboard.bottomRightButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changePasswordWindow(iconTable);
            }
        });
        accountHolderDashboard.exitButton.setIcon(iconTable.get("Logout"));
        accountHolderDashboard.setVisible(true);

    }

    public void transferFundsWindow(Hashtable<String, ImageIcon> iconTable) {
        FourFieldBaseGUI transferFundsScreen = new FourFieldBaseGUI();
        transferFundsScreen.topLeftIcon.setIcon(iconTable.get("BeneficiaryAccountNumber"));
        transferFundsScreen.topRightIcon.setIcon(iconTable.get("Amount"));
        transferFundsScreen.bottomLeftIcon.setIcon(iconTable.get("TransactionFees"));
        transferFundsScreen.bottomLeftInput.setEditable(false);
        transferFundsScreen.bottomRightIcon.setIcon(iconTable.get("TotalAmount"));
        transferFundsScreen.bottomRightInput.setEditable(false);
        transferFundsScreen.submit.setIcon(iconTable.get("Submit"));
        AccountHolder[] beneficiaryAccount = new AccountHolder[1];

        transferFundsScreen.submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                transferFundsScreen.topLeftError.setIcon(null);
                transferFundsScreen.topRightError.setIcon(null);
                transferFundsScreen.bottomLeftError.setIcon(null);
                transferFundsScreen.bottomRightError.setIcon(null);

                String beneficiaryUserName = "";
                String sqlQuery;
                boolean foundAccount = false;
                DecimalFormat df = new DecimalFormat("0.00");

                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "admin");
                        Statement stmt = conn.createStatement();) {
                    sqlQuery = "use jbank;";
                    stmt.executeUpdate(sqlQuery);
                    if (transferFundsScreen.submit.getIcon().equals((Icon) iconTable.get("Submit"))) {
                        try {
                            int beneficiaryAccountNumber = Integer
                                    .parseInt(transferFundsScreen.topLeftInput.getText().trim());
                            if (checkIfAccountExists(beneficiaryAccountNumber)) {
                                sqlQuery = "select username from accountholder where accountnumber = "
                                        + beneficiaryAccountNumber + ";";
                                ResultSet resultSet = stmt.executeQuery(sqlQuery);
                                resultSet.next();
                                beneficiaryUserName = resultSet.getString(1);
                                foundAccount = true;
                            }
                        } catch (Exception exception) {
                        }
                        if (!foundAccount && checkIfAccountExists(transferFundsScreen.topLeftInput.getText())) {
                            sqlQuery = "select username from accountholder where username = '"
                                    + transferFundsScreen.topLeftInput.getText() + "';";
                            ResultSet resultSet = stmt.executeQuery(sqlQuery);
                            resultSet.next();
                            beneficiaryUserName = resultSet.getString(1);
                            foundAccount = true;
                        }
                        if (!foundAccount) {
                            transferFundsScreen.topLeftError.setIcon(iconTable.get("UserDoesntExist"));
                            return;
                        }

                        sqlQuery = "select u.username , u.password , a.accountnumber , a.balance , a.passwordattempts , a.accounttype from user as u, accountholder as a where u.username = a.username and u.username = '"
                                + beneficiaryUserName + "';";
                        ResultSet resultSet = stmt.executeQuery(sqlQuery);
                        resultSet.next();
                        if (resultSet.getString(6).equals("NRI")) {
                            beneficiaryAccount[0] = new NRIAccount(resultSet.getString(1), resultSet.getString(2),
                                    resultSet.getInt(3), resultSet.getDouble(4), resultSet.getInt(5));
                        } else {
                            beneficiaryAccount[0] = new IndianAccount(resultSet.getString(1), resultSet.getString(2),
                                    resultSet.getInt(3), resultSet.getDouble(4), resultSet.getInt(5));
                        }

                        try {
                            Double amount = Double.parseDouble(transferFundsScreen.topRightInput.getText().trim());
                            transferFundsScreen.bottomLeftInput.setText(df.format(calculateTransactionFees(amount)));
                            transferFundsScreen.bottomRightInput
                                    .setText(df.format(amount + calculateTransactionFees(amount)));
                            transferFundsScreen.topRightInput.setText(df.format(amount));
                            if (!checkTransactionValidity(amount)) {
                                transferFundsScreen.bottomRightError.setIcon(iconTable.get("InsufficientBalance"));
                                return;
                            } else {
                                transferFundsScreen.topLeftInput.setEditable(false);
                                transferFundsScreen.topRightInput.setEditable(false);
                                transferFundsScreen.submit.setIcon(iconTable.get("Confirm"));
                            }
                        } catch (Exception exception) {
                            transferFundsScreen.topRightError.setIcon(iconTable.get("EnterADecimalValue"));
                        }
                    } else if (transferFundsScreen.submit.getIcon().equals((Icon) iconTable.get("Confirm"))) {
                        Double amount = Double.parseDouble(transferFundsScreen.topRightInput.getText());
                        sqlQuery = "update accountholder set balance = balance - "
                                + df.format(amount + calculateTransactionFees(amount)) + " where username = '"
                                + username + "';";
                        stmt.executeUpdate(sqlQuery);
                        sqlQuery = "update accountholder set balance = balance + " + df.format(amount)
                                + " where username = '" + beneficiaryAccount[0].username + "';";
                        stmt.executeUpdate(sqlQuery);
                        sqlQuery = "insert into transaction (sender,receiver,amount) values ('" + username + "','"
                                + beneficiaryAccount[0].username + "'," + df.format(amount) + ");";
                        stmt.executeUpdate(sqlQuery);
                        transferFundsScreen.submit.setIcon(iconTable.get("Success"));
                        javax.swing.Timer timer = new Timer(2000, callBack -> {
                            transferFundsScreen.dispose();
                        });
                        timer.setRepeats(false);
                        timer.start();
                    }

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        transferFundsScreen.exitButton.setIcon(iconTable.get("Logout"));
        transferFundsScreen.setVisible(true);

    }

    public void printStatementWindow(Hashtable<String, ImageIcon> iconTable) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "admin");
                Statement stmt = conn.createStatement();) {
            String sqlQuery = "use jbank;";
            stmt.executeUpdate(sqlQuery);
            sqlQuery = "select username,amount from (select transactionid ,sender as username,amount from transaction where receiver = '"
                    + username
                    + "' union select transactionid, receiver as username, amount*-1 as amount from transaction where sender = '"
                    + username + "' order by transactionid desc) as t;";

            ResultSet rs = stmt.executeQuery(sqlQuery);

            DisplayGUI printstatementScreen = new DisplayGUI(rs, "Username,Amount".split(","));
            printstatementScreen.exitButton.setIcon(iconTable.get("Logout"));
            printstatementScreen.setVisible(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void changePasswordWindow(Hashtable<String, ImageIcon> iconTable) {
        FourFieldBaseGUI changePasswordScreen = new FourFieldBaseGUI();
        changePasswordScreen.topLeftIcon.setIcon(iconTable.get("UsernameAccountNumber"));
        changePasswordScreen.topRightIcon.setIcon(iconTable.get("NewPassword"));
        changePasswordScreen.bottomLeftIcon.setIcon(iconTable.get("OldPassword"));
        changePasswordScreen.bottomRightIcon.setIcon(iconTable.get("ConfirmPassword"));
        changePasswordScreen.submit.setIcon(iconTable.get("Submit"));
        changePasswordScreen.exitButton.setIcon(iconTable.get("Logout"));

        changePasswordScreen.submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changePasswordScreen.topLeftError.setIcon(null);
                changePasswordScreen.bottomLeftError.setIcon(null);
                changePasswordScreen.topRightError.setIcon(null);
                changePasswordScreen.bottomLeftError.setIcon(null);

                String sqlQuery;
                boolean foundAccount = false;
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "admin");
                        Statement stmt = conn.createStatement();) {
                    sqlQuery = "use jbank;";
                    stmt.executeUpdate(sqlQuery);
                    try {
                        int enteredNumber = Integer
                                .parseInt(changePasswordScreen.topLeftInput.getText().trim());
                        if (enteredNumber == accountNumber) {
                            foundAccount = true;
                        }
                    } catch (Exception exception) {
                        if (changePasswordScreen.topLeftInput.getText().trim().equalsIgnoreCase(username)) {
                            foundAccount = true;
                        }
                    }
                    if (!foundAccount) {
                        changePasswordScreen.topLeftError.setIcon(iconTable.get("IncorrectUsername"));
                        return;
                    }

                    if (!passwordHash(changePasswordScreen.bottomLeftInput.getText().trim()).equals(password)) {
                        changePasswordScreen.bottomLeftError.setIcon(iconTable.get("IncorrectPassword"));
                        return;
                    }

                    String enteredPassword = changePasswordScreen.topRightInput.getText().trim();
                    String enteredConfirmPassword = changePasswordScreen.bottomRightInput.getText().trim();

                    if (passwordHash(enteredPassword).equals(password)) {
                        changePasswordScreen.topRightError.setIcon(iconTable.get("SameAsCurrentPassword"));
                        return;
                    }

                    if (!checkPasswordStrength(enteredPassword)) {
                        changePasswordScreen.topRightError.setIcon(iconTable.get("WeakPassword"));
                        return;
                    }

                    if (!enteredPassword.equals(enteredConfirmPassword)) {
                        changePasswordScreen.bottomRightError.setIcon(iconTable.get("PasswordsDoNotMatch"));
                        return;
                    }

                    sqlQuery = "update user set password = '" + passwordHash(enteredConfirmPassword)
                            + "' where username = '" + username + "';";
                    stmt.executeUpdate(sqlQuery);
                    changePasswordScreen.submit.setIcon(iconTable.get("Success"));
                    javax.swing.Timer timer = new Timer(2000, callBack -> {
                        changePasswordScreen.dispose();
                    });
                    timer.setRepeats(false);
                    timer.start();

                } catch (Exception exception) {
                    changePasswordScreen.topLeftError.setIcon(iconTable.get("IncorrectUsername"));
                }
            }
        });
        changePasswordScreen.setVisible(true);
    }
}

class NRIAccount extends AccountHolder {
    public NRIAccount(String username, String password, int accountNumber, Double balance, int passwordAttempts) {
        super(username, password, accountNumber, balance, passwordAttempts);
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "admin");
                Statement stmt = conn.createStatement();) {
            String sqlQuery = "use jbank;";
            stmt.executeUpdate(sqlQuery);
            sqlQuery = "select minimumbalance , transactionfees from account where accounttype = 'NRI'";
            ResultSet resultSet = stmt.executeQuery(sqlQuery);
            resultSet.next();
            this.minimumBalance = resultSet.getDouble(1);
            this.transactionFees = resultSet.getDouble(2);
        } catch (Exception exception) {
        }
    }
}

class IndianAccount extends AccountHolder {
    public IndianAccount(String username, String password, int accountNumber, Double balance, int passwordAttempts) {
        super(username, password, accountNumber, balance, passwordAttempts);
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "admin");
                Statement stmt = conn.createStatement();) {
            String sqlQuery = "use jbank;";
            stmt.executeUpdate(sqlQuery);
            sqlQuery = "select minimumbalance , transactionfees from account where accounttype = 'Indian'";
            ResultSet resultSet = stmt.executeQuery(sqlQuery);
            resultSet.next();
            this.minimumBalance = resultSet.getDouble(1);
            this.transactionFees = resultSet.getDouble(2);

        } catch (Exception exception) {
        }
    }
}
