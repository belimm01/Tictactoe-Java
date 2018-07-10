package com.tictactoe.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tictactoe.common.MessageBean;
import com.tictactoe.common.MoveBean;
import com.tictactoe.common.RoomBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client {

    private static String SERVER_ADDRESS = "192.168.73.106";//lokalni adressa, pred zapnutim musime zkontrolovat ji
    private static int PORT = 8901;//pomosi portu klient muze se pripojit na server
    private Socket socket;

    private BufferedReader in;//nacita
    private PrintWriter out;//posila
    private Session session;

    private static ObjectMapper objectMapper = new ObjectMapper();//Json

    private static Client instance = new Client();
    private Object games;

    //Pomoci konstruktoru klient se pripoji na server
    private Client() {
        try {
            socket = new Socket(SERVER_ADDRESS, PORT);//vytvari novy soket
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));//prijma data
            out = new PrintWriter(socket.getOutputStream(), true);//posila data
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Singleton
    public static Client getInstance() {
        return instance;
    }

    //Vytvarim novy pokoj
    public String createRoom(RoomBean room) throws Exception {
        out.println(MessageBean.msgToString("createRoom", room));
        MessageBean message = MessageBean.stringToMsg(in.readLine());//nacita data 
        if (message.getMessage().equals("error")) {//jestli msg se rovna error
            throw new MyException(message.getData());//nastane tato akce, vyhody vyjimku 
        }
        return message.getData();//vrace data jako retezec
    }

    //Pripojim se k pokoju
    public String connectToRoom(String roomName) throws Exception {
        out.println(MessageBean.msgToString("connectToRoom", roomName));//posila msg v konkretni room
        MessageBean message = MessageBean.stringToMsg(in.readLine());//nacita data 
        if (message.getMessage().equals("error")) {//jestli msg se rovna error
            throw new MyException(message.getData());//nastane tato akce, vyhody vyjimku 
        }
        return message.getData();
    }

    //Tady jsou vsechny vytvorine pokoji
    public Map<String, RoomBean> getRooms() throws IOException {
        out.println(MessageBean.msgToString("getRooms", null));
        MessageBean message = MessageBean.stringToMsg(in.readLine());//prijma retezec 
        return objectMapper.readValue(message.getData(), new TypeReference<Map<String, RoomBean>>() {//konvertuje Json v msg
        });
    }

    public RoomBean getRoom(String currentRoom) throws IOException {
        out.println(MessageBean.msgToString("getRoom", currentRoom));
        MessageBean message = MessageBean.stringToMsg(in.readLine());//nacitame retezec 
        return objectMapper.readValue(message.getData(), RoomBean.class);//konvertuje Json v msg
    }

    //Odpojim se
    public void disconnect(String roomName) throws JsonProcessingException {
        out.println(MessageBean.msgToString("disconnect", roomName));
    }

    //Znicim pokoj
    public void destroyRoom(String roomName) throws JsonProcessingException {
        out.println(MessageBean.msgToString("destroyRoom", roomName));
    }

    //Nastartujeme hru
    public void startGame(String currentRoom) throws JsonProcessingException {
        out.println(MessageBean.msgToString("startGame", currentRoom));
    }

    public void move(String room, int row, int col) throws Exception {
        Map<String, String> params = new HashMap<>();//vytvarim kolekce 
        params.put("room", room);//vkladam do kolekce skutecny pokoj
        params.put("move", objectMapper.writeValueAsString(new MoveBean(row, col)));//vkladam do kolekce herni tah
        out.println(MessageBean.msgToString("move", params));
    }

    //vrace session hracu
    public Session getSession() {
        return session;
    }

    //nastavi session 
    public void setSession(Session session) {
        this.session = session;
    }

    public BufferedReader getIn() {
        return in;
    }

    //vrace data 
    public List<HashMap<String, String>> getGames() throws IOException {
        out.println(MessageBean.msgToString("getGames", null));
        //Pomoci Json nacita data (PRIJMA)
        MessageBean message = MessageBean.stringToMsg(in.readLine());
        return objectMapper.readValue(message.getData(), new TypeReference<List<HashMap<String, String>>>() {
        });
    }
}
