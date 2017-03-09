package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMessageTokenInfo extends DataObject {
	
	private int id;
	
	private int userId;
	
	private String tokenId;
	
	public UserMessageTokenInfo()
	{
		
	}
	
	public UserMessageTokenInfo(ResultSet rs) throws SQLException
	{
		if (null != rs)
		{
			this.id = rs.getInt("id");
			this.userId = rs.getInt("user_id");
			this.tokenId = rs.getString("token_id");
		}
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
	
	public String getTokenId() {
		return tokenId;
	}
	
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	
	public static void main(String[] args) {
		String token = "EC1C68E3313A9BECE3C64548DE33FADE3C45EA079EE98AED2A4268287EEFC7E3";
		System.out.println(token.length());
	}
}
