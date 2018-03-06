package Client;

import Client.GUI.Test.FrameController;
import Client.GUI.Test.MailController;
import Helpers.Constants;
import Helpers.EventPOP3;
import Helpers.States;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Observable;
import java.util.Observer;

public class ReceptionThread implements Runnable {

    private MailController mailController;
    private EventPOP3 eventPOP3;

    public ReceptionThread(MailController frameController, EventPOP3 eventPOP3){
        this.mailController = frameController;
        this.eventPOP3 = eventPOP3;
    }

    public ReceptionThread(MailController mailController){
        this.mailController = mailController;
    }

    @Override
    public void run() {
        try {
            String recievedString;
            BufferedReader input = new BufferedReader(new InputStreamReader(mailController.getInputStream()));
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(mailController.getOutputStream()));

            while (mailController.getSocket().isConnected()) {
                if ((recievedString = input.readLine()) != null) {
                    String[] splitedString = recievedString.split(" ");
                    // OK
                    if (splitedString[0].contains(Constants.ok)) {
                        mailController.log(recievedString);
                    }
                    // ERR
                    else if (splitedString[0].contains(Constants.err)) {
                        mailController.log(recievedString);
                    }
                    // DATA
                    else {
                        if(!recievedString.equals(".")){
                            mailController.log(recievedString);
                        }
                        else {
                            mailController.log(recievedString);
                        }
                    }
                }
            }
        } catch(Exception e){
            mailController.log("disconnected");
            mailController.setState(States.AUTHORIZATION);
        }
    }

    public MailController getMailController() {
        return mailController;
    }
    public void setMailController(MailController mailController) {
        this.mailController = mailController;
    }

}
