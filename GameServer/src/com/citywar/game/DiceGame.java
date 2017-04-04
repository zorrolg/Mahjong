/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.game;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.citywar.bll.common.LanguageMgr;
import com.citywar.dice.entity.HallTypeInfo;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.dice.entity.UserRefWorkInfo;
import com.citywar.dice.entity.UserReferenceInfo;
import com.citywar.game.action.RobotProcessAction;
import com.citywar.game.action.StartPrepareAction;
import com.citywar.game.action.Dice.DiceCheckGameStateAction;
import com.citywar.game.action.Dice.DiceProcessAction;
import com.citywar.gameobjects.ai.DicePointFactory;
import com.citywar.manager.GameMgr;
import com.citywar.manager.LevelMgr;
import com.citywar.manager.ReferenceMgr;
import com.citywar.manager.RobotChatMgr;
import com.citywar.manager.UserTopMgr;
import com.citywar.manager.WorldMgr;
import com.citywar.room.BaseRoom;
import com.citywar.socket.Packet;
import com.citywar.type.GameDataType;
import com.citywar.type.GamePlayerEndState;
import com.citywar.type.GameState;
import com.citywar.type.HallGameType;
import com.citywar.type.PlayerState;
import com.citywar.type.PropType;
import com.citywar.type.TaskConditionType;
import com.citywar.type.UserCmdOutType;
import com.citywar.util.Config;
import com.citywar.util.ThreadSafeRandom;
import com.citywar.util.TickHelper;

/**
 * 色子游戏
 * 
 * @author Dream
 * @date 2011-12-15
 * @version
 * 
 */
public class DiceGame extends BaseGame {
	private static Logger logger = Logger.getLogger(DiceGame.class.getName());

	/**
	 * global random
	 */
	// private ThreadSafeRandom random = new ThreadSafeRandom();
	private long m_passTick = 0;
	private byte DiceNumber;
	private byte DicePoint;
	private boolean HasCallOne;
	private int SortFlag;
	private int GameTotal;

	private HashMap<Integer, byte[]> dicePoints;
	private HashMap<Integer, Integer> dicePointsCount;

	private int successID;
	private int failID;
	private boolean isPlayerCall;

	public boolean isPlayerCall() {
		return isPlayerCall;
	}

	public void setPlayerCall(boolean isPlayerCall) {
		this.isPlayerCall = isPlayerCall;
	}

	public HashMap<Integer, byte[]> getDicePoints() {
		return dicePoints;
	}

	public byte[] getDicePoints(int playerID) {
		return dicePoints.get(playerID);
	}

	public int getMaxDicePoints(boolean isCallOne) {
		int maxPoint = 2;
		int maxPointNumber = 0;

		if (isCallOne) {
			for (int point : dicePointsCount.keySet()) {
				if (dicePointsCount.get(point) >= maxPointNumber) {
					maxPoint = point;
					maxPointNumber = dicePointsCount.get(point);
				}
			}
		} else {
			int numberOne = dicePointsCount.containsKey(1) ? dicePointsCount.get(1) : 0;
			for (int point : dicePointsCount.keySet()) {
				if (dicePointsCount.get(point) + numberOne >= maxPointNumber
						|| (point == 1 && dicePointsCount.get(point) >= maxPointNumber)) {
					maxPoint = point;
					maxPointNumber = (point == 1 ? dicePointsCount.get(point) : dicePointsCount.get(point) + numberOne);
				}
			}
		}

		return maxPoint;
	}

	public boolean getHasCallOne() {
		return HasCallOne;
	}

	public void setHasCallOne(boolean callone) {
		this.HasCallOne = callone;
	}

	public byte getDiceNumber() {
		return DiceNumber;
	}

	public void setDiceNumber(byte diceNumber) {
		this.DiceNumber = diceNumber;
		// System.out.println("setDiceNumber========================" + diceNumber);
	}

	public byte getDicePoint() {
		return DicePoint;
	}

	public void setDicePoint(byte dicePoint) {
		this.DicePoint = dicePoint;
	}

	public void resetDice() {
		this.DiceNumber = 1;
		this.DicePoint = 1;
		this.dicePoints.clear();
		this.HasCallOne = false;
		this.turnIndex = 0;

		isPlayerCall = true;

		if (this.isChangeTurn)
			SortFlag = (GameTotal % 2 == 0 ? 1 : -1);
		else
			SortFlag = 1;

	}

	/**
	 * 玩家强退移除（扣经验，扣money）
	 */
	@Override
	public Player removePlayer(GamePlayer gp) {
		if (null == gp) {
			return null;
		}
		Player player = super.removePlayer(gp);
		if (player != null && gameState == GameState.Playing) {
			// System.out.println("在游戏运行阶段退出游戏....");
			// 当游戏正在进行时并且玩家也正在游戏中退出，扣除经验.现在不扣了
			if (player.getPlayerState() == PlayerState.Playing) {
				// gp.addGpDirect(-gp.getPlayerInfo().getLevel() * 5);
				gp.addCoins(hallType.getWager() * -2);// 强退游戏
				// 然后重置游戏,发送重新可以开始游戏的指令
				gameState = GameState.Prepared;
				resetDice();
				AddAction(new StartPrepareAction(0));
				onGameOverred();
				Packet pkg = new Packet(UserCmdOutType.SEND_USER_EXIT);
				pkg.putInt(gp.getUserId());
				player.getPlayerDetail().getOut().sendUpdatePrivateInfo(player.getPlayerDetail().getPlayerInfo(),
						(byte) 1);
				this.sendToAll(pkg);
				ClearWaitTimer();
			}
		}
		// 此段逻辑的处理，是防止如下情况：例，房间里有3人，一人准备后，马上退出，剩下两个人如果不准备，10s后会被移除。
		else if (player != null && gameState == GameState.Prepared) {
			// System.out.println("在游戏准备阶段退出游戏....");
			// 标记是否更改玩家状态
			boolean flag = true;
			List<Player> list = getAllPlayers();
			for (Player p : list) {
				if (p.getPlayerState() >= PlayerState.EndShaked) {
					flag = false;
					break;
				}
			}

			if (flag) {
				for (Player p : getAllPlayers()) {
					p.setPlayerState(PlayerState.Idle);
					sendShakePrepare();
					sendUpdatePlayerState();
				}
			}
		} else if (player != null && gameState == GameState.GameOver) {
			if (player.getPlayerID() == successID || player.getPlayerID() == failID) {
				ClearAllAction();
				AddAction(new StartPrepareAction(100));
			}
		}
		return player;
	}

	/**
	 * 
	 * @param gameId
	 * @param hallId
	 * @param roomId
	 * @param list
	 * @param roomType
	 * @param gameType
	 * @param timeType
	 * @param hallType
	 */
	public DiceGame(int gameId, int hallId, BaseRoom baseRoom, List<GamePlayer> list, int gameType, int timeType,
			HallTypeInfo hallType) {
		super(gameId, hallId, baseRoom, gameType, timeType, hallType, true, true, 2, false, true);
		this.gameState = GameState.Created;
		GameTotal = 0;
		dicePoints = new HashMap<Integer, byte[]>();
		dicePointsCount = new HashMap<Integer, Integer>();
		for (GamePlayer gamePlayer : list) {
			Player player = new Player(gamePlayer, this);
			addGamePlayer(gamePlayer.getUserId(), player);
		}
		resetDice();

		hallGameType = hallType.getHallType();

		// if(hallGameType == 2)
		// System.out.println("hallGameType=============================" + hallGameType);
	}

	@Override
	public void checkState(int delay) {
		// System.out.println("checkState==============" + TickHelper.GetTickCount() + "====" + delay);
		AddAction(new DiceCheckGameStateAction(delay));
	}

	@Override
	public void Pause(int time) {
		m_passTick = Math.max(m_passTick, TickHelper.GetTickCount() + time);
	}

	@Override
	public void Resume() {
		m_passTick = 0;
	}

	@Override
	public void processData(Packet packet) {
		if (players.containsKey(packet.getClientId())) {
			Player player = players.get(packet.getClientId());
			AddAction(new DiceProcessAction(player, packet));
		}
	}

	// /**
	// * 赢放灌输方喝酒
	// * @param successPlayer
	// * @param failPlayer
	// */
	// private boolean punishDrink(GamePlayer successPlayer, GamePlayer failPlayer) {
	// boolean isDrink = false;
	// int drunkCut = -1;
	// boolean isExistAndIsUsedLiquor = successPlayer.getPropBag()
	// .isExistAndIsUsedTypeEffect(PropType.LIQUOR_TYPE);//玩家在使用烈酒
	// if(isExistAndIsUsedLiquor) {
	// if(successPlayer.getPropBag().usePropItemByType(PropType.LIQUOR_TYPE, 1)) {
	// drunkCut = ItemMgr.getLiquorCru(PropType.LIQUOR_TYPE);
	// }
	// successPlayer.checkUseLiquorAndHavaEffect();
	// }
	// int sumDrunkLevel = failPlayer.getPlayerInfo().getDrunkLevel() + drunkCut;
	// failPlayer.addDrunkLevel(drunkCut);//罚酒
	//
	// if (failPlayer.isFullDrunk()) {//如果输者喝醉了，更新奴隶关系，现在都可以
	// isDrink = true;
	// successPlayer.isFinishTask(TaskType.DrunkUser, 0, 4);//得到了一个酒奴
	// failPlayer.getPlayerReference().changMaster(successPlayer.getUserId());//失败者更换奴隶主
	// failPlayer.getPlayerInfo().setDrunkLevel(
	// failPlayer.getPlayerInfo().getDrunkLevelLimit());
	// if(sumDrunkLevel < 0) {
	// failPlayer.addDrunkLevel(sumDrunkLevel);
	// }
	// }
	// return isDrink;
	// }
	/**
	 * 游戏停止
	 */
	public void gameOver(boolean isGrab) {
		if (gameState == GameState.Playing && gameState != GameState.GameOver
				// 出现一个BUG，当玩家同时点击开的时候会发送两次开到服务器端
				// 即服务器会返回两次结果给客户端~第一次开会将游戏的状态设置成GameOver
				// 如果在这里再加上这个条件那么就会返回两次!
				// || gameState == GameState.GameOver
				) {
			// 用于计算gameover后StartPrepareAction的延时执行时间
			Integer hitDiceNumber = 0;
			int movieDelay = 0;
			try {

				// System.out.println("DiceCmdType.OPEN====================3============");

				Packet packet = new Packet(UserCmdOutType.GAME_OPEN);
				packet.putBoolean(true);
				// 被叫玩家
				Player calledPlayer = (Player) lastTurnPlayer;
				Player callPlayer = (Player) currentTurnPlayer;

				// 总个数
				byte total = 0;
				// 赢家ID
				successID = 0;
				// 输家ID
				failID = 0;
				// 赢家
				GamePlayer successPlayer = null;
				// 输家
				GamePlayer failPlayer = null;
				//
				int successMoney = 0;
				//
				float winCharmValveFloat = 0;

				// 叫开玩家ID
				packet.putInt(callPlayer.getPlayerDetail().getUserId());
				// 被叫玩家ID
				packet.putInt(calledPlayer == null ? -1 : calledPlayer.getPlayerDetail().getUserId());
				packet.putByte(this.DiceNumber);
				packet.putByte(this.DicePoint);
				packet.putBoolean(this.HasCallOne);
				packet.putBoolean(isGrab);
				packet.putByte((byte) getTurnQueue().size());

				// System.out.println("gamer open=========================================================" + DiceNumber + "====" + DicePoint + "====" + HasCallOne);
				// 这里发送此轮游戏中的玩家的信息
				int currentStage = 0;
				for (Player player : getTurnQueue()) {
					player.getPlayerDetail().setPlayerEndState(GamePlayerEndState.Draw.getValue());

					byte result = calculatePoint(player.getDiceNumbers());
					packet.putInt(player.getPlayerDetail().getUserId());
					packet.putInt(player.getDiceNumbers().length);
					for (byte point : player.getDiceNumbers()) {
						packet.putByte(point);
					}
					total += result;

					if (!player.getPlayerDetail().getIsRobot())
						currentStage = player.getPlayerDetail().getCurrentStage();
				}

				hitDiceNumber = (int) total;

				// 判断赢家
				if (calledPlayer == null) {
					successPlayer = callPlayer.getPlayerDetail();
					successID = callPlayer.getPlayerDetail().getUserId();
					failID = -1;
				} else if (total < getDiceNumber()) {
					successPlayer = callPlayer.getPlayerDetail();
					successID = callPlayer.getPlayerDetail().getUserId();
					failPlayer = calledPlayer.getPlayerDetail();
					failID = calledPlayer.getPlayerDetail().getUserId();
					currentTurnPlayer = calledPlayer;
				} else {
					successPlayer = calledPlayer.getPlayerDetail();
					successID = calledPlayer.getPlayerDetail().getUserId();
					failPlayer = callPlayer.getPlayerDetail();
					failID = callPlayer.getPlayerDetail().getUserId();
					currentTurnPlayer = callPlayer;
				}

				if (successPlayer != null)
					successPlayer.setPlayerEndState(GamePlayerEndState.Win.getValue());

				if (failPlayer != null)
					failPlayer.setPlayerEndState(GamePlayerEndState.Lose.getValue());

				// 增加多少金钱，这里可以设置规则
				int playerSize = getTurnQueue().size();
				int absorbRate = 100;
				int succeedMony = ((isGrab ? GameDataType.OPEN_GRAB_TIMES * hallType.getWager()
						: GameDataType.OPEN_TIMES * hallType.getWager()) * absorbRate) / 100;

				// 如果赢家赢的钱必须<赢家自己携带的钱
				if (succeedMony > successPlayer.getPlayerInfo().getCoins()) {
					succeedMony = successPlayer.getPlayerInfo().getCoins();
				}

				int dogfallMoney = (hallType.getWager() * GameDataType.DOGFALL_RATE) / 100;
				// int winMoney = (GameDataType.WIN_RATE * succeedMony) / 100 + dogfallMoney * (playerSize - 2);
				float winMoney = (GameDataType.WIN_RATE * succeedMony) / 100;
				float loseMoney = succeedMony;

				// 输家身上的金币
				int loseHasCoins = failPlayer.getPlayerInfo().getCoins();

				if (loseHasCoins < loseMoney) {
					// 输家输的金币 能够满足赢家 不能够满足分红
					if (loseHasCoins > winMoney) {
						loseMoney = winMoney;
						// dogfallMoney = 0;
					} else {// 输家的金币不能够满足赢家 则输的金币=赢家的金币
						loseMoney = winMoney = loseHasCoins;
						// dogfallMoney = 0;
					}
				}

				int masterWinMoney = 0;

				// 增加醒酒度，这里可以设置规则
				boolean isDrink = false;
				int drunkCut = -10;

				// System.out.println("DiceCmdType.OPEN====================4============");
				/////////////////////////////////////////// 卡牌效果 begin/////////////////////////////////////////////////////
				// winCharmValve successMoney loseMoney drunkCut

				// int addDrunk = 0;
				// int removeDrunk = 0;
				// boolean isCoinDelay = false;
				// if(successPlayer != null)
				// {
				// successPlayer.getListGameCard().clear();
				// Map<Integer,Integer> mapCard = GameMgr.getGameEndCardList(hallGameType);
				// for(int value : mapCard.keySet())
				// {
				// if(successPlayer.getPlayerEndState() == mapCard.get(value))
				// {
				// int cardId = 0;
				// if(successPlayer.getIsRobot())
				// cardId = successPlayer.getRobotCardId(value);
				// else
				// cardId = successPlayer.getCardBag().useCardItemByType(value, 1);
				//
				// CardInfo card = CardMgr.findCard(cardId);
				// if(cardId != 0 && card != null)
				// {
				// switch(value)
				// {
				// case CardType.WIN_COIN:
				// winMoney = winMoney * (1 + (float)card.getPara1()/100);
				// movieDelay += 1500;
				// isCoinDelay = true;
				//// System.out.println("gameover=======1======" + winMoney + "===" + card.getPara1());
				// break;
				//
				// case CardType.WIN_DRUNK:
				// addDrunk = card.getPara1() * -1;
				// movieDelay += 3000;
				// break;
				// }
				// successPlayer.getListGameCard().add(cardId);
				// }
				// }
				// }
				// }
				//
				// for (Player tempPlayer : getTurnQueue())
				// {
				// if(tempPlayer.getPlayerDetail() == successPlayer)
				// continue;
				//
				// tempPlayer.getPlayerDetail().getListGameCard().clear();
				//
				// Map<Integer,Integer> mapCard = GameMgr.getGameEndCardList(hallGameType);
				// for(int value : mapCard.keySet())
				// {
				//
				// if(tempPlayer.getPlayerDetail().getPlayerEndState() == mapCard.get(value))
				// {
				// int cardId = 0;
				// if(tempPlayer.getPlayerDetail().getIsRobot())
				// cardId = tempPlayer.getPlayerDetail().getRobotCardId(value);
				// else
				// cardId = tempPlayer.getPlayerDetail().getCardBag().useCardItemByType(value, 1);
				//
				// CardInfo card = CardMgr.findCard(cardId);
				// if(cardId != 0 && card != null)
				// {
				// switch(value)
				// {
				//
				// case CardType.LOSE_COIN:
				// float para1 = (float)card.getPara1();
				// winMoney = winMoney * (1 - (para1)/(para1 + 400));
				//
				// if(!isCoinDelay)
				// movieDelay += 1500;
				//
				// break;
				// case CardType.LOSE_DRUNK:
				// removeDrunk = card.getPara1();
				// break;
				// }
				// tempPlayer.getPlayerDetail().getListGameCard().add(cardId);
				// }
				// }
				// }
				// }
				//
				// loseMoney = winMoney;
				// drunkCut = drunkCut + addDrunk - removeDrunk;
				/////////////////////////////////////////// 卡牌效果 end/////////////////////////////////////////////////////

				// //System.out.println("movieDelay＝＝＝＝＝＝＝＝＝＝0＝＝＝＝＝＝＝＝＝"+movieDelay);
				// System.out.println("DiceCmdType.OPEN====================5============");
				// System.out.println("winCharmValve=====1===" + winMoney + "-" +hallType.getWager() + "======"
				// + successPlayer.getPlayerInfo().getLevel() + "-" + failPlayer.getPlayerInfo().getLevel() + "======"
				// + drunkCut + "======");

				int winCharmValve = 0;
				if (hallGameType == HallGameType.CONTEST) {
					// 金杯公式
					float CoinPercent = (float) (winMoney - hallType.getWager()) / hallType.getWager();
					float CoinPara = 1 * CoinPercent / (CoinPercent + 2);
					float levelPara = (float) (successPlayer.getPlayerInfo().getLevel()
							- failPlayer.getPlayerInfo().getLevel()) / 20;
					float drunkPara = (float) (1 * drunkCut * -1) / (drunkCut * -1 + 3);
					winCharmValveFloat = 3 * (1 - levelPara) + 1 * CoinPara + 1 * drunkPara;
					// winCharmValve = 20 * ( 1- (successPlayer.getPlayerInfo().getLevel() - failPlayer.getPlayerInfo().getLevel())/10);

					winCharmValve = Math.round(winCharmValveFloat);

					if (successPlayer.getPlayerInfo().getVipLevel() > 0)
						winCharmValve = (int) (winCharmValve * (1 + 0.2));

					if (failPlayer.getPlayerInfo().getVipLevel() > 0)
						winCharmValve = (int) (winCharmValve * (1 - 0.2));

					movieDelay += 2000;
					// System.out.println("winCharmValve=====2===" + CoinPara + "======" + levelPara + "======" + drunkPara + "======" + winCharmValveFloat + "======" +
					// winCharmValve + "======");

				}

				// drunk
				// int sumDrunkLevel = failPlayer.getPlayerInfo().getDrunkLevel() + drunkCut;
				failPlayer.addDrunkLevel(drunkCut);// 罚酒

				// System.out.println("drunkCutdrunkCut========" + drunkCut );

				if (failPlayer.isFullDrunk()) {// 如果输者喝醉了，更新奴隶关系，现在都可以

					isDrink = true;
					winCharmValve *= 3;
					successPlayer.isFinishTask(TaskConditionType.DrunkUser, 0, 0);// 得到了一个酒奴
					String strMsg = LanguageMgr.getTranslation("CityWar.Trumpet.Drunk",
							successPlayer.getPlayerInfo().getUserName(), failPlayer.getPlayerInfo().getUserName());
					WorldMgr.sendSystemTrumpet(strMsg);

					int workType = 0;
					if (hallGameType == HallGameType.SOCIAL) {
						winMoney *= 3;
						loseMoney *= 3;
						UserRefWorkInfo work = ReferenceMgr.getRndWork();
						// 你已经成为了(%s)的酒奴，他派你去(%s)，为期(%s)。取消本次派遣，消耗(%s)钻石。
						int minuteRate = Integer.parseInt(Config.getValue("ref_minute_exchangerate"));
						int money = work.getWorkTime() * 60 / minuteRate;
						// String strNotify = LanguageMgr.getTranslation("CityWar.Ref.Notify", successPlayer.getPlayerInfo().getUserName(), work.getWorkName());
						failPlayer.getOut().sendPlayerDrunkNotify(work.getWorkTime() * 3600, money, work.getWorkName(),
								successPlayer.getPlayerInfo().getUserName());
						workType = work.getId();

						failPlayer.getPlayerReference().changMaster(successPlayer.getUserId(), winCharmValve, workType);// 失败者更换奴隶主
					} else {
						failPlayer.getPlayerInfo()
						.setDrunkLevelContest(failPlayer.getPlayerInfo().getDrunkLevelLimit());
					}

					// KKMgr.AddDiceKKInfo(1, successPlayer, failPlayer, 0, "");
					// String str = String.format("%s在KK同城大话骰中被灌醉，成为了%s的私人奴隶，痛不欲生", failPlayer.getPlayerInfo().getUserName(), successPlayer.getPlayerInfo().getUserName());
					// KKMgr.addKKInfo(failPlayer, str, "1");

				}
				if (isDrink) {// 有醉酒动画
					movieDelay += 2000;
				}

				if (winCharmValve > failPlayer.getPlayerInfo().getCharmValve())
					winCharmValve = failPlayer.getPlayerInfo().getCharmValve();

				if (winMoney > failPlayer.getPlayerInfo().getCoins()) {
					winMoney = failPlayer.getPlayerInfo().getCoins();
					loseMoney = failPlayer.getPlayerInfo().getCoins();
				}
				if (winCharmValve == 0)
					movieDelay -= 2000;
				// if(winCharmValve < 0)
				// System.out.println("DiceCmdType.OPEN====================6============");
				// //System.out.println("movieDelay＝＝＝＝＝＝＝＝＝＝1＝＝＝＝＝＝＝＝＝"+movieDelay);

				// 设置玩家胜负场次，总场次，醒酒度，money等属性
				packet.putInt(playerSize);
				GamePlayer gamePlayer = null;

				for (Player tempPlayer : getTurnQueue()) {
					tempPlayer.setPlayerState(PlayerState.Open);// 状态改变提前点
					gamePlayer = tempPlayer.getPlayerDetail();
					// 系统需要抽取服务费
					// gamePlayer.addCoins( - hallType.getSystemTaxes());
					gamePlayer.addTotal();

					if (gamePlayer.getIsRobot())
						gamePlayer.setCurrentStage(currentStage);

					if (tempPlayer.getPlayerID() == successID) {
						gamePlayer.addWin();
						// //奴隶主需要分成
						// masterWinMoney = winMoney * GameDataType.MASTER_WIN_RATE / 100;//税金
						// successMoney = winMoney - masterWinMoney;//最终结果
						//
						// /**
						// * 防沉迷特别处理
						// */
						// UserProperty userProperty = tempPlayer.getPlayerDetail().getUserProperty();
						// if(null != userProperty) {//防沉迷
						// winMoney = (winMoney * userProperty.getIncomeRatio()) / 100;
						//
						// }
						//
						// int MasterReferenceId = gamePlayer.getPlayerReference().getMasterReferenceId();
						// if(MasterReferenceId == 0) {//如果没有主人，抽的税是自己的
						// successMoney = successMoney + masterWinMoney;
						// masterWinMoney = 0;
						// } else {//如果有主人，税是主人的
						// UserReferenceInfo refInfo = ReferenceMgr.getUserReferenceInfoByUserReferenceId(MasterReferenceId);
						// if(null != refInfo) {
						// Player root = RobotMgr.getRobotByID(refInfo.getMasterUserId());
						// if(null != root) {//如果主人是机器人的话，机器人立即收税金
						// //既不会存储在关系表中，也不能及时的给机器人加钱了。
						// //很容易识破是机器人，假设机器人在玩游戏，但是机器人还在同时收税
						// //或者将来在做成定时的机器人去收税。
						// //但是我觉得是没必要的。这样有个不好的就是，机器人的税都逃不掉
						// } else {
						// int oldCoins = refInfo.getIncomeCoins();
						// refInfo.setIncomeCoins(oldCoins + masterWinMoney);
						// refInfo.setOp(Option.UPDATE);
						// }
						// }
						// }
						successMoney = (int) winMoney;
						gamePlayer.addCoins(successMoney);

						if (winCharmValve > 0)
							UserTopMgr.checkLevelTop(true, gamePlayer, this.hallType, gamePlayer.getCurrentStage(),
									winCharmValve);

						// gamePlayer.addGpDirect(GameDataType.FAIL_ADD_GP);//失败20
					} else if (tempPlayer.getPlayerID() == failID) {
						gamePlayer.addLose();
						gamePlayer.addCoins((int) loseMoney * -1);

						if (winCharmValve > 0)
							UserTopMgr.checkLevelTop(false, gamePlayer, this.hallType, gamePlayer.getCurrentStage(),
									-winCharmValve);

						// gamePlayer.addGpDirect(GameDataType.FAIL_ADD_GP);//失败20

					} else {
						// gamePlayer.addGpDirect(GameDataType.DOGFALL_ADD_GP);//不胜不负：45
						// gamePlayer.addCoins(dogfallMoney * -1);//不胜不负：45
					}
					// 更改玩家游戏状态移到BaseRoom.removePlayer里面,这样会实时更新内存，便于外部获取
					// gamePlayer.setPlaying(false);
					PlayerInfo info = gamePlayer.getPlayerInfo();
					packet.putInt(info.getUserId()); // 游戏币
					packet.putInt(info.getCoins()); // 游戏币
					packet.putInt(info.getWin()); // 胜场次
					packet.putInt(info.getLose()); // 负场次

					// 道具数量, 必须重构
					packet.putInt(gamePlayer.getPropBag().getPropCountByType(PropType.WAKE_TYPE));
					// packet.putInt(gamePlayer.getPropBag().getPropCount(PropType.WAKE_DRUG));
					packet.putInt(gamePlayer.getDrunkLevel()); // 当前醉酒度
					packet.putInt(info.getDrunkLevelLimit()); // 最大醉酒度
					packet.putInt(LevelMgr.getUserUpgradeGp(info)); // 经验值
					packet.putInt(info.getLevel()); // 等级
					packet.putInt(info.getCharmValve());
					gamePlayer.getOut().sendUpdateBaseInfo();
					gamePlayer.getOut().sendUpdatePrivateInfo(info, (byte) 1);

					// ---------------开始加入任务模块----------------
					int isCallOne = 0;// 0-无条件,1-斋局,2-非斋局
					if (this.getHasCallOne()) {
						isCallOne = 1;
					}
					int gameResult = 0;
					if (successID == gamePlayer.getUserId()) { // 胜利结果
						gameResult = 1;
					} else if (failID == gamePlayer.getUserId()) {
						gameResult = 2;
					} else { // 胜负与自己无关是平局
						gameResult = 3;
					}
					if (!gamePlayer.getIsRobot()) {// 不是机器人才可以有任务
						gamePlayer.isFinishTask(TaskConditionType.FinishGame, gameResult, isCallOne);
						gamePlayer.isFinishTask(TaskConditionType.GameCount, gameResult, isCallOne);
						// gamePlayer.isFinishTask(TaskType.TaskType, gameResult, isCallOne);
						// ---------------加入任务模块end----------------
					} else {// 是机器人才可以有自动聊天（才需要改变状态）
						gamePlayer.setGameResultState(gameResult);
						RobotChatMgr.playerOnGameSendChat(gamePlayer, RobotChatMgr.GAME_OVER_WORD, 10);// 类型为10

					}
					/*
					 * -------------加入奴隶主的信息 begin
					 */
					UserReferenceInfo userReferenceInfo = gamePlayer.getPlayerReference().getMasterUserReferenceInfo();
					packet.putInt(null != userReferenceInfo ? userReferenceInfo.getMasterUserId() : 0);
					packet.putStr(null != userReferenceInfo ? userReferenceInfo.getMasterUserName() : "");
					packet.putStr(null != userReferenceInfo ? userReferenceInfo.getMasterPicPath() : "");
					/*
					 * -------------加入奴隶主的信息 end
					 */

					///////
					// packet.putInt(tempPlayer.getPlayerDetail().getListGameCard().size());
					// for (Integer point : tempPlayer.getPlayerDetail().getListGameCard())
					// {
					// packet.putInt(point);
					// }

					//////
				}
				// System.out.println("game over winCharmValve===========================================" + winCharmValve);

				packet.putInt(successID);
				packet.putInt((int) winMoney);// 换成主人跟自己的合。现在还要加上防沉迷
				packet.putInt(failID);
				packet.putInt((int) loseMoney);
				packet.putInt(winCharmValve);
				packet.putBoolean(isDrink);
				packet.putByte((byte) drunkCut);// 配合客户端减少流量
				packet.putInt(masterWinMoney);// 主人的收入
				packet.putInt(dogfallMoney);// 第三方分红

				packet.putInt(this.getGameRecordId());
				sendToAll(packet);
				addGameRecordList(packet);

				// for (Player tempPlayer : getTurnQueue())
				// {
				// if(!tempPlayer.getPlayerDetail().getCurrentRoom().canStayHere(tempPlayer.getPlayerDetail()))
				// {
				// AwardMgr.checkCoinsLessAwardCoins(tempPlayer.getPlayerDetail());
				//// movieDelay += 3000;
				// break;
				// }
				// }

				if (getTurnQueue().size() < hallType.getPlayerCountMin()) {
					// int rnd = random.next(1, 101);
					// System.out.println("onStartAddRobot====================" + rnd + "============");
					// if(rnd > 50)
					this.getBaseRoom().onStartAddRobot();
				}

				// System.out.println("DiceCmdType.OPEN====================7============");
				// for (Player tempPlayer : getTurnQueue())
				// gamePlayer.getOut().sendCardUsedList(tempPlayer.getPlayerDetail().getListGameCard());
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("dicegame exception:" + e.getMessage());
			}

			dicePointsCount.clear();
			gameState = GameState.GameOver;
			GameTotal += 1;
			startGameDelay = (this.getTurnQueue().size() * 1.6 + hitDiceNumber.floatValue() * 0.3 + 7.5) * 1000.0
					+ movieDelay;

			resetDice();
			sendUpdatePlayerState();// 更新玩家状态

			ClearWaitTimer();
			onGameOverred();

			// if(gameState == GameState.Wait)
			// {
			// HallMgr.checkEndGame(getHallId(), 3000);
			// }
			// else
			// {
			// Integer countSize = this.getTurnQueue().size();
			// Double delay = (countSize.floatValue() * 1.6 + hitDiceNumber.floatValue() * 0.3 + 7.5) * 1000.0;
			//// System.out.println("================delay==" + delay + ", movieDelay==" + movieDelay);
			// delay = delay + movieDelay;
			// AddAction(new StartPrepareAction(delay.intValue()));
			//// System.out.println("================delay==" + delay + ", movieDelay==" + movieDelay);
			// }
		}
	}

	/**
	 * @param diceNumbers
	 * @param diceNumber
	 * @param isCallOne
	 *            是否斋
	 */
	public byte calculatePoint(byte[] diceNumbers) {
		byte result = 0;
		for (int i = 0; i < diceNumbers.length; i++) {
			if (this.HasCallOne) {
				if (diceNumbers[i] == this.DicePoint)
					result++;
			} else {
				if (diceNumbers[i] == DicePoint || diceNumbers[i] == 1)
					result++;
			}
		}
		return result;
	}

	// /**
	// * 判断某人手上是否有当前场上叫的点数
	// *
	// * @param diceNumbers
	// * @param diceNumber
	// * @param isCallOne
	// * 是否斋
	// */
	// public boolean isHaveCurrentPoint(byte[] diceNumbers, int dicePoint)
	// {
	// boolean result = false;
	// for (int i = 0; i < diceNumbers.length; i++)
	// {
	// if (this.HasCallOne)
	// {
	// if (diceNumbers[i] == dicePoint)
	// {
	// result = true;
	// break;
	// }
	// }
	// else
	// {
	// if (diceNumbers[i] == dicePoint || diceNumbers[i] == 1)
	// {
	// result = true;
	// break;
	// }
	// }
	// }
	// return result;
	// }

	/**
	 * 判断某人手上是否有当前场上叫的点数
	 * 
	 * @param diceNumbers
	 * @param diceNumber
	 * @param isCallOne
	 *            是否斋
	 */
	public int haveCurrentPointCount(byte[] diceNumbers, int dicePoint) {
		int count = 0;
		for (int i = 0; i < diceNumbers.length; i++) {
			if (this.HasCallOne) {
				if (diceNumbers[i] == dicePoint) {
					count += 1;
				}
			} else {
				if (diceNumbers[i] == dicePoint || diceNumbers[i] == 1) {
					count += 1;
				}
			}
		}
		return count;
	}

	/**
	 * 判断是否可以结束游戏
	 * 
	 * @return
	 */
	public boolean canGameOver() {
		// 玩家人数少于2人，turn次数大于20，叫的点数大于20，游戏结束
		if (this.getTurnQueue().size() < 2 || turnIndex >= 20 || DiceNumber >= ((this.getTurnQueue().size() * 5) + 1)) {
			// 这里执行一次查找，是要保证当前开的玩家是对方，防止当前玩家叫20，或者达到了20循环，同时又叫了开
			findNextLiving();
			return true;
		}

		return false;
	}

	/**
	 * drive the game to next turn.
	 */
	public void nextTurn() {

		// System.out.println("nextTurn================================================================================================" + this.DiceNumber);
		if (gameState == GameState.Playing) {
			if (!isPlayerCall && currentTurnPlayer != null && !currentTurnPlayer.getPlayerDetail().getIsRobot()) {
				if (currentTurnPlayer.PlayerTimeOutCall()) {
					return;
				}
				currentTurnPlayer.addNoOperateTime();

			}

			// current turn count add one.
			turnIndex++;
			isPlayerCall = false;
			currentTurnPlayer = findNextLiving();
			sendGameNextTurn(currentTurnPlayer, this);

			// if current player is robot,add robot logical action.
			if (currentTurnPlayer.getPlayerDetail().getIsRobot()
					|| currentTurnPlayer.getPlayerDetail().getIsRobotState()) {
				ThreadSafeRandom random = new ThreadSafeRandom();
				AddAction(new RobotProcessAction(random.next(1, 6) * 1000, currentTurnPlayer));// 机器人随机一个叫的时间
				return;
			}

			// if the player has trusteed the game,add trustee action to deal
			// with the condition.
			// if (currentTurnPlayer.getPlayerState() == PlayerState.Trustee)
			// {
			// AddAction(new TrusteePlayerAction(random.next(3, 6) * 1000,
			// currentTurnPlayer));
			// return;
			// }

			// this turn wait for 20 seconds.
			WaitTime(1000 * 20);// csf0423 以前是12 + 8
			onBeginNewTurn();
		}
	}

	/***
	 * 发送下一轮的信息
	 */
	@Override
	public void sendGameNextTurn(Player living, BaseGame game) {
		Packet pkg = new Packet(UserCmdOutType.GAME_TURN);
		pkg.putInt(living.getPlayerID());
		pkg.putInt(lastTurnPlayer == null ? -1 : lastTurnPlayer.getPlayerID());
		pkg.putByte(getDiceNumber());
		pkg.putByte(getDicePoint());
		pkg.putBoolean(HasCallOne);

		sendToAll(pkg);
		addGameRecordList(pkg);

		// System.out.println("gamer sendGameNextTurn=========================================================" + getDiceNumber() + "====" + getDicePoint() + "====" +
		// getHasCallOne());
	}

	/**
	 * 游戏正在进行时候需要单独发送Gameturn信息
	 * 
	 * @param living
	 * @param game
	 * @param player
	 */
	@Override
	public void sendGameNextTurn(Player living, BaseGame game, Player player) {
		Packet pkg = new Packet(UserCmdOutType.GAME_TURN);
		pkg.putInt(living.getPlayerID());
		pkg.putInt(lastTurnPlayer == null ? -1 : lastTurnPlayer.getPlayerID());
		pkg.putByte(getDiceNumber());
		pkg.putByte(getDicePoint());
		pkg.putBoolean(HasCallOne);
		sendToPlayer(pkg, player);

	}

	@Override
	public Player findNextLiving() {
		if (turnPlayerQueue == null || turnPlayerQueue.size() < 1)
			return null;

		Player living = null;

		synchronized (turnPlayerQueue) {
			living = turnPlayerQueue.get(0);
			turnPlayerQueue.remove(0);
			turnPlayerQueue.add(living);
		}

		lastTurnPlayer = currentTurnPlayer;
		currentTurnPlayer = living;
		return currentTurnPlayer;
	}

	/**
	 * drive the game to start.
	 */
	@Override
	public void start() {
		super.start();
		resetDice();
		clearPlayer();
		// changing the state of all the players to playing.

		successID = 0;
		// 输家ID
		failID = 0;

		int taxes = hallType.getSystemTaxes();
		List<Player> list = getAllPlayers();
		for (Player player : list) {
			player.setPlayerState(PlayerState.Playing);
			player.getPlayerDetail().addCoins(taxes * -1);
			addPlayer(player);

			player.resetPlayer(hallType.getGameType());
		}
		int gamePlayerCount = list.size();// 记录游戏类型的局数
		GameMgr.addGameTypeCount(gamePlayerCount);
		sendUpdatePlayerState();

		sortQueue();
		SendGameStart(taxes);
		gameState = GameState.Playing;

		WaitTime(1 * 1000);
		// checkState(0);

	}

	@Override
	public void SendGameStart(int wager) {
		Packet packet = new Packet(UserCmdOutType.GAME_START);

		packet.putInt(wager);//
		packet.putInt(getTurnQueue().size());// 玩家总数
		// 这里发送此轮游戏中的玩家的信息
		for (Player player : getTurnQueue()) {
			packet.putInt(player.getPlayerDetail().getUserId());// 玩家ID
			packet.putInt(player.getPlayerDetail().getPlayerInfo().getCoins());
			packet.putByte(player.getSameCount());
			packet.putInt(player.getDiceNumbers().length);// 玩家的筛子数
			for (byte point : player.getDiceNumbers()) {
				packet.putByte(point);// 每一颗筛子的点数
			}
		}

		sendToAll(packet);
		addGameRecordList(packet);

	}

	@Override
	public void SendGameContinue(GamePlayer playerCon) {
		Packet packet = new Packet(UserCmdOutType.GAME_START);

		packet.putInt(0);//
		packet.putInt(getTurnQueue().size());// 玩家总数
		// 这里发送此轮游戏中的玩家的信息
		for (Player player : getTurnQueue()) {
			packet.putInt(player.getPlayerDetail().getUserId());// 玩家ID
			packet.putInt(player.getPlayerDetail().getPlayerInfo().getCoins());
			packet.putByte(player.getSameCount());
			packet.putInt(player.getDiceNumbers().length);// 玩家的筛子数
			for (byte point : player.getDiceNumbers()) {
				packet.putByte(point);// 每一颗筛子的点数
			}
		}

		playerCon.getOut().sendTCP(packet);
		// sendToAll(packet);
	}

	/**
	 * 游戏玩家队列排序
	 */
	private void sortQueue() {
		List<Player> tempList = new LinkedList<Player>();
		int currentPos = 0;
		if (currentTurnPlayer != null) {
			currentPos = ((Player) currentTurnPlayer).getPlayerDetail().getCurrentRoomPos();
			currentTurnPlayer = null;
		}
		synchronized (turnPlayerQueue) {
			for (int j = 0; j < 4; j++) {
				int index = 0;
				if (currentPos + (SortFlag * j) > 3) {
					index = currentPos + (SortFlag * j) - 4;
				} else if (currentPos + (SortFlag * j) < 0) {
					index = currentPos + (SortFlag * j) + 4;
				} else {
					index = currentPos + (SortFlag * j);
				}

				for (int i = 0; i < turnPlayerQueue.size(); i++) {
					int pos = ((Player) turnPlayerQueue.get(i)).getPlayerDetail().getCurrentRoomPos();
					if (pos == index) {
						tempList.add(turnPlayerQueue.get(i));
						break;
					}
				}
			}
			turnPlayerQueue.clear();
			turnPlayerQueue.addAll(tempList);
		}
	}

	@Override
	public void stop() {
		gameState = GameState.Stopped;
		GameTotal = 0;

		synchronized (players) {
			players.clear();
		}

		synchronized (turnPlayerQueue) {
			turnPlayerQueue.clear();
		}

		super.stop();
	}

	/**
	 * create dice points randomly,and send to client, at the same time change game state to Prepared.
	 */
	@Override
	public void sendShakeGame(Player player)// csf
	{
		byte sameCount = 0;
		byte[] diceNumbers = DicePointFactory.getDiceNumber(5);

		dicePoints.put(player.getPlayerID(), diceNumbers);
		player.setDiceNumbers(diceNumbers);
		player.setSameCount(sameCount);
		player.setDiceNumber(0);
		player.setDicePoint(0);
		player.setIsCallOne(false);// csf

		Packet packet = new Packet(UserCmdOutType.GAME_START_BOUT);
		packet.putByte(diceNumbers);
		packet.putByte(sameCount);
		sendToPlayer(packet, player);

		if (gameState != GameState.Prepared)
			gameState = GameState.Prepared;

		for (int i = 1; i <= 5; i++) {
			int point = diceNumbers[i - 1];
			if (dicePointsCount.containsKey(point))
				dicePointsCount.put(point, dicePointsCount.get(point) + 1);
			else
				dicePointsCount.put(point, 1);
		}
	}

	/**
	 * check the players in the game whether are ready.
	 * 
	 * @return true 没有得到；false 已经得到
	 */
	@Override
	public boolean isAllComplete() {
		List<Player> list = getAllPlayers();
		if (list.size() <= 1)
			return false;

		for (Player player : list) {
			if (player.getPlayerState() != PlayerState.EndShaked)
				return false;
		}
		return true;
	}

	/**
	 * 玩家叫开，改变turn顺序
	 * 
	 * @param openID
	 */
	public void changeTurn(int openID) {
		currentTurnPlayer = findPlayer(openID);
	}

	private int getGameRecordId() {
		int gameRecordId = 0;

		if (hallType.getHallType() == HallGameType.PENGYOU) {
			gameRecordId = this.getBaseRoom().getPengId();
		} else if (hallType.getHallType() == HallGameType.CONTEST) {

		}
		return gameRecordId;
	}

	/**
	 * if the game has only one player or the game is playing,return; otherwise,checking player's state,sending true message that means game can start when all player had shaked
	 * cellphone ,in other side,sending false message.
	 */
	@Override
	public void sendShakePrepare() {
		Collection<Player> list = getPlayers().values();
		if ((getGameState() == GameState.Created || getGameState() == GameState.Prepared) && list.size() > 1) {
			// 这里的逻辑有BUG,这个是发送给房间所有的用户是否可以摇筛子，如果用户已经摇过了就不可以再发
			// 把SendToALL改为一个个的判断发

			boolean isShaked = false;
			for (Player player : list) {
				if (player.getPlayerState() == PlayerState.EndShaked) {
					isShaked = true;
				}
			}
			if (isShaked) {
				sendNotShakeUserPrepare();
				return;
			}
			for (Player player : list) {
				Packet packet = new Packet(UserCmdOutType.SHAKE_PREPARE);
				isShaked = false;
				if (player.getPlayerState() == PlayerState.EndShaked) {
					isShaked = true;
				}
				if (!isShaked) {
					packet.putBoolean(isShaked);
					sendToPlayer(packet, player);
				}
			}
		}
	}

	@Override
	public void sendNotShakeUserPrepare() {
		Collection<Player> list = getPlayers().values();
		if ((getGameState() == GameState.Created || getGameState() == GameState.Prepared) && list.size() > 1) {
			for (Player player : list) {

				boolean isShaked = false;
				if (player.getPlayerState() == PlayerState.EndShaked) {
					isShaked = true;
				}
				if (!isShaked) {

					Packet packet = new Packet(UserCmdOutType.SHAKE_OTHERS_PREPARE);
					packet.putBoolean(isShaked);
					sendToPlayer(packet, player);
				}
			}
		}
	}

	/**
	 * change the state of player who id is userid
	 * 
	 * @param userId
	 * @param state
	 */
	@Override
	public void changePlayerRobotState(int userId, boolean isRobotState) {
		synchronized (players) {
			if (players.get(userId) != null) {
				players.get(userId).getPlayerDetail().setIsRobotState(isRobotState);
				players.get(userId).setRobotLevel(7);
			}

		}

		synchronized (turnPlayerQueue) {
			for (Player p : turnPlayerQueue)
				if (p.getPlayerID() == userId)
					p.getPlayerDetail().setIsRobotState(isRobotState);
		}
	}

}
