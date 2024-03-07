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

<<<<<<< HEAD
    opens front to javafx.fxml;
    exports front;
    exports front.controller;
    opens front.controller to javafx.fxml;
=======
    opens cl to javafx.fxml;
    opens cl.controller to javafx.fxml;
    exports cl.controller;
    exports cl;
>>>>>>> 79682d5594d8e05e3357c89b93ad4e30ffb8a988
}