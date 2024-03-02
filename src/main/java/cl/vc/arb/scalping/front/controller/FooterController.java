package cl.vc.arb.scalping.front.controller;

import cl.vc.arb.scalping.front.Repository;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ResourceBundle;


@Data
public class FooterController implements Initializable {


    @FXML
    private CheckBox hideHead;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        hideHead.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                boolean isChecked = hideHead.isSelected();
                Repository.getPrincipalController().getStrategiesDataController().getHideHead().setVisible(!isChecked);
                Repository.getPrincipalController().getStrategiesDataController().getHideHead().setManaged(!isChecked);
            }
        });


    }

}
