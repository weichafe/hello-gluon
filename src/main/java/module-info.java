module scalping {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.gluonhq.attach.util;
    requires com.gluonhq.attach.display;
    requires akka.actor;
    requires java.datatransfer;
    requires java.desktop;
    requires module.v1;
    requires protobuf.java;
    requires com.gluonhq.attach.storage;
    requires javafx.graphics;
    requires java.base;
    requires com.gluonhq.charm.glisten;

    opens front to javafx.fxml;
    exports front;
    exports front.controller;
    opens front.controller to javafx.fxml;
}