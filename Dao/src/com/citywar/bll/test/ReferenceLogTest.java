/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.bll.test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.citywar.bll.ReferenceLogBussiness;
import com.citywar.dice.entity.UserReferenceLogInfo;

/**
 * @author caosf
 * @date 2011-12-29
 * @version
 * 
 */
public class ReferenceLogTest {

	public static void selectReferenceLogs() {
		List<UserReferenceLogInfo> list = ReferenceLogBussiness.getUserAllReferencesLogLimit(14116, 50);
		for(UserReferenceLogInfo info : list) {
			System.out.println("UserReferenceLogInfo===" + info);
		}
	}

	public static void addReferenceLogs() {
		List<UserReferenceLogInfo> list = new ArrayList<UserReferenceLogInfo>();
		UserReferenceLogInfo info = new UserReferenceLogInfo(0, 14116, 14116, 12, 1, 14116, 14116,
				 new Timestamp(System.currentTimeMillis()), "da撒旦法师法发顺丰s", "da撒旦法师法发顺丰s", "da撒旦法师法发顺丰s");
		list.add(info);
		ReferenceLogBussiness.insertUserReferenceLogs(list);
	}

	public static void updateReferenceLogs() {
		List<UserReferenceLogInfo> list = new ArrayList<UserReferenceLogInfo>();
		UserReferenceLogInfo info = new UserReferenceLogInfo(8, 111, 111, 12, 1, 11, 11,
				 new Timestamp(System.currentTimeMillis()), "sada撒旦法师法发顺丰sdf", "sada撒旦法师法发顺丰sdf", "sada撒旦法师法发顺丰sdf");
		list.add(info);
		ReferenceLogBussiness.updateUserReferenceLogs(list);
	}
	
	public static void selectAllReferenceLogs() {
		List<UserReferenceLogInfo> list = ReferenceLogBussiness.getUserAllReferencesLog();
		UserReferenceLogInfo info2 = null;
		for(UserReferenceLogInfo info : list) {
			if(info!=null&&info2!=null&&info.getOwnerUserId()==info2.getOwnerUserId()
					&& info2.getPassivesUserId()==info.getPassivesUserId()) {
				//System.out.println("UserReferenceLogInfo===" + info);
				//System.out.println("UserReferenceLogInfo2===" + info2);
				//System.out.println("======================================================");
			}
			info2 = info;
		}
	}
}
