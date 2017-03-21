package com.citywar.gameutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.citywar.bll.UserLetterBussiness;
import com.citywar.dice.entity.Option;
import com.citywar.dice.entity.UserLetter;
import com.citywar.game.GamePlayer;
import com.citywar.gameobjects.UserNoReadyLetter;
import com.citywar.manager.LetterMgr;

public class PlayerMessagegBox {
	
//    private static final Logger logger = Logger.getLogger(PlayerMessagegBox.class.getName());
    
	private final ReadWriteLock lock ;
	
	private final  int userID;
	
	/** 未读私信  登录的时候全部load出来*/
	private Map<Integer, List<UserLetter>> noReadLetter;
	
	/** 已读私信  玩家退出时 把已经读过的数据 从数据库删除*/
	private List<UserLetter> hasReadLetter;
	
	/** 在线收到的私信    如果用户没读这些私信  当玩家退出游戏时  保存入库  */
	private Map<Integer, List<UserLetter>> onLineLetter;
	
	/** 自上次发送数据给客户端后  又收到了新的数据*/
	private boolean hasNewLetter = false;
	
	/** 添加好友提示私信*/
	private List<UserLetter> tipsLetter;
	
	   /** 玩家当前私信对象的ID*/
    private int letterUserId;
	
	public PlayerMessagegBox(GamePlayer gameplayer)
	{
		this(gameplayer.getUserId());
	}
	public PlayerMessagegBox(int userId){
		userID = userId;
		lock = new ReentrantReadWriteLock();
		onLineLetter  = new HashMap<Integer, List<UserLetter>>();
		noReadLetter  = new HashMap<Integer, List<UserLetter>>();
		hasReadLetter = new ArrayList<UserLetter>();
		tipsLetter = new ArrayList<UserLetter>();
	}
	public void loadDataFromDB()
	{
		//从数据库中查询离线未读私信
		List<UserLetter> selfLetters =  UserLetterBussiness.selectNoReadLetter(userID);
		
		if(selfLetters != null && selfLetters.size()>0)
		{
			for(UserLetter data: selfLetters)
			{
				addNoReadUserLetter(data,noReadLetter);
				hasNewLetter = true;
			}
		}
		
		//从内存中查询离线未读私信 作为在线私信
		List<UserLetter> result = LetterMgr.delete(userID);
		if(result!=null  && result.size()>0){
			for(UserLetter data: result)
			{
				addNoReadUserLetter(data,onLineLetter);
				hasNewLetter = true;
			}
		}
		
		//查询添加好友的私信内容
		tipsLetter = UserLetterBussiness.selectAddFriendLetter(userID);
		if(tipsLetter == null){
			tipsLetter = new ArrayList<UserLetter>();
		}
		//从内存中查询离线未读的添加好友参数的私信
		tipsLetter.addAll(LetterMgr.deleteAddFriend(userID));
		
		//同一个用户对自己 的 添加 删除好友的私信只保留一条
		if(tipsLetter != null ){
			List<UserLetter> tipsResult = new ArrayList<UserLetter>();
			for (int i = 0; i < tipsLetter.size(); i++) {
				coverTips(tipsResult, tipsLetter.get(i));
			}
			tipsLetter = tipsResult;
		}

		
		if(tipsLetter != null && tipsLetter.size()>0){
			hasNewLetter = true;
		}
	}
	
	/** 
	 * 去除添加好友私信重复的问题 
	 * 
	 * */
	private void coverTips(List<UserLetter> result,UserLetter coverLetter){
		if(result != null && coverLetter != null ){
			UserLetter currentLetter = null;
             for (int i = 0; i < result.size(); i++) {
            	 currentLetter = result.get(i);
            	 
            	 if(currentLetter.getType() == UserLetter.ADD_FRIEND 
            			 || currentLetter.getType() == UserLetter.HAS_FRIEND){
            		 
            		 if(currentLetter.getSenderId() == coverLetter.getSenderId()
            				 && currentLetter.getType() == coverLetter.getType()){
            			 
            			 //如果集合中存在同一个用户的添加好友私信 则后面的私信作为已读私信
            			 hasReadLetter.add( coverLetter );
            			 return ;
            		 }
				}
			}
             result.add(coverLetter);
		}
		
	}
	
	private void addNoReadUserLetter(UserLetter addUserLetter,Map<Integer, List<UserLetter>> container){
		
		if(addUserLetter == null || addUserLetter.getSenderId()<=0){
			return ;
		}
		List<UserLetter> list =container.get(addUserLetter.getSenderId());
		if(list == null){
			list = new ArrayList<UserLetter>();
			container.put(addUserLetter.getSenderId(), list);
		}
		list.add(addUserLetter);
	}
	
	
	/**
	 * 
	 * 往内存中存放私信
	 * 
	 */
	public boolean putLetter(UserLetter putData) {
		boolean result = false;

		try {
			lock.writeLock().lock();
			switch (putData.getType()) {
			case UserLetter.USER_LETTER:
			case UserLetter.SEND_GIFT:
				addNoReadUserLetter(putData, onLineLetter);
				break;
			case UserLetter.ADD_FRIEND:
			case UserLetter.HAS_FRIEND:
				tipsLetter.add(putData);
//				System.out.println("未知私信====添加====" + userID + "====="  + tipsLetter.size());
				break;
			default:
				throw new IllegalArgumentException(" 程序异常 类型");
			}

			hasNewLetter = true;
			result = true;
		} finally {
			lock.writeLock().unlock();
		}

		return result;
	}
	
	/**
	 * 取出添加好友所产生的提示私信
	 * @return
	 */
	public List<UserLetter> readAddFriend()
	{ 
		List<UserLetter> result = new ArrayList<UserLetter>();
		try{
			lock.writeLock().lock();

//			System.out.println("未知私信====获取====" + userID + "====="  + tipsLetter.size());
			result.addAll(tipsLetter);
			hasReadLetter.addAll(tipsLetter);
			tipsLetter.clear();
		}finally{
			lock.writeLock().unlock();
		}
		return result;
	}
	/** 
	 * 读某玩家发给自己的私信
	 * */
	public List<UserLetter> readLetter(int sendId){
		List<UserLetter> result = new ArrayList<UserLetter>();
		
		try{
			lock.writeLock().lock();
			
			//离线的私信
			List<UserLetter> noReadList = noReadLetter.remove(sendId);
			if(noReadList!= null )
			{
				for(UserLetter currentData:noReadList)
				{
					currentData.setOp(Option.DELETE);
					result.add(currentData);
					hasReadLetter.add(currentData);
				}
			}
			
			//在线的私信
			List<UserLetter> onLineList = onLineLetter.remove(sendId);
			if(onLineList!= null)
				{
				for(UserLetter currentData:onLineList)
				{
					result.add(currentData);
				}
			}
			
		}finally{
			lock.writeLock().unlock();
		}
		return result;
	}
	public void cleanLetter(int sendId){
		try{
			lock.writeLock().lock();
			
		  //离线的私信
			List<UserLetter> removeData = noReadLetter.remove(sendId);
			if(removeData!=null){
				for(UserLetter currentData:removeData)
				{
					currentData.setOp(Option.DELETE);
					hasReadLetter.add(currentData);
				}
			}
		  //在线的私信
		  onLineLetter.remove(sendId);
			
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	/**
	 * 玩家退出时  把未读信息入库
	 */
	public void saveDataToDB(){
		
		try{
			
			//删除已经度过的私信
			lock.writeLock().lock();
			UserLetterBussiness.delete(hasReadLetter);
			hasReadLetter.clear();
			
			//把在线收到的未读私信入库,并把在线私信转为未读私信
			List<UserLetter> addList = new ArrayList<UserLetter>();
			
			//把添加好友的提示私信保存入库
			for (UserLetter  currentData : tipsLetter) {
				if(currentData.getOp() == Option.INSERT){
					addList.add(currentData);
				}
			}
			
			Integer key;
			List<UserLetter> currentValue;
			Iterator<Integer> keys = onLineLetter.keySet().iterator();
			while(keys.hasNext()){
				
				key = keys.next();
				currentValue = onLineLetter.get(key);
				
				if(currentValue != null && currentValue.size()>0){
					
					addList.addAll(currentValue);
					
					if(noReadLetter.containsKey(key) && noReadLetter.get(key)!=null){
						noReadLetter.get(key).addAll(currentValue);
					}else{
						noReadLetter.put(key, currentValue);
					}
				}
			}
			UserLetterBussiness.insertEntity(addList);
			onLineLetter.clear();
			tipsLetter.clear();
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public boolean hasNewLetter(){
		return hasNewLetter;
	}
	
	/**
	 * @return 指定的玩家还有几条未读短信
	 */
	public int getNoReadCount(int senderId){
		int resultCount = 0;
		if(noReadLetter.get(senderId) != null){
			resultCount += noReadLetter.get(senderId).size();
		}
		if(onLineLetter.get(senderId) != null){
			 resultCount += onLineLetter.get(senderId).size();
		}
		
		return resultCount;
	}
	/**
	 * 用户还有多少未读私信
	 * 
	 * 
	 * @return (好友提示未读私信+离线私信+在线私信)
	 */
	public int getNoReadCount(){
		
		try{
			lock.readLock().lock();
			
			int resultCount = 0;
			
			
			if(tipsLetter !=null && tipsLetter.size()>0){
				resultCount += tipsLetter.size();
			}
			
			Map<Integer, Object> keys = new HashMap<Integer, Object>();
			keys.putAll(noReadLetter);
			keys.putAll(onLineLetter);
			Iterator<Integer> it =keys.keySet().iterator();
			while(it.hasNext()){
				resultCount += getNoReadCount(it.next());
			}
			return resultCount;
		}finally{
			lock.readLock().unlock();
		}
		
		
	}
	
	
	public List<UserNoReadyLetter> getNoReadLetter() 
	{
		 List<UserNoReadyLetter> result = new ArrayList<UserNoReadyLetter>();
		
		Map<Integer, Object> keys = new HashMap<Integer, Object>();
		keys.putAll(noReadLetter);
		keys.putAll(onLineLetter);
		
		Iterator<Integer> it =keys.keySet().iterator();
		UserLetter currentData = null;
		int senderId = 0;
		while(it.hasNext()){
			senderId = it.next();
			currentData =getUserLastLetter(senderId);
			if(currentData != null){
				result.add(new UserNoReadyLetter(currentData, getNoReadCount(senderId)));
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @return 指定玩家发送给自己的最后一条私信
	 */
	private  UserLetter getUserLastLetter(Integer userId){
		
		List<UserLetter> data = onLineLetter.get(userId);
		if(data != null && data.size()>0)
		{
			return data.get(data.size()-1);
		}
		
		data = noReadLetter.get(userId);
		if(data != null && data.size()>0)
		{
			return data.get(data.size()-1);
		}
		return null;
	}
	public int getLetterUserId() {
		return letterUserId;
	}
	public void setLetterUserId(int letterUserId) {
		this.letterUserId = letterUserId;
		
	}
}
