package com.dz.abdallah.mychat;

import java.sql.Time;
import java.util.Date;

/**
 * Created by Abdallah on 24/12/2017.
 */

public class Message {

    private String User;
    private String Msg;
    private Long time;

    public Message(String hello, String abdallah, long v) {
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }



    public Message(String user, String msg) {

        User = user;
        Msg = msg;
        this.time = new Date().getTime();

    }


}
