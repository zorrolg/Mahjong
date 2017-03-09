/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 玩家协议注释接口，用于标注来自于网关的协议处理器
 * 
 * @author sky
 * @date 2011-05-04
 * @version
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UserCmdAnnotation
{
    /**
     * 协议编号
     */
    int code();

    /**
     * 协议描述
     */
    String desc();
}
