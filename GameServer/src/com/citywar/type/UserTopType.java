package com.citywar.type;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.citywar.dice.entity.PlayerInfo;
import com.citywar.gameutil.PlayerInfoAndAround;

/**
 * 
 * 排行榜 
 * 
 * @author zhiyun.peng
 *
 */
public enum UserTopType {
	
//	/**
//	 * 奴隶数排序
//	 */
//	SLAVECOUNT(1,"",100) {
//		@Override
//		public List<PlayerInfo> sort( List<PlayerInfo> sourceData) {
//			
//			Collections.sort(sourceData,new Comparator<PlayerInfo>() {
//
//				@Override
//				public int compare(PlayerInfo o1,PlayerInfo o2) 
//				{
//					 int slaveCount1 = ((PlayerInfo)o1).getSlaveCount();
//			    	 int slaveCount2 = ((PlayerInfo)o2).getSlaveCount();
//			    	 if(slaveCount1 == slaveCount2) {
//			    		 slaveCount1 = ((PlayerInfo)o1).getGp();
//			    		 slaveCount2 = ((PlayerInfo)o2).getGp();
//			    	 }
//			         return slaveCount2 - slaveCount1  ;
//				}
//				
//			});
//			return null;
//		}
//		
//		@Override
//		public List<PlayerInfoAndAround> sortPA( List<PlayerInfoAndAround> sourceData) {
//			
//			Collections.sort(sourceData,new Comparator<PlayerInfoAndAround>() {
//
//				@Override
//				public int compare(PlayerInfoAndAround o1,PlayerInfoAndAround o2) 
//				{
//					 int charm1 = ((PlayerInfoAndAround)o1).getAroudPlayer().getSlaveCount();
//			    	 int charm2 = ((PlayerInfoAndAround)o2).getAroudPlayer().getSlaveCount();
//			    	 if(charm1 == charm2) {
//			    		 charm1 = ((PlayerInfoAndAround)o1).getAroudPlayer().getGp();
//			    		 charm2 = ((PlayerInfoAndAround)o2).getAroudPlayer().getGp();
//			    	 }
//			         return charm2 - charm1 ;
//				}
//			});
//			
//			return null;
//		}
//	},
//	
//	/**
//	 * 金币排序
//	 */
//	GOLD(2," Coins desc,GP desc ",100) {
//		@Override
//		public List<PlayerInfo> sort( List<PlayerInfo> sourceData) {
//			
//			Collections.sort(sourceData,new Comparator<PlayerInfo>() {
//
//				@Override
//				public int compare(PlayerInfo o1,PlayerInfo o2) 
//				{
//					 int coin1 = ((PlayerInfo)o1).getCoins();
//			    	 int coin2 = ((PlayerInfo)o2).getCoins();
//			    	 if(coin1 == coin2) {
//			    		 coin1 = ((PlayerInfo)o1).getGp();
//				    	 coin2 = ((PlayerInfo)o2).getGp();
//			    	 }
//			         return coin2 - coin1 ;
//				}
//				
//			});
//			
//			return null;
//		}
//		
//		@Override
//		public List<PlayerInfoAndAround> sortPA( List<PlayerInfoAndAround> sourceData) {
//			
//			Collections.sort(sourceData,new Comparator<PlayerInfoAndAround>() {
//
//				@Override
//				public int compare(PlayerInfoAndAround o1,PlayerInfoAndAround o2) 
//				{
//					 int charm1 = ((PlayerInfoAndAround)o1).getAroudPlayer().getCoins();
//			    	 int charm2 = ((PlayerInfoAndAround)o2).getAroudPlayer().getCoins();
//			    	 if(charm1 == charm2) {
//			    		 charm1 = ((PlayerInfoAndAround)o1).getAroudPlayer().getGp();
//			    		 charm2 = ((PlayerInfoAndAround)o2).getAroudPlayer().getGp();
//			    	 }
//			         return charm2 - charm1 ;
//				}
//			});
//			
//			return null;
//		}
//	},
	
//	/**
//	 * 魅力排序
//	 */
//	CHARM(3,"", " CharmValve desc,GP desc ",100) {
//		@Override
//		public List<PlayerInfo> sort( List<PlayerInfo> sourceData) {
//			
//			Collections.sort(sourceData,new Comparator<PlayerInfo>() {
//
//				@Override
//				public int compare(PlayerInfo o1,PlayerInfo o2) 
//				{
//					 int charm1 = ((PlayerInfo)o1).getCharmValve();
//			    	 int charm2 = ((PlayerInfo)o2).getCharmValve();
//			    	 if(charm1 == charm2) {
//			    		 charm1 = ((PlayerInfo)o1).getGp();
//			    		 charm2 = ((PlayerInfo)o2).getGp();
//			    	 }
//			         return charm2 - charm1;
//				}
//			});
//			
//			return null;
//		}
//		
//		@Override
//		public List<PlayerInfoAndAround> sortPA( List<PlayerInfoAndAround> sourceData) {
//			
//			Collections.sort(sourceData,new Comparator<PlayerInfoAndAround>() {
//
//				@Override
//				public int compare(PlayerInfoAndAround o1,PlayerInfoAndAround o2) 
//				{
//					 int charm1 = ((PlayerInfoAndAround)o1).getAroudPlayer().getCharmValve();
//			    	 int charm2 = ((PlayerInfoAndAround)o2).getAroudPlayer().getCharmValve();
//			    	 if(charm1 == charm2) {
//			    		 charm1 = ((PlayerInfoAndAround)o1).getAroudPlayer().getGp();
//			    		 charm2 = ((PlayerInfoAndAround)o2).getAroudPlayer().getGp();
//			    	 }
//			         return charm2 - charm1 ;
//				}
//			});
//			
//			return null;
//		}
//	},
	
	/**
	 * 胜利场次排序
	 */
	WIN(4," isrobot = 0 ", " win+lose desc ",300) {
		@Override
		public List<PlayerInfo> sort( List<PlayerInfo> sourceData) {
			
			Collections.sort(sourceData,new Comparator<PlayerInfo>() {

				@Override
				public int compare(PlayerInfo o1,PlayerInfo o2) 
				{
					Integer charm1 = ((PlayerInfo)o1).getWin() + ((PlayerInfo)o1).getLose();
					Integer charm2 = ((PlayerInfo)o2).getWin() + ((PlayerInfo)o2).getLose();
//			    	 if(charm1 == charm2) {
//			    		 charm1 = ((PlayerInfo)o1).getGp();
//			    		 charm2 = ((PlayerInfo)o2).getGp();
//			    	 }
					
					System.out.println("sort=========" + charm1 + "===" + charm2);
					return charm2.compareTo(charm1);
				}
			});
			
			return null;
		}
		
		@Override
		public List<PlayerInfoAndAround> sortPA( List<PlayerInfoAndAround> sourceData) {
			
			Collections.sort(sourceData,new Comparator<PlayerInfoAndAround>() {

				@Override
				public int compare(PlayerInfoAndAround o1,PlayerInfoAndAround o2) 
				{
					Integer charm1 = ((PlayerInfoAndAround)o1).getAroudPlayer().getWin() + ((PlayerInfoAndAround)o1).getAroudPlayer().getWin();
					Integer charm2 = ((PlayerInfoAndAround)o2).getAroudPlayer().getLose() + ((PlayerInfoAndAround)o2).getAroudPlayer().getLose();
//			    	 if(charm1 == charm2) {
//			    		 charm1 = ((PlayerInfoAndAround)o1).getAroudPlayer().getGp();
//			    		 charm2 = ((PlayerInfoAndAround)o2).getAroudPlayer().getGp();
//			    	 }
					
					System.out.println("sortPA=========" + charm1 + "===" + charm2);			         
					return charm2.compareTo(charm1);
				}
			});
			
			return null;
		}
	},
	
	/**
	 * 玩家当日赚的金币数排序
	 */
	INCREASECOINS(5,"", " increase_coins desc,online_time desc ",100) {
		@Override
		public List<PlayerInfo> sort( List<PlayerInfo> sourceData) {
			
			Collections.sort(sourceData,new Comparator<PlayerInfo>() {

				@Override
				public int compare(PlayerInfo o1,PlayerInfo o2) 
				{
					 int charm1 = ((PlayerInfo)o1).getDayIncreaseCoins();
			    	 int charm2 = ((PlayerInfo)o2).getDayIncreaseCoins();
			    	 if(charm1 == charm2) {
			    		 charm1 = ((PlayerInfo)o1).getGp();
			    		 charm2 = ((PlayerInfo)o2).getGp();
			    	 }
			         return charm2 - charm1 ;
				}
			});
			
			return null;
		}
		
		@Override
		public List<PlayerInfoAndAround> sortPA( List<PlayerInfoAndAround> sourceData) {
			
			Collections.sort(sourceData,new Comparator<PlayerInfoAndAround>() {

				@Override
				public int compare(PlayerInfoAndAround o1,PlayerInfoAndAround o2) 
				{
					 int charm1 = ((PlayerInfoAndAround)o1).getAroudPlayer().getDayIncreaseCoins();
			    	 int charm2 = ((PlayerInfoAndAround)o2).getAroudPlayer().getDayIncreaseCoins();
			    	 if(charm1 == charm2) {
			    		 charm1 = ((PlayerInfoAndAround)o1).getAroudPlayer().getGp();
			    		 charm2 = ((PlayerInfoAndAround)o2).getAroudPlayer().getGp();
			    	 }
			         return charm2 - charm1 ;
				}
			});
			
			return null;
		}
	};
	
	int id;
	
	String strWhere;
	
	/** 排序字段 */
	String orderField;
	
	/** 取排行榜前几名 */
	int topCount;
	
	UserTopType(int id, String strWhere, String orderField,int topCount)
	{
		this.id = id;
		this.strWhere   =  strWhere;
		this.topCount   =  topCount;
		this.orderField = orderField;
	}

	public String getWhere(){
		return this.strWhere;
	}
	
	public String getOrderField() {
		return orderField;
	}
	public abstract List<PlayerInfo> sort(List<PlayerInfo> sourceData);
	
	public abstract List<PlayerInfoAndAround> sortPA(List<PlayerInfoAndAround> sourceData);

    public static UserTopType valueOf(int id)
	{
		for (UserTopType function : values())
		{
			if (function.id == id) { return function; }
		}

		throw new IllegalArgumentException(" illegal UserTopType id:" + id);
	}

	public int getTopCount() {
		return topCount;
	}
	
}
