package com.citywar.dice.entity;

import java.sql.Timestamp;


public class PengGame {
    private Integer id;

    private Integer roomid;

    private Timestamp gamedate;

    private String playerlist;

    private String namelist;

    private String scorelist;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoomid() {
        return roomid;
    }

    public void setRoomid(Integer roomid) {
        this.roomid = roomid;
    }

    public Timestamp getGamedate() {
        return gamedate;
    }

    public void setGamedate(Timestamp gamedate) {
        this.gamedate = gamedate;
    }

    public String getPlayerlist() {
        return playerlist;
    }

    public void setPlayerlist(String playerlist) {
        this.playerlist = playerlist == null ? null : playerlist.trim();
    }

    public String getNamelist() {
        return namelist;
    }

    public void setNamelist(String namelist) {
        this.namelist = namelist == null ? null : namelist.trim();
    }

    public String getScorelist() {
        return scorelist;
    }

    public void setScorelist(String scorelist) {
        this.scorelist = scorelist == null ? null : scorelist.trim();
    }
}