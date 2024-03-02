package cl.vc.arb.scalping.front.controller;

import cl.vc.algos.scalping.proto.ScalpingStrategyProtos;
import cl.vc.arb.scalping.front.Repository;
import cl.vc.arb.scalping.front.controller.button.ButtonCellStartBuy;
import cl.vc.arb.scalping.front.controller.button.ButtonCellStartSell;
import cl.vc.arb.scalping.front.controller.button.ButtonCellSubscribe;
import cl.vc.arb.scalping.front.controller.notifications.NotificationsController;
import cl.vc.module.protocolbuff.generalstrategy.GeneralStrategy;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.ResourceBundle;

@Slf4j
@Data
public class StrategiesDataController implements Initializable {

    private final DecimalFormat decimalFormat = new DecimalFormat("#,###.000000");
    private HashMap<String, ScalpingStrategyProtos.ScalpingStrategy.Builder> mapStrategy = new HashMap<>();

    @FXML private Button btnSubscribeAll;
    @FXML private Button btnStartInflowAll;
    @FXML private Button btnStopInflowAll;
    @FXML private Button btnStartFlowbackAll;
    @FXML private Button btnStopFlowbackAll;
    @FXML private Button btnCancelAll;

    @FXML private TableView<ScalpingStrategyProtos.ScalpingStrategy.Builder> tvStrategies;
    @FXML private TableColumn<ScalpingStrategyProtos.ScalpingStrategy.Builder, Boolean> tcMarketData;
    @FXML private TableColumn<ScalpingStrategyProtos.ScalpingStrategy.Builder, Boolean> buy;
    @FXML private TableColumn<ScalpingStrategyProtos.ScalpingStrategy.Builder, Boolean> sell;
    @FXML private TableColumn<ScalpingStrategyProtos.ScalpingStrategy.Builder, String> tcStrategyName;
    @FXML private TableColumn<ScalpingStrategyProtos.ScalpingStrategy.Builder, String> idStrategy0;
    @FXML private TableColumn<ScalpingStrategyProtos.ScalpingStrategy.Builder, String> tcSecurity;
    @FXML private TableColumn<ScalpingStrategyProtos.ScalpingStrategy.Builder, Double> ask;
    @FXML private TableColumn<ScalpingStrategyProtos.ScalpingStrategy.Builder, Double> bid;
    @FXML private TableColumn<ScalpingStrategyProtos.ScalpingStrategy.Builder, Double> close;
    @FXML private TableColumn<ScalpingStrategyProtos.ScalpingStrategy.Builder, Double> pnl;
    @FXML private TableColumn<ScalpingStrategyProtos.ScalpingStrategy.Builder, Double> stoploss;
    @FXML private TableColumn<ScalpingStrategyProtos.ScalpingStrategy.Builder, String> near;
    @FXML private TableColumn<ScalpingStrategyProtos.ScalpingStrategy.Builder, Double> takeProfit;
    @FXML private TableColumn<ScalpingStrategyProtos.ScalpingStrategy.Builder, Double> qty;
    @FXML private TableColumn<ScalpingStrategyProtos.ScalpingStrategy.Builder, Double> round;

    @FXML private TableColumn<ScalpingStrategyProtos.ScalpingStrategy.Builder, String> trend;
    @FXML private VBox hideHead;

    private ObservableList<ScalpingStrategyProtos.ScalpingStrategy.Builder> strategiesObsListButton;
    private FilteredList<ScalpingStrategyProtos.ScalpingStrategy.Builder> filteredData;
    private FilteredList<ScalpingStrategyProtos.ScalpingStrategy.Builder> filteredDataButton;
    private SortedList<ScalpingStrategyProtos.ScalpingStrategy.Builder> sortedData;
    private SortedList<ScalpingStrategyProtos.ScalpingStrategy.Builder> sortedDataButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.tcMarketData.setCellFactory(column -> new ButtonCellSubscribe("Subscribe", "Unsubscribe"));
        this.buy.setCellFactory(column -> new ButtonCellStartBuy("Start", "Stop"));
        this.sell.setCellFactory(column -> new ButtonCellStartSell("Start", "Stop"));

        this.tcMarketData.setCellValueFactory(new PropertyValueFactory<ScalpingStrategyProtos.ScalpingStrategy.Builder, Boolean>("marketData"));
        this.buy.setCellValueFactory(new PropertyValueFactory<ScalpingStrategyProtos.ScalpingStrategy.Builder, Boolean>("inflow"));
        this.sell.setCellValueFactory(new PropertyValueFactory<ScalpingStrategyProtos.ScalpingStrategy.Builder, Boolean>("flowback"));

        this.tcStrategyName.setCellValueFactory(new PropertyValueFactory<ScalpingStrategyProtos.ScalpingStrategy.Builder, String>("strategyName"));
        this.idStrategy0.setCellValueFactory(new PropertyValueFactory<ScalpingStrategyProtos.ScalpingStrategy.Builder, String>("strategyId"));
        this.tcSecurity.setCellValueFactory(new PropertyValueFactory<ScalpingStrategyProtos.ScalpingStrategy.Builder, String>("symbol"));
        this.near.setCellValueFactory(new PropertyValueFactory<ScalpingStrategyProtos.ScalpingStrategy.Builder, String>("near"));

        this.ask.setCellValueFactory(new PropertyValueFactory<ScalpingStrategyProtos.ScalpingStrategy.Builder, Double>("ask"));
        this.bid.setCellValueFactory(new PropertyValueFactory<ScalpingStrategyProtos.ScalpingStrategy.Builder, Double>("bid"));
        this.close.setCellValueFactory(new PropertyValueFactory<ScalpingStrategyProtos.ScalpingStrategy.Builder, Double>("close"));

        this.pnl.setCellValueFactory(new PropertyValueFactory<ScalpingStrategyProtos.ScalpingStrategy.Builder, Double>("pnl"));

        this.stoploss.setCellValueFactory(new PropertyValueFactory<ScalpingStrategyProtos.ScalpingStrategy.Builder, Double>("stopLoss"));
        this.takeProfit.setCellValueFactory(new PropertyValueFactory<ScalpingStrategyProtos.ScalpingStrategy.Builder, Double>("takeProfit"));

        this.round.setCellValueFactory(new PropertyValueFactory<ScalpingStrategyProtos.ScalpingStrategy.Builder, Double>("round"));
        this.qty.setCellValueFactory(new PropertyValueFactory<ScalpingStrategyProtos.ScalpingStrategy.Builder, Double>("qty"));
        this.trend.setCellValueFactory(new PropertyValueFactory<ScalpingStrategyProtos.ScalpingStrategy.Builder, String>("trend"));

        this.strategiesObsListButton = FXCollections.observableArrayList();
        this.filteredData = new FilteredList<>(this.strategiesObsListButton, p -> true);
        this.filteredDataButton = new FilteredList<>(this.strategiesObsListButton, p -> true);
        this.sortedData = new SortedList<>(filteredData);
        this.sortedDataButton = new SortedList<>(filteredDataButton);

        this.sortedData.comparatorProperty().bind(this.tvStrategies.comparatorProperty());
        this.sortedDataButton.comparatorProperty().bind(this.tvStrategies.comparatorProperty());

        this.tvStrategies.setItems(sortedData);
        this.tvStrategies.setItems(sortedDataButton);

        this.tvStrategies.setEditable(true);
        this.tvStrategies.getSortOrder().add(this.tcStrategyName);
        this.tvStrategies.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        this.tvStrategies.getSortOrder().add(this.tcStrategyName);
        this.tvStrategies.setRowFactory(createRowStyleCallback());

        round.setCellFactory(createThousandsSeparatorCellFactory());
        qty.setCellFactory(createThousandsSeparatorCellFactory());
        pnl.setCellFactory(createThousandsSeparatorCellFactory());
        stoploss.setCellFactory(createThousandsSeparatorCellFactory());
        takeProfit.setCellFactory(createThousandsSeparatorCellFactory());

        tvStrategies.setOnMousePressed((MouseEvent event) -> {

            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                ScalpingStrategyProtos.ScalpingStrategy.Builder strategy = tvStrategies.getSelectionModel().getSelectedItem();
                this.showStrategyManteiner(strategy, false);
            }
        });


    }


    private Callback<TableView<ScalpingStrategyProtos.ScalpingStrategy.Builder>, TableRow<ScalpingStrategyProtos.ScalpingStrategy.Builder>> createRowStyleCallback() {
        return tableView -> {
            final TableRow<ScalpingStrategyProtos.ScalpingStrategy.Builder> row = new TableRow<ScalpingStrategyProtos.ScalpingStrategy.Builder>() {
                @Override
                protected void updateItem(ScalpingStrategyProtos.ScalpingStrategy.Builder item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null) return;

                    if (item.getTrend().equals(ScalpingStrategyProtos.Trend.BAJISTA)) {
                        setStyle("-fx-background-color: linear-gradient(to bottom, rgba(238,27,8,0.6), rgba(238,27,8,0.6));");
                    } else if (item.getTrend().equals(ScalpingStrategyProtos.Trend.ALCISTA)) {
                        setStyle("-fx-background-color: linear-gradient(to bottom, rgba(64, 224, 208, 0.3), rgba(102,229,39,0.6));");
                    } else if (item.getTrend().equals(ScalpingStrategyProtos.Trend.NEUTRAL)) {
                        setStyle("-fx-background-color: linear-gradient(to bottom, rgba(159,153,35,0.3), rgba(229,169,39,0.6));");
                    }
                }
            };
            return row;
        };
    }


    public void removeStrategy(String strategyId) {
        try {

            Platform.runLater(() -> {
                ScalpingStrategyProtos.ScalpingStrategy.Builder strategyView = mapStrategy.get(strategyId);
                strategiesObsListButton.remove(strategyView);
                mapStrategy.remove(strategyId);
                this.tvStrategies.getSortOrder().add(this.idStrategy0);
                this.tvStrategies.sort();
                tvStrategies.refresh();
            });


        } catch (Exception e) {
            log.error("Error al eliminar strategia {} de la tabla...", strategyId, e);
        }
    }


    private Callback<TableColumn<ScalpingStrategyProtos.ScalpingStrategy.Builder, Double>,
            TableCell<ScalpingStrategyProtos.ScalpingStrategy.Builder, Double>> createThousandsSeparatorCellFactory() {

        return new Callback<TableColumn<ScalpingStrategyProtos.ScalpingStrategy.Builder, Double>, TableCell<ScalpingStrategyProtos.ScalpingStrategy.Builder, Double>>() {
            @Override
            public TableCell<ScalpingStrategyProtos.ScalpingStrategy.Builder, Double>
            call(TableColumn<ScalpingStrategyProtos.ScalpingStrategy.Builder, Double> param) {
                return new TableCell<ScalpingStrategyProtos.ScalpingStrategy.Builder, Double>() {
                    @Override
                    protected void updateItem(Double item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(decimalFormat.format(item));
                        }
                    }
                };
            }
        };
    }


    public void loadStrategy(ScalpingStrategyProtos.ScalpingStrategy.Builder strategy) {


        Platform.runLater(() -> {

            ScalpingStrategyProtos.ScalpingStrategy.Builder strategyViewNew = this.loadDataStrategy(strategy);

            if (mapStrategy.containsKey(strategyViewNew.getStrategyId())) {

                ScalpingStrategyProtos.ScalpingStrategy.Builder old = mapStrategy.get(strategyViewNew.getStrategyId());

                /*

                if (strategyViewNew.getScalpingStrategyStatus().getSubscribed() != old.getScalpingStrategyStatus().getSubscribed() ||
                        strategyViewNew.getScalpingStrategyStatus().getStart() != old.getScalpingStrategyStatus().getStart()) {
                    this.tvStrategies.sort();
                    tvStrategies.refresh();
                }

                 */

                ScalpingStrategyProtos.ScalpingStrategy.Builder strategyView = mapStrategy.get(strategyViewNew.getStrategyId());
                update(strategyViewNew, strategyView);
                mapStrategy.put(strategyViewNew.getStrategyId(), strategyView);
                this.tvStrategies.sort();
                tvStrategies.refresh();

            } else {
                addStrategy(strategyViewNew);
            }

        });

    }


    public synchronized void addStrategy(ScalpingStrategyProtos.ScalpingStrategy.Builder s) {
        Platform.runLater(() -> {
            try {
                if (!mapStrategy.containsKey(s.getStrategyId())) {
                    mapStrategy.put(s.getStrategyId(), s);
                    this.strategiesObsListButton.add(s);
                    tvStrategies.refresh();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }

    private ScalpingStrategyProtos.ScalpingStrategy.Builder update(ScalpingStrategyProtos.ScalpingStrategy.Builder strategyViewNew, ScalpingStrategyProtos.ScalpingStrategy.Builder strategyView) {
        try {

            strategyView.setStatusStrategy(strategyViewNew.getStatusStrategy());
            strategyView.setScalpingStrategyStatus(strategyViewNew.getScalpingStrategyStatus());
            strategyView.setStrategyId(strategyViewNew.getStrategyId());
            strategyView.setStrategyName(strategyViewNew.getStrategyName());
            strategyView.setOrdersPrefix(strategyViewNew.getOrdersPrefix());
            strategyView.setPnl(strategyViewNew.getPnl());
            strategyView.setAccount(strategyViewNew.getAccount());
            strategyView.setQty(strategyViewNew.getQty());
            strategyView.setAsk(strategyViewNew.getAsk());
            strategyView.setBid(strategyViewNew.getBid());
            strategyView.setClose(strategyViewNew.getClose());
            strategyView.setCurrency(strategyViewNew.getCurrency());
            strategyView.setSettlType(strategyViewNew.getSettlType());
            strategyView.setExchangeDestination(strategyViewNew.getExchangeDestination());
            strategyView.setExchangeSource(strategyViewNew.getExchangeSource());
            strategyView.setTakeProfit(strategyViewNew.getStopLoss());
            strategyView.setStopLoss(strategyViewNew.getTakeProfit());
            strategyView.setSpreadTakeprofit(strategyViewNew.getSpreadTakeprofit());
            strategyView.setSpreadStoploss(strategyViewNew.getSpreadStoploss());
            strategyView.setSymbol(strategyViewNew.getSymbol());
            strategyView.setTrend(strategyViewNew.getTrend());
            strategyView.setPnl(strategyViewNew.getPnl());
            strategyView.setNear(strategyViewNew.getNear());
            strategyView.setDecimalPx(strategyViewNew.getDecimalPx());
            strategyView.setMovingAveragePeriod(strategyViewNew.getMovingAveragePeriod());
            strategyView.setMaxTrades(strategyViewNew.getMaxTrades());
            strategyView.setMaxTrend(strategyViewNew.getMaxTrend());
            strategyView.setTrendThreshold(strategyViewNew.getTrendThreshold());
            strategyView.setTrendConfirmationPeriod(strategyViewNew.getTrendConfirmationPeriod());
            strategyView.setRound(strategyViewNew.getRound());
            strategyView.setHandlInst(strategyViewNew.getHandlInst());


        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return strategyView;
    }
    private ScalpingStrategyProtos.ScalpingStrategy.Builder loadDataStrategy(ScalpingStrategyProtos.ScalpingStrategy.Builder strategy) {

        ScalpingStrategyProtos.ScalpingStrategy.Builder strategyView = ScalpingStrategyProtos.ScalpingStrategy.newBuilder();

        try {

            strategyView.setStatusStrategy(strategy.getStatusStrategy());
            strategyView.setScalpingStrategyStatus(strategy.getScalpingStrategyStatus());
            strategyView.setStrategyId(strategy.getStrategyId());
            strategyView.setStrategyName(strategy.getStrategyName());
            strategyView.setOrdersPrefix(strategy.getOrdersPrefix());
            strategyView.setPnl(strategy.getPnl());
            strategyView.setAccount(strategy.getAccount());
            strategyView.setQty(strategy.getQty());
            strategyView.setAsk(strategy.getAsk());
            strategyView.setBid(strategy.getBid());
            strategyView.setClose(strategy.getClose());
            strategyView.setCurrency(strategy.getCurrency());
            strategyView.setSettlType(strategy.getSettlType());
            strategyView.setExchangeDestination(strategy.getExchangeDestination());
            strategyView.setExchangeSource(strategy.getExchangeSource());
            strategyView.setTakeProfit(strategy.getStopLoss());
            strategyView.setStopLoss(strategy.getTakeProfit());
            strategyView.setSpreadTakeprofit(strategy.getSpreadTakeprofit());
            strategyView.setSpreadStoploss(strategy.getSpreadStoploss());
            strategyView.setSymbol(strategy.getSymbol());
            strategyView.setTrend(strategy.getTrend());
            strategyView.setPnl(strategy.getPnl());
            strategyView.setNear(strategy.getNear());

            strategyView.setMovingAveragePeriod(strategy.getMovingAveragePeriod());
            strategyView.setMaxTrend(strategy.getMaxTrend());
            strategyView.setMaxTrades(strategy.getMaxTrades());
            strategyView.setTrendThreshold(strategy.getTrendThreshold());
            strategyView.setTrendConfirmationPeriod(strategy.getTrendConfirmationPeriod());
            strategyView.setDecimalPx(strategy.getDecimalPx());
            strategyView.setRound(strategy.getRound());
            strategyView.setHandlInst(strategy.getHandlInst());

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }


        return strategyView;
    }

    @FXML public void addStrategy(ActionEvent event) {
        this.showStrategyManteiner(null, false);
    }

    private void showStrategyManteiner(ScalpingStrategyProtos.ScalpingStrategy.Builder strategyView, boolean clonar) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Strategy.fxml"));
            Parent parent;
            parent = fxmlLoader.load();
            StrategyController strategyController = fxmlLoader.getController();
            Scene scene = new Scene(parent);
            Stage stage = new Stage();

            if (strategyView == null) {
                stage.setTitle("New trategy");
                strategyController.getBtnUpdateStrategy().setDisable(true);
                strategyController.getBtnDeleteStrategy().setDisable(true);
                strategyController.getBtnAddStrategy().setDisable(false);

            } else if (clonar) {
                strategyController.showCloneStrategy(strategyView);
            } else {
                strategyController.showStrategyData(strategyView);
                strategyController.getBtnUpdateStrategy().setDisable(false);
                strategyController.getBtnDeleteStrategy().setDisable(false);
            }

            strategyController.setStage(stage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();


        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @FXML
    public void onActionCancelAll(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancell All");
        alert.setHeaderText("");
        alert.setContentText("Do you want to cancel all the strategies??");

        ButtonType buttonTypeAceptar = new ButtonType("OK");
        ButtonType buttonTypeCancelar = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeAceptar, buttonTypeCancelar);

        Scene scene = alert.getDialogPane().getScene();
        scene.getStylesheets().add(getClass().getResource("/css/SimpleTheme.css").toExternalForm());

        alert.showAndWait().ifPresent(response -> {

            if (response == buttonTypeAceptar) {
                /*

                strategiesObsListButton.forEach(s -> {

                    if (s.getScalpingStrategyStatus().getStart()) {

                        GeneralStrategy.OperationsControl operationsControl =
                                GeneralStrategy.OperationsControl
                                        .newBuilder()
                                        .setStrategyId(s.getStrategyId())
                                        .setUsername(Repository.getUsername())
                                        .setTimestamp(System.currentTimeMillis())
                                        .setOperationControl(GeneralStrategy.OperationsControlEnum.STOP_STRATEGY).build();
                        Repository.getNettyProtobufClient().sendMessage(operationsControl);

                    }

                });

                 */

            } else if (response == buttonTypeCancelar) {

            }
        });


    }


    @FXML
    public void unSubscribeAllStrategies(ActionEvent event) {
        Platform.runLater(() -> {
            strategiesObsListButton.forEach(s -> {
                /*

                if (s.getScalpingStrategyStatus().getSubscribed() && !s.getScalpingStrategyStatus().getStart()) {
                    try {
                        Thread.sleep(50);
                        GeneralStrategy.OperationsControl operationsControl =
                                GeneralStrategy.OperationsControl
                                        .newBuilder()
                                        .setStrategyId(s.getStrategyId())
                                        .setUsername(Repository.getUsername())
                                        .setTimestamp(System.currentTimeMillis())
                                        .setOperationControl(GeneralStrategy.OperationsControlEnum.UNSUBSCRIBE).build();
                        Repository.getNettyProtobufClient().sendMessage(operationsControl);

                    } catch (InterruptedException e) {
                        log.error(e.getMessage(), e);
                    }
                }

                 */
            });
        });

    }


    @FXML
    public void subscribeAllStrategies(ActionEvent event) {
        Platform.runLater(() -> {
            strategiesObsListButton.forEach(s -> {
                if (!s.getScalpingStrategyStatus().getSubscribed()) {
                    try {

                        Thread.sleep(100);
                        GeneralStrategy.OperationsControl operationsControl =
                                GeneralStrategy.OperationsControl
                                        .newBuilder()
                                        .setStrategyId(s.getStrategyId())
                                        .setUsername(Repository.getUsername())
                                        .setTimestamp(System.currentTimeMillis())
                                        .setOperationControl(GeneralStrategy.OperationsControlEnum.SUBSCRIBE).build();
                        Repository.getNettyProtobufClient().sendMessage(operationsControl);

                    } catch (InterruptedException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            });
        });

    }




    @FXML
    public void onActionShowNotifications(ActionEvent event) {

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Notifications.fxml"));
            Parent parent;
            parent = fxmlLoader.load();
            NotificationsController notificationsController = fxmlLoader.getController();
            notificationsController.loadNotificationsData(Repository.getNotificationList());

            Scene scene = new Scene(parent);
            Stage stage = new Stage();

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(true);
            stage.setScene(scene);
            stage.showAndWait();

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
