package com.citywar.manager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.citywar.bll.UserLetterBussiness;
import com.citywar.dice.entity.UserLetter;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.gameobjects.Player;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.util.HeadPicUtil;

/**
 * 私信管理类
 * 
 * @author zhiyun.peng
 * 
 */
public class LetterMgr {

	/** 离线私信 */
	private static Map<Integer, List<UserLetter>> offLine = new HashMap<Integer, List<UserLetter>>();
	
	private static List<UserLetter> addFriendTip = new ArrayList<UserLetter>();
	
    private static ReadWriteLock rwLock = new ReentrantReadWriteLock();

    
    
    /**
     * 添加离线私信
     */
	public static  void add(UserLetter letter) 
	{
		if(letter == null)
			return ;
		
		try {
			rwLock.writeLock().lock();
			
			if(letter.getType() == UserLetter.ADD_FRIEND 
					|| letter.getType() == UserLetter.HAS_FRIEND){
				addFriendTip.add(letter);
			}else{
				if(offLine.containsKey(letter.getReceiverId()))
				{
					offLine.get(letter.getReceiverId()).add(letter);
				}else{
					List<UserLetter> list = new ArrayList<UserLetter>();
					list.add(letter);
					offLine.put(letter.getReceiverId(), list);
				}
			}
			
		} finally{
			rwLock.writeLock().unlock();
		}
	}
	
	/**
     * 删除离线私信
     */
	public static List<UserLetter> delete(int userId){
		
		try {
			rwLock.writeLock().lock();
			return offLine.remove(userId);
		} finally{
			rwLock.writeLock().unlock();
		}
	}
	public static List<UserLetter> deleteAddFriend(int userId){
		try {
			rwLock.writeLock().lock();
			List<UserLetter> result = new ArrayList<UserLetter>();
			for (UserLetter letter : addFriendTip) {
				if(letter.getReceiverId() == userId){
					result.add(letter);
				}
			}
			addFriendTip.removeAll(result);
			return result;
		} finally{
			rwLock.writeLock().unlock();
		}
	}
	
	/**
     * 把离线私信入库 并清空
     */
	public static void saveToDB(){
		
		if(offLine == null || offLine.size() == 0)
		{
			return ;
		}
		
		try {
			rwLock.writeLock().lock();
			
			List<UserLetter> saveList = new ArrayList<UserLetter>();
			Iterator<Integer> keys = offLine.keySet().iterator();
			while(keys.hasNext()){
				saveList.addAll(offLine.remove(keys.next()));
			}
			saveList.addAll(addFriendTip);
			UserLetterBussiness.insertEntity(saveList);
			offLine.clear();
			addFriendTip.clear();
			
		} finally{
			rwLock.writeLock().unlock();
		}
	}

	
	public static  void send(UserLetter letter) 
    {

		GamePlayer receiver = WorldMgr.getPlayerByID(letter.getReceiverId());
		if (null == receiver) {
			//从在值班的机器人中取
			Player robot = RobotMgr.getOnDutyRobotByID(letter.getReceiverId());
			if (null != robot) {
				receiver = robot.getPlayerDetail();
			}
		}
		
		if(receiver != null) //私信接收者在线 
		{
			sendOnLineLetter(receiver, letter);
			
			//发送者和接收者 不处于即时聊天状态时  作为未读私信放入内存
			if(receiver.getMsgBox().getLetterUserId() != letter.getSenderId())
			{
				receiver.getMsgBox().putLetter(letter);
			}
		}
		else {//私信接收者不在线 马上入库(否则在定时入库之前 接受者登录 将看不到信息)
			add(letter);			
		}
    }
	
	
	public static void sendOnLineLetter(GamePlayer receiver,UserLetter letter){
    	
    	if(receiver != null && letter != null )
    	{
    		
    		String tsStr = "";
        	DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");  
        	tsStr = sdf.format(letter.getSendTime());   
        	
    		Packet packet = new Packet(UserCmdOutType.USER_REVICE_LETTER);
    		packet.putInt(letter.getSenderId());
    		packet.putByte((byte)letter.getSenderVip());
    		packet.putStr(letter.getSenderName());
    		packet.putStr(HeadPicUtil.getRealSmallPicPath(letter.getSenderId(),letter.getSenderPic()));
    		packet.putStr(tsStr);
    		packet.putStr(letter.getContent());
    		packet.putInt(letter.getType());
    		packet.putInt(letter.getReceiverId());
    		packet.putStr(letter.getReceiverName());
   
    		
    		receiver.getOut().sendTCP(packet);
    	}
    	
    }
	
	
}
