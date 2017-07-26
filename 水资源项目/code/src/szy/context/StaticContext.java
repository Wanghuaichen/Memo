package szy.context;


import gov.nasa.worldwind.WorldWindow;

import java.util.ArrayList;

import szy.config.LoginLayer1;
import szy.request.Device;
import szy.request.SensorRange;
import ui.PlaceMarks;
import ui.SiteMenu;
import ui.set.SetSite;

public class StaticContext {
	public static ArrayList<Device>devList;
	public static SiteMenu siteMenu;
	public static WorldWindow wwd;
	public static PlaceMarks pms;
	public static int Line=0;
	public static LoginLayer1 ly;
	public static SetSite window;
	public static ArrayList<SensorRange>senR;

}
