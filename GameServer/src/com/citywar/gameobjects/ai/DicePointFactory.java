package com.citywar.gameobjects.ai;

import java.util.HashMap;
import java.util.Map;

import com.citywar.util.ThreadSafeRandom;

public class DicePointFactory {
	
	private static final ThreadSafeRandom random = new ThreadSafeRandom();
    /**
     * 取得N个随机骰子的结果
     * @param count
     * @return
     */
    public static byte[] getDiceNumber(int count)
    {
       Map<Byte,Integer> hashMap = new HashMap<Byte,Integer>(5);
       for (int i = 0; i < 5; i++) {
    	   Byte num = (byte)random.next(1,7);
           if(hashMap.containsKey(num))
           {
        	  Integer value = hashMap.get(num)+1;
        	  hashMap.put(num, value);
           }else{
        	   hashMap.put(num, 1);
           }
	   }
       
       return random.getDiceNumber(count);
    }
    
    /**
     * 随即取得N颗同样的骰子
     */
    public static byte[] getDiceSame(int sumCount,int n){
    	byte num = (byte)random.next(6);
    	byte[] result = new byte[n];
    	for (int i = 0; i < result.length; i++) 
    	{
    		result[i] = num;
		}
    	return result;
    }
    
    /** 5个骰子中  确保同样的点数最多不超过3颗*/
    public static byte[] createDiceSameLess3()
    {
    	byte[] result = new byte[5];
    	byte[] noValue = new byte[2];
    	//先随即取出2个不重复的骰子
    	result[0] = noValue[0] = (byte)random.next(1,7);
    	result[1] = createRandomDicePoint(noValue);
    	
    	//再随即取出3个不重复的骰子
    	result[2] = noValue[0] = (byte)random.next(1,7);
    	result[3] = noValue[1] = createRandomDicePoint(noValue);
    	result[4] = createRandomDicePoint(noValue);
    	return result;
    }
    
    /** 5个骰子中  确保3颗是一样   剩下2颗随即*/
    public static byte[] createDiceSame3()
    {
    	byte[] result = new byte[5];
    	byte[] noValue = new byte[1];
    	
    	result[0] = result[1] = result[2] = noValue[0] = (byte)random.next(1,7);
    	result[3] = createRandomDicePoint(noValue);
    	result[4] = createRandomDicePoint(noValue);
    	return result;
    }
    
    /** 5个骰子中  确保4颗是一样   剩下1颗随即*/
    public static byte[] createDiceSame4()
    {
    	byte num1 = (byte)random.next(1,7);
    	byte num2 = createRandomDicePoint(new byte[]{num1});
    	return new byte[]{num1,num1,num1,num1,num2};
    }
    /** 5个骰子中  确保4颗是一样   剩下1颗随即*/
    public static byte[] createDiceSame5()
    {
    	byte num1 = (byte)random.next(1,7);
    	return new byte[]{num1,num1,num1,num1,num1};
    }
    
    /**
     * 
     * @param noValue 不能返回这些数字
     */
    public static byte createRandomDicePoint(byte[] noValue)
    {
    	while(true){
    		byte result = (byte)random.next(1,7);
    		boolean isNotFind = true;
    		for (int i = 0; i < noValue.length; i++) {
        		if(noValue[i]  == result){
        			isNotFind = false;
        			break;
        		}
    		}
    		if(isNotFind){
    			return result;
    		}
    	}
    }
    public static void main(String[] args) {
    	for (int i = 0; i < 10; i++) {
    		//System.out.println(Arrays.toString(createDiceSameLess3()));
		}
	}
}
