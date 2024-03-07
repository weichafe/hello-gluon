package cl.controller;

import com.gluonhq.charm.glisten.animation.FadeInLeftBigTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.FloatingActionButton;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import java.net.URL;
import java.util.ResourceBundle;


public class HomePresenter implements Initializable {

    @FXML
    private View home;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FloatingActionButton fab = new FloatingActionButton();
        fab.setOnAction(e -> {
            System.out.println("FAB click");
        });
        fab.showOn(home);

        home.setShowTransitionFactory(v -> new FadeInLeftBigTransition(v));

        home.showingProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                final AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> System.out.println("menu")));
                appBar.setTitleText("The Home View");
                appBar.getActionItems().addAll(
                        MaterialDesignIcon.SEARCH.button(),
                        MaterialDesignIcon.FAVORITE.button());
                appBar.getMenuItems().addAll(new MenuItem("Settings"));

                Swatch.PURPLE.assignTo(home.getScene());
            }
        });
    }

    @FXML
    public void onClick() {
        System.out.println("click");
    }

    public View getHome() {
        return home;
    }
}
