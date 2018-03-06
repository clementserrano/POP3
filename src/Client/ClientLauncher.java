package Client;

import Client.GUI.Test.FrameController;
import Helpers.Constants;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
public class ClientLauncher extends Application {

    static AnchorPane root;
    private FXMLLoader fxmlLoader;
    private final URL frameURL = getClass().getResource("/Client/GUI/Test/frame.fxml");
    private final URL loginURL = getClass().getResource("/Client/GUI/Test/loginScreen.fxml");

    public static void main(String[] args) {
        Application.launch(ClientLauncher.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //fxmlLoader = new FXMLLoader(frameURL);

        fxmlLoader = new FXMLLoader(loginURL);
        root = fxmlLoader.load();
        primaryStage.setTitle("Messagerie");
        primaryStage.setScene(new Scene(root, Constants.frameDimension.width,Constants.frameDimension.height));
        primaryStage.show();
        /*
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                frameController.shutDown();
                Platform.exit();
                System.exit(0);
            }
        });
        */
    }

}
