package com.citywar.bll.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.PropertyConfigurator;

import com.citywar.bll.ItemBussiness;
import com.citywar.bll.TaskBussiness;
import com.citywar.dice.db.DBManager;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.dice.entity.TaskInfo;
import com.citywar.dice.entity.UserItemInfo;
import com.citywar.dice.entity.UserTaskInfo;
import com.citywar.util.Config;

public class SynchroMain
{

    /**
     * @param args
     */

    private static String configPath;

    public static void main(String[] args)
    {
        if (args.length <= 0)
        {
            System.err.println("请输入配置文件地址路径...");
            return;
        }
        configPath = args[0];
        if (!Config.initConfig(configPath))
        {
            //System.out.println("加载服务器配置文件失败！");
            return;
        }

        PropertyConfigurator.configure(Config.getValue("log4j.path"));

        if (!DBManager.initConfig(Config.getValue("db.path")))
        {
            //System.out.println("加载DB配置文件失败！");
            return;
        }

        long time = System.currentTimeMillis();

        System.err.println("启动耗时: " + (System.currentTimeMillis() - time));
        //System.out.println("DB 测试启动成功!");

        synchro1();
    }

    /**
     * 太慢了
     */
	@SuppressWarnings("unused")
	private static void synchro3() {
		try {
			Map<Integer, PlayerInfo> all = new HashMap<Integer, PlayerInfo>();
			List<PlayerInfo> allList = PlayerInfoTest.getAllPlayer("t_u_player");
			for(PlayerInfo info1 : allList) {
				all.put(info1.getUserId(), info1);
			}
			List<PlayerInfo> allListCopy = PlayerInfoTest.getAllPlayer("t_u_player_copy");
			for(PlayerInfo info2 : allListCopy) {
				if(null == info2.getAccount()) {
					continue ;
				}
				if(all.containsKey(info2.getUserId())) {
					PlayerInfo info1 = all.get(info2.getUserId());
					if(null == info1
							|| null == info1.getAccount()) {
						continue ;
					}
					if(info1.getAccount().equals(info2.getAccount()) &&
								info1.getUserPwd().equals(info2.getUserPwd())
								&& info2.getCoins() >= info1.getCoins()) {
						PlayerInfoTest.updatePlayer(info2);
						//System.out.println(info1.getAccount() +"==="+ info2.getAccount());
					}
				} else {
					PlayerInfoTest.addPlayer(info2);
					//System.out.println(info2.getAccount() +"+++");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * 太慢了
     */
	@SuppressWarnings("unused")
	private static void synchro2() {
		try {
			Map<Integer, UserItemInfo> all = new HashMap<Integer, UserItemInfo>();
			List<UserItemInfo> allList = ItemBussiness.getAllUserItemInfos();
			List<UserItemInfo> tempList = new ArrayList<UserItemInfo>();
			for(UserItemInfo info1 : allList) {
				all.put(info1.getUserId(), info1);
			}
			List<UserItemInfo> allListCopy = ItemBussiness.getAllUserItemInfos("t_u_item_copy");
			for(UserItemInfo info2 : allListCopy) {
				if(all.containsKey(info2.getItemId())) {
					UserItemInfo info1 = all.get(info2.getItemId());
					if(info1.getUserId()==info2.getUserId() &&
								info1.getTemplateId()==info2.getTemplateId() &&
								info1.getCount()<info2.getCount()
						) {
						tempList.add(info2);
					}
				} else {
					ItemBussiness.addUserItem(info2);
				}
			}
			ItemBussiness.updateItem(tempList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * 太慢了
     */
	private static void synchro1() {
		try {
			List<PlayerInfo> allList = PlayerInfoTest.getAllPlayer("t_u_player");
			for(PlayerInfo info2 : allList) {
				if(null == info2.getAccount() ||
						null == info2.getRegisterDate()) {
					continue ;
				}
				List<UserTaskInfo> taskList = TaskBussiness.getUserYesterdayTasks(info2.getUserId());
				if(null == taskList || taskList.size() <= 0) {//特别重要 csf
		        	taskList = TaskBussiness.getAllUserTasks(info2.getUserId());
		        }
				if(null == taskList || taskList.size() <= 0) {//特别重要 csf
					TaskInfo task =  new TaskInfo();
					task.setTaskId(1);
					UserTaskInfo userTaskInfo = new UserTaskInfo(info2.getUserId(), task);
	                boolean result = TaskBussiness.insertUserCounldCompletedTask(userTaskInfo);
	                System.out.println("------------------------------" + result);
		        }
			}
			//System.out.println("------------------------------");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
