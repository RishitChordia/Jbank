import java.sql.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.io.*;
import java.security.MessageDigest;

public class User {
    String username;
    String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static void accountHolderLoginWindow(Hashtable<String, ImageIcon> iconTable) {
        TwoFieldExtendedGUI loginScreen = new TwoFieldExtendedGUI();
        loginScreen.topIcon.setIcon(iconTable.get("UsernameAccountNumber"));
        loginScreen.bottomIcon.setIcon(iconTable.get("Password"));
        loginScreen.submit.setIcon(iconTable.get("Login"));
        loginScreen.extraButtonTop.setIcon(iconTable.get("CreateNewIndianAccount"));
        loginScreen.extraButtonBottom.setIcon(iconTable.get("CreateNewNRIAccount"));

        loginScreen.extraButtonTop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newAccountWindow("Indian", iconTable);
                javax.swing.Timer timer = new Timer(1000, callBack -> {
                    loginScreen.dispose();
                });
                timer.setRepeats(false);
                timer.start();
            }
        });
        loginScreen.extraButtonBottom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newAccountWindow("NRI", iconTable);
                javax.swing.Timer timer = new Timer(1000, callBack -> {
                    loginScreen.dispose();
                });
                timer.setRepeats(false);
                timer.start();
            }
        });
        loginScreen.submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginScreen.topError.setIcon(null);
                loginScreen.bottomError.setIcon(null);
                String username = "";
                String sqlQuery;
                boolean foundAccount = false;
                AccountHolder newAccount;
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "admin");
                        Statement stmt = conn.createStatement();) {
                    sqlQuery = "use jbank;";
                    stmt.executeUpdate(sqlQuery);
                    try {
                        int accountNumber = Integer
                                .parseInt(loginScreen.topInput.getText().trim());
                        if (AccountHolder.checkIfAccountExists(accountNumber)) {
                            sqlQuery = "select username from accountholder where accountnumber = "
                                    + accountNumber + ";";
                            ResultSet resultSet = stmt.executeQuery(sqlQuery);
                            resultSet.next();
                            username = resultSet.getString(1);
                            foundAccount = true;
                        }
                    } catch (Exception exception) {
                    }
                    if (!foundAccount && checkIfAccountExists(loginScreen.topInput.getText())) {
                        sqlQuery = "select username from accountholder where username = '"
                                + loginScreen.topInput.getText().trim() + "';";
                        ResultSet resultSet = stmt.executeQuery(sqlQuery);
                        resultSet.next();
                        username = resultSet.getString(1);
                        foundAccount = true;
                    }
                    if (!foundAccount) {
                        loginScreen.topError.setIcon(iconTable.get("UserDoesntExist"));
                        return;
                    }

                    sqlQuery = "select u.username , u.password , a.accountnumber , a.balance , a.passwordattempts , a.accounttype from user as u, accountholder as a where u.username = a.username and u.username = '"
                            + username + "';";
                    ResultSet resultSet = stmt.executeQuery(sqlQuery);
                    resultSet.next();
                    if (resultSet.getString(6).equals("NRI")) {
                        newAccount = new NRIAccount(resultSet.getString(1), resultSet.getString(2),
                                resultSet.getInt(3), resultSet.getDouble(4), resultSet.getInt(5));
                    } else {
                        newAccount = new IndianAccount(resultSet.getString(1), resultSet.getString(2),
                                resultSet.getInt(3), resultSet.getDouble(4), resultSet.getInt(5));
                    }
                    if (newAccount.passwordAttempts >= 5) {
                        loginScreen.topError.setIcon(iconTable.get("AccountBlockedContactManager"));
                        return;
                    }
                    if (!passwordHash(loginScreen.bottomInput.getText().trim()).equals(newAccount.password)) {
                        loginScreen.bottomError.setIcon(iconTable.get("IncorrectPassword"));
                        sqlQuery = "update accountholder set passwordattempts = passwordattempts+1 where username = '"
                                + username + "';";
                        stmt.executeUpdate(sqlQuery);
                        return;
                    } else {
                        newAccount.accountHolderWindow(iconTable);
                        sqlQuery = "update accountholder set passwordattempts = 0 where username = '" + username + "';";
                        stmt.executeUpdate(sqlQuery);
                    }
                    javax.swing.Timer timer = new Timer(1000, callBack -> {
                        loginScreen.dispose();
                    });
                    timer.setRepeats(false);
                    timer.start();
                } catch (Exception exception) {
                    loginScreen.topError.setIcon(iconTable.get("UserDoesntExist"));
                }
            }
        });
        loginScreen.exitButton.setIcon(iconTable.get("Logout"));

        loginScreen.setVisible(true);

    }

    public static void newAccountWindow(String accountType, Hashtable<String, ImageIcon> iconTable) {
        FourFieldBaseGUI newAccountScreen = new FourFieldBaseGUI();
        newAccountScreen.topLeftIcon.setIcon(iconTable.get("UsernameAccountNumber"));
        newAccountScreen.topRightIcon.setIcon(iconTable.get("Password"));
        newAccountScreen.bottomLeftIcon.setIcon(iconTable.get("StartingBalance"));
        newAccountScreen.bottomRightIcon.setIcon(iconTable.get("ConfirmPassword"));
        newAccountScreen.submit.setIcon(iconTable.get("Submit"));
        AccountHolder[] newAccount = new AccountHolder[1];

        newAccountScreen.submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                newAccountScreen.topLeftError.setIcon(null);
                newAccountScreen.topRightError.setIcon(null);
                newAccountScreen.bottomLeftError.setIcon(null);
                newAccountScreen.bottomRightError.setIcon(null);

                String enteredPassword = newAccountScreen.topRightInput.getText().trim();
                String enteredConfirmPassword = newAccountScreen.bottomRightInput.getText().trim();
                String enteredUsername = newAccountScreen.topLeftInput.getText().trim();
                String sqlQuery;

                Double startingBalance = 0.00;
                int newAccountNumber = 0;

                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "admin");
                        Statement stmt = conn.createStatement();) {
                    sqlQuery = "use jbank;";
                    stmt.executeUpdate(sqlQuery);
                    if (newAccountScreen.submit.getIcon().equals((Icon) iconTable.get("Submit"))) {
                        try {
                            Integer.parseInt(newAccountScreen.topLeftInput.getText().trim());

                            newAccountScreen.topLeftError.setIcon(iconTable.get("EnterAtleastOneLetter"));
                            return;
                        } catch (Exception exception) {
                        }

                        if (checkIfAccountExists(enteredUsername)) {
                            newAccountScreen.topLeftError.setIcon(iconTable.get("UsernameAlreadyExists"));
                            return;
                        }

                        if (!checkPasswordStrength(enteredPassword)) {
                            newAccountScreen.topRightError.setIcon(iconTable.get("WeakPassword"));
                            return;
                        }

                        if (!enteredPassword.equals(enteredConfirmPassword)) {
                            newAccountScreen.bottomRightError.setIcon(iconTable.get("PasswordsDoNotMatch"));
                            return;
                        }

                        try {
                            startingBalance = Double.parseDouble(newAccountScreen.bottomLeftInput.getText().trim());
                        } catch (NumberFormatException exception) {
                            newAccountScreen.bottomLeftError.setIcon(iconTable.get("EnterADecimalValue"));
                            return;
                        }
                        while (true) {
                            newAccountNumber = (int) (Math.random() * 1000000) + 10000000;
                            if (!AccountHolder.checkIfAccountExists(newAccountNumber)
                                    && !BankManager.checkIfAccountExists(newAccountNumber)) {
                                break;
                            }
                        }

                        if (accountType.equals("Indian")) {
                            newAccount[0] = new IndianAccount(enteredUsername, passwordHash(enteredPassword),
                                    newAccountNumber,
                                    startingBalance, 0);
                        } else {
                            newAccount[0] = new NRIAccount(enteredUsername, passwordHash(enteredPassword),
                                    newAccountNumber,
                                    startingBalance, 0);
                        }

                        if (newAccount[0].balance < newAccount[0].minimumBalance) {
                            newAccountScreen.bottomLeftError.setIcon(iconTable.get("InsufficientBalance"));
                            return;
                        }

                        sqlQuery = "insert into user values ('" + newAccount[0].username + "','"
                                + newAccount[0].password
                                + "','AccountHolder');";
                        stmt.executeUpdate(sqlQuery);

                        sqlQuery = "insert into accountholder values('" + newAccount[0].username + "',"
                                + newAccount[0].accountNumber + "," + newAccount[0].balance + ","
                                + newAccount[0].passwordAttempts + ",'" + accountType + "');";
                        stmt.executeUpdate(sqlQuery);

                        newAccountScreen.topLeftInput.setEditable(false);
                        newAccountScreen.topRightInput.setEditable(false);
                        newAccountScreen.bottomLeftInput.setEditable(false);
                        newAccountScreen.bottomRightInput.setEditable(false);
                        newAccountScreen.bottomRightIcon.setIcon(iconTable.get("YourAccountNumber"));
                        newAccountScreen.bottomRightInput.setText(String.valueOf(newAccountNumber));
                        newAccountScreen.submit.setIcon(iconTable.get("Dashboard"));

                    } else if (newAccountScreen.submit.getIcon().equals((Icon) iconTable.get("Dashboard"))) {
                        newAccount[0].accountHolderWindow(iconTable);
                        javax.swing.Timer timer = new Timer(1000, callBack -> {
                            newAccountScreen.dispose();
                        });
                        timer.setRepeats(false);
                        timer.start();
                    }

                } catch (Exception exception) {
                    exception.printStackTrace();
                }

            }
        });
        newAccountScreen.exitButton.setIcon(iconTable.get("Logout"));

        newAccountScreen.setVisible(true);

    }

    public static void bankManagerLoginWindow(Hashtable<String, ImageIcon> iconTable) {
        TwoFieldBaseGUI loginScreen = new TwoFieldBaseGUI();
        loginScreen.topIcon.setIcon(iconTable.get("UsernameManagerID"));
        loginScreen.bottomIcon.setIcon(iconTable.get("Password"));
        loginScreen.submit.setIcon(iconTable.get("Login"));
        loginScreen.submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginScreen.topError.setIcon(null);
                loginScreen.bottomError.setIcon(null);
                String username = "";
                String sqlQuery;
                boolean foundManager = false;
                BankManager newManager;
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "admin");
                        Statement stmt = conn.createStatement();) {
                    sqlQuery = "use jbank;";
                    stmt.executeUpdate(sqlQuery);
                    try {
                        int managerID = Integer
                                .parseInt(loginScreen.topInput.getText().trim());
                        if (BankManager.checkIfAccountExists(managerID)) {
                            sqlQuery = "select username from bankmanager where managerid = "
                                    + managerID + ";";
                            ResultSet resultSet = stmt.executeQuery(sqlQuery);
                            resultSet.next();
                            username = resultSet.getString(1);
                            foundManager = true;
                        }
                    } catch (Exception exception) {
                    }
                    if (!foundManager && checkIfAccountExists(loginScreen.topInput.getText())) {
                        sqlQuery = "select username from bankmanager where username = '"
                                + loginScreen.topInput.getText().trim() + "';";
                        ResultSet resultSet = stmt.executeQuery(sqlQuery);
                        resultSet.next();
                        username = resultSet.getString(1);
                        foundManager = true;
                    }
                    if (!foundManager) {
                        loginScreen.topError.setIcon(iconTable.get("UserDoesntExist"));
                        return;
                    }

                    sqlQuery = "select u.username , u.password , b.managerid from user as u, bankmanager as b where b.username = u.username and u.username = '"
                            + username + "';";
                    ResultSet resultSet = stmt.executeQuery(sqlQuery);
                    resultSet.next();

                    newManager = new BankManager(resultSet.getString(1), resultSet.getString(2),
                            resultSet.getInt(3));

                    if (!passwordHash(loginScreen.bottomInput.getText().trim()).equals(newManager.password)) {
                        loginScreen.bottomError.setIcon(iconTable.get("IncorrectPassword"));
                        return;
                    } else {
                        newManager.bankManagerWindow(iconTable);
                    }

                    javax.swing.Timer timer = new Timer(1000, callBack -> {
                        loginScreen.dispose();
                    });
                    timer.setRepeats(false);
                    timer.start();
                } catch (Exception exception) {
                    loginScreen.topError.setIcon(iconTable.get("UserDoesntExist"));
                }
            }
        });

        loginScreen.exitButton.setIcon(iconTable.get("Logout"));

        loginScreen.setVisible(true);

    }

    public static boolean checkIfAccountExists(String username) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "admin");
            Statement stmt = conn.createStatement();
            String sqlQuery = "use jbank;";
            stmt.executeUpdate(sqlQuery);
            sqlQuery = "select username from user where username = '" + username.trim() + "';";
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

    public static boolean checkPasswordStrength(String password) {
        boolean flagLowerCase = false; // for lowercase
        boolean flagUpperCase = false; // for uppercase
        boolean flagNumber = false; // for number

        for (int i = 0; i < password.length(); i++) {
            if (password.charAt(i) >= 97 && password.charAt(i) <= 122) {
                flagLowerCase = true;
            } else if (password.charAt(i) >= 65 && password.charAt(i) <= 90) {
                flagUpperCase = true;
            } else if (password.charAt(i) >= 48 && password.charAt(i) <= 57) {
                flagNumber = true;
            }
        }
        return flagLowerCase & flagUpperCase & flagNumber;
    }

    public static String passwordHash(String inp) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(inp.getBytes());
            String stringHash = new String(messageDigest.digest());
            String finalHash = "";
            for (int i = 0; i < stringHash.length() - 1; i++) {
                int choice = stringHash.charAt(i + 1) % 3;
                if (choice == 0) {
                    finalHash += (char) (stringHash.charAt(i) % 26 + 'A');
                }
                if (choice == 1) {
                    finalHash += (char) (stringHash.charAt(i) % 26 + 'a');
                } else {
                    finalHash += (char) (stringHash.charAt(i) % 10 + '0');
                }
            }
            return finalHash.substring(0, Math.min(stringHash.length() - 1, 30));
        } catch (Exception e) {
            return null;
        }
    }

    public static Hashtable<String, ImageIcon> createIconTable() {
        Hashtable<String, ImageIcon> iconTable = new Hashtable<>();
        for (String folderName : new File("./src/Assets").list()) {
            for (String fileName : new File("./src/Assets/" + folderName).list()) {
                if (folderName.equals("LargeButtons")) {
                    Image img = new ImageIcon("./src/Assets/LargeButtons/" + fileName).getImage();
                    iconTable.put(fileName.substring(0, fileName.length() - 4),
                            new ImageIcon(img.getScaledInstance(318, 210, java.awt.Image.SCALE_SMOOTH)));
                } else {
                    iconTable.put(fileName.substring(0, fileName.length() - 4),
                            new ImageIcon("./src/Assets/" + folderName + "/" + fileName));
                }
            }
        }
        return iconTable;
    }
}
