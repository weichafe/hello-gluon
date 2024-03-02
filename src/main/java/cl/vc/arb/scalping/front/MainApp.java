package cl.vc.arb.scalping.front;

import com.gluonhq.attach.display.DisplayService;
import com.gluonhq.attach.util.Platform;
import com.gluonhq.charm.glisten.application.AppManager;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Dimension2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ResourceBundle;

import static com.gluonhq.charm.glisten.application.AppManager.HOME_VIEW;


public class MainApp extends Application {

    private final AppManager appManager = AppManager.initialize(this::postInit);

    public static Stage primaryStage;

    @Override
    public void init() {
        appManager.addViewFactory(HOME_VIEW, () -> {


            View view = null;
            try {


                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Principal.fxml"));
                Parent  root = loader.load();
                loader.getController();
                Scene scene = new Scene(root);
                Stage ventanaApp = new Stage();
                ventanaApp.setResizable(true);
                ventanaApp.setMaximized(false);
                ventanaApp.setScene(scene);

                //ventanaApp.getIcons().add(new Image(properties.getProperty("app.icon.ruta")));

                ventanaApp.setOnCloseRequest(e -> {
                    //Repository.getNettyProtobufClient().stopClient();
                    javafx.application.Platform.exit();
                    System.exit(0);
                });

                ventanaApp.setOnShowing(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        MainApp.primaryStage.hide();
                    }
                });


                view = new View(root) {
                    @Override
                    protected void updateAppBar(AppBar appBar) {
                        appBar.setTitleText("Gluon Mobile");
                    }
                };

                return view;

            } catch (Exception e) {
                return new View(new Label("Error loading the splash screen."));
            }
        });
    }


    private void postInit(Scene scene) {

        if (Platform.isDesktop()) {
            Dimension2D dimension2D = DisplayService.create()
                    .map(DisplayService::getDefaultDimensions)
                    .orElse(new Dimension2D(640, 480));
            scene.getWindow().setWidth(dimension2D.getWidth());
            scene.getWindow().setHeight(dimension2D.getHeight());
        }
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        appManager.start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
