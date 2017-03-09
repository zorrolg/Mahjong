package com.citywar.type;

public enum ChannelType
{
    
	// zhifubao
	APPLE("appley"),
	
	GOOGLE("googleplay"),
	
	// zhifubao
    ZHIFUBAO("alipay"),
    
    // zhifubao
    YILIAN("yilian"),
    
    // zhifubao
    CAIFUTONG("tenpay"),
    
    // 百度
    BAIDU("baidu"),
    
    // 360
    SANLIULING("360"),
    
    // 91
    JIUYAO("91"),

    // 华为
    HUAWEI("huawei"),
    
    // 同步推
    TONGBU("tongbu"),
    
    // 手闲
    SHOUXIAN("soxan"),

    // 豌豆荚
    WANDOUJIA("wandoujia"),
    
    // 金立
    JINLI("jinli"),
    
    // oppo
    OPPO("oppo"),
    
    // itools
    ITOOLS("itools"),
    
    // dangle
    DANGLE("dangle"),
    
    // dangle-android
    DANGLEAD("danglead"),
    
    // 快用
    KUAIYONG("kuaiyong"),
    
    // 
    LENOVO("lenovo"),
    
    // 
    JIFENG("jifeng"),

	AISI("aisi");
	
	
    
    private final String channelName;

    private ChannelType(String channel)
    {
        this.channelName = channel;
    }


    public String getValue()
    {
        return channelName;
    }
    
    public String getValueTag()
    {
        return channelName + "_";
    }
}
