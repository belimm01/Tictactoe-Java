package com.tictactoe.client.gui;

import com.tictactoe.client.Client;
import com.tictactoe.common.PlayerBean;
import com.tictactoe.common.RoomBean;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.ScheduledService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class RoomController {
    @FXML
    private AnchorPane pane;
    @FXML
    private Label yourMark;
    @FXML
    private Label waitForPlayers;
    @FXML
    private ListView<PlayerBean> players;

    protected ScheduledService<Void> refreshPlayersListService;

    @FXML
    private void initialize() {
        players.setCellFactory(param -> new ListCell<PlayerBean>() {
            @Override
            protected void updateItem(PlayerBean item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getMark());
                }
            }
        });

        yourMark.setText("Your mark: " + Client.getInstance().getSession().getMark());

        refreshPlayersListService = Scheduler.schedule(this::refreshPlayersList, 100);
    }
    //obnovi list hracu 
    protected void refreshPlayersList() {
        Platform.runLater(() -> {
            if (!refreshPlayersListService.isRunning())
                return;
            try {
                RoomBean room = Client.getInstance().getRoom(Client.getInstance().getSession().getCurrentRoom());

                if (room == null) {
                    Stage stage = (Stage) pane.getScene().getWindow();
                    switchScene(stage, "play.fxml");
                    stage.setTitle("");

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);

                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Disconnected");

                    alert.showAndWait();
                    return;
                }
                if (room.getGameBean() != null) {
                    Stage stage = (Stage) pane.getScene().getWindow();
                    switchScene(stage, "grid.fxml");
                }
                waitForPlayers.setText("Wait for players: " + room.getPlayerBeans().size() + " / " + room.getMaxPlayersNumber());
                players.setItems(FXCollections.observableArrayList(room.getPlayerBeans()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void disconnect(ActionEvent actionEvent) throws IOException {

        Client.getInstance().disconnect(Client.getInstance().getSession().getCurrentRoom());
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        switchScene(stage, "play.fxml");
        stage.setTitle("");
    }

    protected void switchScene(Stage stage, String scene) throws IOException {
        refreshPlayersListService.cancel();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource(scene))));
    }
}
