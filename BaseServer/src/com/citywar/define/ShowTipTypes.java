package com.citywar.define;

public interface ShowTipTypes
{
    /**
     * Don't show buggle when owen the item
     */
    public static final int NotShow = 0;

    /**
     * Show the big buggle when owen the item
     */
    public static final int ShowInOneArea = 1;

    /**
     * Show the accross area buggle when owen the item
     */
    public static final int ShowInMutiArea = 2;

    /**
     * Show the big buggle when many condition match! the area open time to now.
     * active player count top 200 active player's level top 200 firght power
     * player 's fright power
     */
    public static final int DynamicShowInOneArea = 3;

    /**
     * Show the accross area buggle when many condition match like
     * DynamicShowInOneArea
     */
    public static final int DynamicShowInMutiArea = 4;
}
