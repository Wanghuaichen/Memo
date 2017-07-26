package szy.analytic;

import java.util.ArrayList;

import szy.context.StaticContext;
import szy.request.Device;
import szy.request.Sensor;
import szy.request.SensorRange;
import ui.AlarmIcon.AlarmLevel;

public class JudgeWaterQuality {
	
	public static AlarmLevel getQualityFromSensor(Sensor _sen)
	{
		AlarmLevel al=null;
		SensorRange s=null;
		for(int i=0;i<StaticContext.senR.size();i++)
		{
			if(StaticContext.senR.get(i).getSensorId()==_sen.getSensorId())
			{
				s=StaticContext.senR.get(i);
				break;
			}
		}
		if(_sen.getSensorId()==200)//ph
		{
			double ph=_sen.getData();
			System.out.println("asdf"+_sen.getData());
			if(s.getDown()<=ph&&ph<=s.getUp())
				al=null;
			else			
				al= AlarmLevel.red;
		}
		else if(_sen.getSensorId()==700)//turbidity
		{
			double turbidity=_sen.getData();
			if(s.getDown()<=turbidity&&turbidity<=s.getUp())
				al= null;
			else
				al= AlarmLevel.red;
		}
		else if(_sen.getSensorId()==600)//conductivity
		{
			double conductivity=_sen.getData();
			if(s.getDown()<=conductivity&&conductivity<=s.getUp())
				al= null;
			else
				//al= AlarmLevel.red;
				al= AlarmLevel.red;
		}
		else if (_sen.getSensorId()==300)//NO3
		{
			double nitrate=_sen.getData();
			if(s.getDown()<=nitrate&&nitrate<=s.getUp())
				al= null;
			else
				//al= AlarmLevel.red;
				al= AlarmLevel.red;
			
		}
		else if (_sen.getSensorId()==400)//lanz
		{
			double BlueAlgae=_sen.getData();
			if(s.getDown()<=BlueAlgae&&BlueAlgae<=s.getUp())
				al= null;
			else
				//al= AlarmLevel.red;
				al= AlarmLevel.red;
			
		}
		else if (_sen.getSensorId()==1000)//yelvsu
		{
			double chlorophyll=_sen.getData();
			if(s.getDown()<=chlorophyll&&chlorophyll<=s.getUp())
				al= null;
			else
				//al= AlarmLevel.red;
				al= AlarmLevel.red;
			
		}
		else if (_sen.getSensorId()==500)//yulv
		{
			double yulv=_sen.getData();
			if(s.getDown()<=yulv&&yulv<=s.getUp())
				al= null;
			else
				//al= AlarmLevel.red;
				al= AlarmLevel.red;
			
		}
		else if (_sen.getSensorId()==1300)//t
		{
			double t=_sen.getData();
			if(s.getDown()<=t&&t<=s.getUp())
				al= null;
			else
				//al= AlarmLevel.red;
				al= AlarmLevel.red;
			
		}
		else if (_sen.getSensorId()==1400)//pressure
		{
			double pressure=_sen.getData();
			if(s.getDown()<=pressure&&pressure<=s.getUp())
				al= null;
			else
				//al= AlarmLevel.red;
				al= AlarmLevel.red;
			
		}
		else if (_sen.getSensorId()==1500)//waterlevel
		{
			double water=_sen.getData();
			if(s.getDown()<=water&&water<=s.getUp())
				al= null;
			else
				//al= AlarmLevel.red;
				al= AlarmLevel.red;
			
		}else if (_sen.getSensorId()==2000)//NH4
		{
			double nh=_sen.getData();
			System.out.println("asdf"+_sen.getData());
			if(s.getDown()<=nh&&nh<=s.getUp())
				al= null;
			else
				//al= AlarmLevel.red;
				al= AlarmLevel.red;
			
		}
		else if (_sen.getSensorId()==1100)//do
		{
			double rjy=_sen.getData();
			if(s.getDown()<=rjy&&rjy<=s.getUp())
				al= null;
			else
				//al= AlarmLevel.red;
				al= AlarmLevel.red;
			
		}
		if(al!=null)
			_sen.setAbnormal(true);
		return al;
	}
	//�жϽڵ�ˮ�ʵĵȼ�
	public static AlarmLevel getQualityFromDev(Device _dev)//����null ��ʾ�� 
	{
		AlarmLevel res=null;
		System.out.println("kon"+_dev.getSens()+_dev.getName());
		if(_dev.getSens()!=null)
		{
			ArrayList<Sensor>sens=_dev.getSens();
			for(int i=0;i<sens.size();i++)
			{
				Sensor sen=sens.get(i);
				AlarmLevel al=getQualityFromSensor(sen);
				
				if(al!=null)
				{
					if(res==null||res.ordinal()<al.ordinal())
						res=al;
				}
			}
			
		}else
		{
			System.out.println("kong"+_dev.getSens());
		}
		
		return res;
		
	}
}
