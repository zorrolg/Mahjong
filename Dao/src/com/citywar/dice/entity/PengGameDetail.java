package com.citywar.dice.entity;

import java.sql.Timestamp;


public class PengGameDetail {
    private Integer penggameid;

    private Integer pengid;

    private Timestamp gamedate;

    private String playerlist;

    private String namelist;

    private String gamelist;

    public Integer getPenggameid() {
        return penggameid;
    }

    public void setPenggameid(Integer penggameid) {
        this.penggameid = penggameid;
    }

    public Integer getPengid() {
        return pengid;
    }

    public void setPengid(Integer pengid) {
        this.pengid = pengid;
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

    public String getGamelist() {
        return gamelist;
    }

    public void setGamelist(String gamelist) {
        this.gamelist = gamelist == null ? null : gamelist.trim();
    }
}