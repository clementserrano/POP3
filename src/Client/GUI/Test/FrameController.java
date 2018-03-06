package Client.GUI.Test;

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
    public int messageNumber;
    public Thread receptionThread;

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
    private TextField nbMessage;
    @FXML
    private Button connect;
    @FXML
    private Button disconnect;
    @FXML
    private Button login;
    @FXML
    private Button stat;
    @FXML
    private Button retr;
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
            receptionThread = new Thread(new ReceptionThread(this));
            receptionThread.setDaemon(true);
            receptionThread.start();

        } catch (Exception e) {
            log("Failed to connect");
            e.printStackTrace();
        }
    }

    @FXML
    public void disconnection() {
        try {
            if (socket != null && socket.isConnected()) {
                log("Disconnecting");
                sendToServer(EventPOP3.QUIT);
                state = States.AUTHORIZATION;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void retrive() {
        try {
            sendToServer(EventPOP3.RETR, nbMessage.getText());
        }catch (Exception e ){
            e.printStackTrace();
        }
    }

    @FXML
    public void getStat(){
        try{
            sendToServer(EventPOP3.STAT);
        }catch (Exception e ){
            e.printStackTrace();
        }
    }


    /*
    @FXML
    public void refreshMailList(){
        try {
            if (socket != null && socket.isConnected() && state.equals(States.TRANSACTION)){
                log("Refreshing mail list");
                messageNumber = 0;
                sendToServer(EventPOP3.STAT);
                if (messageNumber > 0) {
                    for (int i = 0; i < messageNumber; i++) {
                        sendToServer(EventPOP3.RETR, i + "");
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    */

    @FXML
    public void login() {
        if (socket != null && socket.isConnected() && state.equals(States.AUTHORIZATION)) {
            try {
                log("Loging in your Mail Box");
                String passwordMD5 = new String(MessageDigest.getInstance("MD5").digest(getPassword().getBytes()), StandardCharsets.UTF_8);
                sendToServer(EventPOP3.APOP, getUserName(), passwordMD5);
                sendToServer(EventPOP3.RETR, "1");
                state = States.TRANSACTION;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setupTextAreas(){
        console.setWrapText(true);
    }
    public void log(String string){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                console.appendText(string);
                console.appendText("\n");
            }
        });
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


}
