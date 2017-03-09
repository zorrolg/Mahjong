package com.citywar.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import com.citywar.bll.FeedbackBussiness;
import com.citywar.dice.entity.FeedbackInfo;
import com.citywar.dice.entity.Option;

/**
 * 用户反馈管理器
 * 
 * @author shanfeng.cao
 * 
 */
public class FeedbackMgr {
	
	/*
	 * 保存用户反馈过来的需求。没过一段时间再同时入库
	 */
	private static List<FeedbackInfo> AllUserFeedbackstList;
	
	private static ReadWriteLock rwLock;

	private static final Logger logger = Logger.getLogger(FeedbackMgr.class.getName());

	/**
	 * 用户反馈定时入库管理初始化
	 * 
	 * @return
	 */
	public static boolean init() {
		rwLock = new ReentrantReadWriteLock(false);
		AllUserFeedbackstList = new ArrayList<FeedbackInfo>();
		return true;
	}
	

	/**
	 * 把用户反馈保存到数据库中去
	 * 
	 * @return
	 */
	public static void saveIntoDB() {
		List<FeedbackInfo> updateFeedback = new ArrayList<FeedbackInfo>();
		List<FeedbackInfo> insertFeedback = new ArrayList<FeedbackInfo>();
		rwLock.readLock().lock();
	    try
	    {
			for (FeedbackInfo info : AllUserFeedbackstList) {
				if (info == null)
					continue;
				switch (info.getOp()) {
				case Option.INSERT:
					insertFeedback.add(info);
					break;
				case Option.UPDATE:
					updateFeedback.add(info);
					break;
				default:
					break;
				}
				info.setOp(Option.NONE);
				FeedbackBussiness.insertUserFeedbacks(insertFeedback);//插入新的
				FeedbackBussiness.updateUserFeedbacks(updateFeedback);//更新需要更新的
			}
			AllUserFeedbackstList.clear();
	    } catch (Exception e) {
			logger.error("[ FeedbackMgr : saveIntoDBByList ]", e);
		} finally {
	    	rwLock.readLock().unlock();
		}
	}
	
	public static void addFeedback(FeedbackInfo info) {
		rwLock.writeLock().lock();
	    try
	    {
	    	AllUserFeedbackstList.add(info);
		} finally {
	    	rwLock.writeLock().unlock();
		}
	}
}
