import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TwoFieldGUI extends TextFieldGUI {
    JLabel topIcon;
    JTextField topInput;
    JLabel topError;

    JLabel bottomIcon;
    JTextField bottomInput;
    JLabel bottomError;

    JButton submit;
    JButton exitButton;

    public int addComponents(int verticalPosition) {

        this.topIcon = new JLabel(new ImageIcon("./src/icons/label.png"));
        this.topIcon.setBounds((this.screenWidth - this.textFieldWidth) / 2, verticalPosition, this.textFieldWidth,
                this.textFieldHeight);
        verticalPosition += this.textFieldHeight;
        this.topInput = new JTextField();
        this.topInput.setBounds((this.screenWidth - this.textFieldWidth) / 2, verticalPosition, this.textFieldWidth,
                this.textFieldHeight);
        verticalPosition += this.textFieldHeight;
        this.topError = new JLabel(new ImageIcon("./src/icons/error.png"));
        this.topError.setBounds((this.screenWidth - this.textFieldWidth) / 2, verticalPosition, this.textFieldWidth,
                this.errorMessageHeight);
        verticalPosition += this.errorMessageHeight;

        this.bottomIcon = new JLabel(new ImageIcon("./src/icons/label.png"));
        this.bottomIcon.setBounds((this.screenWidth - this.textFieldWidth) / 2, verticalPosition, this.textFieldWidth,
                this.textFieldHeight);
        verticalPosition += this.textFieldHeight;
        this.bottomInput = new JTextField();
        this.bottomInput.setBounds((this.screenWidth - this.textFieldWidth) / 2, verticalPosition, this.textFieldWidth,
                this.textFieldHeight);
        verticalPosition += this.textFieldHeight;
        this.bottomError = new JLabel(new ImageIcon("./src/icons/error.png"));
        this.bottomError.setBounds((this.screenWidth - this.textFieldWidth) / 2, verticalPosition, this.textFieldWidth,
                this.errorMessageHeight);
        verticalPosition += this.errorMessageHeight;

        this.submit = new JButton(new ImageIcon("./src/icons/header.png"));
        this.submit.setBounds((this.screenWidth - this.loginButtonWidth) / 2,
                verticalPosition + this.largePaddingHeight, this.loginButtonWidth, this.textFieldHeight);
        verticalPosition += this.textFieldHeight + 2 * this.largePaddingHeight;

        this.exitButton = new JButton();
        this.exitButton.setBounds(this.screenWidth - this.headerHeight, 0, this.headerHeight, this.headerHeight);

        this.exitButton.setBorderPainted(false);
        this.submit.setBorderPainted(false);

        this.topInput.setFont(new Font("Arial", Font.PLAIN, this.fontSize));
        this.bottomInput.setFont(new Font("Arial", Font.PLAIN, this.fontSize));

        this.topInput.setHorizontalAlignment(JTextField.CENTER);
        this.bottomInput.setHorizontalAlignment(JTextField.CENTER);

        this.background.add(this.topIcon);
        this.background.add(this.topInput);
        this.background.add(this.topError);
        this.background.add(this.bottomIcon);
        this.background.add(this.bottomInput);
        this.background.add(this.bottomError);
        this.background.add(this.submit);

        this.header.add(this.exitButton);
        this.exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        return verticalPosition;
    }
}
