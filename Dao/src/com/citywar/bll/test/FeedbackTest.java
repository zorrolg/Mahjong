/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.bll.test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.citywar.bll.FeedbackBussiness;
import com.citywar.dice.entity.FeedbackInfo;

/**
 * @author caosf
 * @date 2012-05-25
 * @version
 * 
 */
public class FeedbackTest {

	public static void selectFeedbacks() {
		List<FeedbackInfo> list = FeedbackBussiness.getUserAllFeedbackLimit(14116, 50);
		for(FeedbackInfo info : list) {
			System.out.println("UserReferenceLogInfo===" + info);
		}
	}

	public static void addFeedbacks() {
		List<FeedbackInfo> list = new ArrayList<FeedbackInfo>();
		FeedbackInfo info = new FeedbackInfo(0, 14116, 1,
				 new Timestamp(System.currentTimeMillis()), "da撒旦法师法发顺丰s");
		list.add(info);
		FeedbackBussiness.insertUserFeedbacks(list);
	}
	public static void updateFeedbacks() {
		List<FeedbackInfo> list = new ArrayList<FeedbackInfo>();
		FeedbackInfo info = new FeedbackInfo(1, 14116, 1,
				 new Timestamp(System.currentTimeMillis()), "da撒旦法sfsfsf丰s");
		list.add(info);
		FeedbackBussiness.updateUserFeedbacks(list);
	}
}
