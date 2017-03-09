package com.citywar.bll.test;

import org.apache.log4j.PropertyConfigurator;

import com.citywar.dice.db.DBManager;
import com.citywar.util.Config;

public class TestMain
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

        testReferenceslog();
    }

    @SuppressWarnings("unused")
	private static void testPlayerInfo()
    {
        // PlayerInfoTest.getUserNameById();
        PlayerInfoTest.query();
    }

    @SuppressWarnings("unused")
    private static void testFriends()
    {
        FriendsTest.addFriends();
    }
    
    @SuppressWarnings("unused")
    private static void testReferences()
    {
    	ReferenceTest.addReferences();
    }

    @SuppressWarnings("unused")
    private static void testItem()
    {
		 UserItemTest.getSingleList(10511);
		 UserItemTest.addItem(4, new int[] { 1001, 1002 });
    }
    
    @SuppressWarnings("unused")
    private static void testFeedback()
    {
    	FeedbackTest.updateFeedbacks();
    	FeedbackTest.selectFeedbacks();
    }

    private static void testReferenceslog()
    {
//    	ReferenceLogTest.addReferenceLogs();
//    	ReferenceLogTest.selectReferenceLogs();
//    	ReferenceLogTest.updateReferenceLogs();
    	ReferenceLogTest.selectAllReferenceLogs();
    }

    @SuppressWarnings("unused")
    private static void testGiftTemplate()
    {
    	GiftTemplateTest.queryList();
    }

    @SuppressWarnings("unused")
    private static void testUserGift()
    {
    	UserGiftTest.addReferenceLogs();
    	UserGiftTest.queryList();
    	UserGiftTest.updateReferenceLogs();
    	UserGiftTest.queryList();
    	UserGiftTest.selectAllUserGiftIdAndSumCount();
    }
}
