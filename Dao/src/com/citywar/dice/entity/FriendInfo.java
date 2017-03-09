/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.citywar.util.HeadPicUtil;

/**
 * @author charles
 * @date 2011-12-16
 * @version
 * 
 */
public class FriendInfo extends DataObject
{
    /**
     * 好友表ID
     */
    private int id;

    /**
     * 用户ID
     */
    private int userId;

    /**
     * 好友用户ID
     */
    private int friendId;
    
    /**
     * 好友用户ID
     */
//    private int requestId;

    /**
     * 好友用户名
     */
    private String friendName;
    /**
     * 好友金币数
     */
    private int friendCoins;
    /**
     * 好友的金钱
     */
    private int friendMoney;

    /**
     * 好友签名
     */
    private String friendSign;

    /**
     * 好友
     */
    private String friendPos;

    /**
     * 好友照片
     */
    private String friendPicPath;
    /**
     * 相册集合路径
     */
    private List<String> picPathList;
    /**
     * 好友win次数
     */
    private int win;

    /**
     * 好友lose次数
     */
    private int lose;

    /**
     * 好友lose次数
     */
    private int drunkLevelLimit;
    
    /**
     * 好友城市信息
     */
    private String city;

    /**
     * 好友用户等级
     */
    private int level;

    /**
     * 好友性别
     */
    private short sex;
    /**
     * 好友当前的经验值
     */
    private int friendGp;
    /**
     * 好友当前的魅力值
     */
    private int friendCharmValve;
    /**
     * 好友当前的魅力值
     */
    private int vipLevel;
    
    
    private Timestamp friendLastLoginDate;
    
    
    public FriendInfo(ResultSet rs) throws SQLException
    {
    	this.setId(rs.getInt("ID"));
        this.setUserId(rs.getInt("UserId"));
        this.setFriendId(rs.getInt("FriendId"));
        this.setFriendName(rs.getString("FriendName"));
        this.setFriendMoney(rs.getInt("FriendMoney"));
        this.setFriendSign(rs.getString("FriendSign"));
        this.setFriendPos(rs.getString("FriendPos"));
        //从数据库中取出的相册集合字符串,通过逗号分割
        String picPathFromDB = rs.getString("FriendPicPath");
        //有多张图片时,默认第一张为头像地址,后面分别是相册图片
        List<String> picPathList = new ArrayList<String>();
        if (null != picPathFromDB)
        {
        	String[] result = picPathFromDB.split(",");
            if (result.length == 1) 
            {
            	 //如果长度为1,可能头像为空字符串或者只有头像没有其他相册图片
            	this.setFriendPicPath(picPathFromDB);
            } 
            else if (result.length > 1) 
            {
            	//如果长度大于1,有多长图片
            	for (int i = 0; i < result.length; i++)
            	{
            		if (i == 0)
            		{
            			this.setFriendPicPath(result[i]);
            		}
            		else
            		{
            			picPathList.add(result[i]);
            		}
            	}
            } 
            else
            {
            	//其他情况说明有异常
            	this.setFriendPicPath("");
            }
        }
        else 
        {
        	//从数据库中取出的头像地址为空
        	this.setFriendPicPath("");
        }
        this.setPicPathList(picPathList);
        this.setWin(rs.getInt("Win"));
        this.setLose(rs.getInt("Lose"));
//        this.setDrunkLevelLimit(rs.getInt("drunkLevelLimit"));
        this.setCity(rs.getString("City"));
        this.setLevel(rs.getInt("Level"));
        this.setSex(rs.getByte("Sex"));
        this.setFriendGp(rs.getInt("FriendGp"));
        this.setFriendCoins(rs.getInt("friendCoins"));
        this.setFriendCharmValve(rs.getInt("friendCharmValve"));
        
        this.friendLastLoginDate = rs.getTimestamp("LastLoginDate");
        this.vipLevel = rs.getInt("VipLevel");
        
    }

    /**
     * @return the userId
     */
    public int getUserId()
    {
        return userId;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    /**
     * @return the friendId
     */
    public int getFriendId()
    {
        return friendId;
    }

    /**
     * @param friendId
     *            the friendId to set
     */
    public void setFriendId(int friendId)
    {
        this.friendId = friendId;
    }

    /**
     * @return the friendName
     */
    public String getFriendName()
    {
        return friendName;
    }

    /**
     * @param friendName
     *            the friendName to set
     */
    public void setFriendName(String friendName)
    {
        this.friendName = friendName;
    }

    public int getFriendCoins() {
		return friendCoins;
	}

	public void setFriendCoins(int friendCoins) {
		this.friendCoins = friendCoins;
	}
	/**
     * @return the friendMoney
     */
    public int getFriendMoney()
    {
        return friendMoney;
    }

    /**
     * @param friendMoney
     *            the friendMoney to set
     */
    public void setFriendMoney(int friendMoney)
    {
        this.friendMoney = friendMoney;
    }

    /**
     * @return the friendSign
     */
    public String getFriendSign()
    {
        return friendSign;
    }

    /**
     * @param friendSign
     *            the friendSign to set
     */
    public void setFriendSign(String friendSign)
    {
        this.friendSign = friendSign;
    }

    /**
     * @return the friendPos
     */
    public String getFriendPos()
    {
        return friendPos;
    }

    /**
     * @param friendPos
     *            the friendPos to set
     */
    public void setFriendPos(String friendPos)
    {
        this.friendPos = friendPos;
    }

    /**
     * @return the friendPicPath
     */
    public String getFriendPicPath()
    {
        return friendPicPath;
    }
    
	public String getFriendRealPicPath()
	{
		return HeadPicUtil.getRealPicPath(friendId, friendPicPath);
	}
	
	public String getFriendRealPicPath(String picpath)
	{
		if (friendId <= 0 || picpath.isEmpty())
			return "";
		return HeadPicUtil.getRealPicPath(friendId, picpath);
	}

    /**
     * @param friendPicPath
     *            the friendPicPath to set
     */
    public void setFriendPicPath(String friendPicPath)
    {
        this.friendPicPath = friendPicPath;
    }

    public List<String> getPicPathList() {
		return picPathList;
	}

	public void setPicPathList(List<String> picPathList) {
		this.picPathList = picPathList;
	}

	/**
     * @return the win
     */
    public int getWin()
    {
        return win;
    }

    /**
     * @param win
     *            the win to set
     */
    public void setWin(int win)
    {
        this.win = win;
    }

    /**
     * @return the lose
     */
    public int getLose()
    {
        return lose;
    }

    /**
     * @param lose
     *            the lose to set
     */
    public void setLose(int lose)
    {
        this.lose = lose;
    }

    /**
     * @return the lose
     */
    public int getDrunkLevelLimit()
    {
        return drunkLevelLimit;
    }

    /**
     * @param lose
     *            the lose to set
     */
    public void setDrunkLevelLimit(int drunkLevelLimit)
    {
        this.drunkLevelLimit = drunkLevelLimit;
    }
    
    
    /**
     * @return the city
     */
    public String getCity()
    {
        return city;
    }

    /**
     * @param city
     *            the city to set
     */
    public void setCity(String city)
    {
        this.city = city;
    }

    /**
     * @return the level
     */
    public int getLevel()
    {
        return level;
    }

    /**
     * @param level
     *            the level to set
     */
    public void setLevel(int level)
    {
        this.level = level;
    }

    /**
     * @return the id
     */
    public int getId()
    {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * @return the sex
     */
    public short getSex()
    {
        return sex;
    }

    /**
     * @param sex
     *            the sex to set
     */
    public void setSex(short sex)
    {
        this.sex = sex;
    }

	public int getFriendGp() {
		return friendGp;
	}

	public void setFriendGp(int friendGp) {
		this.friendGp = friendGp;
	}

	public int getFriendCharmValve() {
		return friendCharmValve;
	}

	public void setFriendCharmValve(int friendCharmValve) {
		this.friendCharmValve = friendCharmValve;
	}
	
	public Timestamp getFriendLastLoginDate() {
		return friendLastLoginDate;
	}

	public void setFriendLastLoginDate(Timestamp friendLastLoginDate) {
		this.friendLastLoginDate = friendLastLoginDate;
	}
	
	
	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}
	
}
