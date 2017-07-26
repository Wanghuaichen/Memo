package szy.analytic;
import szy.request.Device;
import ui.AlarmIcon.AlarmLevel;

public class JudgeGPSPosition {
	 private static double EARTH_RADIUS = 6378.137; 
	public static AlarmLevel getGPSFroDev(Device _dev)//����null ��ʾ�� 
	{
		AlarmLevel res=null;
		if(_dev.getGPS()==1)
		{ 
		
			if(getDistance(_dev.getYlat(),_dev.getYlan(),_dev.getGPSlat(),_dev.getGPSlan())>20000.0)
			{
				System.out.println("distance"+getDistance(_dev.getYlat(),_dev.getYlan(),_dev.getGPSlat(),_dev.getGPSlan()));
				res= AlarmLevel.orange;
				_dev.setGPSAbnormal(true);
			}
			else if(getDistance(_dev.getYlat(),_dev.getYlan(),_dev.getGPSlat(),_dev.getGPSlan())>5000.0)
		{
			System.out.println("distance"+getDistance(_dev.getYlat(),_dev.getYlan(),_dev.getGPSlat(),_dev.getGPSlan()));
			res= AlarmLevel.red;
			_dev.setGPSAbnormal(true);
		}
			else if(getDistance(_dev.getYlat(),_dev.getYlan(),_dev.getGPSlat(),_dev.getGPSlan())>2000.0)
		{
			System.out.println("distance"+getDistance(_dev.getYlat(),_dev.getYlan(),_dev.getGPSlat(),_dev.getGPSlan()));
			res= AlarmLevel.yellow;
			_dev.setGPSAbnormal(true);
		}else{
			_dev.setGPSAbnormal(false);
		}
		}

		return res;
		
	}
    private static double rad(double d) { 
        return d * Math.PI / 180.0; 
    }
 public static Double getDistance(Double lat1Str, Double lng1Str, Double lat2Str, Double lng2Str) {
        Double lat1 = lat1Str;
        Double lng1 = lng1Str;
        Double lat2 = lat2Str;
        Double lng2 = lng2Str;
         
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double difference = radLat1 - radLat2;
        double mdifference = rad(lng1) - rad(lng2);
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(difference / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(mdifference / 2), 2)));
        distance = distance * EARTH_RADIUS;
        distance = Math.round(distance * 10000) / 10000;
     
          
        return distance;
    }
// public static DrawGPSMarker()
// {
//	 
// }
}
