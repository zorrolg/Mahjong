/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.define;

/**
 * @author : Cookie
 * @date : 2011-7-12
 * @version GS和CS之间的协议号
 */
public interface CSPacketType
{
    /**
     * RSA密钥
     **/
    public static final short RSAKey = 0x0;

    /**
     * 用户登陆
     **/
    public static final short LOGIN = 0x1;

    /**
     * 踢用户下线
     **/
    public static final short KITOFF_USER = 0x2;

    /**
     * 允许用户登陆
     **/
    public static final short ALLOW_USER_LOGIN = 0x3;

    /**
     * 用户下线
     **/
    public static final short USER_OFFLINE = 0x4;

    /**
     * 用户上线
     **/
    public static final short USER_ONLINE = 0x5;

    /**
     * 更新用户状态
     **/
    public static final short USER_STATE = 0x6;

    /**
     * 更新防沉迷系统状态
     **/
    public static final short UPDATE_ASS = 0x07;

    /**
     * 更改配置文件状态
     **/
    public static final short UPDATE_CONFIG_STATE = 0x08;

    /**
     * 充值
     **/
    public static final short CHARGE_MONEY = 0x09;

    /**
     * 兑换礼金
     **/
    public static final short CHARGE_GIFTTOKEN = 0x10;

    /**
     * 场景聊天
     **/
    public static final short SCENE_CHAT = 0x13;

    /**
     * 系统公告
     **/
    public static final short SYS_NOTICE = 0x0a;

    /**
     * 重新加载
     **/
    public static final short SYS_CMD = 0x0b;

    /**
     * 网络测试
     **/
    public static final short PING = 0x0c;

    /**
     * 更新人物结婚属性
     **/
    public static final short UPDATE_PLAYER_MARRIED_STATE = 0x0d;

    /**
     * 获得物品提示
     **/
    public static final short GET_ITEM_MESS = 0x0e;

    /**
     * 发送结婚房间信息
     **/
    public static final short MARRY_ROOM_INFO_TO_PLAYER = 0x1a;

    /**
     * 发送关闭结婚房间信息
     **/
    public static final short MARRY_ROOM_DISPOSE = 0xf1;

    /**
     * 关闭服务器
     **/
    public static final short SHUTDOWN = 0x0f;

    /**
     * 私聊
     **/
    public static final short CHAT_PERSONAL = 0x25;

    /**
     * 系统消息转发
     **/
    public static final short SYS_MESS = 0x26;

    /**
     * 大喇叭
     **/
    public static final short B_BUGLE = 0x48;

    /**
     * 战报
     **/
    public static final short DISPATCHES = 0x7b;

    /**
     * 邮件响应
     **/
    public static final short MAIL_RESPONSE = 0x75;

    /**
     * 发生返回消息
     **/
    public static final short MESSAGE = 0x76;

    /**
     * 跨区大喇叭
     **/
    public static final short AREA_BIG_BUGLE = 0x19;

    /**
     * 回复提出的问题
     **/
    public static final short ADVANCE_QUESTION = 0xd5;

    /**
     * 更新物品列表缓存
     **/
    public static final short ItemBuffer = 0xd6;

    /**
     * 转发系统消息
     **/
    public static final short SEND_SYS_MESSAGE = 0xd9;

    /************************************** 公会 ********************************************/
    /**
     * 公会响应
     **/
    public static final short CONSORTIA_RESPONSE = 0x80;
    /**
     * 公会创建
     **/
    public static final short CONSORTIA_CREATE = 0x82;
    /**
     * 解散公会
     **/
    public static final short CONSORTIA_DELETE = 0x83;
    /**
     * 添加盟友
     **/
    public static final short CONSORTIA_ALLY_ADD = 0x93;
    /**
     * 聊天
     **/
    public static final short CONSORTIA_CHAT = 0x9b;
    /**
     * 公会贡献
     **/
    public static final short CONSORTIA_OFFER = 0x9c;
    /**
     * 公会财富
     **/
    public static final short CONSORTIA_RICHES = 0x9d;

    /**
     * 公会战
     **/
    public static final short CONSORTIA_FIGHT = 0x9e;

    /**
     * 公会升级
     **/
    public static final short CONSORTIA_UPGRADE = 0x9f;

    /**
     * 好友状态
     **/
    public static final short FRIEND_STATE = 0xa5;

    /**
     * 被添加好友响应
     **/
    public static final short FRIEND_RESPONSE = 0xa6;

    /**
     * 更新限量商品上线数
     **/
    public static final short UPDATE_SHOPLIMIT = 0xa8;

    /**
     * 倍率调整，(经验 工会战财富 功勋)
     **/
    public static final short RATE = 0xb1;

    /**
     * 时报期数设置
     **/
    public static final short WEEKLY_VERSION = 0xb3;

    /**
     * 物品掉落宏观控制
     **/
    public static final short MACRO_DROP = 0xb2;

    /**
     * 服务器的IP，端口
     **/
    public static final short IP_PORT = 0xf0;

    /**
     * 删除战斗服务器玩家
     **/
    public static final short KIT_OFF_PLAYER = 0xf2;

    /**
     * 更新商品公告
     **/
    public static final short UPDATE_SHOPNOTICE = 0xcc;

    /**
     * 更新服务器属性
     **/
    public static final short UPDATE_SERVERPROPERTIE = 0xcd;

    /**
     * 答题活动奖励
     **/

    public static final short ANSWER_ACTIVE = 0x22;

    /****************************** VIP ******************************/

    /**
     * VIP充值更新
     **/
    public static final short VIP_RENEWAL = 0x5c;

    /**
     * 玩家禁言
     **/
    public static final short BAN_CHAT = 0x5d;

    /****************************** GIFT ******************************/

    /**
     * 玩家魅力值增加
     **/
    public static final short GIFT_GP_ADD = 0xd7;

    /**
     * GM赠送礼品
     **/
    public static final short GIFT_RECEIVE_BYGM = 0xd8;
    /****************************** 师徒 *****************************/

    /**
     * 用户请求拜师
     **/
    public static final short APPLY_FOR_APPRENTICE = 0x64;

    /**
     * 用户请求收徒
     **/
    public static final short APPLY_FOR_MASTER = 0x65;

    /**
     * 师傅通过请求
     **/
    public static final short MASTER_CONFIRM = 0x66;

    /**
     * 徒弟通过请求
     **/
    public static final short APPRENTICE_CONFIRM = 0x67;

    /**
     * 师傅拒绝请求
     **/
    public static final short MASTER_REFUSE = 0x68;

    /**
     * 徒弟拒绝请求
     **/
    public static final short APPRENTICE_REFUSE = 0x69;

    /**
     * 通知客户端更新师徒状态
     **/
    public static final short REFRESH_APP_STATES = 0x6a;

    /**
     * 师徒经验加成
     **/
    public static final short APP_GP_BONUS = 0x6f;

    /**
     * 玩家出师
     **/
    public static final short APPRENTICE_GRADUATE = 0x6b;

    /**
     * 给对方发送解除关系的消息
     **/
    public static final short SEND_BREAK_APPSHIP_MSG = 0x6e;

    /**
     * 通知玩家好友更新玩家的师徒状态
     **/
    public static final short UPDATE_FRIENDS_APP_STATE = 0x71;

    /**
     * SNS分享信息提示
     **/
    public static final short SNS_NOTICE = 0x11;

    /**
     * 更新中心服师徒推荐列表
     */
    public static final short APPCLUB_SET_UPDATE = 0xfd;
    
    /**
     * 好友状态改变
     */
    public static final short FRIEND_STATE_CHANGE = 0xfe;

    /**
     * 广播
     */
    public static final short BROADCAST = 0xff;
}
