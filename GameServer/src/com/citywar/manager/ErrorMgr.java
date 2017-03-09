package com.citywar.manager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.citywar.dice.entity.UserLetter;


/**
 * 
 * @author zhiyun.peng
 * 
 * 查询客户端反馈的异常
 *
 */
public class ErrorMgr {
	
	
	private static String savePath = "";
	private static SimpleDateFormat dataformat = new SimpleDateFormat("yyyy-MM-dd");  
	private static SimpleDateFormat dataformat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
    static {
    	File directory = new File("clientError");// 参数为空  
    	//System.out.println(directory.getAbsolutePath());
    	if(!directory.exists()){
    		directory.mkdirs();
    	}
    	savePath = directory.getAbsolutePath()+File.separator;
    }
	
	public static void putErrorLetter(UserLetter errorLetter) 
	{
		Date newDate = Calendar.getInstance().getTime();
		File userFile = new File(savePath+errorLetter.getSenderId()+File.separator+dataformat.format(newDate)+".log");
		//System.out.println(userFile.getAbsolutePath());
		
		if(!userFile.getParentFile().exists()){
			userFile.getParentFile().mkdirs();
		}
		if(!userFile.exists()){
			try {
				userFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			
			
//			String str = json.
			appendContent(userFile, dataformat2.format(newDate)+" "+errorLetter.getContent());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	/** 
     * 追加文件：使用FileWriter 
     *   
     * @param fileName 
     * @param content 
     */  
    public static void appendContent(File file, String content) {   
        try {   
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件   
            FileWriter writer = new FileWriter(file, true);   
            writer.write("\t\n"+content); 
            writer.close();   
        } catch (IOException e) {   
            e.printStackTrace();   
        }   
    }   

	
	public static void main(String[] args) throws IOException {
		UserLetter newLetter = new UserLetter();
    
    	newLetter.setSenderId(22);
    	newLetter.setSenderName("aaaaaa");
    	newLetter.setReceiverId(1);
    	newLetter.setReceiverName("asdfasdf");
    	newLetter.setContent("aaaaaaaaaaaaaaaaaaa");
    	putErrorLetter(newLetter);
	}

}
