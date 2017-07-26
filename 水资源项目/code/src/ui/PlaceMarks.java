package ui;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.avlist.AVList;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.layers.IconLayer;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.pick.PickedObject;
import gov.nasa.worldwind.render.AbstractBrowserBalloon;
import gov.nasa.worldwind.render.BalloonAttributes;
import gov.nasa.worldwind.render.BasicBalloonAttributes;
import gov.nasa.worldwind.render.GlobeBrowserBalloon;
import gov.nasa.worldwind.render.Size;
import gov.nasa.worldwindx.examples.util.BalloonController;
import gov.nasa.worldwindx.examples.util.HotSpotController;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;

import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.json.JSONException;

import szy.analytic.JudgeGPSPosition;
import szy.analytic.JudgeWaterQuality;
import szy.context.StaticContext;
import szy.request.Device;
import szy.request.Request;
import szy.request.Sensor;
import ui.AlarmIcon.AlarmLevel;
import ui.Control.ContextMenu;
import ui.Control.ContextMenuInfo;
import ui.Control.ContextMenuItemAction;
import ui.Control.ContextMenuItemInfo;
import gov.nasa.worldwindx.examples.ApplicationTemplate;

import java.util.TimerTask;

public class PlaceMarks {
	public class DevicePlacemark extends PointPlacemark {

		// highlight(event, event.getTopObject());
		AbstractBrowserBalloon balloon;
		ContextMenu contextMenu;
		int devId;
		RenderableLayer layer;
		IconLayer iconlayer;

		RenderableLayer contextMenuLayer;
		public AlarmIcon ai;
		public boolean isAlarm;
		String cmii;
		private int on_line = 1;
		PointPlacemarkAttributes placeMarkAttrs;
		private AlarmLevel level;
		private AlarmIcon GpsAi;

		public DevicePlacemark(int _devId, RenderableLayer _layer,
				IconLayer _iconlayer) {
			super(StaticContext.devList.get(_devId).getPosition());
			// if(!on_line)
			// placeMarkAttrs=this.getActiveAttributes();

			placeMarkAttrs = this.defaultAttributes;
			// placeMarkAttrs.labelScale=20.0;

			// placeMarkAttrs.setImageAddress("images/pushpins/plain-brown.png");

			this.devId = _devId;
			this.layer = _layer;
			this.iconlayer = _iconlayer;
			this.isAlarm = false;

			this.setLabelText(StaticContext.devList.get(devId).getName());
			on_line = StaticContext.devList.get(devId).getActive();
			if (on_line == 1) {
				cmii = "关闭设备";
			} else {
				cmii = "打开设备";
			}
			ContextMenuItemInfo[] itemInfo = new ContextMenuItemInfo[] {
					// 在此处定义右键菜单的名称
					new ContextMenuItemInfo(devId, "navigateTo2", cmii),
					new ContextMenuItemInfo(devId, "navigateTo1", "定位至该点"),
					new ContextMenuItemInfo(devId, "navigateTo3", "更新参数"),
					new ContextMenuItemInfo(devId, "navigateTo4", "查看GPS位置"),

			};
			// 通过AVList的方式将contextmenuInfo作为对象传入Placemark
			// AVlist是一个worldwind定义的接口，可以通过setValue将对象绑定是placemark。
			this.setValue(ContextMenu.CONTEXT_MENU_INFO, new ContextMenuInfo(
					StaticContext.devList.get(devId).getName(), itemInfo));

		}

		// public void CreateMenu() {
		//
		// ContextMenuItemInfo[] itemInfoAdmin = new ContextMenuItemInfo[] {
		// // 在此处定义右键菜单的名称
		// new ContextMenuItemInfo(devId, "navigateTo2", cmii),
		// new ContextMenuItemInfo(devId, "navigateTo1", "定位至该点"),
		// new ContextMenuItemInfo(devId, "navigateTo3", "更新参数"),
		// new ContextMenuItemInfo(devId, "navigateTo4", "查看GPS位置"), };
		// ContextMenuItemInfo[] itemInfo = new ContextMenuItemInfo[] {
		// // 在此处定义右键菜单的名称
		// new ContextMenuItemInfo(devId, "navigateTo1", "定位至该点"), };
		// // 通过AVList的方式将contextmenuInfo作为对象传入Placemark
		// // AVlist是一个worldwind定义的接口，可以通过setValue将对象绑定是placemark。
		// if (StaticContext.Line == 1)
		// this.setValue(ContextMenu.CONTEXT_MENU_INFO,
		// new ContextMenuInfo(StaticContext.devList.get(devId)
		// .getName(), itemInfoAdmin));
		// else
		// this.setValue(ContextMenu.CONTEXT_MENU_INFO,
		// new ContextMenuInfo(StaticContext.devList.get(devId)
		// .getName(), itemInfo));
		// }

		public int getDevId() {
			return devId;
		}

		public void setDevId(int devId) {
			this.devId = devId;
		}

		public RenderableLayer getLayer() {
			return layer;
		}

		public void setLayer(RenderableLayer layer) {
			this.layer = layer;
		}

		public IconLayer getIconlayer() {
			return iconlayer;
		}

		public void setIconlayer(IconLayer iconlayer) {
			this.iconlayer = iconlayer;
		}

		public String getCmii() {
			return cmii;
		}

		public void setCmii(String cmii) {
			this.cmii = cmii;
		}

		public PointPlacemarkAttributes getPlaceMarkAttrs() {
			return placeMarkAttrs;
		}

		public void setPlaceMarkAttrs(PointPlacemarkAttributes placeMarkAttrs) {
			this.placeMarkAttrs = placeMarkAttrs;
		}

		public boolean isAlarm() {
			return isAlarm;
		}

		public void setAlarm(boolean isAlarm) {
			this.isAlarm = isAlarm;
		}

		public void drawPlaceMark() {

			this.layer.addRenderable(this);

		}

		public void removePlaceMark() {
			layer.removeRenderable(this);
		}

		// public void removePlaceMark()
		// {
		// this.layer.removeRenderable(renderable);
		// }
		public void drawBalloon() {
			this.layer.addRenderable(balloon);
		}

		public void setBalloon(AbstractBrowserBalloon _abb) {
			if (balloon != null)
				this.layer.removeRenderable(balloon);
			this.balloon = _abb;

			this.setValue(AVKey.BALLOON, this.balloon);

		}

		public void updateDevInfo() {
			try {
				StaticContext.devList.get(devId).loadData();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		public void alarm(AlarmIcon.AlarmLevel al) {
			if (isAlarm == true)
				return;
			isAlarm = true;
			ai = new AlarmIcon(StaticContext.devList.get(devId).getPosition(),
					wwd, iconlayer);
			if (StaticContext.devList.get(devId).getActive() == 0)
				ai.removeAlarm();
			// ais.add(ai);
			System.out.print("查看："
					+ StaticContext.devList.get(devId)
					+ StaticContext.devList.get(devId).getPosition()
							.getLatitude());
			// di.alarm(al);
			Action action = ai.getAlarmAction(al);// 错误在这
			action.actionPerformed(null);
			isPmMoved = false;
		}

		public void stopAlarm() {
			if (isAlarm == false)
				return;
			isAlarm = false;

			ai.stopAlarm();

		}

		public void removeAlarm() {
			ai.removeAlarm();

			// wwd.redraw();
		}

		public void reputAlarm() {
			ai = new AlarmIcon(StaticContext.devList.get(devId).getPosition(),
					wwd, iconlayer);
			Action action = ai.getAlarmAction(AlarmLevel.red);// 错误在这
			action.actionPerformed(null);
			// isPmMoved=false;
		}

		public int drawGPSPlackMark() {
			int f = 0;
			Device dev = null;

			dev = StaticContext.devList.get(devId);

			level = JudgeGPSPosition.getGPSFroDev(dev);
			if (dev.getGPS() == 1) {
				if (dev.isGPSAbnormal()) {
					if (level.equals(AlarmLevel.orange)) {

						JOptionPane.showMessageDialog(null, "GPS坐标浮动过大");
					} else if (level.equals(AlarmLevel.red)
							|| level.equals(AlarmLevel.yellow)) {
						GpsAi = new AlarmIcon(dev.getGPSPosition(), wwd,
								iconlayer);
						Action action = GpsAi.getGpsAction(level);// 错误在这
						action.actionPerformed(null);
						f = 1;
					} else {
						JOptionPane.showMessageDialog(null, "GPS信号异常");
					}

				} else {
					JOptionPane.showMessageDialog(null, "GPS正常");
				}
			} else {
				JOptionPane.showMessageDialog(null, "该设备不存在GPS定位仪");
			}
			return f;
		}

		public void removeGpsPlackMark() {
			GpsAi.removeAlarm();

			// wwd.redraw();
		}

	}

	public class SelectRunnable implements Runnable {
		private SelectEvent event;

		public SelectRunnable(SelectEvent _e) {
			event = _e;
		}

		@Override
		public void run() {
			MouseEvent me = event.getMouseEvent();
			if (me != null && me.getID() == MouseEvent.MOUSE_CLICKED
					&& me.getButton() == MouseEvent.BUTTON1)// 这个button是什么
			{
				// 左键点击
				PickedObject po = event.getTopPickedObject();
				Object o = po.getObject();
				if (o instanceof DevicePlacemark) {
					DevicePlacemark dp = (DevicePlacemark) o;
					AbstractBrowserBalloon balloon = null;
					// 绘制气球
					System.out.print("test:"
							+ StaticContext.devList.get(dp.devId));
					try {
						balloon = new GlobeBrowserBalloon(
								PlaceMarks.getDevHtmlInfo(StaticContext.devList
										.get(dp.devId)), StaticContext.devList
										.get(dp.devId).getPosition());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					BalloonAttributes attrs = new BasicBalloonAttributes();
					attrs.setSize(new Size(Size.NATIVE_DIMENSION, 0d, null,
							Size.NATIVE_DIMENSION, 0d, null));
					balloon.setAttributes(attrs);
					balloon.setAlwaysOnTop(true);
					balloon.setVisible(true);
					dp.setBalloon(balloon);
					dp.drawBalloon();

				}
			} else if (me != null && me.getID() == MouseEvent.MOUSE_CLICKED
					&& me.getButton() == MouseEvent.BUTTON3) {
				// 右键菜单
				PickedObject po = event.getTopPickedObject();
				Object o = po.getObject();

				if (o instanceof DevicePlacemark) {

					AVList params = (AVList) o;
					// ((DevicePlacemark) o).CreateMenu();
					ContextMenuInfo menuInfo = (ContextMenuInfo) params
							.getValue(ContextMenu.CONTEXT_MENU_INFO);
					if (menuInfo == null)
						return;
					ContextMenu menu = new ContextMenu(
							(Component) event.getSource(), menuInfo);

					menu.show(event.getMouseEvent());

				}
			}
		}

	}

	private ArrayList<DevicePlacemark> dps;
	// private ArrayList<AlarmIcon> ais;
	// layer用于绘制placemark
	private RenderableLayer layer;
	private IconLayer iconlayer;
	private WorldWindow wwd;
	protected HotSpotController hotSpotController;
	protected BalloonController balloonController;
	private boolean isPmMoved;
	// protected int flag;
	protected static final String BALLOON_CONTENT_PATH = "img/11.html";
	private static double date;
	private static final String IMAGEBlued_PATH = "images/water.png";
	private static final String IMAGEGrayd_PATH = "images/water1.png";

	public PlaceMarks(WorldWindow _wwd) {

		this.wwd = _wwd;
		this.layer = new RenderableLayer();
		dps = new ArrayList<DevicePlacemark>();
		// ais=new ArrayList<AlarmIcon>();
		this.iconlayer = new IconLayer();
		this.layer.setName("placemark");
		// Add a controller to send input events to BrowserBalloons.
		this.hotSpotController = new HotSpotController(this.wwd);
		// Add a controller to handle link and navigation events in
		// BrowserBalloons.
		this.balloonController = new BalloonController(this.wwd);
		updateDevsInfo();

	}

	public ArrayList<DevicePlacemark> getDps() {
		return dps;
	}

	public void updateDevsInfo() {
		for (int i = 0; i < StaticContext.devList.size(); i++) {
			try {
				StaticContext.devList.get(i).loadData();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	// 检测并报警一次
	public void judgeAndAlarmOnce() {
		updateDevsInfo();
		if (isPmMoved == true) {
			iconlayer.removeAllIcons();
			for (int i = 0; i < StaticContext.devList.size(); i++) {
				dps.get(i).setAlarm(false);
			}
		}
		// for(int i=0;i<ais.size();i++)
		for (int i = 0; i < StaticContext.devList.size(); i++) {
			AlarmLevel al = JudgeWaterQuality
					.getQualityFromDev(StaticContext.devList.get(i));
			AlarmLevel al2 = JudgeGPSPosition
					.getGPSFroDev(StaticContext.devList.get(i));
			System.out.println("alTest" + al + StaticContext.devList.get(i));
			if (al != null) {

				dps.get(i).alarm(al);
				if (StaticContext.devList.get(i).getActive() == 1) {
					StaticContext.devList.get(i).setAbnormal(1);
				}
				// StaticContext.siteMenu.init();
				// //////////////////////////////////////////////////
				System.out.print("第二警报线程启动" + dps.get(i));
			} else if (al2 != null) {
				dps.get(i).alarm(al2);
				if (StaticContext.devList.get(i).getActive() == 1) {
					StaticContext.devList.get(i).setAbnormal(1);
				}
				// StaticContext.siteMenu.init();
				// //////////////////////////////////////////////////
				System.out.print("第二警报线程启动" + dps.get(i));
			} else {
				System.out.println(al);
				dps.get(i).stopAlarm();
				if (StaticContext.devList.get(i).getActive() == 1) {
					StaticContext.devList.get(i).setAbnormal(2);
					System.out.println("kankann"
							+ StaticContext.devList.get(i).getDevName()
							+ StaticContext.devList.get(i).getAbnormal());

				}
				// StaticContext.siteMenu.init();
				// //////////////////////////////////////////////////
				System.out.print("第二警报线程失败"
						+ StaticContext.devList.get(i).getDevName() + "\n");
			}

		}

	}

	// 检测并报警(间隔一分钟)
	public void judgeAndAlarm() {

		judgeAndAlarmOnce();
		Timer timer = new Timer();
		Date time = new Date();
		timer.schedule(new TimerTask() {// 这一段没有运行

					@Override
					public void run() {
						// iconlayer.removeAllIcons();
						judgeAndAlarmOnce();
						judgeAndAlarmOnce();
						System.out.print("警报线程启动");

					}

				}

				, time, 3600 * 1000);

	}

	/*
	 * 将设备信息转换成html格式
	 */
	public static String getDevHtmlInfo(Device _dev) throws IOException {

		String htmlString = "";
		String image = "";
		String url=geturl(_dev);
		System.out.println("urlis" + url);
		String logo = "logo";
		String blank = "_blank";
		String color = "#cc3333";
		String Flosty = "float:right";
		// InputStream contentStream = null;
		String font5 = "<font color=" + color
				+ " ><strong>当前设备未打开！</strong></font>";
		String font = "";
		font = getfont(_dev);
		image = getimage(_dev);

		if (_dev.getActive() == 0) {
			htmlString += "<div>" + font5 + "</div><hr/>";
			htmlString += "<div><a href=" + url + " target=" + blank + ">"
					+ _dev.getName() + "查看历史数据分析（点击查看）</a></div><hr/>";
		} else {
			htmlString += "<div><a href=" + url + " target=" + blank + ">"
					+ _dev.getName() + "数据分析（点击查看）</a></div><hr/>";
			htmlString += " <div>" + font + "<img src=" + image + " alt="
					+ logo + " style=" + Flosty + "/></div><hr/>";
			JudgeGPSPosition.getGPSFroDev(_dev);
			if (_dev.isGPSAbnormal())
				htmlString += "<div><font color=" + color
						+ " ><strong>GPS坐标异常!</strong></font></div><hr/>";
			try {
				_dev.loadData();
				ArrayList<Sensor> sens = _dev.getSens();
				System.out.print("大小：" + sens.size());
				htmlString += "最近更新时间：" + compareDate(sens) + "<hr/>";
				for (int i = 0; i < sens.size(); i++) {
					Sensor sen = sens.get(i);
					JudgeWaterQuality.getQualityFromSensor(sen);
					date = sen.getData();
					if (sen.isAbnormal()) {
						// _dev.setAbnormal(true);

						htmlString += "<div ><font color=\"red\">"
								+ sen.getSenName() + ":" + date + "("
								+ sen.getUnit() + Comare_date(sen.getTime()) + "</font></div>";
					} else {
						// _dev.setAbnormal(false);
						htmlString += "<div >" + sen.getSenName() + ":" + date
								+ "(" + sen.getUnit()
								+ Comare_date(sen.getTime()) + "</div>";
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				htmlString += _dev.getName() + "暂无数据";
			}
		}
		System.out.print("sdfhvhbghhgrhgrgr" + htmlString);

		return htmlString;
	}

	private static String compareDate(ArrayList<Sensor> sens) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date d1 = df.parse(sens.get(0).getTime());
		for (int i = 0; i < sens.size() - 1; i++) {

			Date d2 = df.parse(sens.get(i + 1).getTime());
			if (d1.getTime() < d2.getTime()) {
				d1 = d2;
			}
		}
		// Comare_date();

		String Firstdate = df.format(d1);
		return Firstdate;
	}

	private static String geturl(Device _dev) throws IOException {
		Properties property = new Properties();
		InputStream in = new BufferedInputStream(new FileInputStream(
				"config/config.properties"));
		property.load(in);
		String urla = property.getProperty("adress");
		String u=urla + _dev.getDevName();
		return u;
	}

	private static String getimage(Device _dev) throws IOException {
		String image = null;
		Properties property = new Properties();
		InputStream in = new BufferedInputStream(new FileInputStream(
				"config/config.properties"));
		property.load(in);
		String image1 = property.getProperty("image1");
		String image2 = property.getProperty("image2");
		String image3 = property.getProperty("image3");
		String image4 = property.getProperty("image4");
		String image5 = property.getProperty("image5");
		if ((_dev.getPower()) >= 80.0) {
			image = image1;
		}
		if ((_dev.getPower()) >= 60.0 && (_dev.getPower()) < 80.0) {
			image = image2;
		}
		if ((_dev.getPower()) >= 40.0 && (_dev.getPower()) < 60.0) {
			image = image3;
		}
		if ((_dev.getPower()) >= 20.0 && (_dev.getPower()) < 40.0) {
			image = image4;
		} else if ((_dev.getPower()) <= 20.0) {
			image = image5;
		}
		return image;
	}

	private static String getfont(Device _dev) {
		String font = null;
		String color = "#cc3333";
		String color1 = "#00BFFF";
		String font1 = "<font color=" + color1
				+ " ><strong>电量充足:</strong></font>";
		String font2 = "<font color=" + color
				+ " ><strong>电量较低:</strong></font>";
		String font3 = "<font color=" + color
				+ " ><strong>电量极低:</strong></font>";
		String font4 = "<font color=" + color
				+ " ><strong>请蓄电！:</strong></font>";

		if ((_dev.getPower()) >= 80.0) {
			font = font1;

		}
		if ((_dev.getPower()) >= 60.0 && (_dev.getPower()) < 80.0) {

			font = font1;
		}
		if ((_dev.getPower()) >= 40.0 && (_dev.getPower()) < 60.0) {

			font = font2;
		}
		if ((_dev.getPower()) >= 20.0 && (_dev.getPower()) < 40.0) {

			font = font3;
		} else if ((_dev.getPower()) <= 20.0) {

			font = font4;
		}
		return font;
	}

	private static String Comare_date(String _date) {
		Date currentTime = new Date();
		long between = 0;
		String htmlP = ")";
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		try {
			Date olddate = dft.parse(_date);
			between = currentTime.getTime() - olddate.getTime();

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (between > (12 * 3600000)) {
			//htmlP=")<font color=\"red\">" + "数据未更新!" + "</font>";
			
			//return "<font color=\"red\">" + "数据未更新!" + "</font>";
		}
		return htmlP;
	}

	/*
	 * 加载placemark 只是加载到layer里面 ，并未渲�?
	 */
	public void loadPlaceMarks() {
		try {
			loadPlaceMark();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StaticContext.wwd.addSelectListener(new SelectListener()// 加载了两次监听事件
				{
					private MyBasicDrager dragger = new MyBasicDrager(
							StaticContext.wwd);

					public void selected(SelectEvent event) {
						this.dragger.selected(event);
						isPmMoved = this.dragger.isPmMoved();

					}
				});

		this.wwd.addSelectListener(new SelectListener() {
			public void selected(SelectEvent event) {
				SelectRunnable sr = new SelectRunnable(event);
				sr.run();
			}

		});

	}

	public void loadPlaceMark() throws IOException, JSONException {
		for (int i = 0; i < StaticContext.devList.size(); i++) {
			DevicePlacemark dp = new DevicePlacemark(i, this.layer,
					this.iconlayer);
			if (StaticContext.devList.get(i).getActive() == 0) {
				// dp.cmii="打开设备";
				// dp.placeMarkAttrs.setImageAddress("images/pushpins/plain-brown.png");
				dp.placeMarkAttrs.setImageAddress(IMAGEGrayd_PATH);
				dp.placeMarkAttrs.setScale(0.3 * dp.placeMarkAttrs.getScale());
				dp.placeMarkAttrs
						.setImageOffset(PointPlacemarkAttributes.DEFAULT_IMAGE_OFFSET);
			} else {
				// dp.cmii="关闭设备";
				dp.placeMarkAttrs.setImageAddress(IMAGEBlued_PATH);
				dp.placeMarkAttrs.setScale(0.3 * dp.placeMarkAttrs.getScale());
				dp.placeMarkAttrs
						.setImageOffset(PointPlacemarkAttributes.DEFAULT_IMAGE_OFFSET);
			}
			dps.add(dp);
			// 将placemark 绘制到图层（此时该图层未必可见）
			dp.drawPlaceMark();
		}
	}

	public void draw() {
		ApplicationTemplate.insertAfterPlacenames(this.wwd, this.layer);
		ApplicationTemplate.insertAfterPlacenames(this.wwd, this.iconlayer);
	}

}
