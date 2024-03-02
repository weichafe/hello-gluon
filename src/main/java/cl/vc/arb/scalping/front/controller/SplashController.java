package cl.vc.arb.scalping.front.controller;

import akka.actor.ActorSystem;
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
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class SplashController implements Initializable {

    public static Stage ventanaApp;
    @Getter
    public static ActorSystem actorSystem = ActorSystem.create();
    @Getter
    private static Properties properties;
    @FXML
    private ImageView image;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        FadeTransition transition = new FadeTransition(Duration.millis(500), image);

        properties = new Properties();

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("STRATEGIES_EXECUTIONS_ALGO");
        comboBox.getSelectionModel().selectFirst();

        ComboBox<String> enviroment = new ComboBox<>();
        enviroment.getItems().addAll("PRODUCTION", "TEST");
        enviroment.getSelectionModel().selectFirst();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("ALGO PAIRS");
        alert.setHeaderText("Select an option");

        VBox vBox = new VBox(10);
        vBox.setPrefWidth(300);
        vBox.getChildren().addAll(new Label("Select an option"), enviroment, comboBox);
        alert.getDialogPane().setContent(vBox);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/SimpleTheme.css").toExternalForm());

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(okButtonType, cancelButtonType);

        Node okButton = dialogPane.lookupButton(okButtonType);
        Node cancelButton = dialogPane.lookupButton(cancelButtonType);
        okButton.getStyleClass().add("button");
        cancelButton.getStyleClass().add("button");

        ButtonType resultado = alert.showAndWait().orElse(ButtonType.CANCEL);

        if (resultado == okButtonType) {
            try {
                String select = comboBox.getSelectionModel().getSelectedItem();
                String enviromentCom = enviroment.getSelectionModel().getSelectedItem();

                if (enviromentCom.equals("PRODUCTION")) {
                    properties.load(SplashController.class.getClassLoader().getResourceAsStream("enviroment/" + select + ".properties"));
                } else {
                    properties.load(SplashController.class.getClassLoader().getResourceAsStream("enviroment/" + select + "_TEST.properties"));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            transition.setFromValue(0.1);
            transition.setToValue(1.0);
            transition.setAutoReverse(true);
            transition.setCycleCount(1);
            transition.play();
        } else {
            System.exit(1);
        }


        transition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                System.setProperty("enviroment", "PRODUCTION");

                Stage ventana = (Stage) image.getScene().getWindow();
                ventanaApp = new Stage();
                Parent root = null;

                try {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Principal.fxml"), resources);
                    root = loader.load();
                    loader.getController();
                    Scene scene = new Scene(root);
                    ventanaApp.setResizable(true);
                    ventanaApp.setMaximized(false);
                    ventanaApp.setScene(scene);

                    ventanaApp.getIcons().add(new Image(properties.getProperty("app.icon.ruta")));

                    ventanaApp.setOnCloseRequest(e -> {
                        Repository.getNettyProtobufClient().stopClient();
                        Platform.exit();
                        System.exit(0);
                    });

                    ventanaApp.setOnShowing(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            ventana.hide();
                        }
                    });

                    ventanaApp.show();

                } catch (Exception e) {

                }
            }
        });

    }

}
