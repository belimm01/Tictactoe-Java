package com.tictactoe.server.online;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tictactoe.common.MessageBean;
import com.tictactoe.common.MoveBean;
import com.tictactoe.server.game.GameStatus;
import com.tictactoe.server.game.Player;
import org.bson.Document;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.time.Instant;
import java.util.*;

public class Game extends com.tictactoe.server.game.Game {
    private String roomName;

    public Game(String roomName, int size, int targetScore, List<Player> players) {
        super(size, targetScore, players);//vyvolam konstruktor parant tridy 
        this.roomName = roomName;
    }

    private Integer currIndex = 0;

    //nasledujici tah
    public void nextMove(MoveBean move, OnlinePlayer onlinePlayer, PrintWriter out) throws IOException {

        Player currPlayer = players.get(currIndex);//vrace index hracu
        if (!Objects.equals(onlinePlayer.getMark(), currPlayer.getMark())) {//jestli skutecny hrac se nerovna online hracu
            out.println(MessageBean.msgToString("message", "Not your turn"));//vypise , ze neni tvuj herni tah
            return;
        }
        if (!validateMove(move, currPlayer, out))
            return;//jestli neni mozny zadny tah

        //fronta hracu
        currIndex++;
        if (currIndex >= players.size()) {
            currIndex = 0;
        }

        if (!updateGameStatus(out)) {
            return;
        }
        //posila vsem hracu informace 
        for (Player p : players) {
            if (p != currPlayer) {
                PrintWriter _out = new PrintWriter(((OnlinePlayer) p).getSocket().getOutputStream(), true);
                Map<String, String> res = new HashMap<>();
                res.put("col", String.valueOf(move.getCol()));
                res.put("row", String.valueOf(move.getRow()));
                res.put("mark", currPlayer.getMark());
                _out.println(MessageBean.msgToString("opponentMoved", res));
            }
        }
    }
    
    private boolean updateGameStatus(PrintWriter out) throws IOException {
        GameStatus gameStatus = this.calcGameStatus();

        if (gameStatus != GameStatus.CONTINUE) {//pokud hra bezi
            for (Player p : players) {
                PrintWriter _out = new PrintWriter(((OnlinePlayer) p).getSocket().getOutputStream(), true);
                if (gameStatus == GameStatus.VICTORY) {//jestli nekdo vyhral
                    if (Objects.equals(p.getMark(), winner)) {
                        _out.println(MessageBean.msgToString("finish", "victory"));
                    } else {
                        _out.println(MessageBean.msgToString("finish", "defeat"));
                    }
                }
                if (gameStatus == GameStatus.DRAW) {
                    _out.println(MessageBean.msgToString("finish", "draw"));
                }

            }
            //vytvari databaze a pridavam data ze hry 
            MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");
            MongoClient mongoClient = new MongoClient(connectionString);


            MongoDatabase database = mongoClient.getDatabase("tictactoe");

            MongoCollection<Document> collection = database.getCollection("results");//vypise vsechny vysledky do GUI

            Document doc = new Document("room", roomName)
                    .append("winner", winner)
                    .append("gameStatus", gameStatus.name())
                    .append("grid", printGrid())
                    .append("date", Date.from(Instant.now()));//pridavam do databaze data

            collection.insertOne(doc);
            return false;
        }
        return true;
    }

    //kontroluje jestli je ten tah mozny
    private boolean validateMove(MoveBean move, Player player, PrintWriter out)
            throws JsonProcessingException {
        if (move == null) {
            out.println(MessageBean.msgToString("message", "No more moves"));
            return false;
        }

        if (!this.makeMove(player.getMark(), move.getRow(), move.getCol())) {//kontroluje jestli je ten tah mozny 
            out.println(MessageBean.msgToString("message", "Invalid move"));
            return false;
        }
        Map<String, Integer> loc = new HashMap<>();
        loc.put("col", move.getCol());
        loc.put("row", move.getRow());
        out.println(MessageBean.msgToString("validMove", loc));
        return true;
    }
}
