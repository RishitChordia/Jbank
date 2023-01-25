import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;

public class DisplayGUI extends BaseGUI {
    JPanel displayPanel;
    JScrollPane scrollPane;
    JButton exitButton;

    public DisplayGUI(ResultSet queryResult, String[] columnHeaders) {
        super();
        this.displayPanel = new JPanel();
        this.scrollPane = new JScrollPane(displayPanel);

        this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.background.add(this.scrollPane);

        this.displayPanel.setBackground(Color.decode("#B3E4E8"));
        this.displayPanel.setForeground(Color.decode("#B3E4E8"));

        int columns = columnHeaders.length;
        int rows = 1;
        int labHeight = 0;
        for (String string : columnHeaders) {
            JLabel label = new JLabel();
            label.setBorder(BorderFactory.createLineBorder(Color.decode("#181C3C"), 4));
            label.setText(string);
            label.setFont(new Font("Poppins Medium", Font.BOLD, 40));
            label.setBackground(Color.decode("#B3E4E8"));
            label.setHorizontalAlignment(JTextField.CENTER);
            label.setForeground(Color.decode("#30346c"));
            labHeight = (int) label.getPreferredSize().getHeight();
            this.displayPanel.add(label);
        }
        try {
            while (queryResult.next()) {
                rows += 1;
                for (int i = 1; i <= columns; i++) {
                    JLabel label = new JLabel();
                    label.setBorder(BorderFactory.createLineBorder(Color.decode("#181C3C"), 4));
                    label.setText(queryResult.getString(i));
                    label.setFont(new Font("Poppins Medium", Font.PLAIN, 30));
                    label.setBackground(Color.decode("#B3E4E8"));
                    label.setForeground(Color.decode("#30346c"));
                    label.setHorizontalAlignment(JTextField.CENTER);
                    this.displayPanel.add(label);
                }
            }
            this.scrollPane.setBounds(0, 0, this.screenWidth,
                    Math.min(this.screenHeight - this.headerHeight, rows * (labHeight + 3) + 6));
            this.displayPanel.setLayout(new GridLayout(rows, columns, 3, 3));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.exitButton = new JButton();
        this.exitButton.setBounds(this.screenWidth - this.headerHeight, 0, this.headerHeight, this.headerHeight);
        this.exitButton.setBorderPainted(false);

        this.header.add(this.exitButton);
        this.exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}
