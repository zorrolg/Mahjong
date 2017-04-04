/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.game.action.Dice;

import com.citywar.game.BaseGame;
import com.citywar.game.DiceGame;
import com.citywar.game.Player;
import com.citywar.game.action.IAction;
import com.citywar.socket.Packet;
import com.citywar.type.DiceCmdType;
import com.citywar.type.GameState;

/**
 * 
 * @author Dream
 * @date 2011-12-15
 * @version
 * 
 */
public class DiceProcessAction implements IAction
{
    private Player player;

    private Packet packet;

    public DiceProcessAction(Player player, Packet pkg)
    {
        this.player = player;
        this.packet = pkg;
    }

    public void execute(BaseGame game, long tick)
    {
        byte cmdId = (byte) this.packet.getParam1();
        DiceGame diceGame = (DiceGame) game;
        if(diceGame.getGameState() == GameState.GameOver) {
        	return ;
        }
        // 游戏内逻辑处理
        switch (cmdId)
        {
            // 游戏开叫，结果分发给用户
            case DiceCmdType.CALL:
                // 如果开叫玩家不是游戏里当前玩家，无视
                if (null == diceGame.getCurrentTurnPlayer()
                	|| this.player.getPlayerID() != diceGame.getCurrentTurnPlayer().getPlayerID())
                    return;

                byte diceNumber = this.packet.getByte();
                byte dicePoint = this.packet.getByte();
                boolean isCallOne = this.packet.getBoolean();
                int playerSize = diceGame.getTurnQueue().size(); 
//                System.out.println("gamer call=========================================================" + diceNumber + "====" + dicePoint + "====" +isCallOne);
                //System.out.println("玩家叫的时候diceGame.getTurnQueue()的大小为==" + playerSize);
                int LowestCanCallNumber = player.getLowestCanCallNum(diceGame, dicePoint, isCallOne);
                if(diceNumber < LowestCanCallNumber 
                		|| diceNumber < playerSize
                		|| diceNumber > (playerSize * 5) 
                		|| dicePoint < 1 
                		|| dicePoint > 6) {
                	//System.out.println("叫数出现异常...不处理");
                	break;
                }
                
                diceGame.setDiceNumber(diceNumber);
                diceGame.setDicePoint(dicePoint);
                diceGame.setPlayerCall(true);
                if (!diceGame.getHasCallOne())
                {
                	diceGame.setHasCallOne(isCallOne);
                }
                //如果用户叫的数量等于当前玩家的数量那么服务器强制认为是斋    
                if (diceGame.getDiceNumber() == diceGame.getTurnQueue().size()
                		|| dicePoint == 1) 
                {
                	diceGame.setHasCallOne(true);
                }
                
                diceGame.checkState(0);
                break;

            // 开游戏
            case DiceCmdType.OPEN:
            	
            	int openID = packet.getClientId();
            	
            	//System.out.println("DiceCmdType.OPEN====================0============" + openID);
            	
                
            	if(null == diceGame.getLastTurnPlayer()
            			|| diceGame.getLastTurnPlayer().getPlayerID() == openID
            			|| diceGame.getTurnIndex() == 0) {
            		break;
            	}
            	
            	//System.out.println("DiceCmdType.OPEN====================1============");
                boolean isGrab = packet.getBoolean();
                if (null != diceGame.getCurrentTurnPlayer()
                		&& this.player.getPlayerID() != diceGame.getCurrentTurnPlayer().getPlayerID()) {
                	isGrab = true;
                }
                
                if(diceGame.getTurnQueue().size() <= 2)
                	isGrab = false;
                
                
                player.setNoOperateTime(0);
//                if(isGrab)
//                {
//                	int cardId = player.getPlayerDetail().getCardBag().useCardItemByType(CardType.ACT_GRABOPEN, 1);                	
//                	if(cardId == 0)
//                	{
//                		Packet pkg = new Packet(UserCmdOutType.GAME_OPEN);
//                		pkg.putBoolean(false);
//                		pkg.putStr(LanguageMgr.getTranslation("CityWar.GameOpen.NotHaveGrabCard"));
//                        player.getPlayerDetail().getOut().sendTCP(pkg);
//                        break;
//                	}
//                	
//                	player.getPlayerDetail().getOut().sendCardUsed(cardId);
//                	
//                }
                
                //System.out.println("DiceCmdType.OPEN====================2============");
                
                diceGame.changeTurn(openID);
                diceGame.gameOver(isGrab);
                break;

            default:
                break;
        }
    }

    public boolean isFinished(BaseGame game, long tick)
    {
        return true;
    }
}
