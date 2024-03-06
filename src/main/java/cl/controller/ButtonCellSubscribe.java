package cl.controller;

import cl.vc.algos.scalping.proto.ScalpingStrategyProtos;
import cl.Repository;
import cl.vc.module.protocolbuff.generalstrategy.GeneralStrategy;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;


public class ButtonCellSubscribe extends TableCell<ScalpingStrategyProtos.ScalpingStrategy.Builder, Boolean> {

    final Button cellButtonSubscribe = new Button();
    final Button cellButtonUnsubscribe = new Button();
    private boolean disable;

    public ButtonCellSubscribe(String subscribe, String unsubscribe) {
        try {
            cellButtonSubscribe.setText(subscribe);
            cellButtonSubscribe.setPrefWidth(120);

            cellButtonSubscribe.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {

                    ScalpingStrategyProtos.ScalpingStrategy.Builder strategyView = (ScalpingStrategyProtos.ScalpingStrategy.Builder) getTableRow().getItem();

                    GeneralStrategy.OperationsControl operationsControl =
                            GeneralStrategy.OperationsControl
                                    .newBuilder()
                                    .setStrategyId(strategyView.getStrategyId())
                                    .setUsername(Repository.getUsername())
                                    .setTimestamp(System.currentTimeMillis())
                                    .setOperationControl(GeneralStrategy.OperationsControlEnum.SUBSCRIBE).build();

                    Repository.getNettyProtobufClient().sendMessage(operationsControl);
                }
            });

            cellButtonUnsubscribe.setText(unsubscribe);
            cellButtonUnsubscribe.setPrefWidth(120);

            cellButtonUnsubscribe.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    ScalpingStrategyProtos.ScalpingStrategy.Builder strategyView = (ScalpingStrategyProtos.ScalpingStrategy.Builder) getTableRow().getItem();
                    GeneralStrategy.OperationsControl operationsControl =
                            GeneralStrategy.OperationsControl
                                    .newBuilder()
                                    .setStrategyId(strategyView.getStrategyId())
                                    .setUsername(Repository.getUsername())
                                    .setTimestamp(System.currentTimeMillis())
                                    .setOperationControl(GeneralStrategy.OperationsControlEnum.UNSUBSCRIBE).build();

                    Repository.getNettyProtobufClient().sendMessage(operationsControl);
                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    protected void updateItem(Boolean t, boolean empty) {
        super.updateItem(t, empty);
        if (!empty) {

            ScalpingStrategyProtos.ScalpingStrategy.Builder strategyView = (ScalpingStrategyProtos.ScalpingStrategy.Builder) getTableView().getItems().get(getIndex());


            if (strategyView.getScalpingStrategyStatus().getStartBuy() || strategyView.getScalpingStrategyStatus().getStartSell()) {
                this.cellButtonSubscribe.setDisable(true);
                this.cellButtonUnsubscribe.setDisable(true);
            } else {
                this.cellButtonSubscribe.setDisable(false);
                this.cellButtonUnsubscribe.setDisable(false);
            }

            if (strategyView.getScalpingStrategyStatus().getSubscribed()) {
                setGraphic(cellButtonUnsubscribe);
            } else {
                setGraphic(cellButtonSubscribe);
            }

            setAlignment(javafx.geometry.Pos.CENTER);

        } else {
            setGraphic(null);
        }
    }
}
