package com.citywar.gameutil;

import java.sql.Timestamp;
/**
 * 用户奴隶关系信息转换BO--按时间排序,纯碎转换成业务信息给客户端显示
 * @author Jacky.zheng
 *
 */
public class PlayerReferenceEventBO implements Comparable<PlayerReferenceEventBO>{
	
	private String oneUserName;
	private String oneUserPicPith;
	private int referenceType;
	private String theOtherUserName;
	private String theOtherPicPith;
	private Timestamp time;

	public String getOneUserName() {
		return oneUserName;
	}

	public void setOneUserName(String oneUserName) {
		this.oneUserName = oneUserName;
	}

	public int getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(int referenceType) {
		this.referenceType = referenceType;
	}

	public String getOneUserPicPith() {
		return oneUserPicPith;
	}

	public void setOneUserPicPith(String oneUserPicPith) {
		this.oneUserPicPith = oneUserPicPith;
	}

	public String getTheOtherUserName() {
		return theOtherUserName;
	}

	public void setTheOtherUserName(String theOtherUserName) {
		this.theOtherUserName = theOtherUserName;
	}

	public String getTheOtherPicPith() {
		return theOtherPicPith;
	}

	public void setTheOtherPicPith(String theOtherPicPith) {
		this.theOtherPicPith = theOtherPicPith;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	@Override
	public int compareTo(PlayerReferenceEventBO o) {
		return this.time.after(o.getTime()) ? 1 : (this.time.before(o.getTime()) ? -1 : 0);
	}
}
