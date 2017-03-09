/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.type;

/**
 * 发送给flash客户端的协议定义
 * 
 * @author sky
 * @date 2011-05-04
 * @version
 * 
 */
public interface UserCmdOutType
{

    /*
     * ----------------------------登陆注册相关------------------------
     */

    /**
     * 注册新用户
     */
    public final static short USER_UPDATE = 0x00;
    
    /**
     * 玩家修改自己的信息
     * 
     * @since 2.2
     */
    public final static short USER_UPDATE_INFO = 0x100;

    /**
     * sdk登录
     * 
     * @since 2.2
     */
    public final static short SDK_LOGIN = 0x101;
    
    /**
     * 发送登陆成功或失败的消息
     */
    public final static short LOGIN_ACK = 0x01;
    
    /**
     * 验证帐号
     */
    public final static short USER_ACCOUNT_CHECK = 0x0d;
    
    /**
     * 查看大礼包
     */
    public final static short GET_GIFT_BAG = 0xf9;

    /**
     * 创建临时用户的响应
     */
    public final static short USER_TEMP = 0x04;

    /**
     * 注册返回
     */
    public final static short USER_REG = 0x05;
    
    /**
     * 请求设备最后一次登录的账号
     */
    public final static short USER_NEWER_ACCOUNT = 0x48;

    /**
     * 用户是否可以注册会员
     * @since V2.2
     */
    public final static short USER_IS_REG_MEMBER = 0x110;
    
    /**
     * 注册账号
     * @since V2.2
     */
    public final static short USER_REG_MEMBER  = 0x111;
    
    /**
     * 用户开关房间位置
     */
    public final static short USER_OPERATION_PLACE = 0x112;
    
    /**
     * 发送登陆奖励
     */
    public final static short LOGIN_AWARD = 0x09;

    /**
     * 用户被踢下线
     */
    public final static short KIT_USER = 0x07;

    /**
     * 更新财富数值(点券金币功勋礼金)
     */
    public final static short UPDATE_PRIVATE_INFO = 0x02;

    /**
     * 更新人物基本属性
     */
    public final static short UPDATE_BASE_INFO = 0x03;

    /**
     * 广播道具使用
     */
    public final static short BROC_PROP_USE = 0x08;
    
    
    /** 用户使用或者关闭某种道具 */
    public final static short OPEN_OR_CLOSE_ITEM = 0x1f1;
    
    

    /**
     * 实名制注册
     */
    public final static short USER_REAL_REG = 0x0a;
    
    /**
     * 实名制注册
     */
    public final static short USER_BUILD = 0x0c;
    
    
    
    /**
     * 在线时长
     */
    public final static short USER_ONLINE_TIME = 0x10a;

    /*
     * ---------------------------- end 登陆注册相关 end ------------------------
     */
    
    // ------------------- 服务器端发送提示语end--------------------
    /**
     * 有确定按钮的提示语,并返回客户的操作
     */
    public final static short MESSAGE_AND_OPERATE = 0x13;
    /**
     * 没有按钮的提示框，带类型
     */
    public final static short PROMPT_MESSAGE = 0x14;
    
    /**
     * 用户举报
     */
    public final static short USER_REPORT_PIC = 0x15;
    // ------------------- 服务器端发送提示语end--------------------
    

    // ------------------- 个人信息相关 -----------------------

    /**
     * 显示个人资料
     */
    public final static short USER_INFO_SHOW = 0x21;

    /**
     * 编辑个人资料
     */
    public final static short USER_UPDATE_IMAGE = 0x22;

    // ------------------- end 个人信息相关 end -----------------------
    
    // ------------------- 邀请用户游戏end--------------------
    public final static short RESPONSE_INVITE_USER = 0x27;
    public final static short SEND_INVITE_USER = 0x28;
    // ------------------- 邀请用户游戏end--------------------
    
    
    
    // ------------------- 卡片相关 -----------------------
    /**
     * 建筑信息
     */
    public final static short USER_BUILD_INFO = 0x30;
    
    /**
     * 建筑升级
     */
    public final static short USER_BUILD_UPDATE = 0x31;
    
    /**
     * 建筑
     */
    public final static short USER_BUILD_CHECK = 0x32;
    
    /**
     * 建筑加速
     */
    public final static short USER_BUILD_QUICK = 0x33;
    
    
    
    /**
     * 卡片信息
     */
    public final static short USER_CARD_DEVELOP_INFO = 0x34;
    
    /**
     * 卡片研究
     */
    public final static short USER_CARD_DEVELOP_UPDATE = 0x35;
    
    /**
     * 卡片研究
     */
    public final static short USER_CARD_DEVELOP_CHECK = 0x36;
    
    /**
     * 卡片研究加速
     */
    public final static short USER_CARD_DEVELOP_QUICK = 0x37;
    
    
    
    /**
     * 卡片生产
     */
    public final static short USER_CARD_FACTORY_INFO = 0x38;
    
    /**
     * 卡片生产
     */
    public final static short USER_CARD_FACTORY_UPDATE = 0x39;
    
    /**
     * 卡片生产
     */
    public final static short USER_CARD_FACTORY_CHECK = 0x3a;
    
    /**
     * 卡片生产加速
     */
    public final static short USER_CARD_FACTORY_QUICK = 0x3b;
    
    /**
     * 卡片使用
     */
    public final static short USER_CARD_USE = 0x3c;
    
    /**
     * 卡片快速购买
     */
    public final static short USER_CARD_BUY = 0x3d;
    // ------------------- end 卡片相关 end -----------------------
    
    
    
    
    // ---------------------- 商店商品相关 ----------------------------
    /**
     * 玩家购买商品
     */
    public final static short BUY_ITEM = 0xe0;

    /**
     * 显示商品信息
     */
    public final static short SHOP_GOODS_SHOW = 0xe1;

    /**
     * 玩家使用道具
     */
    public final static short PROP_USE = 0xe2;

    /**
     * 广播玩家使用道具效果（保持一局）
     */
    public final static short BROC_PROP_USE_EFFECT = 0xe6;

    /**
     * 醉酒后提示玩家使用道具
     */
    public final static short DRUNK_PROP_USE = 0xe3;

    /**
     * 游戏中显示商品信息
     */
    public final static short SHOP_OUTSIDE_GOODS_SHOW = 0xe4;

    /**
     * 显示道具信息
     */
    public final static short USER_ITEM_BAG_SHOW = 0xe5;
    
    /**
     * 不足时钻石购买
     */
    public final static short USER_PAY_MONEY = 0xe7;
    
    /**
     * 获取汇率
     */
    public final static short USER_EXCHANGE_RATE = 0xe8;
    
    /**
     * 显示道具信息
     */
    public final static short USER_PERSENT_MONEY = 0xe9;

    // ---------------------- end 商店商品相关 --------------------------

    // ------------------- 礼品相关 -----------------------
    /**
     * 玩家购买礼品,并赠送
     */
    public final static short SEND_GIFT = 0x70;

    /**
     * 显示可以赠送的礼品
     */
    public final static short GIFT_SHOW = 0x71;

    /**
     * 玩家使用道具（客户端去处理，一局游戏后效果消失）广播
     */
    public final static short GIFT_USE = 0x72;

    /**
     * 玩家礼品盒
     */
    public final static short GIFT_BOX = 0x73;

    /**
     * 玩家赠送记录（自己收到的礼品）
     */
    public final static short SEND_GIFT_LOG = 0x74;

    // ------------------- end 礼品相关 end -----------------------
    
  //-----用户购买订单相关--------------------
    public final static short USER_BUY_ORDER = 0x90;
    
    public final static short USER_BUY_ORDER_ANDROID = 0x9a;
    
    public final static short USER_BUY_ORDER_NOTIFY = 0x9b;
    
    public final static short USER_BUY_ANDROID_KEY = 0x9c;
    //-----用户购买订单相关end--------------------
    
    
    // -------------------- 好友相关响应 ----------------------

    /**
     * load好友响应
     */
    public final static short FRIENDS_LOAD_RESP = 0xa0;

    /**
     * 查看好友详情响应
     */
    public final static short FRIENDS_DETAIL_RESP = 0xa1;

    /**
     * 添加好友响应
     */
    public final static short FRIENDS_ADD_RESP = 0xa2;

    /**
     * 查找好友响应
     */
    public final static short FRIENDS_FIND_RESP = 0xa3;

    /**
     * 删除好友响应
     */
    public final static short FRIENDS_DELETE_RESP = 0xa4;

    // -------------------- 好友相关响应END ------------------

    /*
     * ----------------------------系统或网络相关------------------------
     */

    /**
     * 断开链接
     */
    public static final short DISCONNECT = 0x00;

    /**
     * 返回PING时间
     */
    public final static short PING_ACK = 0x07;

    /**
     * 同步系统时间
     */
    public final static short SYS_DATE = 0x10;

    /**
     * 网络状态
     */
    public final static short NETWORK = 0x06;

    /*
     * ---------------------------- end 系统或网络相关 end ------------------------
     */

    /*
     * --------------------------- 场景和人物相关 ------------------------
     */

    /**
     * 场景中加入用户
     */
    public final static short SCENE_ADD_USER = 0x12;

    /**
     * 删除好友
     */
    public final static short FRIEND_REMOVE = 0xa1;

    /**
     * 好友状态
     */
    public final static short FRIEND_STATE = 0xa5;

    /*
     * ---------------------------- end 场景和人物相关 end ------------------------
     */

    /*
     * --------------------------- 房间相关 dream ------------------------
     */
    /**
     * 创建房间
     */
    public final static short GAME_ROOM_CREATE = 0xc1;
    
    /**
     * 玩家进入房间
     */
    public final static short USER_ENTER_ROOM = 0xcb;
    
    /**
     * 玩家退出
     */
    public final static short GAME_PLAYER_EXIT = 0xc5;
    
    /**
     * 房间上锁，解锁
     */
    public final static short ROOM_LOCK = 0xc9;
    
    /**
     * 房间上锁，解锁
     */
    public final static short ROOM_PLAYER_COIN = 0xce;
    
    /*
     * --------------------------- end 房间相关 end ------------------------
     */
    
    /*
     * --------------------------- 房间、游戏相关 dream ------------------------
     */


    /**
     * 玩家进入
     */
    public final static short GAME_PLAYER_ENTER = 0xc2;

    /**
     * 快速撮合游戏
     */
    public final static short FIND_PLAYER_ROOM = 0xc8;

    /**
     * 房间位置
     */
    public final static short GAME_ROOM_UPDATE_PLACE = 0xc3;

    /**
     * 房间位置
     */
//    public final static short PLAYER_SHAKE_STATE = 0xc4;

    /**
     * 房间列表信息
     */
    public static final short ROOM_UPDATE_HOST = 0xc6;
    
    /**
     * 更新房间基本信息(房主和锁)
     * 
     * @verion 2.2
     */
    public static final short ROOM_UPDATE_INFO = 0x1c6;

    /**
     * 通知客户端可以摇手机
     */
    public static final short SHAKE_PREPARE = 0xc7;
    
    /**
     * 通知客户端可以摇手机
     */
    public static final short ROOM_UPDATE_PLAYER = 0xcc;
    
    
    /**
     * 通知还没有摇的客户端摇手机--解决双摇的问题
     */
    public static final short SHAKE_OTHERS_PREPARE = 0xca;
    /**
     * 通知游戏内的用户有其他用户强制退出了游戏
     */
    public static final short SEND_USER_EXIT = 0xcf;

    /**
     * 开始游戏
     */
    public final static short GAME_START = 0xd0;
    
    /**
     * 玩家聊天
     */
    public final static short USER_CHAT = 0xd4;
    

    /**
     * 房间列表信息
     */
    public static final short GAME_ROOMLIST_UPDATE = 0x5f;

    /**
     * 更新房间设置
     */
    public final static short GAME_ROOM_SETUP_CHANGE = 0xcd;

    /**
     * 创建游戏
     */
    public final static short UPDATE_PLAYER_STATE = 0xc4;

    /**
     * 游戏中得到色子点数
     */
    public static final short GAME_START_BOUT = 0xd1;

    /**
     * 游戏中TURN
     */
    public static final short GAME_TURN = 0xd2;

    /**
     * 游戏中CALL
     */
    public static final short GAME_OPEN = 0xd3;

    /**
     * game over
     */
    public static final short GAME_OVER = 0xd5;
    
    /**
     * 游戏中聊天
     */
    public static final short GAME_CHAT = 0x03;
    

    /**
     * 玩家聊天
     */
    public final static short GAME_ROBOT_STATE = 0xed;
    
    
    
    /**
     * 
     */
    public final static short GAME_DIZHUSTART = 0xd7;
    
    /**
     * 
     */
    public final static short GAME_DIZHUBET = 0xd8;

    /**
     * 
     */
    public final static short GAME_DIZHUTURN = 0xd9;
    
    /**
     * 
     */
    public final static short GAME_DIZHUOVER = 0xda;
    
    
    
    
    
    
    /**
     * 
     */
    public final static short GAME_NIUSTART = 0xdb;
    
    /**
     * 
     */
    public final static short GAME_NIUBET = 0xdc;

    /**
     * 
     */
    public final static short GAME_NIUTURN = 0xdd;
    
    /**
     * 
     */
    public final static short GAME_NIUOVER = 0xde;
    
    
    
    
    /**
     * 
     */
    public final static short GAME_TEXASSTART = 0xea;
    
    /**
     * 
     */
    public final static short GAME_TEXASTURN = 0xeb;
    
    /**
     * 
     */
    public final static short GAME_TEXASOVER = 0xec;
    
    
    
    
    /**
     * 
     */
    public final static short GAME_ERQISTART = 0x1a0;
    /**
     * 
     */
    public final static short GAME_ERQIBET = 0x1a1;
    
    /**
     * 
     */
    public final static short GAME_ERQIMASTER = 0x1a2;
    
    /**
     * 
     */
    public final static short GAME_ERQICOLOR = 0x1a3;
    
    /**
     * 
     */
    public final static short GAME_ERQITURN = 0x1a4;
    
    /**
     * 
     */
    public final static short GAME_ERQIOVER = 0x1a5;
    
    /*
     * --------------------------- end 房间、游戏相关 end ------------------------
     */

    // ------------------- 附近用户相关 -----------------------

    /**
     * 查看附近用户响应
     */
    public final static short AROUND_LOAD_RESP = 0xb2;
    
    /**
     * 查看所有的附近用户响应
     */
    public final static short ALL_AROUND_LOAD_RESP = 0xb0;

    // ------------------- 附近用户相关 end -----------------------


    
    //---------------------用户任务相关----------------------------
    
    public final static short USER_FINISH_TASK = 0x80;
    
    public final static short USER_CURRENT_TASK = 0x81;
    
    public final static short SEND_SUBMIT_TASK = 0x82;
    
    
    
    public final static short USER_BROKE_CHECK = 0x83;
    
    public final static short USER_BROKE_COIN = 0x84;
    //--------------大厅相关 begin ----------------
    /**
     * 获得大厅类型列表
     */
    public final static short HALL_TYPE_LIST = 0x94;
    /**
     * 进入大厅相关指令
     */
    public final static short USER_ENTER_HALL = 0x91;
    /**
     * 只有提示语，没有取消确定按钮
     */
    public final static short RETURN_AND_MESSAGE = 0x95;
    
    /**
     * 只有提示语，没有取消确定按钮
     */
    public final static short RETURN_AND_MESSAGE_ROOM = 0x98;
    
    /**
     * 用户选择大厅类型
     */
    public final static short USER_CHOOSE_HALL_TYPE = 0x195;
    
    
    /**
     * 用户退出房间进入大厅
     */
    public final static short USER_EXIT_ROOM_ENTER_HALL = 0x97;
    /**
     * 退出大厅指令
     */
    public final static short USER_EXIT_HALL = 0x92;
    /**
     * 获得当前最新的大厅房间列表
     */
    public final static short USER_ROOMS_LIST = 0x93;
    
    //-----大厅相关end--------------------
    
  //--------------奴隶相关 begin ----------------
    /**
     * 加载个人奴隶列表
     */
    public final static short USER_SLAVES_LIST = 0x50;
    /**
     * 奴隶详细信息--包括主人和自己的他人的
     */
    public final static short USER_SLAVES_DETAIL = 0x51;
    /**
     * 用户主人的相关信息
     */
    public final static short USER_MASTER_DETAIL = 0x52;
    /**
     * 用户通过赎金来解除奴隶关系
     */
    public final static short USER_BUY_REFERENCE = 0x53;
    /**
     * 主人去收取税收/主人去给奴隶惩罚
     */
    public final static short MASTER_TO_SLAVE_DECIDE = 0x54;
    /**
     * 快喝醉的玩家列表
     */
    public final static short SHOW_DRUNK_PLAYER = 0x55;
    
    /**
     * 喝醉
     */
    public final static short USER_DRUNK_NOTIFY = 0x56;
    
    /**
     * 喝醉
     */
    public final static short USER_DRUNK_LOCK = 0x57;
    
    /**
     * 喝醉
     */
    public final static short USER_DRUNK_UNLOCK = 0x58;
    //----------------奴隶相关 end ----------------
    
    //--------------用户反馈相关 begin ----------------
    /**
     * 用户反馈信息
     */
    public final static short USER_FEEDBACK = 0x60;
    
    public final static short STAGE_USER = 0x61;
    
    public final static short STAGE_PRIZE = 0x62;
    
    public final static short STAGE_CHARTS = 0x63;
    
    public final static short STAGE_IN = 0x64;
    
    public final static short STAGE_TOP_HEAD = 0x65;
    
    public final static short STAGE_ENROLL = 0x66;
    
    public final static short STAGE_INDEX = 0x67;
    
    public final static short STAGE_OVER = 0x68;
    
    public final static short STAGE_WAIT = 0x69;
    
           
    
    public final static short PENGYOU_GAMESTATE = 0x6a;
    
    public final static short PENGYOU_GAMECONFIG = 0x6b;
    
    public final static short PENGYOU_GAMECONTINUE = 0x6c;
    
    public final static short PENGYOU_OUTCONFIRM = 0x6d;
    
    public final static short PENGYOU_HISTORY = 0x6e;
    
    public final static short PENGYOU_HISTORY_DETAIL = 0x6f;
    //----------------用户反馈相关 end ----------------
    
    
    
    
    /**
     * 玩家使用 大喇叭
     */
    public final static short USE_TRUMPET =  0x85;
    
    /**
     * 用户送礼
     */
    public final static short  USER_SEND_GIFT = 0x86;
    /**
     * 5星评价
     */
    
    public final static short USER_REWARD_ACTION = 0x87;
    
    public final static short USER_REWARD_STATE = 0x88;
    

    public final static short USER_DRAW_ACTION = 0x89;
    
    public final static short USER_DRAW_STATE = 0x8a;
    //----------------私信相关 begin ----------------
    
    /**
     * 自上一次发送未读私信后  又有新的私信内容
     */
    public final static short USER_HAS_NOREADLETTER = 0x78;
   
    /**
     * 用户打开与某玩家的私信
     */
    public final static short USER_READ_LETTER = 0x75;
    
    /**
     * 用户发送私信
     */
    public final static short USER_SEND_LETTER = 0x76;
    
    /**
     * 用户请求未读私信列表
     */
    public  final static short USER_REQUEST_NOREADLETTER = 0x79;
    
    /**
     * 用户请求未读私信列表
     */
    public  final static short USER_REVICE_LETTER = 0x7a;
    
    
    public  final static short USER_OPEN_KKCHAT = 0x7b;
    
    /**
     * 用户删除私信
     */
    public final static short USER_DEL_LETTER = 0x41;
    
    /**
     * 排行榜
     */
    public final static short USER_CHARTS = 0x49;
	
	/**
	* 新的排行榜协议 csf0906
	*/
	public final static short USER_NEW_CHARTS = 0x4a;
  
    //----------------私信相关 end ----------------
    
    //----------------系统控制相关 begin ----------------
    /**
     * 系统用户登录
     */
    public final static short SYSTEM_USER_LOGIN = 0xf1;
    /**
     * 系统用户控制
     */
    public final static short SYSTEM_USER_CONTROL = 0xf2;
    //----------------系统控制相关 end ----------------
}
