import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FourFieldGUI extends TextFieldGUI {
    JLabel topLeftIcon;
    JTextField topLeftInput;
    JLabel topLeftError;

    JLabel topRightIcon;
    JTextField topRightInput;
    JLabel topRightError;

    JLabel bottomLeftIcon;
    JTextField bottomLeftInput;
    JLabel bottomLeftError;

    JLabel bottomRightIcon;
    JTextField bottomRightInput;
    JLabel bottomRightError;

    JButton submit;

    JButton exitButton;

    final int gapWidth = 40;

    public int addComponents(int verticalPosition) {

        int spaceOnLeft = (this.screenWidth - 2 * this.textFieldWidth - this.gapWidth) / 2;

        this.topLeftIcon = new JLabel(new ImageIcon("./src/icons/label.png"));
        this.topLeftIcon.setBounds(spaceOnLeft, verticalPosition, this.textFieldWidth, this.textFieldHeight);
        this.topRightIcon = new JLabel(new ImageIcon("./src/icons/label.png"));
        this.topRightIcon.setBounds(this.screenWidth - spaceOnLeft - this.textFieldWidth, verticalPosition,
                this.textFieldWidth, this.textFieldHeight);
        verticalPosition += this.textFieldHeight;

        this.topLeftInput = new JTextField();
        this.topLeftInput.setBounds(spaceOnLeft, verticalPosition, this.textFieldWidth, this.textFieldHeight);
        this.topRightInput = new JTextField();
        this.topRightInput.setBounds(this.screenWidth - spaceOnLeft - this.textFieldWidth, verticalPosition,
                this.textFieldWidth, this.textFieldHeight);
        verticalPosition += this.textFieldHeight;

        this.topLeftError = new JLabel(new ImageIcon("./src/icons/error.png"));
        this.topLeftError.setBounds(spaceOnLeft, verticalPosition, this.textFieldWidth, this.errorMessageHeight);
        this.topRightError = new JLabel(new ImageIcon("./src/icons/error.png"));
        this.topRightError.setBounds(this.screenWidth - spaceOnLeft - this.textFieldWidth, verticalPosition,
                this.textFieldWidth, this.errorMessageHeight);
        verticalPosition += this.errorMessageHeight;

        verticalPosition += this.largePaddingHeight;

        this.bottomLeftIcon = new JLabel(new ImageIcon("./src/icons/label.png"));
        this.bottomLeftIcon.setBounds(spaceOnLeft, verticalPosition, this.textFieldWidth, this.textFieldHeight);
        this.bottomRightIcon = new JLabel(new ImageIcon("./src/icons/label.png"));
        this.bottomRightIcon.setBounds(this.screenWidth - spaceOnLeft - this.textFieldWidth, verticalPosition,
                this.textFieldWidth, this.textFieldHeight);
        verticalPosition += this.textFieldHeight;

        this.bottomLeftInput = new JTextField();
        this.bottomLeftInput.setBounds(spaceOnLeft, verticalPosition, this.textFieldWidth, this.textFieldHeight);
        this.bottomRightInput = new JTextField();
        this.bottomRightInput.setBounds(this.screenWidth - spaceOnLeft - this.textFieldWidth, verticalPosition,
                this.textFieldWidth, this.textFieldHeight);
        verticalPosition += this.textFieldHeight;

        this.bottomLeftError = new JLabel(new ImageIcon("./src/icons/error.png"));
        this.bottomLeftError.setBounds(spaceOnLeft, verticalPosition, this.textFieldWidth, this.errorMessageHeight);
        this.bottomRightError = new JLabel(new ImageIcon("./src/icons/error.png"));
        this.bottomRightError.setBounds(this.screenWidth - spaceOnLeft - this.textFieldWidth, verticalPosition,
                this.textFieldWidth, this.errorMessageHeight);
        verticalPosition += this.errorMessageHeight;

        this.submit = new JButton(new ImageIcon("./src/icons/header.png"));
        this.submit.setBounds((this.screenWidth - this.loginButtonWidth) / 2,
                verticalPosition + this.largePaddingHeight, this.loginButtonWidth, this.textFieldHeight);
        verticalPosition += this.textFieldHeight + 2 * this.largePaddingHeight;

        this.exitButton = new JButton();
        this.exitButton.setBounds(this.screenWidth - this.headerHeight, 0, this.headerHeight, this.headerHeight);

        this.submit.setBorderPainted(false);
        this.exitButton.setBorderPainted(false);

        this.topLeftInput.setFont(new Font("Arial", Font.PLAIN, this.fontSize));
        this.topRightInput.setFont(new Font("Arial", Font.PLAIN, this.fontSize));
        this.bottomLeftInput.setFont(new Font("Arial", Font.PLAIN, this.fontSize));
        this.bottomRightInput.setFont(new Font("Arial", Font.PLAIN, this.fontSize));

        this.topLeftInput.setHorizontalAlignment(JTextField.CENTER);
        this.topRightInput.setHorizontalAlignment(JTextField.CENTER);
        this.bottomLeftInput.setHorizontalAlignment(JTextField.CENTER);
        this.bottomRightInput.setHorizontalAlignment(JTextField.CENTER);

        this.background.add(this.topLeftIcon);
        this.background.add(this.topLeftInput);
        this.background.add(this.topLeftError);
        this.background.add(this.topRightIcon);
        this.background.add(this.topRightInput);
        this.background.add(this.topRightError);
        this.background.add(this.bottomLeftIcon);
        this.background.add(this.bottomLeftInput);
        this.background.add(this.bottomLeftError);
        this.background.add(this.bottomRightIcon);
        this.background.add(this.bottomRightInput);
        this.background.add(this.bottomRightError);
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
