package com.citywar.dice.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.VersionRiseDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.VersionRise;

public class VersionRiseDaoImpl extends BaseDaoImpl implements VersionRiseDao {
	
    private static final Logger logger = Logger.getLogger(VersionRiseDaoImpl.class.getName());

	private final static String TABLE_NAME = "t_s_version_rise";
	
	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		VersionRise data = new VersionRise();
		data.setId(rs.getInt("ver_rise_id"));
		data.setLowVersionCode(rs.getString("low_version_code"));
		data.setHighVersionCode(rs.getString("high_version_code"));
		data.setDesc(rs.getString("ver_rise_desc"));
		data.setState(rs.getInt("ver_rise_state"));
		return data;
	}

	@Override
	public int insert(Object obj) {
		int intResult = -1;
		VersionRise entity = (VersionRise) obj;
        String sqlText = " INSERT INTO " + TABLE_NAME + "(`low_version_code`,`high_version_code`,`ver_rise_desc`,`ver_rise_state`) values (?,?,?,?); ";

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.VARCHAR, entity.getLowVersionCode());
        params.put(Types.VARCHAR, entity.getHighVersionCode());
        params.put(Types.VARCHAR, entity.getDesc());
        params.put(Types.INTEGER, entity.getState());
        
        try
        {
        	intResult = getDbManager().executeLastId(sqlText,params.getParams());//获得id号
        }
        catch (SQLException e)
        {
            logger.error("[ VersionRiseDaoImpl : insert ]", e);
        }
        return intResult;
	}
	
	public  List<VersionRise> doSearchByVerionCode(String versionCode){
        if (versionCode == null)
        {
            return null ;
        }

        String sqlText = "select version_code,ver_rise_desc,ver_rise_state from  (select version_code from t_s_version  t where version_code< ?) as v left join t_s_version_rise r on (v.version_code=r.low_version_code and high_version_code =?) ";
        VersionRise data = null;
        List<VersionRise> result = new ArrayList<VersionRise>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.VARCHAR, versionCode);
        params.put(Types.VARCHAR, versionCode);

        try
        {
            pstmt = getDbManager().executeQuery(sqlText, params.getParams());

            if (null != pstmt)
            {
                rs = pstmt.executeQuery();
                while (rs.next())
                {
                	data = new VersionRise();
                	data.setLowVersionCode(rs.getString("version_code"));
                	data.setHighVersionCode(versionCode);
                	data.setDesc(rs.getString("ver_rise_desc"));
                	data.setState(rs.getInt("ver_rise_state"));
                	result.add(data);
                }
            }
        }
        catch (SQLException e)
        {
            logger.error("[ VersionRiseDaoImpl : getFriend ]", e);
        }
        finally
        {
            getDbManager().closeConnection(pstmt, rs);
        }
        return result;
	}
	

	
}
