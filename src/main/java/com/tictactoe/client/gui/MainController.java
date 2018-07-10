package com.tictactoe.client.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainController extends Application {
    public static void main(String[] args) {
        MainController.launch(args);
    }

    //zobrazi hlavni okinko
    @Override
    public void start(Stage stage) throws Exception {
        Scene mainScene = new Scene(FXMLLoader.load(getClass().getResource("main.fxml")));
        stage.setScene(mainScene);
        stage.show();
    }
    //prida tlacitko na okinko
    @FXML
    private void exit(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
     //prida tlacitko na okinko a pri stisknuti tlacitka zobrazi dalsi fxml
    @FXML
    private void play(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("play.fxml"))));
    }
     //prida tlacitko na okinko a pri stisknuti tlacitka zobrazi dalsi fxml
    @FXML
    private void games(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("games.fxml"))));
    }

}
