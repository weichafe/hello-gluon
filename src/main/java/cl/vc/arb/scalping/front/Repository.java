package cl.vc.arb.scalping.front;

import cl.vc.arb.scalping.front.controller.PrincipalController;
import cl.vc.module.protocolbuff.notification.NotificationMessage;
import cl.vc.module.protocolbuff.tcp.NettyProtobufClient;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Repository {


    public static TrayIcon trayIcon;

    public static String username = "vnazar";

    private static List<NotificationMessage.Notification> notificationList = new ArrayList<>();

    private static NettyProtobufClient nettyProtobufClient;

    private static PrincipalController principalController;

    public static boolean isNotification() {
        return true;
    }


    public static TrayIcon getTrayIcon() {
        return trayIcon;
    }

    public static void setTrayIcon(TrayIcon trayIcon) {
        Repository.trayIcon = trayIcon;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Repository.username = username;
    }

    public static List<NotificationMessage.Notification> getNotificationList() {
        return notificationList;
    }

    public static void setNotificationList(List<NotificationMessage.Notification> notificationList) {
        Repository.notificationList = notificationList;
    }

    public static NettyProtobufClient getNettyProtobufClient() {
        return nettyProtobufClient;
    }

    public static void setNettyProtobufClient(NettyProtobufClient nettyProtobufClient) {
        Repository.nettyProtobufClient = nettyProtobufClient;
    }

    public static PrincipalController getPrincipalController() {
        return principalController;
    }

    public static void setPrincipalController(PrincipalController principalController) {
        Repository.principalController = principalController;
    }
}
