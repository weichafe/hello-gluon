package cl.vc.arb.scalping.front;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.net.URL;

@Slf4j
public class MainApp extends Application {

    public static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;

        String musicFile = "/sound/red-alert.mp3";
        URL resource = getClass().getResource(musicFile);// For example

        SystemTray tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().createImage("/img/add1.png"); // Reemplaza con la ruta a tu icono
        TrayIcon trayIcon = new TrayIcon(image, "Blotter Notificación");
        trayIcon.setImageAutoSize(true);
        Repository.setTrayIcon(trayIcon);
        tray.add(trayIcon);

        Parent root = FXMLLoader.load(getClass().getResource("/view/Splash.fxml"));
        Scene scene = new Scene(root, 300, 290);
        primaryStage.setTitle("Splash Screen");
        primaryStage.setResizable(true);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();

    }

}
