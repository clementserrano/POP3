package Client.GUI.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.awt.event.MouseEvent;

public class MailController {

    @FXML
    private ListView mailList;

    @FXML
    private TextArea mailHeader;

    @FXML
    private TextArea mailContent;

    @FXML
    public void onItemClicked(MouseEvent mouseEvent){
        System.out.println("Clicked on " + mailList.getSelectionModel().getSelectedItem());
    }

}
