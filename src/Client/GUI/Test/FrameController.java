package Client.GUI.Test;

import Client.Connexion;
import Helpers.Constants;
import Helpers.EventPOP3;
import Helpers.States;
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
                log("Waiting for connection");
                recieveFromServer(EventPOP3.APOP);
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
        log("(Debug) sending : " + stringBuilder.toString());
        outputStream.write(stringBuilder.toString().getBytes());
    }

    private void recieveFromServer(EventPOP3 eventPOP3) {
        try {
            String recievedString;
            BufferedReader input = new BufferedReader(new InputStreamReader(getInputStream()));
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(getOutputStream()));

            if ((recievedString = input.readLine()) != null) {
                String[] splitedString = recievedString.split(" ");

                if (splitedString[0].contains(Constants.ok)){
                    switch (eventPOP3){

                        case APOP:
                            log("Connected");
                            state = States.TRANSACTION;
                            break;

                        case STAT:
                            int nombreMessageDepotCourrier = Integer.parseInt(splitedString[1]);
                            int tailleDepotCourrierOctet = Integer.parseInt(splitedString[2]);
                            log("+OK "+nombreMessageDepotCourrier +" "+ tailleDepotCourrierOctet);
                            break;

                        case RETR:
                            int tailleMessage = Integer.parseInt(splitedString[1]);
                            log("+OK "+tailleMessage);
                            // TODO reception du message
                            break;

                        case QUIT:
                            // TODO disconnect
                            break;
                    }
                }

                else if (splitedString[0].contains(Constants.err)){
                    switch (eventPOP3){
                        case APOP:
                            // TODO
                            break;
                        case STAT:
                            // TODO
                            break;
                        case RETR:
                            // TODO
                            break;
                        case QUIT:
                            // TODO
                            break;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
