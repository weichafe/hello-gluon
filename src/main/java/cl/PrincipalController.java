package cl;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;


public class PrincipalController implements Initializable {

   // public static ActorRef client;
    //@FXML
    //public StrategiesDataController strategiesDataController;
    //@FXML
    //public FooterController footerController;

//    private NotificationsController notificationsController;

    @FXML
    private Label label;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        label.setText("saddassadadsads");
              /*
            Repository.setPrincipalController(this);
            Notifier.startNotification();
            client = SplashController.actorSystem.actorOf(new RoundRobinPool(1).props(ClientActor.props()), "ClientManager");

            Repository.setNettyProtobufClient(new NettyProtobufClient(SplashController.properties.getProperty("controller"),
                    client, "log/", "front-blotter", NotificationMessage.Component.valueOf(SplashController.properties.getProperty("name"))));
            new Thread(Repository.getNettyProtobufClient()).start();

             */

    }

}