package Client.GUI.Views;

import Helpers.Constants;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class ConnexionFrame extends Stage {

    private Scene scene;
    private Group root;
    private Pane background;

    public ConnexionFrame(){
        this.setHeight(Constants.frameDimension.height);
        this.setWidth(Constants.frameDimension.width);
        this.background = new Pane();
        this.background.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));



        this.root = new Group(background);
        this.scene = new Scene(root, Color.BLUE);
        this.setScene(scene);
        this.show();
    }
}
