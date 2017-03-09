package com.citywar.type;

public enum MessageType
{
    // 普通消息黄色，有屏幕提示
    NORMAL(0),

    // 错误消息红色，有屏幕提示
    ERROR(1),

    // 普通消息黄色，无屏幕提示
    CHATNORMAL(2),

    // 错误消息红色，无屏幕提示
    CHATERROR(3),

    // 弹出框
    ALERT(4),

    // 日常奖励
    DAILYAWARD(5),

    // 成就
    ACHIEVEMENT(6);

    private final byte value;

    private MessageType(int value)
    {
        this.value = (byte) value;
    }

    public static MessageType parse(int type)
    {
        for (MessageType hardType : MessageType.values())
        {
            if (type == hardType.value)
                return hardType;
        }

        return NORMAL;
    }

    public byte getValue()
    {
        return value;
    }
}
