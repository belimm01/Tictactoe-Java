package com.tictactoe.client.gui;

import com.tictactoe.client.Client;
import com.tictactoe.common.RoomBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class RoomHostController extends RoomController {
    @FXML
    private Button start;
    //ActionListener, spuste play.fxml kdyz stisknes exit
    @FXML
    private void exit(ActionEvent actionEvent) throws IOException {
        refreshPlayersListService.cancel();
        Client.getInstance().destroyRoom(Client.getInstance().getSession().getCurrentRoom());
        Client.getInstance().setSession(null);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("play.fxml"))));
        stage.setTitle("");
    }

    @Override
    protected void refreshPlayersList() {
        super.refreshPlayersList();
        RoomBean room = null;
        try {
            room = Client.getInstance().getRoom(Client.getInstance().getSession().getCurrentRoom());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (room != null && room.getPlayerBeans().size() == room.getMaxPlayersNumber()) {
            start.setDisable(false);
        }
    }
    //ActionListener zobrazi pole 
    @FXML
    private void start(ActionEvent actionEvent) throws IOException {

        try {
            Client.getInstance().startGame(Client.getInstance().getSession().getCurrentRoom());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        switchScene(stage, "grid.fxml");
    }
}
