package com.citywar.dice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.UserOrderDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.UserOrderInfo;

public class UserOrderDaoImpl extends BaseDaoImpl implements UserOrderDao {
	
	private static final Logger logger = Logger.getLogger(UserOrderDaoImpl.class.getName());
	
	private final static String USER_ORDER_TABLE = "t_u_order";

	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		UserOrderInfo info = new UserOrderInfo(rs);
		return info;
	}
	
	@Override
	public int insert(Object obj) {
		int intResult = -1;
		UserOrderInfo userOrderInfo = (UserOrderInfo) obj;
        String sqlText = " INSERT INTO " + USER_ORDER_TABLE + "(`channel_name`,`user_id`,`user_name`,`create_time`,`original_purchase_date_pst`," +
        		"`purchase_date_ms`, `original_transaction_id`,`original_purchase_date_ms`," +
        		"`transaction_id`,`quantity`,`bvrs`,`hosted_iap_version`,`product_id`,`purchase_date`," +
        		"`original_purchase_date`,`purchase_date_pst`,`bid`,`item_id`,`money`,`status`) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.VARCHAR, userOrderInfo.getChannelName());
        params.put(Types.INTEGER, userOrderInfo.getUserId());
        params.put(Types.VARCHAR, userOrderInfo.getUserName());
        params.put(Types.TIMESTAMP, userOrderInfo.getCreateTime());
        params.put(Types.VARCHAR, userOrderInfo.getOriginalPurchaseDatePst());
        params.put(Types.VARCHAR, userOrderInfo.getPurchaseDateMs());
        params.put(Types.VARCHAR, userOrderInfo.getOriginalTransactionId());
        params.put(Types.VARCHAR, userOrderInfo.getOriginalPurchaseDateMs());
        params.put(Types.VARCHAR, userOrderInfo.getTransactionId());
        params.put(Types.VARCHAR, userOrderInfo.getQuantity());
        params.put(Types.VARCHAR, userOrderInfo.getBvrs());
        params.put(Types.VARCHAR, userOrderInfo.getHostedIapVersion());
        params.put(Types.VARCHAR, userOrderInfo.getProductId());
        params.put(Types.VARCHAR, userOrderInfo.getPurchaseDate());
        params.put(Types.VARCHAR, userOrderInfo.getOriginalPurchaseDate());
        params.put(Types.VARCHAR, userOrderInfo.getPurchaseDatePst());
        params.put(Types.VARCHAR, userOrderInfo.getBid());
        params.put(Types.VARCHAR, userOrderInfo.getItemId());
        params.put(Types.FLOAT, userOrderInfo.getMoney());
        params.put(Types.VARCHAR, userOrderInfo.getStatus());
        try
        {
            intResult = getDbManager().executeNoneQuery(sqlText,params.getParams());
        }
        catch (SQLException e)
        {
            logger.error("[ UserTaskDaoImpl : insert ]", e);
        }
        return intResult;		
	}
}
