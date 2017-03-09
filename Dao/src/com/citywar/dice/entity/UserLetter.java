package com.citywar.dice.entity;

import java.sql.Timestamp;

public class UserLetter extends DataObject implements Cloneable {
	
	/** 用户聊天发送的私信 */
	public static final int USER_LETTER = 0;
	/** 用户添加好友时 系统发送的私信 */
	public static final int ADD_FRIEND = 1;
	/** 用户添加好友时 系统发送的私信 */
	public static final int HAS_FRIEND = 2;
	/** 赠送礼物 */
	public static final int SEND_GIFT = 3;
	
	public static final int SYSTEM_LETTER = 4;
	
	private int id;
	private int senderId;
	private int senderVip;
	private String senderName;
	private String senderPic;
	
	private int receiverId;
	private String receiverName;
	
	private String content;
	private Timestamp sendTime;
	
	private int type;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSenderId() {
		return senderId;
	}
	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}
	public int getSenderVip() {
		return senderVip;
	}
	public void setSenderVip(int senderVip) {
		this.senderVip = senderVip;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public int getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Timestamp getSendTime() {
		return sendTime;
	}
	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}
	public String getSenderPic() {
		return senderPic;
	}
	public void setSenderPic(String senderPic) {
		this.senderPic = senderPic;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

}
