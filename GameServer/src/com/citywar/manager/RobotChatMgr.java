package com.citywar.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.citywar.bll.RobotChatBussiness;
import com.citywar.dice.entity.RobotChatInfo;
import com.citywar.game.GamePlayer;
import com.citywar.room.BaseRoom;
import com.citywar.util.Config;
import com.citywar.util.ThreadSafeRandom;
import com.citywar.util.TickHelper;

public class RobotChatMgr {

	/** 机器人在游戏中，默认匹配语句 */
	public static final String GAME_OVER_WORD = "游戏中";

	private static final String ALL = "([\\u4E00-\\u9FA5]*|[\\uFE30-\\uFFA0]*|[a-zA-Z_0-9]*|[ \\t\\n\\x0B\\f\\r]*)*";

	private static final String WORD = "([\u4e00-\u9fa5]*|[a-zA-Z_0-9]*)*";

	private static RobotChatThread chatThread;

	private static ScheduledExecutorService s_chatThreadService;

	private static ThreadSafeRandom random;

	private static Map<Integer, List<RobotChatInfo>> weightMap;

	private static List<RobotChatInfo> allTrumpet;
	
	private static Set<Integer> weightKey;

	/**
	 * 玩家新说的话，等待存入数据库的数据
	 */
	private static List<RobotChatInfo> newInfoList;
	/**
	 * 聊天记录最多数量
	 */
	private static int indexTrumpet;
	private static int fullCount;
	/**
	 * 是否聊天记录最多数量
	 */
	private static boolean isFull = false;

	public static boolean init() {
		indexTrumpet = 0;
		fullCount = Integer.valueOf(Config.getValue("maximumRobotChatCount"));
		newInfoList = new ArrayList<RobotChatInfo>();
		random = new ThreadSafeRandom();
		weightMap = new HashMap<Integer, List<RobotChatInfo>>();
		allTrumpet = new ArrayList<RobotChatInfo>();
		weightKey = new HashSet<Integer>();
		chatThread = new RobotChatThread();
		s_chatThreadService = Executors.newScheduledThreadPool(1);
		s_chatThreadService.scheduleAtFixedRate(chatThread, 0, 1,
				TimeUnit.SECONDS);
		return reload();
	}

	/**
	 * 加载机器人聊天库
	 * 
	 * @return
	 */
	public static boolean reload() {
		List<RobotChatInfo> infoList = new ArrayList<RobotChatInfo>();
		boolean result = true;
		infoList = RobotChatBussiness.getAllRobotChat();
		if (null == infoList || infoList.size() <= 0) {
			result = false;
		}
		initWeightMap(infoList);
		return result;
	}

	private static void initWeightMap(List<RobotChatInfo> infoList) {
		weightMap = new HashMap<Integer, List<RobotChatInfo>>();
		weightKey = new TreeSet<Integer>();
		allTrumpet = new ArrayList<RobotChatInfo>();
		if (null == infoList || 0 > infoList.size()) {
			return;
		}
		int key = 0;
		String regex = "";
		for (RobotChatInfo info : infoList) {
			// //System.out.println(info.getAnswer());
			
			if(info.getType() == 0)
				allTrumpet.add(info);
			if (null == info.getRegex() || info.getRegex().isEmpty()) {
				continue;
			}
			regex = info.getRegex().replace("*", ALL);
			regex = regex.replace("&", WORD);
			info.setRegex(regex);
			key = info.getWeight();
			List<RobotChatInfo> chatList = weightMap.get(key);
			if (null == chatList) {
				chatList = new ArrayList<RobotChatInfo>();
				weightKey.add(key);
				weightMap.put(key, chatList);
			}
			chatList.add(info);
		}
	}

	public static RobotChatInfo robotGetChatAnswer(String question, int gameState,
			int type) {
		RobotChatInfo answer = null;
		answer = getChatAnswerFromMap(question, gameState, type);
		return answer;
	}

	private static RobotChatInfo getChatAnswerFromMap(String question, int gameState,
			int type) {
		RobotChatInfo answer = null;
		for (Integer weight : weightKey) {
			answer = getChatAnswerFromList(question, weight, gameState, type);
			if (!"".equals(answer)) {
				break;
			}
		}
		return answer;
	}

	private static RobotChatInfo getChatAnswerFromList(String question, int weight,
			int gameState, int type) {
//		String answer = "";
		List<RobotChatInfo> chatList = weightMap.get(weight);
		int randomInt = 0;
		Pattern pattern = null;
		Matcher match = null;
		boolean isMatching = false;
		for (RobotChatInfo info : chatList) {
			if (-1 == gameState || gameState == info.getGameState()
					|| -1 == info.getGameState()) {// 机器人处于这种状态
				if (-1 == type || type == info.getType()
						|| (-1 == info.getType() && type < 1000)) {// 是这种类型的机器人,并且不是在游戏中
					pattern = Pattern.compile(info.getRegex(), Pattern.DOTALL);
					match = pattern.matcher(question);
					isMatching = match.matches();// 匹配成功
					if (isMatching) {
						randomInt = random.next(0, 1000);// 千分之的概率
						if (randomInt <= info.getProbability()) {
							return info;
//							answer = info.getAnswer();
//							break;
						}
					}
				}
			}
		}
		return null;
//		return answer;
	}

	/**
	 * 保存新的聊天记录
	 */
	public static void saveMessage() {
		synchronized (newInfoList) {
			RobotChatBussiness.insertRobotChatInfos(newInfoList);
			newInfoList.clear();
		}
		int tempCount = RobotChatBussiness.getRobotChatInfoCount();
		if (fullCount <= tempCount) {
			isFull = true;
		}
	}

	/**
	 * 增加新的聊天记录
	 */
	public static void addMessage(String massge) {
		if (isFull) {
			return;
		}
		synchronized (newInfoList) {
			RobotChatInfo info = new RobotChatInfo();// 测试需要
			// 测试，增加机器人智能需要
			info.setAnswer(massge);
			info.setProbability(10);
			info.setType(-1);
			info.setWeight(5);
			info.setGameState(-1);
			newInfoList.add(info);
		}
	}

	public static void playerOnGameSendChat(GamePlayer player, String question,
			int type) {
		BaseRoom room = player.getCurrentRoom();
		int speed = 500;
		int massgeLength = 0;
		if (null == room || null == room.getGame()) {
			return;
		}
		if (type == 10) {
			massgeLength = 6000 / speed;// 游戏结束动画，大概的时间
		}
		RobotChatInfo massge = null;
		if (null != question && !question.equals("")) {
			
			massge = RobotChatMgr.robotGetChatAnswer(question,player.getGameResultState(), type);
			
			if(massge == null)
				return;
			
			massgeLength += massge.getAnswer().length();
			if (null != massge && !massge.equals("")) {
				ThreadSafeRandom random = new ThreadSafeRandom();
				massgeLength = random.next(massgeLength, massgeLength + 20)
						* speed;// 调节机器人打字的速度
				RobotSendChatAction action = new RobotSendChatAction(
						massgeLength, player,massge.getVoiceId(), massge.getAnswer());
				chatThread.addAction(action);
			}
		}
	}

	public static void playerOnGameSendChat(GamePlayer player, String question) {
		int type = player.getUserId() % 3;
		playerOnGameSendChat(player, question, type);// 机器人的类型
	}
	
	public static void sendWorld()
	{
		
		indexTrumpet++;
		if(indexTrumpet >= allTrumpet.size())
			indexTrumpet = 0;
				
		RobotChatInfo info = allTrumpet.get(indexTrumpet);
		WorldMgr.sendSystemTrumpet(info.getAnswer());
				
	}
	
}

class RobotChatThread implements Runnable {

	private static Logger logger = Logger.getLogger(RobotChatMgr.class
			.getName());

	private List<RobotSendChatAction> actions = new ArrayList<RobotSendChatAction>();

	private void addAction(List<RobotSendChatAction> left) {
		synchronized (this.actions) {
			this.actions.addAll(left);
		}
	}

	public void addAction(RobotSendChatAction action) {
		synchronized (this.actions) {
			this.actions.add(action);
		}
	}

	public void ClearAllAction() {
		synchronized (this.actions) {
			this.actions.clear();
		}
	}

	@Override
	public void run() {
		if(null == actions || actions.size() <= 0) {
			return ;
		}
		long current = System.currentTimeMillis();
		List<RobotSendChatAction> temp = new ArrayList<RobotSendChatAction>();
		synchronized (this.actions) {
			temp.addAll(actions);
			this.actions.clear();
		}

		if (temp != null) {
			if (temp.size() > 0) {
				List<RobotSendChatAction> left = new ArrayList<RobotSendChatAction>();
				for (RobotSendChatAction action : temp) {
					try {
						action.execute(current);
						long end = TickHelper.GetTickCount();

						if (end - current > 100) {
							//System.out.println("RobotChatThread update error: time out!");
							logger.error("RobotChatThread update error: time out!");
						}
						
						if (action.isFinished(current) == false) {
							left.add(action);
						}
					} catch (Exception ex) {
						System.err.println("RobotChatThread Update Error:"
								+ ex.getMessage());
						logger.error("RobotChatThread update error:", ex);
					}
				}
				addAction(left);
			}
		}
	}
}

class RobotSendChatAction {
	private long tick;
	private GamePlayer robotPlayer;
	private int voiceId;
	private String massge;

	public RobotSendChatAction(int delay, GamePlayer player, int voiceId, String massge) {
		this.tick = TickHelper.GetTickCount() + delay;
		robotPlayer = player;
		this.voiceId = voiceId;
		this.massge = massge;
	}

	public void execute(long tick) {
		if (this.tick <= tick) {
			handle();
		}
	}

	private void handle() {
		robotPlayer.onGameSendChat(voiceId,massge);
	}

	public boolean isFinished(long tick) {
		return this.tick <= tick;
	}
}