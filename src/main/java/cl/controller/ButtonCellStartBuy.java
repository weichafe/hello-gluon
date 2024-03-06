package cl.controller;

import cl.vc.algos.scalping.proto.ScalpingStrategyProtos;
import cl.Repository;
import cl.vc.module.protocolbuff.generalstrategy.GeneralStrategy;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

public class ButtonCellStartBuy extends TableCell<ScalpingStrategyProtos.ScalpingStrategy.Builder, Boolean> {

    final Button cellButtonStart = new Button();
    final Button cellButtonStop = new Button();

    public ButtonCellStartBuy(String start, String stop) {

        try {
            this.cellButtonStart.setText(start);
            this.cellButtonStart.setPrefWidth(80);
            this.cellButtonStart.getStyleClass().add("button-start");
            this.cellButtonStart.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    ScalpingStrategyProtos.ScalpingStrategy.Builder strategyView = (ScalpingStrategyProtos.ScalpingStrategy.Builder) getTableRow().getItem();
                    GeneralStrategy.OperationsControl operationsControl =
                            GeneralStrategy.OperationsControl
                                    .newBuilder()
                                    .setStrategyId(strategyView.getStrategyId())
                                    .setUsername(Repository.getUsername())
                                    .setTimestamp(System.currentTimeMillis())
                                    .setOperationControl(GeneralStrategy.OperationsControlEnum.START_INFLOW).build();
                    Repository.getNettyProtobufClient().sendMessage(operationsControl);

                }
            });

            this.cellButtonStop.setText(stop);
            this.cellButtonStop.setPrefWidth(80);
            this.cellButtonStop.getStyleClass().add("button-stop");
            this.cellButtonStop.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    ScalpingStrategyProtos.ScalpingStrategy.Builder strategyView = (ScalpingStrategyProtos.ScalpingStrategy.Builder) getTableRow().getItem();
                    GeneralStrategy.OperationsControl operationsControl =
                            GeneralStrategy.OperationsControl
                                    .newBuilder()
                                    .setStrategyId(strategyView.getStrategyId())
                                    .setUsername(Repository.getUsername())
                                    .setTimestamp(System.currentTimeMillis())
                                    .setOperationControl(GeneralStrategy.OperationsControlEnum.STOP_INFLOW).build();
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


            if (strategyView.getScalpingStrategyStatus().getSubscribed()) {
                cellButtonStop.setDisable(false);
                cellButtonStart.setDisable(false);
            } else {
                cellButtonStop.setDisable(true);
                cellButtonStart.setDisable(true);
            }

            if (strategyView.getScalpingStrategyStatus().getStartBuy()) {
                setGraphic(this.cellButtonStop);
            } else {
                setGraphic(this.cellButtonStart);
            }

            setAlignment(javafx.geometry.Pos.CENTER);

        } else {
            setGraphic(null);
        }
    }

}
