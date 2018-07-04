package com.tictactoe.client.gui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tictactoe.client.Client;
import com.tictactoe.common.MarkBean;
import com.tictactoe.common.MessageBean;
import com.tictactoe.common.RoomBean;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.*;

public class GridController extends Application {
    @FXML
    private AnchorPane pane;
    @FXML
    private GridPane grid;
    private ObjectMapper objectMapper = new ObjectMapper();
    private Thread listener;

    //Nahlasi kdo je zvitezi a kdo prohra
    public GridController() {
        listener = new Thread(() -> {
            loop:
            while (true) {
                MessageBean msg;
                try {
                    msg = MessageBean.stringToMsg(Client.getInstance().getIn().readLine());//Dostavam string JSON i convertuju v Object msg Bean
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                switch (msg.getMessage()) {
                    case "validMove":
                        try {
                            validMove(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "opponentMoved":
                        try {
                            opponentMoved(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "finish":
                        if (Objects.equals(msg.getData(), "draw")) {
                            alert("Draw!");
                        }
                        if (Objects.equals(msg.getData(), "victory")) {
                            alert("You win! (" + Client.getInstance().getSession().getMark() + ")");
                        }
                        if (Objects.equals(msg.getData(), "defeat")) {
                            alert("You lose! (" + Client.getInstance().getSession().getMark() + ")");
                        }
                        listener.interrupt();
                        try {
                            Client.getInstance().disconnect(Client.getInstance().getSession().getCurrentRoom());
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                        String currentRoom = Client.getInstance().getSession().getCurrentRoom();
                        try {
                            if (Client.getInstance().getRoom(currentRoom).getPlayerBeans().size() == 0) {
                                Client.getInstance().destroyRoom(currentRoom);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Platform.runLater(() -> {//Puste vlakno v GUI

                            Stage stage = (Stage) pane.getScene().getWindow();
                            try {
                                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("play.fxml"))));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        break loop;
                    case "message":
                        alert(msg.getData());
                        break;
                }
            }
        });
    }

    //nastartuje pole pro hracu
    @Override
    public void start(Stage stage) throws Exception {
        Scene mainScene = new Scene(FXMLLoader.load(getClass().getResource("grid.fxml")));
        stage.setScene(mainScene);
        stage.show();
    }

    @FXML
    private void initialize() throws IOException {
        RoomBean room = Client.getInstance().getRoom(Client.getInstance().getSession().getCurrentRoom());
        int gridSize = room.getGridSize();
        //vytvari tlacitka 
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Button button = new Square();
                button.prefWidthProperty().bind(pane.widthProperty());
                button.prefHeightProperty().bind(pane.heightProperty());
                int row = j;
                int col = i;
                //pridava ActionListener
                button.setOnAction(event -> {
                    try {
                        Client.getInstance().move(Client.getInstance().getSession().getCurrentRoom(), row, col);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                grid.add(button, i, j);
            }
        }
        listener.start();
    }

    //pracuje jako uvedomeny
    private void alert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Information (" + Client.getInstance().getSession().getMark() + "");
            alert.setHeaderText(null);

            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    //kdyz souper dela tah
    private void opponentMoved(MessageBean msg) throws IOException {
        Map<String, String> res = objectMapper.readValue(msg.getData(), new TypeReference<Map<String, String>>() {
        });
        Platform.runLater(() -> {
            for (Node node : grid.getChildren()) {
                if (Objects.equals(GridPane.getRowIndex(node), Integer.parseInt(res.get("row")))
                        && Objects.equals(GridPane.getColumnIndex(node), Integer.parseInt(res.get("col")))) {

                    ((Square) node).setMark(res.get("mark"));
                    break;
                }
            }
        });

    }
    //kontroluje policko (ctvrec) 
    private void validMove(MessageBean msg) throws IOException {
        Map<String, Integer> loc = objectMapper.readValue(msg.getData(), new TypeReference<Map<String, Integer>>() {
        });
        Platform.runLater(() -> {
            for (Node node : grid.getChildren()) {
                if (Objects.equals(GridPane.getRowIndex(node), loc.get("row"))
                        && Objects.equals(GridPane.getColumnIndex(node), loc.get("col"))) {

                    ((Square) node).setMark(Client.getInstance().getSession().getMark());

                    break;
                }
            }
        });
    }
    
    static class Square extends Button {
        private final Map<String, Image> images = new HashMap<>();
        private String mark;

        Square() {
            for (MarkBean m : MarkBean.values()) {
                images.put(m.getMark(), new Image(Client.class.getResourceAsStream("mark" + m.getMark() + ".png")));
            }
        }
        //vrace symbol
        public String getMark() {
            return mark;
        }
        //nastavi znak
        public void setMark(String mark) {
            this.mark = mark;
            this.setGraphic(new ImageView(images.get(mark)));
        }
    }
}