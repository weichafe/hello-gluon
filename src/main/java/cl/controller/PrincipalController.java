package cl.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;


public class PrincipalController implements Initializable {

    @FXML
    private Label label;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        label.setText("saddassadadsads");
    }

}
