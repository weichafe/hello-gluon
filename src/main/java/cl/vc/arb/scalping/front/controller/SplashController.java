package cl.vc.arb.scalping.front.controller;

import akka.actor.ActorSystem;
import cl.vc.arb.scalping.front.MainApp;
import cl.vc.arb.scalping.front.Repository;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Principal.fxml"), resources);
                root = loader.load();
                loader.getController();
                Scene scene = new Scene(root);
                ventanaApp.setResizable(true);
                ventanaApp.setMaximized(false);
                ventanaApp.setScene(scene);

                //ventanaApp.getIcons().add(new Image(properties.getProperty("app.icon.ruta")));

                ventanaApp.setOnCloseRequest(e -> {
                    Repository.getNettyProtobufClient().stopClient();
                    Platform.exit();
                    System.exit(0);
                });

                ventanaApp.setOnShowing(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        MainApp.primaryStage.hide();
                    }
                });

                ventanaApp.show();



            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception ex){
            ex.printStackTrace();
        }

    }

}
