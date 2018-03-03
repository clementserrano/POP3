package Client.GUI.Controllers;

import Helpers.States;
import javafx.fxml.FXML;

import java.awt.*;
import java.awt.event.ActionEvent;

public class ConnexionController {

    @FXML
    private TextField hostAdressTextField;
    @FXML
    private TextField portTextField;
    @FXML
    private TextField userNameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button connexion;

    @FXML
    private void connect(ActionEvent event){
        System.out.println("Trying to connect");
    }
}
