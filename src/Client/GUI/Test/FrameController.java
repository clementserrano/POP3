package Client.GUI.Test;

import Client.ReceptionAsyncTask;
import Client.ReceptionThread;
import Helpers.EventPOP3;
import Helpers.States;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;


public class FrameController {

    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    private States state = States.AUTHORIZATION;

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
            new Thread(new ReceptionThread(this)).start();

        } catch (Exception e) {
            log("Failed to connect");
            e.printStackTrace();
        }
    }

    @FXML
    public void login() {
        if (socket != null && socket.isConnected() && state.equals(States.AUTHORIZATION)) {
            try {
                log("Loging in your Mail Box");
                String passwordMD5 = new String(MessageDigest.getInstance("MD5").digest(getPassword().getBytes()), StandardCharsets.UTF_8);
                sendToServer(EventPOP3.APOP, getUserName(), passwordMD5);
                //log("Waiting for connection");
                //recieveFromServer(EventPOP3.APOP);
                sendToServer(EventPOP3.QUIT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void setupTextAreas(){
        console.setWrapText(true);
        mailContent.setWrapText(true);
    }
    public void log(String string){
        console.appendText(string);
        console.appendText("\n");
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

    private void recieveFromServer(EventPOP3 eventPOP3) {
        new Thread(new ReceptionThread(this, eventPOP3)).start();
    }

}
