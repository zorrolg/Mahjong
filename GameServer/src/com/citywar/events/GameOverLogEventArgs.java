/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.events;

import java.util.Date;

import com.citywar.event.BaseEventArg;
import com.citywar.type.GameType;
import com.citywar.type.RoomType;

/**
 * @author : Cookie
 * @date : 2011-5-13
 * @version
 * 
 */
public class GameOverLogEventArgs extends BaseEventArg
{
    int roomId;
    RoomType roomType;
    GameType fightType;
    int changeTeam;
    Date playBegin;
    Date playEnd;
    int userCount;
    int mapId;
    String teamA;
    String teamAGrade;
    String teamB;
    String teamBGrade;
    String playResult;
    int winTeam;
    String bossWar;

    public GameOverLogEventArgs(Object object, int eventType, int roomId,
            RoomType roomType, GameType fightType, int changeTeam,
            Date playBegin, Date playEnd, int userCount, int mapId,
            String teamA, String teamAGrade, String teamB, String teamBGrade,
            String playResult, int winTeam, String bossWar)
    {
        super(object, eventType);
        this.roomId = roomId;
        this.roomType = roomType;
        this.fightType = fightType;
        this.changeTeam = changeTeam;
        this.playBegin = playBegin;
        this.playEnd = playEnd;
        this.userCount = userCount;
        this.mapId = mapId;
        this.teamA = teamA;
        this.teamAGrade = teamAGrade;
        this.teamB = teamB;
        this.teamBGrade = teamBGrade;
        this.playResult = playResult;
        this.winTeam = winTeam;
        this.bossWar = bossWar;
    }

    /**
     * @return the roomId
     */
    public int getRoomId()
    {
        return roomId;
    }

    /**
     * @param roomId
     *            the roomId to set
     */
    public void setRoomId(int roomId)
    {
        this.roomId = roomId;
    }

    /**
     * @return the roomType
     */
    public RoomType getRoomType()
    {
        return roomType;
    }

    /**
     * @param roomType
     *            the roomType to set
     */
    public void setRoomType(RoomType roomType)
    {
        this.roomType = roomType;
    }

    /**
     * @return the fightType
     */
    public GameType getFightType()
    {
        return fightType;
    }

    /**
     * @param fightType
     *            the fightType to set
     */
    public void setFightType(GameType fightType)
    {
        this.fightType = fightType;
    }

    /**
     * @return the changeTeam
     */
    public int getChangeTeam()
    {
        return changeTeam;
    }

    /**
     * @param changeTeam
     *            the changeTeam to set
     */
    public void setChangeTeam(int changeTeam)
    {
        this.changeTeam = changeTeam;
    }

    /**
     * @return the playBegin
     */
    public Date getPlayBegin()
    {
        return playBegin;
    }

    /**
     * @param playBegin
     *            the playBegin to set
     */
    public void setPlayBegin(Date playBegin)
    {
        this.playBegin = playBegin;
    }

    /**
     * @return the playEnd
     */
    public Date getPlayEnd()
    {
        return playEnd;
    }

    /**
     * @param playEnd
     *            the playEnd to set
     */
    public void setPlayEnd(Date playEnd)
    {
        this.playEnd = playEnd;
    }

    /**
     * @return the userCount
     */
    public int getUserCount()
    {
        return userCount;
    }

    /**
     * @param userCount
     *            the userCount to set
     */
    public void setUserCount(int userCount)
    {
        this.userCount = userCount;
    }

    /**
     * @return the mapId
     */
    public int getMapId()
    {
        return mapId;
    }

    /**
     * @param mapId
     *            the mapId to set
     */
    public void setMapId(int mapId)
    {
        this.mapId = mapId;
    }

    /**
     * @return the teamA
     */
    public String getTeamA()
    {
        return teamA;
    }

    /**
     * @param teamA
     *            the teamA to set
     */
    public void setTeamA(String teamA)
    {
        this.teamA = teamA;
    }

    /**
     * @return the teamAGrade
     */
    public String getTeamAGrade()
    {
        return teamAGrade;
    }

    /**
     * @param teamAGrade
     *            the teamAGrade to set
     */
    public void setTeamAGrade(String teamAGrade)
    {
        this.teamAGrade = teamAGrade;
    }

    /**
     * @return the teamB
     */
    public String getTeamB()
    {
        return teamB;
    }

    /**
     * @param teamB
     *            the teamB to set
     */
    public void setTeamB(String teamB)
    {
        this.teamB = teamB;
    }

    /**
     * @return the teamBGrade
     */
    public String getTeamBGrade()
    {
        return teamBGrade;
    }

    /**
     * @param teamBGrade
     *            the teamBGrade to set
     */
    public void setTeamBGrade(String teamBGrade)
    {
        this.teamBGrade = teamBGrade;
    }

    /**
     * @return the playResult
     */
    public String getPlayResult()
    {
        return playResult;
    }

    /**
     * @param playResult
     *            the playResult to set
     */
    public void setPlayResult(String playResult)
    {
        this.playResult = playResult;
    }

    /**
     * @return the winTeam
     */
    public int getWinTeam()
    {
        return winTeam;
    }

    /**
     * @param winTeam
     *            the winTeam to set
     */
    public void setWinTeam(int winTeam)
    {
        this.winTeam = winTeam;
    }

    /**
     * @return the bossWar
     */
    public String getBossWar()
    {
        return bossWar;
    }

    /**
     * @param bossWar
     *            the bossWar to set
     */
    public void setBossWar(String bossWar)
    {
        this.bossWar = bossWar;
    }

}
