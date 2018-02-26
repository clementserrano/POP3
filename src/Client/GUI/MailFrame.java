package Client.GUI;

import Helpers.Constants;

import javax.swing.*;
import java.awt.*;

public class MailFrame extends JFrame {

    public MailFrame(){
        this.setTitle(Constants.mainFrameName);
        this.setSize(Constants.frameDimension);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        this.setContentPane(new Panel());

        this.setVisible(true);
    }

}
