
import java.awt.*;
import java.util.*;

public class Simulation{

    public static void main(String[] args) {
    	// Run titlescreen, which can call simulation using user input
        TitleScreen frame = new TitleScreen("AntFarm");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //frame.setBounds(300,300,900,900);
        frame.setBounds((int)(0.5 * screenSize.getWidth())-150, (int) (0.5 * screenSize.getHeight())-250, 900, 900);
        frame.pack();
        frame.setVisible(true);  

    }
}
