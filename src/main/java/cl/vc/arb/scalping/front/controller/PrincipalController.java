package cl.vc.arb.scalping.front.controller;

import akka.actor.ActorRef;
import akka.routing.RoundRobinPool;
import cl.vc.arb.scalping.front.Repository;
import cl.vc.arb.scalping.front.client.ClientActor;
import cl.vc.arb.scalping.front.controller.notifications.NotificationsController;
import cl.vc.arb.scalping.front.util.Notifier;
import cl.vc.module.protocolbuff.notification.NotificationMessage;
import cl.vc.module.protocolbuff.tcp.NettyProtobufClient;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;


public class PrincipalController implements Initializable {

    public static ActorRef client;
    @FXML
    public StrategiesDataController strategiesDataController;
    @FXML
    public FooterController footerController;

    private NotificationsController notificationsController;

    public PrincipalController() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            Repository.setPrincipalController(this);
            Notifier.startNotification();
            client = SplashController.actorSystem.actorOf(new RoundRobinPool(1).props(ClientActor.props()), "ClientManager");
            /*
            Repository.setNettyProtobufClient(new NettyProtobufClient(SplashController.properties.getProperty("controller"),
                    client, "log/", "front-blotter", NotificationMessage.Component.valueOf(SplashController.properties.getProperty("name"))));
            new Thread(Repository.getNettyProtobufClient()).start();

             */

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
