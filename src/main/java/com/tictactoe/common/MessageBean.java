package com.tictactoe.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class MessageBean {
    private String message;
    private String data;

    //vrace zpravu
    public String getMessage() {
        return message;
    }
    //nastavi zpravu
    public void setMessage(String message) {
        this.message = message;
    }
    //vrace data
    public String getData() {
        return data;
    }
    //nastavi data
    public void setData(String data) {
        this.data = data;
    }
    //konvertuje zpravu v retezec a vrace ji 
    public static String msgToString(String message, Object data) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        MessageBean msg = new MessageBean();//vytvarim novy msg
        msg.message = message;
        if (data instanceof String) {
            msg.data = data.toString();//jestli data je retezec , nacte do msg.data 
        } else {
            msg.data = objectMapper.writeValueAsString(data);//jestli ze ne , nacte jako Object 
        }
        return objectMapper.writeValueAsString(msg);
    }
    //konvertuje retez v zpravu(Object) a vrace ji
    public static MessageBean stringToMsg(String message) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();//vytvarim object 
        return objectMapper.readValue(message, MessageBean.class);//konvertujeme a vracem msg
    }
}
