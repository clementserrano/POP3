package Client;

import Client.GUI.Test.FrameController;
import Helpers.AsyncTask;
import Helpers.Constants;
import Helpers.EventPOP3;
import Helpers.States;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static Helpers.EventPOP3.APOP;


public class ReceptionAsyncTask extends AsyncTask {

    private FrameController frameController;
    private EventPOP3 eventPOP3;

    public ReceptionAsyncTask(FrameController frameController, EventPOP3 eventPOP3){
        this.frameController = frameController;
        this.eventPOP3 = eventPOP3;
    }

    @Override
    public void onPreExecute() {
    }

    @Override
    public void doInBackground() {
        /*
        try {
            String recievedString;
            BufferedReader input = new BufferedReader(new InputStreamReader(frameController.getInputStream()));
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(frameController.getOutputStream()));

            if ((recievedString = input.readLine()) != null) {
                String[] splitedString = recievedString.split(" ");
                frameController.log(recievedString);
                if (splitedString[0].contains(Constants.ok)){
                    switch (eventPOP3){

                        case APOP:
                            frameController.log("Authentication OK");
                            frameController.setState(States.TRANSACTION);
                            break;

                        case STAT:
                            int nombreMessageDepotCourrier = Integer.parseInt(splitedString[1]);
                            int tailleDepotCourrierOctet = Integer.parseInt(splitedString[2]);
                            frameController.log("+OK "+nombreMessageDepotCourrier +" "+ tailleDepotCourrierOctet);
                            break;

                        case RETR:
                            int tailleMessage = Integer.parseInt(splitedString[1]);
                            frameController.log("+OK "+tailleMessage);
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
        */
    }

    @Override
    public void onPostExecute() {
    }

    @Override
    public void progressCallback(Object... params) {

    }
}
