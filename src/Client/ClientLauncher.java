package Client;

import Client.GUI.Controllers.ConnexionController;
import Helpers.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ClientLauncher extends Application {

    static AnchorPane root;
    static List<Pane> panes = new ArrayList<>();
    static int currentPane = 0;

    private FXMLLoader fxmlLoader;
    private final URL connexionStageURL = getClass().getResource("/Client/GUI/Views/connexionStage.fxml");
    private final URL mainStageURL = getClass().getResource("/Client/GUI/Views/mainStage.fxml");

    public static void main(String[] args) {
        Application.launch(ClientLauncher.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        fxmlLoader = new FXMLLoader(mainStageURL);
        root = fxmlLoader.load();

        panes.add(FXMLLoader.load(connexionStageURL));


        primaryStage.setScene(new Scene(root, Constants.frameDimension.width,Constants.frameDimension.height));
        primaryStage.show();
    }

    public static Pane getPane(stage stage){
        return panes.get(stage.getId());
    }

    public static void setPane(stage stage){
        root.getChildren().remove(panes.get(currentPane));
        root.getChildren().add(getPane(stage));
        currentPane = stage.getId();
    }

    public enum stage {
        //MAIN(0),
        CONNEXION(0);

        private int id;
        stage(int id){
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
