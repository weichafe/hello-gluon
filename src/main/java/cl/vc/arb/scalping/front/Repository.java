package cl.vc.arb.scalping.front;

import cl.vc.arb.scalping.front.controller.PrincipalController;
import cl.vc.module.protocolbuff.notification.NotificationMessage;
import cl.vc.module.protocolbuff.tcp.NettyProtobufClient;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Repository {

    @Getter
    @Setter
    public static TrayIcon trayIcon;
    @Getter
    @Setter
    public static String username = "vnazar";
    @Getter
    private static List<NotificationMessage.Notification> notificationList = new ArrayList<>();
    @Getter
    @Setter
    private static NettyProtobufClient nettyProtobufClient;
    @Getter
    @Setter
    private static PrincipalController principalController;

    public static boolean isNotification() {
        return true;
    }

}
