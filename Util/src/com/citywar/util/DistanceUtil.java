/**
*All rights reserved. This material is confidential and proprietary to CityWar
*/
package com.citywar.util;

/**
 * 距离计算通用类
 * @author charles
 * @date 2011-12-20
 * @version 
 *
 */
public class DistanceUtil
{
    /**
     * 根据经纬度计算两点距离
     * @param _Latidute1 第一点纬度
     * @param _Longitude1 第一点经度
     * @param _Latidute2 第二点纬度
     * @param _Longitude2 第二点经度
     * @return 两点距离
     */
    public static double distanceByLatLon(double _Latidute1, double _Longitude1, 
            double _Latidute2, double _Longitude2) 
    {
    	if((_Latidute1 == 0 && _Longitude1 == 0) || (_Latidute2 == 0 && _Longitude2 == 0))   {
    		return 20000000;
    	}
        double radLat1 = _Latidute1 * Math.PI / 180;
        double radLat2 = _Latidute2 * Math.PI / 180;
        double a = radLat1 - radLat2;
        double b = _Longitude1 * Math.PI / 180 - _Longitude2 * Math.PI / 180;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378137.0;// 取WGS84标准参考椭球中的地球长半径(单位:m)
        s = Math.round(s * 10000) / 10000;
        return s;
    }
 
    /**
     * 获取 'x,y' 格式的经纬度信息
     * @param userPos ‘x,y'
     * @return 经纬度组成的int数组
     */
    public static double[] getLatAndLon(String userPos)
    {
        double userLatitude = 0.0;
        double userLongitude = 0.0;
        String[] posArray = userPos.split(",");
        if(posArray.length == 2)
        {
            userLatitude = Double.parseDouble(posArray[0]);
            userLongitude = Double.parseDouble(posArray[1]);
        }
        return new double[]{userLatitude,userLongitude};
    }
}
