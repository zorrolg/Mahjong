/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.type;

/**
 * 所有发送到GameServer的玩家指令编号定义
 * 
 * @author sky
 * @date 2011-04-28
 * @version
 * 
 */
public interface UserCmdType
{
	
    // ------------------- 登陆相关 -----------------------

    /**
     * 更新用户注册信息
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
     * 登录指令
     */
    public final static short LOGIN = 0x01;
    
    /**
     * 查看大礼包
     */
    public final static short GET_GIFT_BAG = 0xf9;

    /**
     * 验证帐号
     */
    public final static short USER_ACCOUNT_CHECK = 0x0d;
    
    
    /**
     * 用户退出或掉线
     */
    public final static short LOGOUT = 0x16;

    /**
     * 请求临时账户
     */
    public final static short USER_TEMP = 0x04;

    /**
     * 直接注册账户
     */
    public final static short USER_REG = 0x05;
    
    
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
     * 请求设备最后一次登录的账号
     */
    public final static short USER_NEWER_ACCOUNT = 0x48;

    /**
     * 实名制注册
     */
    public final static short USER_REAL_REG = 0x0a;
    
    /**
     * 在线时长
     */
    public final static short USER_ONLINE_TIME = 0x10a;

    // ------------------- end 登陆相关end -----------------------
    
    // ------------------- 服务器端发送提示语end--------------------
    /**
     * 有确定按钮的提示语,并返回客户的操作
     */
    public final static short MESSAGE_AND_OPERATE = 0x13;
    
    /**
     * 用户举报
     */
    public final static short USER_REPORT_PIC = 0x15;
    // ------------------- 服务器端发送提示语end--------------------
    

    // ------------------- 个人信息相关 -----------------------

    /**
     * 显示个人资料 (没用上)
     */
    public final static short USER_INFO_SHOW = 0x21;

    /**
     * 编辑个人资料(没用上)
     */
    public final static short USER_UPDATE_IMAGE = 0x22;
    
    /**
     * 用户上传头像(没用上)
     */
    public final static short UPLOAD_USER_HEAD_PIC = 0x23;
    // ------------------- end 个人信息相关 end -----------------------

    // ------------------- 邀请用户游戏end--------------------
    /**
     * 邀请
     */
    public final static short INVITE_USER = 0x27;
    
    /**
     * 
     */
    public final static short CHOOSE_INVITE_RESULT = 0x28;
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
    
    
    // ------------------- 商店相关 -----------------------
    /**
     * 玩家购买商品
     */
    public final static short BUY_ITEM = 0xe0;

    /**
     * 显示商店的商品信息(未与客户端商榷)
     */
    public final static short SHOP_GOODS_SHOW = 0xe1;

    /**
     * 玩家使用道具
     */
    public final static short PROP_USE = 0xe2;

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
    
   
    
    // ------------------- end 商店相关 end -----------------------
    
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
     * 玩家使用礼品（客户端去处理，一局游戏后效果消失）暂时还没用到
     */
    //public final static short GIFT_USE = 0x72;

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
    
    public final static short USER_BUY_ANDROID_KEY = 0x9c;
    //-----用户购买订单相关end--------------------

    // ------------------- 好友相关 -----------------------
    /**
     * load好友
     */
    public final static short FRIENDS_LOAD = 0xa0;

    /**
     * 查看好友详情
     */
    public final static short FRIENDS_DETAIL = 0xa1;

    /**
     * 添加好友
     */
    public final static short FRIENDS_ADD = 0xa2;

    /**
     * 查找好友
     */
    public final static short FRIENDS_FIND = 0xa3;

    /**
     * 删除好友
     */
    public final static short FRIENDS_DELETE = 0xa4;

    // ------------------- 好友相关 end -----------------------

    // ------------------- 房间相关 -----------------------
    /**
     * 玩家进入房间
     */
    public final static short USER_ENTER_ROOM = 0xc1;

    /**
     * 玩家离开房间
     */
    public final static short USER_LEVEL_ROOM = 0xc5;

    /**
     * 快速撮合游戏
     */
    public final static short FIND_PLAYER_ROOM = 0xc8;
    
    /**
     * 房间上锁，解锁
     */
    public final static short ROOM_LOCK = 0xc9;
    
    /**
     * 房间上锁，解锁
     */
    public final static short ROOM_CHANGE = 0xca;
    
    
    // ------------------- 房间相关 end -----------------------
    
    /**
     * 游戏准备
     */
    public final static short SHAKE_PREPARE = 0xc0;
    

    /**
     * 游戏叫
     */
    public final static short GAME_CALL = 0xd2;

    /**
     * 用户超时之前保存的预设值
     */
    public final static short GAME_USE_TIMEOUT_CALL = 0xd6;
    
    /**
     * 游戏开
     */
    public final static short GAME_OPEN = 0xd3;

    /**
     * 玩家聊天
     */
    public final static short USER_CHAT = 0xd4;
    
    /**
     * 玩家聊天
     */
    public final static short SCENE_SMILE = 0xd5;
        
    
    /**
     * 玩家聊天
     */
    public final static short GAME_ROBOT_STATE = 0xed;
    
    

    /**
     * 
     */
    public final static short GAME_DIZHUBET = 0xd8;
    
    /**
     * 
     */
    public final static short GAME_DIZHUPASS = 0xd9;
    
    /**
     * 
     */
    public final static short GAME_DIZHUHAND = 0xda;

    
    
    
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
    

    
    // ------------------- 附近用户相关 -----------------------

    // ------------------- 附近用户相关 -----------------------

    /**
     * 查看附近用户, Alex－旧版本
     */
//    public final static short AROUND_LOAD = 0xb2;
    
    /**
     * 查看所有的附近用户
     */
    public final static short ALL_AROUND_LOAD = 0xb0;

    /**
     * 接受用户地理位置信息
     */
    public final static short AROUND_RECEIVE_XY = 0xb1;

    // ------------------- 附近用户相关 end -----------------------
    
    //-----任务相关---------------------
    /**
     * 领取任务的奖励
     */
    public final static short GET_TASK_PRIZE = 0x80;
    /*
     * 返回用户当前正在做的任务 
     */
    public final static short USER_CURRENT_TASK = 0x81;
    
    
    public final static short USER_BROKE_CHECK = 0x83;
    
    public final static short USER_BROKE_COIN = 0x84;
    //-----任务相关end--------------------

    //--------------大厅相关 begin ----------------
    /**
     * 获得大厅类型列表
     */
    public final static short HALL_TYPE_LIST = 0x94;

    
    /**
     * 用户选择大厅类型
     */
    public final static short USER_CHOOSE_HALL_TYPE = 0x195;
    /**
     * 用户退出房间进入大厅
     */
    public final static short USER_EXIT_ROOM_ENTER_HALL = 0x97;
    /**
     * 进入大厅相关指令
     */
    public final static short USER_ENTER_HALL = 0x91;
    /**
     * 退出大厅指令
     */
    public final static short USER_EXIT_HALL = 0x92;
    /**
     * 获得当前最新的大厅房间列表
     */
    public final static short USER_ROOMS_LIST = 0x93;
    /**
     * 玩家在首页点击登录大厅
     */
    public final static short USER_ENTER_SUIT_HALL = 0x96;
    
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
    public final static short USER_DRUNK_LOCK = 0x57;
    /**
     * 
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
    
    public final static short STAGE_WAIT = 0x69;
    
    
    
    public final static short PENGYOU_GAMESTATE = 0x6a;
    
    public final static short PENGYOU_GAMECONFIG = 0x6b;
    
    public final static short PENGYOU_GAMECONTINUE = 0x6c;
    
    public final static short PENGYOU_OUTCONFIRM = 0x6d;
    
    public final static short PENGYOU_HISTORY = 0x6e;
    
    public final static short PENGYOU_HISTORY_DETAIL = 0x6f;
    //----------------用户反馈相关 end ----------------
    
   
    /**
     * 用户使用大喇叭
     */
    public final static short USE_TRUMPET = 0x85;
    
    /**
     * 用户送礼
     */
    public final static short  USER_SEND_GIFT = 0x86;
    
    
    public final static short USER_REWARD_ACTION = 0x87;
    
    public final static short USER_REWARD_STATE = 0x88;
    
    
    public final static short USER_DRAW_ACTION = 0x89;
    
    public final static short USER_DRAW_STATE = 0x8a;
    
  //----------------私信相关 begin ----------------
    /**
     * 用户打开与某玩家的私信
     */
    public final static short USER_READ_LETTER = 0x75;
    /**
     * 用户发送私信
     */
    public final static short USER_SEND_LETTER = 0x76;
    
    /**
     * 用户离开私信界面 （用户进入私信界面的逻辑放在0x75）
     */
    public final static short USER_LEFT_LETTERUI = 0x77;
    
    /**
     * 用户请求未读私信列表
     */
    public final static short USER_REQUEST_NOREADLETTER = 0x79;
    
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
	* 排行榜协议	Alex－旧版本
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
