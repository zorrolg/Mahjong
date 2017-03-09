package com.citywar.util;

public class HeadPicUtil {
	/**
     * 构造玩家图像的地址的静态字符串,通过Ningx访问80端口直接映射到Web服务器的image文件夹,Tomcat在8081端口,
     */
//	private static final String SERVER_ADDRESS = "http://192.168.1.204/uploadimg/";
	
	/**
	 * 站位机器人头像根路径
	 */
//	private static final String ROOMROM_ADDRESS = SERVER_ADDRESS+"roomRobPic/";
	
	
	private static String SERVER_ADDRESS = Config.getValue("picserver.path");
	private static String ROOMROM_ADDRESS = SERVER_ADDRESS + "rob/";
	
	
	/**
	 * 获取头像的缩略图格式
	 * @param picpath
	 * @return
	 */
	public static String getSmallPicPath(String picpath) 
	{
		return picpath;
		
//		if(picpath == null ){
//			return "";
//		}
//		int idx = picpath.lastIndexOf(".");
//		
//		if (idx != -1) 
//		{
//			picpath = picpath.substring(0, idx) + "_68x68" + picpath.substring(idx);
//		}
//		return picpath;
	}
	
	
	public static String getRealPicPath(int userId,String picPath)
	{
		if (picPath == null || userId <= 0 || picPath.isEmpty())
			return "";

		return picPath;//SERVER_ADDRESS + userId + "/" + picPath;
	}
	
	public static String getRealSmallPicPath(int userId,String picPath)
	{
		if (picPath == null || userId <= 0 || picPath.isEmpty() )
			return "";
		
		return getRealPicPath(userId, getSmallPicPath(picPath));
	}
	/**
	 * 得到站位机器人的完整头像路径
	 */
	public static String getRealRoomRobPicPath(String picPath)
	{
		return ROOMROM_ADDRESS + picPath;
	}
	public static String getMiddlePicPath(String picpath)
	{
		return picpath;
//		if(picpath == null ){
//			return "";
//		}
//		
//		int idx = picpath.lastIndexOf(".");
//		
//		if (idx != -1) 
//		{
//			picpath = picpath.substring(0, idx) + "_180x180" + picpath.substring(idx);
//		}
//		return picpath;
	}
}
