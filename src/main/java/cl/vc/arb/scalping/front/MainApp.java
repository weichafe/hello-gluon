package cl.vc.arb.scalping.front;

import com.gluonhq.attach.display.DisplayService;
import com.gluonhq.attach.util.Platform;
import com.gluonhq.charm.glisten.visual.Swatch;
import com.gluonhq.hello.HelloGluonApp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Dimension2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.extern.slf4j.Slf4j;
import com.gluonhq.attach.display.DisplayService;
import com.gluonhq.attach.util.Platform;
import com.gluonhq.charm.glisten.application.AppManager;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.FloatingActionButton;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.application.Application;
import javafx.geometry.Dimension2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

import static com.gluonhq.charm.glisten.application.AppManager.HOME_VIEW;

@Slf4j
public class MainApp extends Application {

    private final AppManager appManager = AppManager.initialize(this::postInit);

    public static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }


    private void postInit(Scene scene) {
        Swatch.LIGHT_GREEN.assignTo(scene);
        scene.getStylesheets().add(HelloGluonApp.class.getResource("styles.css").toExternalForm());

        if (Platform.isDesktop()) {
            Dimension2D dimension2D = DisplayService.create()
                    .map(DisplayService::getDefaultDimensions)
                    .orElse(new Dimension2D(640, 480));
            scene.getWindow().setWidth(dimension2D.getWidth());
            scene.getWindow().setHeight(dimension2D.getHeight());
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        appManager.addViewFactory(HOME_VIEW, () -> {

            try {

                this.primaryStage = primaryStage;

                Parent root = FXMLLoader.load(getClass().getResource("/view/Splash.fxml"));
                Scene scene = new Scene(root, 300, 290);
                primaryStage.setTitle("Splash Screen");
                primaryStage.setResizable(true);
                primaryStage.setScene(scene);
                primaryStage.initStyle(StageStyle.TRANSPARENT);
                primaryStage.show();

                View view = new View(root) {
                    @Override
                    protected void updateAppBar(AppBar appBar) {
                        appBar.setTitleText("Gluon Mobile");
                    }
                };

                return view;

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

    }

}
