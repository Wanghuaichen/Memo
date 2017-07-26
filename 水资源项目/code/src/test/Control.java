package test;

import gov.nasa.worldwind.avlist.AVList;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.render.PointPlacemark;
import gov.nasa.worldwindx.applications.worldwindow.util.Util;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;



public class Control {
	 protected static class ContextMenuController implements SelectListener
	    {
	        protected PointPlacemark lastPickedPlacemark = null;

	        public void selected(SelectEvent event)
	        {
	            try
	            {
	                if (event.getEventAction().equals(SelectEvent.ROLLOVER))
	                    highlight(event, event.getTopObject());
	                else if (event.getEventAction().equals(SelectEvent.RIGHT_PRESS)) // Could do RIGHT_CLICK instead
	                    showContextMenu(event);//右键点击显示contextMenu
	            }
	            catch (Exception e)
	            {
	                Util.getLogger().warning(e.getMessage() != null ? e.getMessage() : e.toString());
	            }
	        }

	        @SuppressWarnings( {"UnusedDeclaration"})
	        protected void highlight(SelectEvent event, Object o)
	        {
	            if (this.lastPickedPlacemark == o)
	                return; // same thing selected

	            // Turn off highlight if on.
	            if (this.lastPickedPlacemark != null)
	            {
	                this.lastPickedPlacemark.setHighlighted(false);
	                this.lastPickedPlacemark = null;
	            }

	            // Turn on highlight if object selected.
	            if (o != null && o instanceof PointPlacemark)
	            {
	                this.lastPickedPlacemark = (PointPlacemark) o;
	                this.lastPickedPlacemark.setHighlighted(true);
	            }
	        }

	        protected void showContextMenu(SelectEvent event)//显示点击右键后内容
	        {
	            if (!(event.getTopObject() instanceof PointPlacemark))
	                return;

	            // See if the top picked object has context-menu info defined. Show the menu if it does.

	            Object o = event.getTopObject();
	            if (o instanceof AVList) // Uses an AVList in order to be applicable to all shapes.
	            {
	                AVList params = (AVList) o;
	                ContextMenuInfo menuInfo = (ContextMenuInfo) params.getValue(ContextMenu.CONTEXT_MENU_INFO);
	                //显示的时候取出Value，实例化menuInfo，能取出两个value，说明存进去两个
	                if (menuInfo == null)
	                    return;

	                if (!(event.getSource() instanceof Component))
	                    return;

	                ContextMenu menu = new ContextMenu((Component) event.getSource(), menuInfo);
	                menu.show(event.getMouseEvent());
	            }
	        }
	    }
	    /** The ContextMenuInfo class specifies the contents of the context menu. */
	    protected static class ContextMenuInfo
	    {
	        protected String menuTitle;
	        protected ContextMenuItemInfo[] menuItems;

	        public ContextMenuInfo(String title, ContextMenuItemInfo[] menuItems)
	        {
	            this.menuTitle = title;
	            this.menuItems = menuItems;
	        }
	    }
	    /** The ContextMenu class implements the context menu. */
	    protected static class ContextMenu
	    {
	        public static final String CONTEXT_MENU_INFO = "ContextMenuInfo";

	        protected ContextMenuInfo ctxMenuInfo;
	        protected Component sourceComponent;
	        protected JMenuItem menuTitleItem;
	        protected ArrayList<JMenuItem> menuItems = new ArrayList<JMenuItem>();

	        public ContextMenu(Component sourceComponent, ContextMenuInfo contextMenuInfo)
	        {
	            this.sourceComponent = sourceComponent;
	            this.ctxMenuInfo = contextMenuInfo;//传入给contextMenu的值有两个，下面循环用到了

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
	                this.menuItems.add(new JMenuItem(new ContextMenuItemAction(itemInfo)));
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

	

	    /** The ContextMenuItemInfo class specifies the contents of one entry in the context menu. */
	    protected static class ContextMenuItemInfo
	    {
	        protected String displayString;
	  
	        public ContextMenuItemInfo(String displayString)
	        {
	            this.displayString = displayString;
	            
	        }
	    }

	    /** The ContextMenuItemAction responds to user selection of a context menu item. */
	    public static class ContextMenuItemAction extends AbstractAction
	    {
	        protected ContextMenuItemInfo itemInfo;

	        public ContextMenuItemAction(ContextMenuItemInfo itemInfo)
	        {
	            super(itemInfo.displayString);

	            this.itemInfo = itemInfo;//把iteminfo传给他，才能显示
	        }

	        public void actionPerformed(ActionEvent event)
	        {
	            System.out.println(this.itemInfo.displayString); // Replace with application's menu-item response.
	        }
	    }
}
