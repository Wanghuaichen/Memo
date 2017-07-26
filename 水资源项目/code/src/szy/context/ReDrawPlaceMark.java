package szy.context;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.IOException;

import org.json.JSONException;

import ui.ProcessBar;

public class ReDrawPlaceMark {

	public static  void DrawPlaceMark() {
		ProcessBar pb=  new ProcessBar("f");
		pb.show();
		 Point2D p = StaticContext.siteMenu.layout.getFrame().getScreenPoint();
		 Point pp = new Point((int)p.getX(),(int)p.getY());
		 StaticContext.siteMenu.setPosition(pp);
  	     StaticContext.siteMenu.getLayer().removeAllRenderables();//delect sitemenu
		//delect plackmark and Alarm
		for(int i=0;i<StaticContext.pms.getDps().size();i++)
		{
			 if(StaticContext.pms.getDps().get(i).isAlarm)
			StaticContext.pms.getDps().get(i).getIconlayer().removeAllIcons();
			 StaticContext.pms.getDps().get(i).getLayer().removeAllRenderables();
		}
       StaticContext.pms.getDps().clear();//clear pms
       try {
		StaticContext.pms.loadPlaceMark();//reput Plackmarks
		
		StaticContext.pms.judgeAndAlarmOnce();
		
	} catch (IOException e1) {
		e1.printStackTrace();
	} catch (JSONException e1) {
		e1.printStackTrace();
	}
	}


}
