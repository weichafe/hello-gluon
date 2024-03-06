package cl.controller;

import akka.actor.ActorSystem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class SplashController implements Initializable {

    public static Stage ventanaApp;

    public static ActorSystem actorSystem = ActorSystem.create();

    public static Properties properties;
    @FXML
    private ImageView image;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {

            System.setProperty("enviroment", "PRODUCTION");


            ventanaApp = new Stage();
            Parent root = null;

            try {
                properties = new Properties();
                //properties.load(getClass().getClassLoader().getResourceAsStream("enviroment/STRATEGIES_EXECUTIONS_ALGO.properties"));


                //ventanaApp.show();



            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception ex){
            ex.printStackTrace();
        }

    }

}
