package com.tictactoe.client.gui;

import com.tictactoe.client.Client;
import com.tictactoe.client.MyException;
import com.tictactoe.client.Session;
import com.tictactoe.common.RoomBean;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.ScheduledService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

public class PlayController {
    public AnchorPane pane;
    @FXML
    private ListView<RoomBean> rooms;

    
    //vypise v ListView vytvoreny vsechny room
    @FXML
    private void initialize() {
        rooms.setCellFactory(param -> new ListCell<RoomBean>() {
            @Override
            protected void updateItem(RoomBean item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + " | Players: " + item.getPlayerBeans().size() + "/" + item.getMaxPlayersNumber() + "| Field size: " + item.getGridSize());
                }
            }
        });
        //pri kliknuti 2 krat dostanes do room
        rooms.setOnMouseClicked(click -> {
            if (click.getClickCount() == 2) {
                RoomBean currentItemSelected = rooms.getSelectionModel()
                        .getSelectedItem();
                String res;
                try {
                    res = Client.getInstance().connectToRoom(currentItemSelected.getName());
                    Client.getInstance().setSession(new Session());
                    Client.getInstance().getSession().setCurrentRoom(currentItemSelected.getName());
                    Client.getInstance().getSession().setMark(res);
                    Stage stage = (Stage) ((Node) click.getSource()).getScene().getWindow();
                    switchScene(stage, "room.fxml");

                    stage.setTitle("You: (" + Client.getInstance().getSession().getMark() + ")");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (MyException e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);

                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText(e.getMessage());

                    alert.showAndWait();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    //zobrazi okinko createRoom
    @FXML
    private void createRoom(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        switchScene(stage, "createRoom.fxml");
    }
    //pri tlacitko back zobrazi main.fxml
    @FXML
    private void back(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        switchScene(stage, "main.fxml");
    }

    //obnovi list vytvorenych room
    @FXML
    private void refresh(ActionEvent actionEvent) throws IOException {
        refreshRoomsList();
    }

   //obnovi list vytvorenych room
    private void refreshRoomsList() {
        Platform.runLater(() -> {
            try {
                Map<String, RoomBean> _rooms = null;
                _rooms = Client.getInstance().getRooms();
                rooms.setItems(FXCollections.observableArrayList(_rooms.values()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    //prepne okinko
    protected void switchScene(Stage stage, String scene) throws IOException {
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource(scene))));
    }
}
