package Client.GUI.Test;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class FrameController {

    @FXML
    private TextField hostAdress;
    @FXML
    private TextField port;
    @FXML
    private TextField userName;
    @FXML
    private TextField password;
    @FXML
    private Button connect;
    @FXML
    private Button login;
    @FXML
    private ListView mailList;
    @FXML
    private TextArea console;
    @FXML
    private TextArea mailContent;

    @FXML
    public void connect(){
        log("Trying to connect to " + hostAdress.getText() + " on port " + port.getText());
    }

    @FXML
    public void login(){
        log("Loging in your Mail Box");
    }

    private void log(String string){
        console.appendText(string);
        console.appendText("\n");
    }

}
