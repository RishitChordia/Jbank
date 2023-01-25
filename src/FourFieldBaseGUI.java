public class FourFieldBaseGUI extends FourFieldGUI {

    public FourFieldBaseGUI() {
        super();
        int verticalPosition = (this.screenHeight - this.headerHeight - 5 * this.textFieldHeight
                - 2 * this.errorMessageHeight - 3 * this.largePaddingHeight) / 2;
        this.addComponents(verticalPosition);
    }

}
