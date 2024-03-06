package cl.controller;

import cl.vc.module.protocolbuff.notification.NotificationMessage;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class NotificationsController implements Initializable {

    @FXML private TableView<NotificationMessage.Notification> tvNotifications;
    @FXML private TableColumn<NotificationMessage.Notification, String> tcTime;
    @FXML private TableColumn<NotificationMessage.Notification, String> tcMessage;

    @FXML private TableColumn<NotificationMessage.Notification, String> tcTitle;
    @FXML private TableColumn<NotificationMessage.Notification, String> tcLevel;
    @FXML private TableColumn<NotificationMessage.Notification, String> tcComponent;
    @FXML private TableColumn<NotificationMessage.Notification, String> tcType;

    @FXML private ObservableList<NotificationMessage.Notification> notificationsObsList;
    @FXML private FilteredList<NotificationMessage.Notification> filteredData;
    @FXML private SortedList<NotificationMessage.Notification> sortedData;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        tcTime.setCellValueFactory(cellData -> {
            long seconds = cellData.getValue().getTime().getSeconds();
            int nanos = cellData.getValue().getTime().getNanos();
            Instant instant = Instant.ofEpochSecond(seconds, nanos);
            LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = dateTime.format(formatter);
            return new SimpleStringProperty(formattedDateTime);
        });

        tcLevel.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getLevel().name()));
        tcTitle.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTitle()));
        tcMessage.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getComments()));
        tcComponent.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getComponent().name()));
        tcType.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTypeState().name()));

        this.notificationsObsList = FXCollections.observableArrayList();
        this.filteredData = new FilteredList<>(this.notificationsObsList, p -> true);
        this.sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(this.tvNotifications.comparatorProperty());
        this.tvNotifications.setItems(sortedData);
        this.tvNotifications.setEditable(true);
        this.tvNotifications.getSortOrder().add(this.tcTime);
        this.tvNotifications.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tvNotifications.setRowFactory(createRowStyleCallback());

    }


    private javafx.util.Callback<TableView<NotificationMessage.Notification>, TableRow<NotificationMessage.Notification>> createRowStyleCallback() {
        return tableView -> {
            final TableRow<NotificationMessage.Notification> row = new TableRow<NotificationMessage.Notification>() {
                @Override
                protected void updateItem(NotificationMessage.Notification item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null) return;

                    String level = item.getLevel().name();

                    Tooltip tooltip = new Tooltip();
                    tooltip.setText(item.getComments());
                    setTooltip(tooltip);

                    switch (level.toLowerCase()) {
                        case "info":
                            setStyle("-fx-background-color: lightblue;");
                            break;
                        case "warning":
                            setStyle("-fx-background-color: yellow;");
                            break;
                        case "error":
                            setStyle("-fx-background-color: red;");
                            break;
                        case "fatal":
                            setStyle("-fx-background-color: red;");
                            break;
                        default:
                            // Otros niveles
                            setStyle("");
                            break;
                    }
                }
            };

            return row;
        };
    }

    public void loadNotificationsData(List<NotificationMessage.Notification> lstNotifications) {
        this.notificationsObsList.addAll(lstNotifications);
    }


}
