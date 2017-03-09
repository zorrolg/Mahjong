/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.define;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 游戏协议注释接口，通用协议标注
 * 
 * @author sky
 * @date 2011-05-04
 * @version
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CmdAnnotation
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
