package cl.vc.arb.scalping.front.client;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import cl.vc.algos.scalping.proto.ScalpingStrategyProtos;
import cl.vc.arb.scalping.front.Repository;
import cl.vc.arb.scalping.front.util.Notifier;
import cl.vc.module.protocolbuff.TimeGenerator;
import cl.vc.module.protocolbuff.generalstrategy.GeneralStrategy;
import cl.vc.module.protocolbuff.notification.NotificationMessage;
import cl.vc.module.protocolbuff.routing.RoutingMessage;
import cl.vc.module.protocolbuff.session.SessionsMessage;
import cl.vc.module.protocolbuff.tcp.TransportingObjects;
import javafx.application.Platform;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class ClientActor extends AbstractActor {

    private final static HashMap<String, RoutingMessage.Order> ordersById = new HashMap<>();

    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();


    private boolean showNotification = false;

    private ClientActor() {

    }

    public static Props props() {
        return Props.create(ClientActor.class);
    }

    @Override
    public void preStart() {
        try {


            executorService.scheduleAtFixedRate(() -> {
                showNotification = true;

            }, 0, 20, TimeUnit.SECONDS);


        } catch (Exception exc) {

        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(TransportingObjects.class, this::onTransportingObjects)
                .match(NotificationMessage.Notification.class, this::onNotification)
                .match(ScalpingStrategyProtos.ScalpingStrategy.class, this::onPairsStrategy)
                .build();

    }


    private void onPairsStrategy(ScalpingStrategyProtos.ScalpingStrategy message) {


        if (message.getStatusStrategy().equals(GeneralStrategy.StatusStrategy.ADD_STRATEGY) ||
                message.getStatusStrategy().equals(GeneralStrategy.StatusStrategy.UPDATE_STRATEGY)) {
        //    Repository.getPrincipalController().strategiesDataController.loadStrategy(message.toBuilder());
        } else if (message.getStatusStrategy().equals(GeneralStrategy.StatusStrategy.REMOVE_STRATEGY)) {
        //    Repository.getPrincipalController().strategiesDataController.removeStrategy(message.getStrategyId());
        }

    }


    private void onTransportingObjects(TransportingObjects message) {

        try {

            if (message.getMessage() instanceof SessionsMessage.Connect) {
                SessionsMessage.Connect response = ((SessionsMessage.Connect) message.getMessage());
                onConnect(response);

            } else if (message.getMessage() instanceof SessionsMessage.Disconnect) {
                SessionsMessage.Disconnect response = ((SessionsMessage.Disconnect) message.getMessage());
                onDisconnect(response);
            } else {

                getSelf().tell(message.getMessage(), ActorRef.noSender());

            }

        } catch (Exception e) {

        }
    }


    private void onConnect(SessionsMessage.Connect message) {
        try {


            if (showNotification) {
                showNotification = false;
                Notifier.notifyInfo("Connected", "Core service enable");
            }


        } catch (Exception e) {

        }
    }

    private void onDisconnect(SessionsMessage.Disconnect message) {
        try {

            if (showNotification) {
                showNotification = false;

                NotificationMessage.Notification notification = NotificationMessage.Notification.newBuilder()
                        .setComments(message.getText())
                        .setComponent(message.getComponent())
                        .setTypeState(NotificationMessage.TypeState.DISCONNECTION)
                        .setLevel(NotificationMessage.Level.FATAL)
                        .setTime(TimeGenerator.getTimeProto())
                        .setTitle("Error Services").build();

                Repository.getNotificationList().add(notification);
                Notifier.notifyError("Disconnected", message.getComponent() + "\nCore PAIRS service unavailable");
            }

            Platform.runLater(() -> {
             //   Repository.getPrincipalController().strategiesDataController.strategiesObsListButton.clear();
             //   Repository.getPrincipalController().strategiesDataController.mapStrategy.clear();
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void onNotification(NotificationMessage.Notification message) {
        try {

            if (!showNotification) {
                return;
            }

            showNotification = false;

            if (message.getLevel().equals(NotificationMessage.Level.ERROR) || message.getLevel().equals(NotificationMessage.Level.FATAL)) {
                Notifier.notifyError(message.getComponent().name() + "\n" + message.getTitle() + "\n", message.getMessage());

            } else if (message.getLevel().equals(NotificationMessage.Level.INFO) || message.getLevel().equals(NotificationMessage.Level.SUCCESS)) {
                Notifier.notifyInfo(message.getComponent().name() + "\n" + message.getTitle() + "\n", message.getMessage());
            } else {
                Notifier.notifyWarning(message.getComponent().name() + "\n" + message.getTitle() + "\n", message.getMessage());
            }

            Repository.getNotificationList().add(message);

        } catch (Exception e) {

        }
    }


}