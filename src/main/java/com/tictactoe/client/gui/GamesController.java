package com.tictactoe.client.gui;

import com.tictactoe.client.Client;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class GamesController {
    @FXML
    private ListView games;

    //vypise game v ListView (Game tlacitko)
    @FXML
    private void initialize() throws IOException {
        List<HashMap<String, String>> games = Client.getInstance().getGames();

        this.games.setItems(FXCollections.observableArrayList(
                games.stream()
                        .map(g -> g.get("room") + " status: " + g.get("gameStatus") + " winner: " + g.get("winner") + "\n"
                                + g.get("grid") + "Date:" + g.get("date"))
                        .collect(Collectors.toList())
        ));
    }

    @FXML
    private void back(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("main.fxml"))));
    }
}
