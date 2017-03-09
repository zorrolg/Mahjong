package com.citywar.bll;

import java.sql.Types;
import java.util.List;

import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.UserOrderInfo;

public class OrderBusiness {
	
	private final static String USER_ORDER_TABLE = "t_u_order";

	public static boolean insertUserOrder(UserOrderInfo info) {
		return DaoManager.getUserOrderDao().insert(info) > 0;
	}
	
	/**
	 * 根据交易事物transactionId查询所有验证信息
	 * @param userId
	 * @return List<UserOrderInfo>
	 */
	public static List<UserOrderInfo> getUserOrderByTransactionId(String transactionId) 
	{
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.VARCHAR, transactionId);
		return DaoManager.getUserOrderDao().queryList(USER_ORDER_TABLE, "transaction_id like ?", params);
	}
	
	/**
	 * 根据交易事物transactionId查询所有验证信息
	 * @param userId
	 * @return List<UserOrderInfo>
	 */
	public static UserOrderInfo getUserOrderById(String transactionId) 
	{
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.VARCHAR, transactionId);
		return DaoManager.getUserOrderDao().query(USER_ORDER_TABLE, "id = ?", params);
	}
	
	public static boolean update(UserOrderInfo orderInfo)
	{
		
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.VARCHAR, orderInfo.getStatus());
        params.put(Types.VARCHAR, orderInfo.getTransactionId());
        return DaoManager.getUserOrderDao().update(USER_ORDER_TABLE,
                                                    "status=?", "transaction_id=?",
                                                    params) != 0;
	    
	}
}
