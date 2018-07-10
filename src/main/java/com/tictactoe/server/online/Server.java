package com.tictactoe.server.online;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tictactoe.common.MessageBean;
import com.tictactoe.common.MoveBean;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Server {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String, Room> rooms = new HashMap<>();


    public static void main(String[] args) throws Exception {
        System.out.println("Tic Tac Toe Server is Running");
        try (ServerSocket listener = new ServerSocket(8901)) {//pomoci soketu se pripojeme k portu 
            while (true) {
                Socket socket = listener.accept();// soket akceprovan
                new Thread(() -> {//vytvarim vlakno
                    try {
                        BufferedReader input = new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));//soket dostava data od Clienta 
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);//posilam data Clientu
                        while (true) {
                            MessageBean msg = MessageBean.stringToMsg(input.readLine());//vytvarim msg a nacte co jsme zadaly
                            switch (msg.getMessage()) {
                                case "createRoom": {//jestli jsme vybraly Create Room
                                    Room room = objectMapper.readValue(msg.getData(), Room.class);//vytvarim room
                                    OnlinePlayer onlinePlayer = new OnlinePlayer(socket, room.generateAvailableMark(), room.getGridSize());//vytvarim playera , ktery bude moct se pripojet k serveru a bude mit svuj unikatny symbol
                                    room.getPlayers().add(onlinePlayer);//pridavam noveho hrace do kolekce
                                    rooms.put(room.getName(), room);//pridavam do room hrace
                                    out.println(MessageBean.msgToString("success", onlinePlayer.getMark()));//nahrad playera nejakym symbolem 
                                    break;
                                }
                                case "connectToRoom": {//jestli jsme vybraly Connect to Room
                                    Room r = rooms.get(msg.getData());//pripojim se k room pomoci ID
                                    if (r.getPlayers().size() < r.getMaxPlayersNumber()) {//jestli pocet playeru je min nez vetsi pocet playeru
                                        OnlinePlayer onlinePlayer = new OnlinePlayer(socket, r.generateAvailableMark(), r.getGridSize());//vytvarim playera , ktery bude moct se pripojet k serveru a bude mit svuj unikatny symbol
                                        r.getPlayers().add(onlinePlayer);//pridavam do  room hrace
                                        out.println(MessageBean.msgToString("success", onlinePlayer.getMark()));//nahrad playera nejakym symbolem
                                    } else {
                                        out.println(MessageBean.msgToString("error", "Max players limit"));//mazimalni pocet hracu 
                                    }
                                    break;
                                }
                                case "disconnect": {//jestli jsme vybraly Disconnect
                                    Room r = rooms.get(msg.getData());//pripojim se k room pomoci ID
                                    OnlinePlayer onlinePlayer = r.getPlayers().stream().filter(p -> p.getSocket().equals(socket)).findFirst().get();//filtruju hracu podle soketu
                                    r.getPlayers().remove(onlinePlayer);//odebiram od room hrace
                                    r.enableMark(onlinePlayer.getMark());//uvolnuje unikatny symbol
                                    break;
                                }
                                case "destroyRoom": {//jestli jsme vybraly DestroyRoom
                                    Room r = rooms.get(msg.getData());//pripojim se k room pomoci ID
                                    if (r != null) {
                                        rooms.remove(r.getName());//zmaze room podle nazvy
                                    }
                                    break;
                                }
                                case "getRoom": {
                                    Room r = rooms.get(msg.getData());//pripojim se k room pomoci ID
                                    out.println(MessageBean.msgToString("result", r));//vrace room
                                    break;
                                }
                                case "getRooms":
                                    out.println(MessageBean.msgToString("result", rooms));//vrace vsechny room
                                    break;
                                case "startGame": {
                                    Room r = rooms.get(msg.getData());//pripojim se k room pomoci ID
                                    r.startGame();//nastartujeme ve skucnym room hru
                                    break;
                                }
                                case "move": {
                                    Map<String, String> o = objectMapper.readValue(msg.getData(), new TypeReference<Map<String, String>>() {//retezec konvertujem v Object
                                    });
                                    Room r = rooms.get(o.get("room"));//vrace room
                                    MoveBean moveBean = objectMapper.readValue(o.get("move"), MoveBean.class);

                                    OnlinePlayer onlinePlayer = r.getPlayers().stream().filter(p -> p.getSocket().equals(socket)).findFirst().get();

                                    r.getGame().nextMove(moveBean, onlinePlayer, out);

                                    break;
                                }
                                case "getGames": {
                                    MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");
                                    MongoClient mongoClient = new MongoClient(connectionString);

                                    MongoDatabase database = mongoClient.getDatabase("tictactoe");

                                    //vypise vse hry k Client a pak GUI
                                    MongoCollection<Document> collection = database.getCollection("results");
                                    List<Object> games = new LinkedList<>();
                                    for (Document doc : collection.find()) {
                                        Map<String, String> game = new HashMap<>();
                                        game.put("room", doc.get("room").toString());
                                        game.put("winner", doc.get("winner") != null ? doc.get("winner").toString() : null);
                                        game.put("gameStatus", doc.get("gameStatus").toString());
                                        game.put("grid", doc.get("grid").toString());
                                        game.put("date", doc.get("date").toString());
                                        games.add(game);
                                    }
                                    out.println(MessageBean.msgToString("result", games));
                                    break;
                                }
                                case "quit":
                                    return;
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }).start();
            }
        }
    }
}
