import javax.swing.*;
import java.awt.event.*;

public class FourButtonGUI extends LargeButtonGUI {
    JButton topLeftButton;
    JButton topRightButton;
    JButton bottomLeftButton;
    JButton bottomRightButton;

    JButton exitButton;

    public FourButtonGUI() {
        super(); // constructor from BaseGUI

        int spaceOnLeft = (this.screenWidth - 2 * this.buttonWidth - this.gapWidth) / 2;
        int spaceOnTop = (this.screenHeight - this.headerHeight - 2 * this.buttonHeight - this.gapHeight) / 2;

        this.topLeftButton = new JButton();
        this.topLeftButton.setBounds(spaceOnLeft, spaceOnTop, this.buttonWidth, this.buttonHeight);
        this.topRightButton = new JButton();
        this.topRightButton.setBounds(this.screenWidth - spaceOnLeft - this.buttonWidth, spaceOnTop, this.buttonWidth,
                this.buttonHeight);
        this.bottomLeftButton = new JButton();
        this.bottomLeftButton.setBounds(spaceOnLeft,
                this.screenHeight - this.headerHeight - spaceOnTop - this.buttonHeight, this.buttonWidth,
                this.buttonHeight);
        this.bottomRightButton = new JButton();
        this.bottomRightButton.setBounds(this.screenWidth - spaceOnLeft - this.buttonWidth,
                this.screenHeight - this.headerHeight - spaceOnTop - this.buttonHeight, this.buttonWidth,
                this.buttonHeight);

        this.exitButton = new JButton();
        this.exitButton.setBounds(this.screenWidth - this.headerHeight, 0, this.headerHeight, this.headerHeight);

        this.topLeftButton.setBorderPainted(false);
        this.topRightButton.setBorderPainted(false);
        this.bottomLeftButton.setBorderPainted(false);
        this.bottomRightButton.setBorderPainted(false);
        this.exitButton.setBorderPainted(false);

        this.background.add(this.topLeftButton);
        this.background.add(this.topRightButton);
        this.background.add(this.bottomLeftButton);
        this.background.add(this.bottomRightButton);

        this.header.add(this.exitButton);

        this.exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}
