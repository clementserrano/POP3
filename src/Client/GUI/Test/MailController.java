package Client.GUI.Test;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MailController implements Initializable {

    @FXML
    private Button connectBtn, loginBtn, logoutBtn;
    @FXML
    private TextField hostAdress, port, userName, password;
    @FXML
    private ListView mailList;
    @FXML
    private TextArea console, mailContent;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void connect() {
        log("Trying to connect to " + hostAdress.getText() + " on port " + port.getText());
    }

    @FXML
    public void login(ActionEvent event) {
        // TODO : Check PWD & user
        log("Logging in your Mail Box");

        // Chargement de la fenêtre des mails si succès de l'authentification
        changeScreens(event);
    }

    @FXML
    public void logout(ActionEvent event) {
        changeScreens(event);
    }

    private void changeScreens(ActionEvent event) {
        try {
            Stage stage;
            AnchorPane root;
            if (event.getSource() == loginBtn) {

                stage = (Stage) loginBtn.getScene().getWindow();
                root = FXMLLoader.load(getClass().getResource("/Client/GUI/Test/mailScreen.fxml"));

            } else {
                stage = (Stage) logoutBtn.getScene().getWindow();
                root = FXMLLoader.load(getClass().getResource("/Client/GUI/Test/loginScreen.fxml"));
            }
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception ex) {
            Logger.getLogger(MailController.class.getName()).log(Level.SEVERE, "Problème de connexion", ex);
        }
    }

    private void log(String string) {
        console.appendText(string);
        console.appendText("\n");
    }
}
