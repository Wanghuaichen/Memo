package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwindx.examples.util.ScreenShotAction;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import szy.context.StaticContext;
import ui.add.AddDevList;
import ui.delect.DelectDevice;

public class MenuBar {
	JMenu menu;
	JMenuBar menuBar;
	DelectDevice dd;
	AddDevList ad;
	ThresholdList adjust;
	public JMenuItem addItem;
	public JMenuItem delectItem;
	public JMenuItem Adjust;

	public MenuBar(WorldWindow wwd) {
		menu = new JMenu("File");
		menu.addMenuListener(new MenuListener() {
			public void menuCanceled(MenuEvent e) {
			}

			public void menuDeselected(MenuEvent e) {

			}

			public void menuSelected(MenuEvent e) {
				//System.exit(0);
				if(StaticContext.Line==1)
				    {
					addItem.setVisible(true);
				    delectItem.setVisible(true);
				    Adjust.setVisible(true);
				   }
				else
				  {
				   addItem.setVisible(false);
				   delectItem.setVisible(false);
				   Adjust.setVisible(false);
			       }
			}
		});
		// wwd.setModel(new BasicModel());
		JMenuItem snapItem = new JMenuItem("Save ScreenShot");
		snapItem.addActionListener(new ScreenShotAction(wwd));
		addItem = new JMenuItem("Add Device");
		delectItem = new JMenuItem("Delect Device");
		Adjust = new JMenuItem("Adjust the threshold");
		Adjust.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Adjust_actionperformed(e);

			}

		});
		addItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addItem_actionperformed(e);

			}

		});
		delectItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				delectItem_actionPerformed(e);
			}
		});
		menu.add(snapItem);
		menu.add(addItem);
		menu.add(delectItem);
		menu.add(Adjust);
		menuBar = new JMenuBar();
		menuBar.add(menu);

	}

	void delectItem_actionPerformed(ActionEvent e) {
		// 菜单事件
		if (StaticContext.Line == 1)
			dd = new DelectDevice();
		else
			JOptionPane.showMessageDialog(null,
					"Please login the system first!");
		// JOptionPane.showMessageDialog(null, "This property can't update!");

	}

	public void addItem_actionperformed(ActionEvent e) {
		if (StaticContext.Line == 1)
			ad = new AddDevList();
		else
			JOptionPane.showMessageDialog(null,
					"Please login the system first!");

	}
	public void Adjust_actionperformed(ActionEvent e) {
		if (StaticContext.Line == 1)
			adjust = new ThresholdList();
		else
			JOptionPane.showMessageDialog(null,
					"Please login the system first!");

	}

	public JMenuBar getMenuBar() {
		return menuBar;
	}

	public void setMenuBar(JMenuBar menuBar) {
		this.menuBar = menuBar;
	}

}
