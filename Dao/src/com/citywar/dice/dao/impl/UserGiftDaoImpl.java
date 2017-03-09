package com.citywar.dice.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.UserGiftDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.UserGiftInfo;
import com.citywar.dice.entity.UserGiftIntegrationInfo;

public class UserGiftDaoImpl extends BaseDaoImpl implements UserGiftDao {

	private static final Logger logger = Logger
			.getLogger(UserGiftDaoImpl.class.getName());

	private final static String USER_GIFT_TABLE = "t_u_gift";//

	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		UserGiftInfo UserGiftInfo = new UserGiftInfo(rs);
		return UserGiftInfo;
	}

	@Override
	public int insert(Object obj) {
		int intResult = -1;
		UserGiftInfo userGiftInfo = (UserGiftInfo) obj;
		String sqlText = " INSERT INTO "
				+ USER_GIFT_TABLE
				+ "(`owner_user_id`,`gift_id`,`present_user_id`,`count`, `give_time`, `isExist`, `isUsed`, `remark`) values (?,?,?,?,?,?,?,?); ";

		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, userGiftInfo.getOwnerUserId());
		params.put(Types.INTEGER, userGiftInfo.getGiftId());
		params.put(Types.INTEGER, userGiftInfo.getPresentUserId());
		params.put(Types.INTEGER, userGiftInfo.getCount());
		params.put(Types.TIMESTAMP, userGiftInfo.getGiveTime());
		params.put(Types.INTEGER, userGiftInfo.isExist() ? 1 : 0);
		params.put(Types.INTEGER, userGiftInfo.isUsed() ? 1 : 0);
		params.put(Types.VARCHAR, userGiftInfo.getRemark());

		try {
			intResult = getDbManager().executeLastId(sqlText,
					params.getParams());// 获得id号
		} catch (SQLException e) {
			logger.error("[ UserGiftInfoDaoImpl : insert ]", e);
		}
		return intResult;
	}

	@Override
	public int update(Object obj) {
		int intResult = -1;
		UserGiftInfo userGiftInfo = (UserGiftInfo) obj;
		String sqlText = " UPDATE "
				+ USER_GIFT_TABLE
				+ " SET `owner_user_id`=?,`gift_id`=?,`present_user_id`=?,`count`=?, `give_time`=?, `isExist`=?, `isUsed`=?, `remark`=? where id = ?; ";

		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, userGiftInfo.getOwnerUserId());
		params.put(Types.INTEGER, userGiftInfo.getGiftId());
		params.put(Types.INTEGER, userGiftInfo.getPresentUserId());
		params.put(Types.INTEGER, userGiftInfo.getCount());
		params.put(Types.TIMESTAMP, userGiftInfo.getGiveTime());
		params.put(Types.INTEGER, userGiftInfo.isExist() ? 1 : 0);
		params.put(Types.INTEGER, userGiftInfo.isUsed() ? 1 : 0);
		params.put(Types.VARCHAR, userGiftInfo.getRemark());
		params.put(Types.INTEGER, userGiftInfo.getId());

		try {
			intResult = getDbManager().executeNoneQuery(sqlText,
					params.getParams());
		} catch (SQLException e) {
			logger.error("[ UserGiftInfoDaoImpl : update ]", e);
		}
		return intResult;
	}
	
	@Override
	public List<UserGiftIntegrationInfo> selectUserGiftIdAndSumCount(int userId) {
		List<UserGiftIntegrationInfo> resultList = new ArrayList<UserGiftIntegrationInfo>();
		PreparedStatement pstmt = null;
		ResultSet result = null;
		DBParamWrapper params = new DBParamWrapper();
		String sqlText = "select gift_id,sum(count) as sum_count from t_u_gift where owner_user_id = ? group by gift_id";
		params.put(Types.INTEGER, userId);
		try {
			pstmt = getDbManager().executeQuery(sqlText,
					params.getParams());
			result = pstmt.executeQuery();
			while (result.next())
	        {
	            resultList.add(new UserGiftIntegrationInfo(result));
	        }
		} catch (SQLException e) {
			logger.error("[ UserGiftInfoDaoImpl : update ]", e);
		}
        finally
        {
            getDbManager().closeConnection(pstmt, result);
        }
		return resultList;
	}
}
