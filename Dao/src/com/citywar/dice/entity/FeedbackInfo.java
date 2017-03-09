package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class FeedbackInfo extends DataObject {
	
	/**
	 * 反馈ID
	 */
	private int id;
	/**
	 * 反馈属于的用户ID
	 */
	private int userId;
	/**
	 * 反馈类型
	 */
	private int feedbackType;
	/**
	 * 反馈时间
	 */
	private Timestamp feedbackCreateTime;
	/**
	 * 反馈的内容
	 */
	private String feedbackContents;


	public FeedbackInfo(){
		
	}

	public FeedbackInfo(int id, int userId, int feedbackType,
			Timestamp feedbackCreateTime, String feedbackContents) {
		super();
		this.id = id;
		this.userId = userId;
		this.feedbackType = feedbackType;
		this.feedbackCreateTime = feedbackCreateTime;
		this.feedbackContents = feedbackContents;
	}

	public FeedbackInfo(ResultSet rs) throws SQLException {
		this.id = rs.getInt("id");
		this.userId = rs.getInt("user_id");
		this.feedbackType = rs.getInt("feedback_type");
		this.feedbackCreateTime = rs.getTimestamp("feedback_create_time");
		this.feedbackContents = rs.getString("feedback_contents");
	}

	@Override
    public String toString()
    {
    	String feedbackInfoString = String.format(
                "id: %d,userId: %d",
                getId(), getUserId());
        return feedbackInfoString + "feedbackCreateTime: " + getFeedbackCreateTime().toString() + 
        		"feedbackContents: " + getFeedbackContents();
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getFeedbackType() {
		return feedbackType;
	}

	public void setFeedbackType(int feedbackType) {
		this.feedbackType = feedbackType;
	}

	public Timestamp getFeedbackCreateTime() {
		return feedbackCreateTime;
	}

	public void setFeedbackCreateTime(Timestamp feedbackCreateTime) {
		this.feedbackCreateTime = feedbackCreateTime;
	}

	public String getFeedbackContents() {
		return feedbackContents;
	}

	public void setFeedbackContents(String feedbackContents) {
		this.feedbackContents = feedbackContents;
	}
	
}
