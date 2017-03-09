package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RobotChatInfo {
	/**
	 * ID
	 */
	private int id;
	/**
	 * 玩家游戏的状态
	 */
	private int gameState;
	/**
	 * 等级（权值）
	 */
	private int weight;
	/**
	 * 回答的概率
	 */
	private int probability;
	/**
	 * 机器人类型（-1 都匹配 1比较凶的 2比较和蔼的）
	 */
	private int type;
	/**
	 * 正则表达式
	 */
	private String regex;
	/**
	 * 回答
	 */
	private String answer;
	
	private int voicdId;
	
	public RobotChatInfo(ResultSet rs) throws SQLException {
		super();
		this.id = rs.getInt("id");
		this.gameState = rs.getInt("game_state");
		this.weight = rs.getInt("weight");
		this.probability = rs.getInt("probability");
		this.type = rs.getInt("type");
		this.regex = rs.getString("regex");
		this.answer = rs.getString("answer");
		this.voicdId = rs.getInt("voice_id");
	}
	
	public RobotChatInfo() {
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getGameState() {
		return gameState;
	}
	
	public void setGameState(int gameState) {
		this.gameState = gameState;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	public int getProbability() {
		return probability;
	}

	public void setProbability(int probability) {
		this.probability = probability;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getRegex() {
		return regex;
	}
	
	public void setRegex(String regex) {
		this.regex = regex;
	}
	
	public String getAnswer() {
		return answer;
	}
	
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	
	public int getVoiceId() {
		return voicdId;
	}

	public void setVoiceId(int voicdId) {
		this.voicdId = voicdId;
	}
	
}
