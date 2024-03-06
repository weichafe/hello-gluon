package cl;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;



public class FooterController implements Initializable {


    @FXML
    private CheckBox hideHead;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        hideHead.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                boolean isChecked = hideHead.isSelected();
              //  Repository.getPrincipalController().strategiesDataController.hideHead.setVisible(!isChecked);
             //  Repository.getPrincipalController().strategiesDataController.hideHead.setManaged(!isChecked);
            }
        });


    }

}
