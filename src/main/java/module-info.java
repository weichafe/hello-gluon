module scalping {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.gluonhq.attach.util;
    requires com.gluonhq.attach.display;
    requires akka.actor;
    requires java.datatransfer;
    requires java.desktop;
    requires module.v1;
    requires com.gluonhq.attach.storage;
    requires javafx.graphics;
    requires javafx.base;

    opens cl to javafx.fxml;
    exports cl;

}