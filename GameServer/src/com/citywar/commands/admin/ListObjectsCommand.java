/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.commands.admin;

import java.util.List;

import com.citywar.commands.AbstractCommandHandle;
import com.citywar.commands.ConsoleCmdAnnotation;
import com.citywar.commands.PrivLevel;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.gameobjects.Player;
import com.citywar.hall.BaseHall;
import com.citywar.manager.HallMgr;
import com.citywar.manager.ManagerClient;
import com.citywar.manager.RobotMgr;
import com.citywar.manager.RoomMgr;
import com.citywar.manager.WorldMgr;
import com.citywar.room.BaseRoom;
import com.citywar.util.CommonUtil;

/**
 * @author tracy
 * @date 2012-1-16
 * @version
 * 
 */
@ConsoleCmdAnnotation(cmdString = "&list", description = "   服务器信息大全.", level = PrivLevel.Player, usage = {
        "       /list -rl :                    列举所有正在游戏的 room 对象",
        "       /list -pl :                    列举所有 player 对象",
        "       /list -robot :                    列举所有 正在游戏的 Robot对象",
        "       /list -pi [userid] :                    列举指定 userid 的玩家信息",
        "       /list -pn [nickname] :                    列举指定 nickname 的玩家信息",
        "       /list -ri [roomid] :                    列举指定 roomid 房间的信息",
        "       /list -k [roomid] [userid] :                    将指定 roomid 房间中的 userid 玩家踢出" })
public class ListObjectsCommand extends AbstractCommandHandle
{

    @Override
    public boolean onCommand(ManagerClient client, List<String> args)
    {

        if (args.size() > 1)
        {
            if ("-rl".equalsIgnoreCase(args.get(1)))
            {
                // 列出正在使用的房间信息
                displayMessage(client, "room list:");
                displayMessage(client, "-------------------------------");
                List<BaseRoom> rs = RoomMgr.getAllUsingRoom();
                int roomSize = rs.size();
                for (BaseRoom room : rs)
                {
                    displayMessage(client, room.toString());
                }

                displayMessage(client, "-------------------------------");
                displayMessage(client, String.format("total:%s", roomSize+""));
            }
            else if ("-ri".equalsIgnoreCase(args.get(1)))
            {
                displayMessage(client, "room list:");
                displayMessage(client, "-------------------------------");
                // 根据 userId 查找并显示对象信息
                int id = Integer.parseInt(CommonUtil.isNullOrEmpty(args.get(2)) ? "-1"
                        : args.get(2)); // TODO 这样判断限制不够完善 ? 还是使用异常处理,
                                        // 兼容用户输入不能转型的字符串 ?
                BaseHall tempHall = HallMgr.getHallById(id); // 获取到指定大厅对象
                int roomId = 0;//TODO 兼容性写法,待测试
                BaseRoom tempRoom = tempHall.getRoomById(roomId);
            	for (GamePlayer player : tempRoom.getPlayers())
                {
                    displayMessage(client, player.toString());
                }

                displayMessage(client,
                               tempRoom == null ? "could not find room [" + id
                                       + "]" : tempRoom.toString());
                displayMessage(client, "-------------------------------");
            }
            else if ("-pl".equalsIgnoreCase(args.get(1)))
            {
                // 列出所有游戏玩家对像信息
                displayMessage(client, "player list:");
                displayMessage(client, "-------------------------------");
                List<GamePlayer> ps = WorldMgr.getAllPlayers();
                int playerSize = ps.size();
                for (GamePlayer player : ps)
                {
                    displayMessage(client, player.toString());
                }
                displayMessage(client, "-------------------------------");
                displayMessage(client, String.format("total:%s", playerSize+""));
            }
            else if ("-pi".equalsIgnoreCase(args.get(1)))
            {
                displayMessage(client, "player list:");
                displayMessage(client, "-------------------------------");
                // 根据 userId 查找并显示对象信息
                int id = Integer.parseInt(CommonUtil.isNullOrEmpty(args.get(2)) ? "-1"
                        : args.get(2));
                GamePlayer tempPlayer = WorldMgr.getPlayerByID(id);

                displayMessage(client,
                               tempPlayer == null ? "could not find plyaer ["
                                       + id + "]" : tempPlayer.toString());
                displayMessage(client, "-------------------------------");
            }
            else if ("-pn".equalsIgnoreCase(args.get(1)))
            {
                // 根据 userName 查找并显示对象信息
                displayMessage(client, "player list:");
                displayMessage(client, "-------------------------------");
                List<GamePlayer> ps = WorldMgr.getAllPlayers();
                String name = CommonUtil.isNullOrEmpty(args.get(2)) ? "INPUT ERROR"
                        : args.get(2);

                GamePlayer tempPlayer = null;
                for (GamePlayer player : ps)
                {
                    if (player.getPlayerInfo().getUserName().equals(name))
                    {
                        tempPlayer = player;
                        break;
                    }
                }
                displayMessage(client,
                               tempPlayer == null ? "could not find plyaer ["
                                       + name + "]" : tempPlayer.toString());
            }

            else if ("-robot".equalsIgnoreCase(args.get(1)))
            {
                displayMessage(client, "robot list:");
                displayMessage(client, "-------------------------------");
                // 列出当前机器人数量信息
                int playingCount = 0;
                int notPlayingCount = 0;
                for (Player p : RobotMgr.getAllRobotPlayer())
                {
                    // displayMessage(client,
                    // p.getPlayerDetail().getPlayerInfo().getUserName()
                    // + " state:" + p.isPlaying());
                    if (p.isPlaying())
                        playingCount++;
                    else
                        notPlayingCount++;
                }
                displayMessage(client,
                               String.format("Robot Total:%d", playingCount
                                       + notPlayingCount));
                displayMessage(client,
                               String.format("Robot Playing:%d", playingCount));
                displayMessage(client, String.format("Robot Waiting:%d",
                                                     notPlayingCount));
            }
            else if ("-k".equalsIgnoreCase(args.get(1)))
            {
                // 踢出指定玩家
                int roomId = Integer.parseInt(CommonUtil.isNullOrEmpty(args.get(2)) ? "-1"
                        : args.get(2));
                int userId = Integer.parseInt(CommonUtil.isNullOrEmpty(args.get(3)) ? "-1"
                        : args.get(3));
                String message = "Could not find player ! room[ " + roomId
                        + " ] player[ " + userId + " ]";
                int hallId = 0;
                BaseHall hall = HallMgr.getHallById(hallId);
                //TODO 兼容性写法,待测试
                BaseRoom room = hall.getRoomById(roomId);
            	for (GamePlayer player : room.getPlayers())
                {
                    if (player.getUserId() == userId)
                    {

                        message = room.removePlayer(player, false, true, false, 0) ? "Remove Succussed !"
                                : "Remove Failed!";
                    }
                }
                displayMessage(client, message);
            }
        }
        else
        {
            displaySyntax(client);
        }
        return true;
    }
}
