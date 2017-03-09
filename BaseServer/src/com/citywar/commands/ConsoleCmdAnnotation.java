/**
*All rights reserved. This material is confidential and proprietary to CityWar
*/
package com.citywar.commands;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Dream
 * @date 2011-9-14
 * @version 
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConsoleCmdAnnotation
{
    /**
     * 命令字符串
     * @return
     */
    String cmdString();
    
    /**
     * 命令别名
     * @return
     */
    String[] cmdAliases() default {""};
    
    /**
     * 命令等级
     * @return
     */
    int level();
    
    /**
     * 命令描述
     */
    String description()default "";
    
    /**
     * 命令使用方法
     * @return
     */
    String[] usage()default {""};
}
