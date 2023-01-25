public class TwoFieldBaseGUI extends TwoFieldGUI {

    public TwoFieldBaseGUI() {
        super();
        int verticalPosition = (this.screenHeight - this.headerHeight - 5 * this.textFieldHeight
                - 2 * this.errorMessageHeight - 2 * this.largePaddingHeight) / 2;
        this.addComponents(verticalPosition);
    }

}
