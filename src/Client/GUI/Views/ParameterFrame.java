package Client.GUI.Views;

import Helpers.Constants;

import javax.swing.*;
import java.awt.*;

public class ParameterFrame extends JFrame {

    public ParameterFrame(){
        this.setTitle(Constants.parameterFrameName);
        this.setSize(Constants.frameDimension);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        this.setContentPane(new Panel());

        this.setVisible(true);
    }
}
