/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.bll.test;

import java.sql.Timestamp;
import java.util.List;

import com.citywar.bll.ReferenceBussiness;
import com.citywar.dice.entity.UserReferenceInfo;

/**
 * @author caosf
 * @date 2011-12-29
 * @version
 * 
 */
public class ReferenceTest {

	public static void addReferences() {
		List<UserReferenceInfo> list1 = ReferenceBussiness.getUserAllNowValidReferencesLimit(14186, new Timestamp(System.currentTimeMillis()), 50);
		for(UserReferenceInfo info : list1) {
			System.out.println("Slaves==" + info.getSlavesUserName());
		}
//		UserReferenceInfo info1 = ReferenceBussiness.getMasterInfoByUserId(10000114);
//		List<UserReferenceInfo> list = new ArrayList<UserReferenceInfo>();
//		UserReferenceInfo info = new UserReferenceInfo(1, 14116, 14118,
//				"", "", new Timestamp(System.currentTimeMillis()),
//				new Timestamp(System.currentTimeMillis()), 100, 100,
//				14112);
//		list.add(info);
//		ReferenceBussiness.insertUserReferences(list);
//		ReferenceBussiness.updateUserReferences(list);
	}
}
