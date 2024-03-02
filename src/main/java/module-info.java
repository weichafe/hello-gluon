module scalping {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.gluonhq.charm.glisten;
    requires com.gluonhq.attach.util;
    requires com.gluonhq.attach.display;
    requires akka.actor;
    requires java.datatransfer;
    requires java.desktop;
    requires module.v1;
    requires protobuf.java;
    requires com.gluonhq.attach.storage;


    opens cl.vc.arb.scalping.front.client to akka.actor;
    opens cl.vc.arb.scalping.front.controller to javafx.fxml;
    opens cl.vc.arb.scalping.front to javafx.fxml;

    exports cl.vc.arb.scalping.front;
}