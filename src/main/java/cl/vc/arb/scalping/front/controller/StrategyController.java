package cl.vc.arb.scalping.front.controller;

import cl.vc.algos.scalping.proto.ScalpingStrategyProtos;
import cl.vc.arb.scalping.front.Repository;
import cl.vc.arb.scalping.front.util.Notifier;
import cl.vc.module.protocolbuff.IDGenerator;
import cl.vc.module.protocolbuff.generalstrategy.GeneralStrategy;
import cl.vc.module.protocolbuff.mkd.MarketDataMessage;
import cl.vc.module.protocolbuff.routing.RoutingMessage;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

@Slf4j
@Data
public class StrategyController implements Initializable {

    @Setter
    private Stage stage;

    @FXML private TextField prefix;
    @FXML private TextField symbol;
    @FXML private TextField strategyname;

    @FXML private ComboBox<RoutingMessage.HandlInst> handlInst;

    @FXML private ComboBox<MarketDataMessage.SecurityExchangeMarketData> mkd;
    @FXML private ComboBox<RoutingMessage.SecurityExchangeRouting> routing;

    @FXML private TextField account;

    @FXML private TextField spreadStopLoss;
    @FXML private TextField spreadTakeprofit;
    @FXML private TextField qty;
    @FXML private TextField decPx;

    @FXML private ComboBox<RoutingMessage.SettlType> settlType;
    @FXML private ComboBox<RoutingMessage.Currency> currency;
    @FXML private ComboBox<RoutingMessage.SecurityType> securityType;

    @FXML private Button btnAddStrategy;
    @FXML private Button btnUpdateStrategy;
    @FXML private Button btnDeleteStrategy;
    @FXML private Button btnCloneStrategy;

    private String strategyId;
    private DecimalFormat df;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        df = new DecimalFormat("#.00000", new DecimalFormatSymbols(Locale.US));
        ObservableList<MarketDataMessage.SecurityExchangeMarketData> itemsMarketDataSource = FXCollections.observableArrayList();
        Arrays.stream(MarketDataMessage.SecurityExchangeMarketData.values()).forEach(itemsMarketDataSource::add);

        ObservableList<RoutingMessage.SecurityExchangeRouting> itemsSecurityExchange = FXCollections.observableArrayList();
        Arrays.stream(RoutingMessage.SecurityExchangeRouting.values()).forEach(itemsSecurityExchange::add);

        ObservableList<RoutingMessage.SettlType> settlTypes = FXCollections.observableArrayList();
        settlTypes.remove(RoutingMessage.SettlType.UNRECOGNIZED);
        Arrays.stream(RoutingMessage.SettlType.values()).forEach(settlTypes::add);


        ObservableList<RoutingMessage.HandlInst> handlInsts = FXCollections.observableArrayList();
        settlTypes.remove(RoutingMessage.SettlType.UNRECOGNIZED);
        Arrays.stream(RoutingMessage.HandlInst.values()).forEach(handlInsts::add);


        ObservableList<RoutingMessage.Currency> currencies = FXCollections.observableArrayList();
        currencies.remove(RoutingMessage.Currency.UNRECOGNIZED);
        currencies.remove(RoutingMessage.Currency.NO_CURRENCY);
        Arrays.stream(RoutingMessage.Currency.values()).forEach(currencies::add);

        ObservableList<RoutingMessage.ExStrategy> itemsStrategyType = FXCollections.observableArrayList();
        itemsStrategyType.remove(RoutingMessage.ExStrategy.UNRECOGNIZED);
        Arrays.stream(RoutingMessage.ExStrategy.values()).forEach(itemsStrategyType::add);

        ObservableList<RoutingMessage.SecurityType> securityTypes = FXCollections.observableArrayList();
        securityTypes.remove(RoutingMessage.SecurityType.UNRECOGNIZED);
        Arrays.stream(RoutingMessage.SecurityType.values()).forEach(securityTypes::add);

        this.securityType.setItems(securityTypes);
        securityType.getSelectionModel().selectFirst();

        this.mkd.setItems(itemsMarketDataSource);
        this.settlType.setItems(settlTypes);

        settlType.getSelectionModel().select(RoutingMessage.SettlType.T2);
        this.currency.setItems(currencies);

        this.handlInst.setItems(handlInsts);
        handlInst.getSelectionModel().select(RoutingMessage.HandlInst.ISOLATED_MARGIN);

        currency.getSelectionModel().select(RoutingMessage.Currency.USD);
        this.routing.setItems(itemsSecurityExchange);

        this.btnAddStrategy.setDisable(true);
        this.btnUpdateStrategy.setDisable(true);
        this.btnDeleteStrategy.setDisable(true);

        strategyname.setText("BTC/FDUSD");
        prefix.setText("SCALPING");
        decPx.setText("2");

        mkd.getSelectionModel().select(MarketDataMessage.SecurityExchangeMarketData.BINANCE_MKD);
        routing.getSelectionModel().select(RoutingMessage.SecurityExchangeRouting.BINANCE);
        account.setText("47024924/1");

        symbol.setText("BTC/FDUSD");
        spreadStopLoss.setText("4");
        spreadTakeprofit.setText("3");
        qty.setText("0.001");


    }

    public void showStrategyData(ScalpingStrategyProtos.ScalpingStrategy.Builder strategyView) {
        if (strategyView != null) {
            this.loadDataStrategy(strategyView);
        }
    }

    public void showCloneStrategy(ScalpingStrategyProtos.ScalpingStrategy.Builder strategyView) {
        this.btnAddStrategy.setDisable(false);
        this.loadDataStrategy(strategyView);
    }

    @FXML
    public void addStrategy(ActionEvent event) {
        if (!this.validateDataInput()) {
            ScalpingStrategyProtos.ScalpingStrategy strategy = this.loadStrategyMessage(GeneralStrategy.StatusStrategy.ADD_STRATEGY);
            if (strategy == null) {
                return;
            }
            strategy = strategy.toBuilder().setStrategyId(IDGenerator.getID()).build();
            Repository.getNettyProtobufClient().sendMessage(strategy);
        }
    }

    @FXML
    public void updateStrategy(ActionEvent event) {
        if (!this.validateDataInput()) {
            ScalpingStrategyProtos.ScalpingStrategy strategy = this.loadStrategyMessage(GeneralStrategy.StatusStrategy.UPDATE_STRATEGY);
            if (strategy == null) {
                return;
            }
            Repository.getNettyProtobufClient().sendMessage(strategy);
            this.stage.fireEvent(new WindowEvent(this.stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
    }

    @FXML
    public void cloneStrategy(ActionEvent actionEvent) {
        ScalpingStrategyProtos.ScalpingStrategy strategy = this.loadStrategyMessage(GeneralStrategy.StatusStrategy.ADD_STRATEGY);
        if (strategy == null) {
            return;
        }
        strategy = strategy.toBuilder().setStrategyId(IDGenerator.getID()).build();
        Repository.getNettyProtobufClient().sendMessage(strategy);
        this.stage.fireEvent(new WindowEvent(this.stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    @FXML
    public void deleteStrategy(ActionEvent event) {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Remove Strategy");
        alert.setHeaderText("Do you really want to delete the strategy:" + strategyId);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/SimpleTheme.css").toExternalForm());

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(okButtonType, cancelButtonType);

        ButtonType resultado = alert.showAndWait().orElse(ButtonType.CANCEL);

        if (resultado == okButtonType) {
            ScalpingStrategyProtos.ScalpingStrategy strategy = this.loadStrategyMessage(GeneralStrategy.StatusStrategy.REMOVE_STRATEGY);
            strategy = strategy.toBuilder().setStatusStrategy(GeneralStrategy.StatusStrategy.REMOVE_STRATEGY).build();
            Repository.getNettyProtobufClient().sendMessage(strategy);
            this.stage.fireEvent(new WindowEvent(this.stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        }

    }

    private void loadDataStrategy(ScalpingStrategyProtos.ScalpingStrategy.Builder strategyView) {
        try {

            this.strategyId = strategyView.getStrategyId();
            this.strategyname.setText(strategyView.getStrategyName());
            this.prefix.setText(strategyView.getOrdersPrefix());
            this.mkd.setValue(strategyView.getExchangeSource());

            this.routing.setValue(strategyView.getExchangeDestination());

            this.account.setText(strategyView.getAccount());
            this.settlType.setValue(strategyView.getSettlType());
            this.handlInst.setValue(strategyView.getHandlInst());
            this.currency.setValue(strategyView.getCurrency());
            this.securityType.setValue(strategyView.getSecurityType());
            this.symbol.setText(strategyView.getSymbol());
            this.spreadStopLoss.setText(String.valueOf(strategyView.getSpreadStoploss()));
            this.spreadTakeprofit.setText(String.valueOf(strategyView.getSpreadTakeprofit()));
            this.decPx.setText(String.valueOf(strategyView.getDecimalPx()));
            this.qty.setText(df.format(strategyView.getQty()));


        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    private ScalpingStrategyProtos.ScalpingStrategy loadStrategyMessage(GeneralStrategy.StatusStrategy statusstrategy) {

        ScalpingStrategyProtos.ScalpingStrategy.Builder builder = ScalpingStrategyProtos.ScalpingStrategy.newBuilder();

        try {

            if (this.strategyId != null && !this.strategyId.isEmpty()) {
                builder.setStrategyId(this.strategyId);
            } else {
                builder.setStrategyId(IDGenerator.getID());
            }

            builder.setStatusStrategy(statusstrategy);
            builder.setStrategyName(this.strategyname.getText());

            if (!this.prefix.equals("")) {
                builder.setOrdersPrefix(this.prefix.getText());
            }

            builder.setStrategyName(this.strategyname.getText());
            builder.setSettlType(this.settlType.getSelectionModel().getSelectedItem());
            builder.setCurrency(currency.getSelectionModel().getSelectedItem());
            builder.setSecurityType(securityType.getSelectionModel().getSelectedItem());
            builder.setExchangeSource(this.mkd.getValue());
            builder.setExchangeDestination(this.routing.getValue());
            builder.setAccount(this.account.getText());
            builder.setSymbol(this.symbol.getText());
            builder.setSpreadStoploss(Double.valueOf(this.spreadStopLoss.getText()));
            builder.setSpreadTakeprofit(Double.valueOf(this.spreadTakeprofit.getText()));
            builder.setQty(Double.valueOf(this.qty.getText()));
            builder.setDecimalPx(Integer.valueOf(this.decPx.getText()));
            builder.setDecimalPx(Integer.valueOf(this.decPx.getText()));
            builder.setHandlInst(this.handlInst.getValue());

        } catch (Exception e) {
            Notifier.notifyError("Error update strategy", e.getMessage());
        }

        return builder.build();
    }

    private boolean validateDataInput() {
        boolean requerido = false;
        if (requerido) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.initOwner(this.stage);

            alert.show();
        }
        return requerido;
    }

    private RoutingMessage.ExStrategy getStrategyType(StringProperty name) {
        int index = RoutingMessage.ExStrategy.valueOf(name.getValue()).getNumber();
        return RoutingMessage.ExStrategy.forNumber(index);
    }


}

