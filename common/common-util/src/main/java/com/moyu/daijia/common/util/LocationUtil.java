package com.moyu.daijia.common.util;

public class LocationUtil {

    // 地球赤道半径
    private static double EARTH_RADIUS = 6378.137;

    //等同——Math.toRadians()
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * @描述 经纬度获取距离，单位为米
     * @参数 [lat1, lng1, lat2, lng2]
     * @返回值 double
     **/
    public static double getDistance(double lat1, double lng1, double lat2,
                                     double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        s = s * 1000;
        return s;
    }

    public static void main(String[] args) {
        double distance = getDistance(30.57404, 104.073013,
                30.509376, 104.077001);
        System.out.println("距离" + distance + "米");
    }

}
