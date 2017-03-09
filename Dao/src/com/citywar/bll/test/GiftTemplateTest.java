/**
*All rights reserved. This material is confidential and proprietary to CityWar
*/
package com.citywar.bll.test;

import com.citywar.bll.GiftTemplateBussiness;
import com.citywar.dice.entity.GiftTemplateInfo;

/**
 * @author shanfeng.cao
 * @date 2012-06-26
 * @version 
 *
 */
public class GiftTemplateTest
{

    public static void queryList()
    {
        for (GiftTemplateInfo info :GiftTemplateBussiness.getAllGiftTemplateInfos())
        {
            System.out.println(info.getGiftName());
        }
    }
}
