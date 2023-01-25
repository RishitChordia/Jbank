import javax.swing.*;
import java.awt.*;

public class BaseGUI extends JFrame {
    JLabel background;
    JLabel header;
    JLabel BPSLogo;

    final int headerHeight = 118;
    final int screenHeight;
    final int screenWidth;

    public BaseGUI() {
        Dimension desktopSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.screenHeight = (int) desktopSize.getHeight();
        this.screenWidth = (int) desktopSize.getWidth();
        this.setSize(this.screenWidth, this.screenHeight);
        this.setLayout(null);

        this.background = new JLabel(new ImageIcon("./src/Assets/Backgrounds/Background.png"));
        this.background.setBounds(0, this.headerHeight, this.screenWidth, this.screenHeight);
        this.background.setLayout(null);
        this.add(this.background);

        this.header = new JLabel(new ImageIcon("./src/Assets/Backgrounds/Header.png"));
        this.header.setBounds(0, 0, this.screenWidth, this.headerHeight);
        this.header.setLayout(null);
        this.add(this.header);

        this.BPSLogo = new JLabel(new ImageIcon("./src/Assets/SmallButtons/BPSLogo.png"));
        this.BPSLogo.setBounds(0, 0, this.headerHeight, this.headerHeight);
        this.header.add(this.BPSLogo);

        this.setUndecorated(true);
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

}
