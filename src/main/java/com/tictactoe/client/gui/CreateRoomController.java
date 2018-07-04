package com.tictactoe.client.gui;

import com.tictactoe.client.Client;
import com.tictactoe.client.Session;
import com.tictactoe.client.enums.GridSize;
import com.tictactoe.client.enums.PlayersNumber;
import com.tictactoe.common.RoomBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateRoomController {
    @FXML
    private TextField name;
    @FXML
    private ChoiceBox<PlayersNumber> maxPlayersNumber;
    @FXML
    private ChoiceBox<GridSize> gridSize;

    
    //Vytvorime room a zobrazim ji 
    @FXML
    private void createRoom(ActionEvent actionEvent) throws Exception {
        RoomBean room = new RoomBean();
        room.setName(name.getText());//jmeno room
        room.setGridSize(gridSize.getSelectionModel().getSelectedItem().getSize());//nastavim velikost
        room.setTargetScore(gridSize.getSelectionModel().getSelectedItem().getTargetScore());//nastavim score
        room.setMaxPlayersNumber(maxPlayersNumber.getSelectionModel().getSelectedItem().getCount());//nastavim pocet hracu
        String mark = Client.getInstance().createRoom(room);//nahradim hrace nejakym symbolem
        Client.getInstance().setSession(new Session());//Vytvarim nove zasedani
        Client.getInstance().getSession().setCurrentRoom(room.getName());//
        Client.getInstance().getSession().setMark(mark);//kazdemu klientu nahrazim libovolny symbol
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();//otevri okinko 
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("roomHost.fxml"))));
        stage.setTitle("You: (" + Client.getInstance().getSession().getMark() + ")");//vypise vsech aktualnich klientu v pokoji
    }

    //Tlacitko back a Listener
    @FXML
    private void back(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();//zobrazi tlacitko
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("play.fxml"))));//odkaz na fxml
        stage.setTitle("");
    }
}
