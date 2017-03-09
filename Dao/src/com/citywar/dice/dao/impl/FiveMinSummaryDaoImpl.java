package com.citywar.dice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.FiveMinSummaryDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.FiveMinSummaryInfo;

public class FiveMinSummaryDaoImpl extends BaseDaoImpl implements FiveMinSummaryDao  {

	private final static String FIVE_MIN_TABLE = "t_5_min";//5分钟数据表
	
    private static final Logger logger = Logger.getLogger(FiveMinSummaryDaoImpl.class.getName());

	
	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		FiveMinSummaryInfo info = new FiveMinSummaryInfo(rs);
		return info;
	}
	
	@Override
	public int insert(Object obj) {
		int intResult = -1;
		FiveMinSummaryInfo fiveMinSummaryInfo = (FiveMinSummaryInfo) obj;
        String sqlText = "insert into " + FIVE_MIN_TABLE + "(count_date, sub_id , reg, online) values(?,?,?,?)";
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.TIMESTAMP, fiveMinSummaryInfo.getCountDate());
        params.put(Types.INTEGER, fiveMinSummaryInfo.getSubId());
        params.put(Types.INTEGER, fiveMinSummaryInfo.getRegCount());
        params.put(Types.INTEGER, fiveMinSummaryInfo.getOnlineCount());
        try
        {
            intResult = getDbManager().executeNoneQuery(sqlText,
                                                        params.getParams());
        }
        catch (SQLException e)
        {
            logger.error("[ FiveMinSummaryDaoImpl : insert ]", e);
        }
		return intResult;
	}
}
