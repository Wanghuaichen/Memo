package ui;

import gov.nasa.worldwind.avlist.AVList;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.Offset;
import gov.nasa.worldwind.render.PointPlacemark;
import gov.nasa.worldwindx.applications.worldwindow.util.Util;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.json.JSONException;

import szy.context.StaticContext;
import szy.request.Device;
import szy.request.Request;
import ui.set.SetSite;

public class Control {
	protected static class ContextMenuController implements SelectListener {
		protected PointPlacemark lastPickedPlacemark = null;

		public void selected(SelectEvent event) {
			try {
				if (event.getEventAction().equals(SelectEvent.ROLLOVER))
					highlight(event, event.getTopObject());
				else if (event.getEventAction().equals(SelectEvent.RIGHT_PRESS)) // Could
																					// do
																					// RIGHT_CLICK
																					// instead
					showContextMenu(event);// 右键点击显示contextMenu
			} catch (Exception e) {
				Util.getLogger().warning(
						e.getMessage() != null ? e.getMessage() : e.toString());
			}
		}

		@SuppressWarnings({ "UnusedDeclaration" })
		protected void highlight(SelectEvent event, Object o) {
			if (this.lastPickedPlacemark == o)
				return; // same thing selected

			// Turn off highlight if on.
			if (this.lastPickedPlacemark != null) {
				this.lastPickedPlacemark.setHighlighted(false);
				this.lastPickedPlacemark = null;
			}

			// Turn on highlight if object selected.
			if (o != null && o instanceof PointPlacemark) {
				this.lastPickedPlacemark = (PointPlacemark) o;
				this.lastPickedPlacemark.setHighlighted(true);
			}
		}

		protected void showContextMenu(SelectEvent event)// 显示点击右键后内容
		{
			if (!(event.getTopObject() instanceof PointPlacemark))
				return;

			// See if the top picked object has context-menu info defined. Show
			// the menu if it does.

			Object o = event.getTopObject();
			if (o instanceof AVList) // Uses an AVList in order to be applicable
										// to all shapes.
			{
				AVList params = (AVList) o;
				ContextMenuInfo menuInfo = (ContextMenuInfo) params
						.getValue(ContextMenu.CONTEXT_MENU_INFO);
				// 显示的时候取出Value，实例化menuInfo，能取出两个value，说明存进去两个
				if (menuInfo == null)
					return;

				if (!(event.getSource() instanceof Component))
					return;

				ContextMenu menu = new ContextMenu(
						(Component) event.getSource(), menuInfo);
				menu.show(event.getMouseEvent());
			}
		}
	}

	/** The ContextMenuInfo class specifies the contents of the context menu. */
	protected static class ContextMenuInfo {
		protected String menuTitle;
		protected ContextMenuItemInfo[] menuItems;

		public ContextMenuInfo(String title, ContextMenuItemInfo[] menuItems) {
			this.menuTitle = title;
			this.menuItems = menuItems;
		}
	}

	/** The ContextMenu class implements the context menu. */
	protected static class ContextMenu {
		public static final String CONTEXT_MENU_INFO = "ContextMenuInfo";
		public static final String CONTEXT_MENU_INFO_ADMIN = "ContextMenuInfo";

		protected ContextMenuInfo ctxMenuInfo;
		protected Component sourceComponent;
		protected JMenuItem menuTitleItem;
		protected ArrayList<JMenuItem> menuItems = new ArrayList<JMenuItem>();

		public ContextMenu(Component sourceComponent,
				ContextMenuInfo contextMenuInfo) {
			this.sourceComponent = sourceComponent;
			this.ctxMenuInfo = contextMenuInfo;// 传入给contextMenu的值有两个，下面循环用到了

			this.makeMenuTitle();
			this.makeMenuItems();
		}

		protected void makeMenuTitle() {
			this.menuTitleItem = new JMenuItem(this.ctxMenuInfo.menuTitle);
		}

		protected void makeMenuItems() {
			for (ContextMenuItemInfo itemInfo : this.ctxMenuInfo.menuItems) {
				this.menuItems.add(new JMenuItem(new ContextMenuItemAction(
						itemInfo)));
			}
		}

		public void show(final MouseEvent event) {
			JPopupMenu popup = new JPopupMenu();

			popup.add(this.menuTitleItem);

			popup.addSeparator();

			for (JMenuItem subMenu : this.menuItems) {
				popup.add(subMenu);
			}

			popup.show(sourceComponent, event.getX(), event.getY());
		}
	}

	/**
	 * The ContextMenuItemInfo class specifies the contents of one entry in the
	 * context menu.
	 */
	protected static class ContextMenuItemInfo {
		protected String displayString;
		protected int devId;
		protected String key;

		public ContextMenuItemInfo(int _devId, String _key, String displayString) {
			this.displayString = displayString;
			this.devId = _devId;
			this.key = _key;
		}

		public String getDisplayString() {
			return displayString;
		}

		public void setDisplayString(String displayString) {
			this.displayString = displayString;
		}

		public int getDevId() {
			return devId;
		}

		public void setDevId(int devId) {
			this.devId = devId;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

	}

	/**
	 * The ContextMenuItemAction responds to user selection of a context menu
	 * item.
	 */
	public static class ContextMenuItemAction extends AbstractAction {
		protected ContextMenuItemInfo itemInfo;
		private ContextMenuItemInfo itInfo;
		private Timer timer;
		protected int flag;
		 private  static final String  IMAGEBlued_PATH = "images/water.png";
		 private  static final String  IMAGEGrayd_PATH = "images/water1.png";
		  private  static String  ICONGrayx_PATH = "images/waterhui.png";
		public ContextMenuItemAction(ContextMenuItemInfo itemInfo) {
			super(itemInfo.displayString);
			this.itemInfo = itemInfo;// 把iteminfo传给他，才能显示
		}

		public void doMenuEvent(ContextMenuItemInfo _itemInfo) {
			itInfo = _itemInfo;
			if (itInfo.key.equals("navigateTo1")) {
////////////////////////////
				setposition();
			}
			if (itInfo.key.equals("navigateTo2")) {
//////////////////
				changeactive();
			}
			if (itInfo.key.equals("navigateTo3")) {
	/////////////////
				Setwindow();
			}
			if (itInfo.key.equals("navigateTo4")) {
/////////////////////
				if (StaticContext.Line == 1) {
					String cmii;
					if (itInfo.displayString.equalsIgnoreCase("查看GPS位置")) {
						if (StaticContext.pms.getDps().get(itInfo.getDevId())
								.drawGPSPlackMark() == 1) {
							cmii = "关闭GPS位置";
							itInfo.setDisplayString(cmii);
							Position pos = new Position(
									Angle.fromDegreesLatitude(StaticContext.devList.get(
											itInfo.devId).getGPSlat()),//-0.0025
									Angle.fromDegreesLongitude(StaticContext.devList.get(
											itInfo.devId).getGPSlan()), 0);//+0.0041
							//System.out.println("testLan"+pos.latitude);
							StaticContext.wwd.getView().goTo(pos, 4000);
//							timer = new Timer();
//							Date time = new Date();
//							timer.schedule(new TimerTask() {// 这一段没有运行
//
//										
//
//										@Override
//										public void run() {
//											try {
//												StaticContext.devList = Request.getDevList();
//											} catch (IOException e) {
//												e.printStackTrace();
//											} catch (JSONException e) {
//												e.printStackTrace();
//											}
//											StaticContext.pms.getDps().get(itInfo.getDevId())
//													.removeGpsPlackMark();
//											StaticContext.pms.getDps().get(itInfo.getDevId())
//													.drawGPSPlackMark();
//
//										} 
//
//									}
//
//									, time, 10 * 1000);
							
						}
					} else {
						cmii = "查看GPS位置";
						itInfo.setDisplayString(cmii);
						StaticContext.pms.getDps().get(itInfo.getDevId())
								.removeGpsPlackMark();
						StaticContext.wwd.redraw();
						//timer.cancel();
					}
				} else {
					JOptionPane.showMessageDialog(null,
							"Please login the system first!");
				}
			}
		}
		public void setposition()
		{
			Position pos = new Position(
					Angle.fromDegreesLatitude(StaticContext.devList.get(
							itInfo.devId).getYlat()),
					Angle.fromDegreesLongitude(StaticContext.devList.get(
							itInfo.devId).getYlan()), 0);
			StaticContext.wwd.getView().goTo(pos, 4000);
		}
		public void changeactive()
		{
			if (StaticContext.Line == 1) {
				String cmii;
				if (itInfo.displayString.equalsIgnoreCase("打开设备")) {
					StaticContext.pms.getDps().get(itInfo.getDevId()).placeMarkAttrs
							.setImageAddress(IMAGEBlued_PATH);
					cmii = "关闭设备";
					itInfo.setDisplayString(cmii);
					if (StaticContext.pms.getDps().get(itInfo.getDevId()).isAlarm)
						StaticContext.pms.getDps().get(itInfo.getDevId())
								.reputAlarm();
					try {
						StaticContext.devList.get(itInfo.devId)
								.ChangeActive(1);
						StaticContext.siteMenu.getLayer()
								.removeAllRenderables();
						Point2D p = StaticContext.siteMenu.layout
								.getFrame().getScreenPoint();
						Point pp = new Point((int) p.getX(), (int) p.getY());
						StaticContext.siteMenu.setPosition(pp);
						StaticContext.siteMenu.getLayer().addRenderable(
								StaticContext.siteMenu.Renderdraw());
					} catch (IOException e) {
						e.printStackTrace();
					}
					StaticContext.wwd.redraw();
				} else {
					StaticContext.pms.getDps().get(itInfo.getDevId()).placeMarkAttrs
							.setImageAddress(IMAGEGrayd_PATH);
					cmii = "打开设备";
					itInfo.setDisplayString(cmii);
					if (StaticContext.pms.getDps().get(itInfo.getDevId()).isAlarm)
						StaticContext.pms.getDps().get(itInfo.getDevId())
								.removeAlarm();
					try {
						StaticContext.devList.get(itInfo.devId)
								.ChangeActive(0);
						StaticContext.siteMenu.bt.get(itInfo.getDevId())
								.setImageSource(ICONGrayx_PATH);
						Point2D p = StaticContext.siteMenu.layout
								.getFrame().getScreenPoint();
						Point pp = new Point((int) p.getX(), (int) p.getY());
						StaticContext.siteMenu.setPosition(pp);
						StaticContext.siteMenu.getLayer()
								.removeAllRenderables();
						StaticContext.siteMenu.getLayer().addRenderable(
								StaticContext.siteMenu.Renderdraw());
					} catch (IOException e) {
						e.printStackTrace();
					}
					StaticContext.wwd.redraw();
				}
			} else {
				JOptionPane.showMessageDialog(null,
						"Please login the system first!");
			}
		}
		public void Setwindow()
		{
			if (StaticContext.Line == 1) {
				 new SetSite(
						StaticContext.devList.get(itInfo.devId));
		 System.out.println("kankan"+StaticContext.devList.get(itInfo.devId).getDevName()+StaticContext.devList.get(itInfo.devId).getAbnormal());
			} else {
				JOptionPane.showMessageDialog(null,
						"Please login the system first!");
			}
		}


		public void actionPerformed(ActionEvent event) {
			// 此处定义右键菜单的事件
			doMenuEvent(itemInfo);
		}



	}
}
