/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.FriendsDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.FriendInfo;

/**
 * 好友相关DAO实现
 * 
 * @author charles
 * @date 2011-12-16
 * @version
 * 
 */
public class FriendsDaoImpl extends BaseDaoImpl implements FriendsDao
{

    private static final Logger logger = Logger.getLogger(FriendsDaoImpl.class.getName());

    /**
     * 默认一页记录条数
     */
    private final static int PAGE_SIZE = 20;

    /**
     * 获取用户的好友信息列表实现
     * 
     * @param userId
     *            当前用户ID
     * @param pageNo
     *            加载的页码
     * @return 好友信息列表
     */
    public List<FriendInfo> getFriendsList(int userId, int pageNo)
    {
        if (userId <= 0)
        {
            return null;
        }

        String sqlText = "select * from v_friends WHERE userid = ? "
                + "ORDER BY userId LIMIT ?, " + PAGE_SIZE;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<FriendInfo> friendInfos = null;

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, userId);
        params.put(Types.INTEGER, pageNo - 1);

        try
        {
            pstmt = getDbManager().executeQuery(sqlText, params.getParams());

            if (null != pstmt)
            {
                friendInfos = new ArrayList<FriendInfo>();
                rs = pstmt.executeQuery();
                while (rs.next())
                {
                    FriendInfo info = new FriendInfo(rs);
                    friendInfos.add(info);
                }
            }
        }
        catch (SQLException e)
        {
            logger.error("[ FriendsDaoImpl : getFriendsList ]", e);
        }
        finally
        {
            getDbManager().closeConnection(pstmt, rs);
        }

        return friendInfos;
    }

    /**
     * 增加好友实现
     * 
     * @param userId
     *            当前用户ID
     * @param friendUserId
     *            需要添加的好友ID
     * @return 是否添加成功
     */
    public boolean addFriend(int userId, int friendUserId)
    {
        if (userId <= 0 || friendUserId <= 0)
        {
            return false;
        }

        boolean isSuccess = false;
        String sqlText = "insert into t_u_friends (userId, friendId) values (?, ?)";

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, userId);
        params.put(Types.INTEGER, friendUserId);

        try
        {
            isSuccess = getDbManager().executeNoneQuery(sqlText,
                                                        params.getParams()) > -1 ? true
                    : false;
        }
        catch (SQLException e)
        {
            logger.error("[ FriendsDaoImpl : addFriend ]", e);
        }

        return isSuccess;
    }

    /**
     * 获取某个好友的信息实现
     * 
     * @param 当前用户ID
     * @param 好友的ID
     * @return 好友信息
     */
    public FriendInfo getFriend(int userId, int friendUserId)
    {
        if (userId <= 0 || friendUserId <= 0)
        {
            return null;
        }

        String sqlText = "select * from v_friends where userId = ? and friendId = ?";
        FriendInfo friendInfo = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, userId);
        params.put(Types.INTEGER, friendUserId);

        try
        {
            pstmt = getDbManager().executeQuery(sqlText, params.getParams());

            if (null != pstmt)
            {
                rs = pstmt.executeQuery();
                if (rs.next())
                {
                    friendInfo = new FriendInfo(rs);
                }
            }
        }
        catch (SQLException e)
        {
            logger.error("[ FriendsDaoImpl : getFriend ]", e);
        }
        finally
        {
            getDbManager().closeConnection(pstmt, rs);
        }

        return friendInfo;
    }

    /**
     * 删除好友实现
     * 
     * @param userId
     *            当前用户ID
     * @param friendUserId
     *            待删除的用户ID
     * @return 是否删除成功
     */
    public boolean deleteFriend(int userId, int friendUserId)
    {
        if (userId <= 0 || friendUserId <= 0)
        {
            return false;
        }

        boolean delSuccess = false;
        String sqlText = "delete from t_u_friends where userId = ? and friendId = ?";

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, userId);
        params.put(Types.INTEGER, friendUserId);

        try
        {
            delSuccess = getDbManager().executeNoneQuery(sqlText,
                                                         params.getParams()) > -1 ? true
                    : false;
        }
        catch (SQLException e)
        {
            logger.error("[ FriendsDaoImpl : deleteFriend ]", e);
        }

        return delSuccess;
    }

    /**
     * 查找好友实现
     * 
     * @param userId
     *            当前用户ID
     * @param friendNickname
     *            查找的好友昵称
     * @param 需要加载的页码
     * @return 查找到的好友信息列表
     */
    public List<FriendInfo> findFriends(int userId, String friendNickname,
            int pageNo)
    {
        if (userId <= 0)
        {
            return null;
        }

        String sqlText = "select * from v_friends where userId = ? and friendName like '%?%' "
                + "order by userId limit ?, " + PAGE_SIZE;
        List<FriendInfo> friendInfos = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, userId);
        params.put(Types.VARCHAR, friendNickname);
        params.put(Types.INTEGER, pageNo);

        try
        {
            pstmt = getDbManager().executeQuery(sqlText, params.getParams());
            if (null != pstmt)
            {
                friendInfos = new ArrayList<FriendInfo>();
                rs = pstmt.executeQuery();
                while (rs.next())
                {
                    FriendInfo info = new FriendInfo(rs);
                    friendInfos.add(info);
                }
            }
        }
        catch (SQLException e)
        {
            logger.error("[ FriendsDaoImpl : findFriends ]", e);
        }
        finally
        {
            getDbManager().closeConnection(pstmt, rs);
        }

        return friendInfos;
    }

    /**
     * 暂时不需要用到(需要时，最好将调用逻辑写入Business中，而不要在此类的其他方法中直接调用，可能会产生连接不能释放问题)
     * 通过用户ID找到所有好友的ID
     * 
     * @param 当前用户ID
     * @return 好友ID列表
     */
    @SuppressWarnings("unused")
    private List<Integer> getFriendsIds(int userId)
    {
        if (userId < 0)
        {
            return null;
        }
        List<Integer> ids = new ArrayList<Integer>();

        String sqlText = "select friendid from t_u_friends where userid = ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, userId);

        try
        {
            pstmt = getDbManager().executeQuery(sqlText, params.getParams());

            if (null != pstmt)
            {
                rs = pstmt.executeQuery();
                while (rs.next())
                {
                    Integer id = (Integer) rs.getInt("friendid");
                    if (null != id)
                    {
                        ids.add(id);
                    }
                }
            }
        }
        catch (SQLException e)
        {
            logger.error("[ FriendsDaoImpl : getFriendsIds ]", e);
        }
        finally
        {
            getDbManager().closeConnection(pstmt, rs);
        }

        return ids;
    }

    /**
     * 查找好友列表总页数（供分页使用）
     * 
     * @param userId
     * @return 总页数
     */
    public int getFriendsTotalPage(int userId)
    {
        String sqlText = "select count(*) from v_friends WHERE userid = ? ORDER BY userId";

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int totalPage = 0;
        int totalRecords = 0;

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, userId);

        try
        {
            pstmt = getDbManager().executeQuery(sqlText, params.getParams());

            if (null != pstmt)
            {
                rs = pstmt.executeQuery();
                if (rs.next())
                {
                    totalRecords = rs.getInt(1);
                }
            }

            if (totalRecords > 0)
            {
                if (totalRecords % PAGE_SIZE == 0)
                {
                    totalPage = totalRecords / PAGE_SIZE;
                }
                else
                {
                    totalPage = (totalRecords / PAGE_SIZE) + 1;
                }
            }
        }
        catch (SQLException e)
        {
            logger.error("[ FriendsDaoImpl : getFriendsTotalPage ]", e);
        }
        finally
        {
            getDbManager().closeConnection(pstmt, rs);
        }

        return totalPage;
    }

    /**
     * 查询是否某用户是否为好友
     * 
     * @param userId
     *            当前用户ID
     * @param friendUserId
     *            需要加为好友的ID
     * @param 是否为好友
     *            ：是返回true，否返回false
     */
    public boolean isFriend(int userId, int friendUserId)
    {

        if (userId < 0 || friendUserId < 0)
        {
            return false;
        }

        boolean isFriend = false;
        String sqlText = "SELECT COUNT(1) FROM t_u_friends WHERE userId = ? AND friendId = ?";
        int totalRecords = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, userId);
        params.put(Types.INTEGER, friendUserId);

        try
        {
            pstmt = getDbManager().executeQuery(sqlText, params.getParams());

            if (null != pstmt)
            {
                rs = pstmt.executeQuery();
                if (rs.next())
                {
                    totalRecords = rs.getInt(1);
                    if (totalRecords > 0)
                    {
                        isFriend = true;
                    }
                }
            }
        }
        catch (SQLException e)
        {
            logger.error("[ FriendsDaoImpl : isFriend ]", e);
        }
        finally
        {
            getDbManager().closeConnection(pstmt, rs);
        }

        return isFriend;
    }

    /**
     * 获取某用户所有好友信息
     * 
     * @param userId
     *            当前用户ID
     * @return 好友信息列表
     */
    public List<FriendInfo> getAllFriendsList(int userId)
    {
        if (userId <= 0)
        {
            return null;
        }

        String sqlText = "select * from v_friends WHERE userid = ? ORDER BY id";

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<FriendInfo> friendInfos = null;

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, userId);

        try
        {
            pstmt = getDbManager().executeQuery(sqlText, params.getParams());

            if (null != pstmt)
            {
                friendInfos = new ArrayList<FriendInfo>();
                rs = pstmt.executeQuery();
                while (rs.next())
                {
                    FriendInfo info = new FriendInfo(rs);
                    friendInfos.add(info);
                }
            }
        }
        catch (SQLException e)
        {
            logger.error("[ FriendsDaoImpl : getAllFriendsList ]", e);
        }
        finally
        {
            getDbManager().closeConnection(pstmt, rs);
        }

        return friendInfos;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.road.dice.dao.impl.BaseDaoImpl#getTemplate(java.sql.ResultSet)
     */
    @Override
    public Object getTemplate(ResultSet rs) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

}
