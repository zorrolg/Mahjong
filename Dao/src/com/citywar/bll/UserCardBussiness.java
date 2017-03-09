package com.citywar.bll;

import java.sql.Types;

import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.UserCardInfo;

public class UserCardBussiness {
	
	private final static String BUILD_TABLE = "t_u_card";//静态的任务数据表
	


	
	
	public static boolean insertUserCard(UserCardInfo userBuildInfo) {
		return DaoManager.getPlayerCardInfoDao().insert(userBuildInfo) > 0;
	}
	
	
	public static UserCardInfo getUserBuildsInfo(int userId) {
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, userId);
		return DaoManager.getPlayerCardInfoDao().query(BUILD_TABLE, "UserId=?", params);
	}
	
	
	
	public static int updateUserCardsInfo(UserCardInfo userCard) {
		
		DBParamWrapper params = new DBParamWrapper();
		
		params.put(Types.VARCHAR, userCard.getCardDevelopList());
        params.put(Types.INTEGER, userCard.getCardIsDevelop());
        params.put(Types.INTEGER, userCard.getCardDevelopId());
        params.put(Types.TIMESTAMP, userCard.getCardDevelopTime());
        
        params.put(Types.VARCHAR, userCard.getCardFactoryList());
        params.put(Types.INTEGER, userCard.getCardIsFactory());
        params.put(Types.TIMESTAMP, userCard.getCardFactoryTime());
		
		params.put(Types.INTEGER, userCard.getPlayerId());//在更新的时候，可以直接用主键来确定用户

		return DaoManager.getPlayerCardInfoDao().update(BUILD_TABLE, "`card_develop_list` = ?,`card_isdevelop` = ?,`card_develop_id` = ?,`card_develop_time` = ?,`card_factory_list` = ?,`card_isfactory` = ?,`card_factory_time` = ?", "UserId=?", params);
	
	}
	
	
	
	

	
	
	
}



