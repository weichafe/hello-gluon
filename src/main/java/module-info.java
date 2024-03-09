module scalping {

    requires javafx.controls;
    requires javafx.fxml;
    requires com.gluonhq.attach.util;
    requires com.gluonhq.attach.display;
    requires java.datatransfer;
    requires java.desktop;
    requires com.gluonhq.attach.storage;
    requires javafx.graphics;
    requires java.base;
    requires com.gluonhq.charm.glisten;

    exports hellofx;

    opens hellofx to javafx.fxml;

}