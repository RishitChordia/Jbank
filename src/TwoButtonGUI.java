import javax.swing.*;

public class TwoButtonGUI extends LargeButtonGUI {
    JButton leftButton;
    JButton rightButton;

    public TwoButtonGUI() {
        super();
        int spaceOnLeft = (this.screenWidth - 2 * this.buttonWidth - this.gapWidth) / 2;
        int spaceOnTop = (this.screenHeight - this.headerHeight - this.buttonHeight) / 2;

        this.leftButton = new JButton();
        this.leftButton.setBounds(spaceOnLeft, spaceOnTop, this.buttonWidth, this.buttonHeight);
        this.rightButton = new JButton();
        this.rightButton.setBounds(this.screenWidth - spaceOnLeft - this.buttonWidth, spaceOnTop, this.buttonWidth,
                this.buttonHeight);

        this.leftButton.setBorderPainted(false);
        this.rightButton.setBorderPainted(false);

        this.background.add(this.leftButton);
        this.background.add(this.rightButton);

    }
}
