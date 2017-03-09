package com.citywar.dice.db;

public interface CodeId
{
    /* 定义0~1000数据为只增 */
    public static final int BUILDINGID = 1;
    public static final int CASTLEID = 2;
    public static final int USERID = 3;
    public static final int PAWNID = 4;
    public static final int ARMYPAWNID = 5;
    public static final int ARMYID = 6;
    public static final int HEROID = 7;
    public static final int EFFECTID = 8;
    public static final int SKILLID = 9;
    public static final int ITEMID = 10;
    public static final int MAILID = 11;
    public static final int CONSORTIAID = 12;
    public static final int INVITEID = 13;
    public static final int EVENTID = 14;
    /* 定义1000~2000为日增 */
    public static final int LogItem = 1001;
    public static final int LogItemDetail = 1002;
}
