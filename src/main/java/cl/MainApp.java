package cl;


import com.gluonhq.attach.display.DisplayService;
import com.gluonhq.attach.util.Platform;
import com.gluonhq.charm.glisten.application.AppManager;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.TextArea;
import com.gluonhq.charm.glisten.mvc.View;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Dimension2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static com.gluonhq.charm.glisten.application.AppManager.HOME_VIEW;


public class MainApp extends Application {

    private final AppManager appManager = AppManager.initialize(this::postInit);

    public static Stage primaryStage;

    private Parent root;

    @Override
    public void init() {
        appManager.addViewFactory(HOME_VIEW, () -> {

            View view = null;

            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/cl.controller/Principal.fxml"));
                root = loader.load();

                view = new View(root) {
                    @Override
                    protected void updateAppBar(AppBar appBar) {
                        appBar.setTitleText("Gluon Mobile");
                    }
                };

                return view;

            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                String stackTrace = sw.toString();
                System.out.println(stackTrace);
                TextArea textArea = new TextArea(stackTrace);
                textArea.setMinHeight(850);
                return new View(textArea);
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
        try {

            appManager.start(stage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
