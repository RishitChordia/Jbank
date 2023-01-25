import javax.swing.*;

public class TwoFieldExtendedGUI extends TwoFieldGUI {
    JButton extraButtonTop;
    JButton extraButtonBottom;

    final int smallPaddingHeight = 6;

    public TwoFieldExtendedGUI() {
        int verticalPosition = (this.screenHeight - this.headerHeight - 7 * this.textFieldHeight
                - 2 * this.errorMessageHeight - 2 * this.largePaddingHeight - this.smallPaddingHeight) / 2;
        verticalPosition = this.addComponents(verticalPosition);
        this.extraButtonTop = new JButton(new ImageIcon("./src/icons/text.png"));
        this.extraButtonTop.setBounds((this.screenWidth - this.textFieldWidth) / 2, verticalPosition,
                this.textFieldWidth, this.textFieldHeight);
        verticalPosition += this.textFieldHeight + this.smallPaddingHeight;
        this.extraButtonBottom = new JButton(new ImageIcon("./src/icons/text.png"));
        this.extraButtonBottom.setBounds((this.screenWidth - this.textFieldWidth) / 2, verticalPosition,
                this.textFieldWidth, this.textFieldHeight);
        verticalPosition += this.textFieldHeight;

        this.extraButtonTop.setBorderPainted(false);
        this.extraButtonBottom.setBorderPainted(false);

        this.background.add(this.extraButtonTop);
        this.background.add(this.extraButtonBottom);

    }

}
