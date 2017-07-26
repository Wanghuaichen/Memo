
package test;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.*;
import gov.nasa.worldwind.event.*;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.*;
import gov.nasa.worldwindx.applications.worldwindow.util.Util;
import gov.nasa.worldwindx.examples.ApplicationTemplate;

import javax.swing.*;

import szy.request.Device;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
/*
 * ���Ҽ�˵���
 * 
 *�÷�ʾ��
 *����menuinfo
 ContextMenuInfo cmf=new ContextMenuInfo(menuName, itemInfo,new ContextMenuItemAction()//����action
			{
				public void actionPerformed(ActionEvent event)
				{
					
				}
			} 
ʹ�ã�
  ContextMenu menu = new ContextMenu((Component) event.getSource(), menuInfo);
	  menu.show(event.getMouseEvent());
 *
 * 
 * 
 */
    public class ContextMenu
    {
        public static final String CONTEXT_MENU_INFO = "ContextMenuInfo";

        protected ContextMenuInfo ctxMenuInfo;
        protected Component sourceComponent;
        protected JMenuItem menuTitleItem;
        protected ArrayList<JMenuItem> menuItems = new ArrayList<JMenuItem>();

        public ContextMenu(Component sourceComponent, ContextMenuInfo contextMenuInfo)
        {
            this.sourceComponent = sourceComponent;
            this.ctxMenuInfo = contextMenuInfo;

            this.makeMenuTitle();
            this.makeMenuItems();
        }

        protected void makeMenuTitle()
        {
            this.menuTitleItem = new JMenuItem(this.ctxMenuInfo.menuTitle);
       
        }

        protected void makeMenuItems()
        {
            for (ContextMenuItemInfo itemInfo : this.ctxMenuInfo.menuItems)
            {
            	JMenuItem jbutton=new JMenuItem();
            	ctxMenuInfo.action.setContextMenuItemInfo(itemInfo);
            	Color c=new Color(255,255,255);
            	jbutton.setBackground(c);
            	jbutton.setAction(this.ctxMenuInfo.action);
            	jbutton.setText(itemInfo.displayString);
            	// ji=new ImageIcon("img/yy.png");
            //	jbutton.setIcon(ji);//����ʾ����
            	//jbutton.setOpaque(false);
            //	jbutton.setContentAreaFilled(false);
                this.menuItems.add(jbutton);
            }
        }

        public void show(final MouseEvent event)
        {
            JPopupMenu popup = new JPopupMenu();

            popup.add(this.menuTitleItem);

            popup.addSeparator();

            for (JMenuItem subMenu : this.menuItems)
            {
                popup.add(subMenu);
            }

            popup.show(sourceComponent, event.getX(), event.getY());
        }
    }

    /** The ContextMenuInfo class specifies the contents of the context menu. */
    final  class ContextMenuInfo
    {
        protected String menuTitle;
        protected ContextMenuItemInfo[] menuItems;
        protected ContextMenuItemAction action;
        public ContextMenuInfo(String title, ContextMenuItemInfo[] menuItems,ContextMenuItemAction _action)
        {
            this.menuTitle = title;
            this.menuItems = menuItems;
            this.action=_action;
        }
    }

    /** The ContextMenuItemInfo class specifies the contents of one entry in the context menu. */
   final class ContextMenuItemInfo
    {
        protected String displayString;
        protected String key;
        public String getDisplayString() {
			return displayString;
		}
		public void setDisplayString(String displayString) {
			this.displayString = displayString;
		}
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public int getDevId() {
			return devId;
		}
		public void setDevId(int devId) {
			this.devId = devId;
		}
		protected int devId;
        public ContextMenuItemInfo(int _devId,String _key,String displayString)
        {
            this.displayString = displayString;
            key=_key;
            devId=_devId;
        }
    }

    /** The ContextMenuItemAction responds to user selection of a context menu item. */
   //�����࣬ͨ�����صķ�ʽ�������Լ���actionPerformed
    abstract class ContextMenuItemAction extends AbstractAction
    {
        protected ContextMenuItemInfo itemInfo;

        public ContextMenuItemAction()//chuaninfo
        {
        	

      //  this.itemInfo = itemInfo;
         

        }
        public ContextMenuItemInfo getContextMenuItemInfo()
        {
        	return itemInfo;
        }
        public void setContextMenuItemInfo(ContextMenuItemInfo _itemInfo)
        {
        	itemInfo=_itemInfo;
        }
      
    }

   