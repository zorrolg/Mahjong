package com.citywar.usercmd.command;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import com.citywar.bll.PlayerBussiness;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

/**
 * 用户上传头像指令对应的处理类
 * @author Jacky.zheng
 * @date 2012-03-13
 * @version 1.0
 *
 */
@UserCmdAnnotation(code = UserCmdType.UPLOAD_USER_HEAD_PIC, desc = "上传用户头像")
public class UserUploadHeadPicCmd extends AbstractUserCmd {
	
    private static final Logger logger = Logger.getLogger(UserUploadHeadPicCmd.class.getName());

	@Override
	public int execute(GamePlayer player, Packet packet) {
		try 
		{
			int userId = packet.getInt();
			
			if (userId <= 0 ) {
				return 0;
			}
			byte[] bytes = packet.getBytes();//照片数据流
			
			String dir = System.getProperty("user.dir");
			String separator = System.getProperty("file.separator");
			
			File file = UserUploadHeadPicCmd.getFileFromBytes(bytes, dir + separator + player.getUserId() + ".png");
			
			boolean isExsit = true;
			PlayerInfo playerInfo = player.getPlayerInfo();
			if (file.exists()) {// 如果这里文件创建成功,需要更新用户的信息
				if (null != playerInfo) {
					playerInfo.setPicPath(file.getPath());
		            player.setPlayerInfo(playerInfo);
		            PlayerBussiness.updateAll(player.getUserId(), playerInfo);
				}
			}
			player.getOut().SendUpdatePublicPlayer(isExsit, playerInfo);
		} 
		catch (Exception e) 
		{
			
			logger.error("[ UserUploadHeadPicCmd : execute ]", e);
			
		}
		return 0;
	}

	/**   
     * 把字节数组保存为一个文件   
     * @Author Jacky.zheng   
     * @date 2012-03-13 
     */     
    public static File getFileFromBytes(byte[] b, String outputFile) {     
    	BufferedOutputStream stream = null;     
        File file = null;     
        try {     
            file = new File(outputFile);     
            FileOutputStream fstream = new FileOutputStream(file);     
            stream = new BufferedOutputStream(fstream);     
            stream.write(b);     
        } catch (Exception e) {     
           e.printStackTrace();     
        } finally {     
            if (stream != null) {     
               try {     
                    stream.close();     
                } catch (IOException e1) {     
                    e1.printStackTrace();     
                }     
            }     
        }     
        return file;     
    }
    
    /**   
     * 将图片内容解析成字节数组   
     * @Author Jacky.zheng   
     * @date 2012-03-13
     * @param inStream   
     * @return byte[]   
     * @throws Exception   
     */     
    public static byte[] readStream(InputStream inStream) throws Exception {     
    	byte[] buffer = new byte[1024];     
        int len = -1;     
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();     
        while ((len = inStream.read(buffer)) != -1) {     
            outStream.write(buffer, 0, len);     
        }     
        byte[] data = outStream.toByteArray();     
        outStream.close();     
        inStream.close();     
        return data;     
    }
	
	public static void main(String[] args) {
//		String dir = System.getProperty("user.dir");
//		String separator = System.getProperty("file.separator");
		//System.out.println(dir + separator + "123.png");
//		FileInputStream in;
		try {
//			in = new FileInputStream(dir + separator + "12.png");
//			byte[] result = UserUploadHeadPicCmd.readStream(in);
			//System.out.println(result.toString());
//			File out = UserUploadHeadPicCmd.getFileFromBytes(result, dir + separator + "22.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
