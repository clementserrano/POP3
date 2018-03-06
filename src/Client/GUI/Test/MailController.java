package Client.GUI.Test;

import Client.ReceptionThread;
import Helpers.EventPOP3;
import Helpers.States;
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

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MailController extends Observable implements Initializable {

    private static Socket socket;
    private static InputStream inputStream;
    private static OutputStream outputStream;
    public int messageNumber;
    public Thread receptionThread;

    private States state = States.AUTHORIZATION;

    @FXML
    private Button connectBtn, loginBtn, statBtn, retrBtn, logoutBtn;
    @FXML
    private TextField hostAdress, port, userName, password, mailNb;
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
        setupTextAreas();
        if (socket != null && socket.isConnected()){
            log("Already connected");
            return;
        }
        try {
            log("Connecting");
            socket = new Socket(InetAddress.getByName(getHostAdress()), Integer.parseInt(getPort()));
            log("Connected");
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            //Platform.runLater(new ReceptionThread(this));
            receptionThread = new Thread(new ReceptionThread(this));
            receptionThread.setDaemon(true);
            receptionThread.start();

        } catch (Exception e) {
            log("Failed to connect");
            e.printStackTrace();
        }

    }

    @FXML
    public void login(ActionEvent event) {
        if (socket != null && socket.isConnected() && state.equals(States.AUTHORIZATION)) {
            try {
                log("Loging in your Mail Box");
                String passwordMD5 = new String(MessageDigest.getInstance("MD5").digest(getPassword().getBytes()), StandardCharsets.UTF_8);
                sendToServer(EventPOP3.APOP, getUserName(), passwordMD5);
                state = States.TRANSACTION;
                // Chargement de la fenêtre des mails si succès de l'authentification
                changeScreens(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void statMailInfo(){
        try{
            sendToServer(EventPOP3.STAT);
        }catch (Exception e ){
            e.printStackTrace();
        }
    }

    @FXML
    public void retrieveMail(){
        try {
            sendToServer(EventPOP3.RETR, mailNb.getText());
        }catch (Exception e ){
            e.printStackTrace();
        }
    }

    @FXML
    public void logout(ActionEvent event) {
        try {
            if (socket != null && socket.isConnected()) {
                log("Disconnecting");
                sendToServer(EventPOP3.QUIT);
                socket.close();
                state = States.AUTHORIZATION;
                changeScreens(event);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
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

    private void setupTextAreas(){
        console.setWrapText(true);
    }
    public synchronized void log(String string){
        getConsole().appendText(string);
        getConsole().appendText("\n");
    }

    public String getHostAdress(){
        return hostAdress.getText();
    }
    public String getPort(){
        return port.getText();
    }
    public String getUserName(){
        return userName.getText();
    }
    public String getPassword(){
        return password.getText();
    }
    public Socket getSocket() {
        return socket;
    }
    public OutputStream getOutputStream() {
        return outputStream;
    }
    public InputStream getInputStream() {
        return inputStream;
    }
    public States getState() {
        return state;
    }
    public TextArea getConsole() {
        return console;
    }

    public void setState(States state) {
        this.state = state;
    }

    /**
     * UTILS
     */
    private void sendToServer(EventPOP3 eventPOP3, String... args) throws IOException {
        String separator = " ";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(eventPOP3.getCmd());
        for (String arg: args) {
            stringBuilder.append(separator);
            stringBuilder.append(arg);
        }
        stringBuilder.append("\r\n");
        log("(Debug) sending : " + stringBuilder.toString());
        outputStream.write(stringBuilder.toString().getBytes());
    }
}
