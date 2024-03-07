package front;

import javafx.application.Platform;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Notifier {


    private static boolean isCoolingDown = false;

    public static void startNotification() {

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable task = new Runnable() {
            public void run() {
                isCoolingDown = false;
            }
        };

        scheduler.scheduleAtFixedRate(task, 0, 3, TimeUnit.SECONDS);
    }

    public static void notifyInfo(final String title, final String message) {

        if (isCoolingDown || Repository.getPrincipalController() == null) return;

        if (Repository.isNotification()) {

            Platform.runLater(() -> {
                isCoolingDown = true;
                Repository.trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
            });
            isCoolingDown = false;
        }
    }

    public static void notifyWarning(final String title, final String message) {

        if (isCoolingDown || Repository.getPrincipalController() == null) return;

        if (Repository.isNotification()) {
            isCoolingDown = true;

            Platform.runLater(() -> {
                Repository.trayIcon.displayMessage(title, message, TrayIcon.MessageType.WARNING);
            });

            isCoolingDown = false;
        }
    }

    public static void notifyError(final String title, final String message) {

        if (isCoolingDown || Repository.getPrincipalController() == null) return;

        if (Repository.isNotification()) {
            isCoolingDown = true;
            Platform.runLater(() -> {
                Repository.trayIcon.displayMessage(title, message, TrayIcon.MessageType.ERROR);
            });

            isCoolingDown = false;
        }
    }

}
