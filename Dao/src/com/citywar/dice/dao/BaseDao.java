/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.citywar.dice.db.DBBaseManger;
import com.citywar.dice.db.DBParamWrapper;

/**
 * DAO 层基本管理接口
 * 
 * @author tracy
 * @date 2011-12-15
 * @version
 * 
 */
public interface BaseDao
{

    /**
     * 提供增删改查操作的 DB 管理类
     * 
     * @return
     */
    DBBaseManger getDbManager();

    /**
     * [ 增 ] 插入操作
     * 
     * @param obj
     *            将要被插入的对象, <strong><i>子类具体实现</i></strong>
     * @return 插入执行后影响的行数
     */
    int insert(Object obj);

    /**
     * [ 删 ] 提供通用的 <strong><i>删除</i></strong> 操作, 仅适用于记录的物理删除<br>
     * 如果需要逻辑删除, 请使用 update 对 IsExsit 字段进行更新操作
     * 
     * @param tableName
     *            将要进行删除操作的表名
     * @param strWhere
     *            删除的where条件, 形如 "field_1=?, field_2=?, filed_x=?"
     * @param params
     *            删除条件的参数值, 条件中 ? 对应的 值
     * @return 删除语句执行后影响的行数, 用户可在 bll 层做结果的业务逻辑处理
     */
    int delete(String tableName, String strWhere, DBParamWrapper params);

    /**
     * [ 改 ] 提供 <strong><i>通用的更新</i></strong> 的删除操作,
     * 
     * @param tableName
     *            将要被更新的数据表的表名称
     * @param strUpdate
     *            更新的字段值, 形如 Xxx=?, Xxx=?
     * @param strWhere
     *            更新的查询条件, 形如 ( Xxx=? and|or Xxx? ) | (xxx not in (?,?)...
     * @param params
     *            更新的查询条件的参数值
     * @return 更新执行后影响的行数, 用户可在 bll 层做结果的业务逻辑处理
     */
    int update(String tableName, String strUpdate, String strWhere,
            DBParamWrapper params);

    /**
     * [ 改 ] 提供更新操作, 更新全部属性, <strong><i>子类具体实现</i></strong><br>
     * TODO 仔细考虑如何通用性更强
     * 
     * @param obj
     *            将要被更新所有属性的对象
     * @return 是否更新成功
     */
    int update(Object obj);

    /**
     * [ 改 ] 提供更新操作, 更新全部属性, <strong><i>子类具体实现</i></strong><br>
     * TODO 仔细考虑如何通用性更强
     * 
     * @param obj
     *            将要被更新所有属性的对象
     * @param strWhere
     *            更新的查询条件
     * @param params
     *            更新的查询条件的参数值
     * @return 是否更新成功
     */
    int update(Object obj, String strWhere, DBParamWrapper params);

    /**
     * [ 改 ] 提供更新操作, 更新部分属性, <strong><i>子类具体实现</i></strong>
     * 
     * @param strUpdate
     *            更新的字段值, 形如 Xxx=?, Xxx=?
     * @param strWhere
     *            更新的查询条件, 形如 ( Xxx=? and|or Xxx? ) | (xxx not in (?,?)...
     * @param params
     *            更新的查询条件的参数值
     * @return 是否更新成功
     */
    int update(String strUpdate, String strWhere, DBParamWrapper params);



    /**
     * [ 查 ]泛型式查询单个对象, <strong><i>通用查询实现</i></strong><br>
     * -- [ add by tracy : 2011-12-19 ]<br>
     * 
     * @param tableName
     *            需要查询的表名
     * @param strWhere
     *            查询条件, 形如 ( Xxx=? and|or Xxx? ) | (xxx not in (?,?)...
     * @param params
     *            查询条件的参数值
     * @return 查询对象
     */
    <T> T query(String tableName, String strWhere, DBParamWrapper params);

    /**
     * [ 查 ]泛型式查询多个对象 , <strong><i>通用查询实现</i></strong><br>
     * -- [ add by tracy : 2012-12-19 ]<br>
     * 
     * @param tableName
     *            需要查询的表名
     * @param strWhere
     *            查询条件, 形如 ( Xxx=? and|or Xxx? ) | (xxx not in (?,?)...
     * @param params
     *            查询条件的参数值
     * @return 查询对象列表
     */
    <T> List<T> queryList(String tableName, String strWhere,
            DBParamWrapper params);

    /**
     * [ 查 ]泛型式查询多个对象, 有排序, 有分页, <strong><i>通用查询实现</i></strong><br>
     * -- [ add by tracy : 2012-12-19 ]<br>
     * 
     * @param tableName
     *            需要查询的表名
     * @param strWhere
     *            查询条件, 形如 ( Xxx=? and|or Xxx? ) | (xxx not in (?,?)...
     * @param params
     *            查询条件的参数值
     * @param strOrder
     *            排序字段, 形如 "Id,Name"
     * @param strLimit
     *            分页字段, 形如 "10,20"
     * @return 查询对象列表
     */
    <T> List<T> queryList(String tableName, String strWhere,
            DBParamWrapper params, String strOrder, String strLimit);

    /**
     * get 、 gets 、 query 、queryList 操作中需要调用, 填充数据模板信息,
     * <strong><i>仅限包内使用</i></strong><br>
     * 
     * @param rs
     *            传入的结果集
     * @return 结果集对象
     * @throws SQLException
     */
    Object getTemplate(ResultSet rs) throws SQLException;

    /**
     * [ 查 ]查询某个表的某个字段值操作(不支持二进制数据), <strong><i>通用查询实现</i></strong> <br>
     * --[ modyfied by tracy : 2011-12-19 : 重写]<br>
     * 
     * @param tableName
     *            需要查询的表名
     * @param filedToQuery
     *            需要查询的字段值, 形如 "Name"
     * @param strWhere
     *            查询条件, 形如 ( Xxx=? and|or Xxx? ) | (xxx not in (?,?)...
     * @param params
     *            查询条件的参数值
     * @return 查询结果, String 型, 用户根据具体逻辑进行处理
     */
    String getOneValue(String tableName, String filedToQuery, String strWhere,
            DBParamWrapper params);

    /**
     * [ 查 ]查询符合条件的 单个字段的 集合, <strong><i>通用查询实现</i></strong><br>
     * --[ add by tracy : 2011-12-19 ]<br>
     * 
     * @param tableName
     *            需要查询的表名
     * @param filedToQuery
     *            需要查询的字段值, 形如 "Name"
     * @param strWhere
     *            查询条件, 形如 ( Xxx=? and|or Xxx? ) | (xxx not in (?,?)...
     * @param params
     *            查询条件的参数值
     * @param strOrder
     *            排序字段, 形如 "Id,Name"
     * @param strLimit
     *            分页字段, 形如 "10,20"
     * @return 查询结果, String 型 列表, 用户根据具体逻辑进行处理
     */
    List<String> getOneValueList(String tableName, String filedToQuery,
            String strWhere, DBParamWrapper params, String strOrder,
            String strLimit);

    /**
     * [ 查 ]查询符合条件的 多个字段的 集合, <strong><i>通用查询实现</i></strong><br>
     * -- [ add by tracy : 2011-12-19 ]<br>
     * 
     * @param tableName
     *            需要查询的表名
     * @param filedsToQuery
     *            需要查询的多字段值, 形如 "Name,City", 如需要查询所有字段, 请使用 queryList
     * @param strWhere
     *            查询条件, 形如 ( Xxx=? and|or Xxx? ) | (xxx not in (?,?)...
     * @param params
     *            查询条件的参数值
     * @param strOrder
     *            排序字段, 形如 "Id,Name"
     * @param strLimit
     *            分页字段, 形如 "10,20"
     * @return 查询结果, 包含 String 型 列表的一个列表, 可看成一个二维的结果集, 用户根据具体逻辑进行处理
     */
    List<List<String>> getMultiValueList(String tableName,
            String filedsToQuery, String strWhere, DBParamWrapper params,
            String strOrder, String strLimit);

    /**
     * 取分页查询语句, <strong><i>通用实现</i></strong><br>
     * 
     * @param tableName
     *            需要查询的表名
     * @param fields
     *            需要查询的字段值
     * @param pageSize
     *            每页数量
     * @param pageNow
     *            当前所需页数
     * @param strWhere
     *            查询条件
     * @param strOrder
     *            排序字段, 形如 "Id,Name"
     * @return 拼接之后的分页查询语句
     */
    String getPage(String tableName, String fields, int pageSize, int pageNow,
            String strWhere, String strOrder);

    /**
     * 取分页查询语句, <strong><i>通用实现</i></strong><br>
     * 
     * @param join
     *            需要 join 的语句
     * @param pageSize
     *            每页数量
     * @param pageNow
     *            当前所需页数
     * @param strWhere
     *            查询条件
     * @param strOrder
     *            排序字段, 形如 "Id,Name"
     * @return 拼接之后的分页查询语句
     */
    String getPageByJoinSql(String join, int pageSize, int pageNow,
            String strWhere, String strOrder);

}
