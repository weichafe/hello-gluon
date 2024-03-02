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
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ResourceBundle;


public class PrincipalController implements Initializable {

    @Getter
    public static ActorRef client;
    @FXML
    private StrategiesDataController strategiesDataController;
    @FXML
    private FooterController footerController;
    @Getter
    @Setter
    private NotificationsController notificationsController;

    public PrincipalController() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            Repository.setPrincipalController(this);
            Notifier.startNotification();
            client = SplashController.getActorSystem().actorOf(new RoundRobinPool(1).props(ClientActor.props()), "ClientManager");
            Repository.setNettyProtobufClient(new NettyProtobufClient(SplashController.getProperties().getProperty("controller"),
                    client, "log/", "front-blotter", NotificationMessage.Component.valueOf(SplashController.getProperties().getProperty("name"))));
            new Thread(Repository.getNettyProtobufClient()).start();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
