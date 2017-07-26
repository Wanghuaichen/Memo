package ui.set;




import gov.nasa.worldwind.Movable;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;

import javax.swing.*;

import org.json.JSONException;

import szy.context.ReDrawPlaceMark;
import szy.context.StaticContext;
import szy.request.Device;
import szy.request.Request;
import ui.PlaceMarks.DevicePlacemark;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;

public class ParamerChange {

 public JFrame loginFrame = null;
 private JPanel usernamePanel = null;
 private JPanel buttonPanel = null;
 
 public JTextField usernameField = null;
 
 private JLabel usernameLabel = null;
 
 public JButton submit = null;
 public JButton cancel = null;
 Device dev;
 int row;
 Checkbox box;
 public  ParamerChange(int row_value,Device _dev) {
        dev=_dev;
//        model=_model;
        row=row_value;
		String param="test:";

  loginFrame = new JFrame();
 // loginFrame.setModal(true);
  loginFrame.setTitle("Update");
 // loginFrame.setUndecorated(true);
  loginFrame.setBackground(Color.BLACK);
 // loginFrame.setOpacity((float) 0);
 // loginPanel.setOpaque(true);
  usernamePanel = new JPanel();
  buttonPanel = new JPanel();
  usernameField = new JTextField(15);
  usernameLabel = new JLabel();
  submit = new JButton("update");
  cancel = new JButton("Cancel");
  
  loginFrame.setLayout(new GridLayout(3,2));
  loginFrame.add(usernamePanel);
  loginFrame.add(buttonPanel);
  
  usernamePanel.add(usernameLabel);
  usernamePanel.add(usernameField);
  box=new Checkbox("GPS Positioning");
  usernamePanel.add(box);
  buttonPanel.add(submit);
  buttonPanel.add(cancel);

  this.loginFrame.getRootPane().setDefaultButton(submit);  
  loginFrame.pack();
  Toolkit kit=Toolkit.getDefaultToolkit();
  Dimension ScreenSize=kit.getScreenSize();
  int ScreenHeight=ScreenSize.height;
  int ScreenWidth=ScreenSize.width;
  loginFrame.setLocation(ScreenWidth/2, ScreenHeight/2);
  loginFrame.setVisible(true);
  loginFrame.setResizable(false);
	if(row_value==0)
		box.setVisible(false);
		param="Number:";
	if(row_value==1)
	{
		box.setVisible(false);
		param="Name:";
	}
	if(row_value==2)
	{
		box.setVisible(false);
		param="Abnormal:";
	}
	if(row_value==3)
	{
		usernameField.setVisible(false);
		param="State:";
	}
	if(row_value==4)
	{
		box.setVisible(false);
		param="Longitude:";
	}
	if(row_value==5)
	{
		box.setVisible(false);
		param="Latitude:";
	}
	if(row_value==6)
	{
		box.setVisible(false);
		param="Power:";
	}
	if(row_value==7)
	{
		usernameField.setVisible(false);
		if(dev.getGPS()==1)
	     	box.setState(true);
		else
			box.setState(false);
		param="GPS:";
	}
	usernameLabel.setText("Set New "+param+":");
 }
 
// private class ButtonListener implements ActionListener {
////public ButtonListener(WorldWindow wwd){
////
////	
////}
//  public void actionPerformed(ActionEvent event) {
//   
//   String command = event.getActionCommand();
//   
//   if("Login".equals(command)) {
//	  
//       login();
//   }
//   else if("Cancel".equals(command)) {
//    usernameField.setText("");
//    loginFrame.setVisible(false);
//   }
//  }
//  
// }
 public int change(){
	
	    //Ҫֱ���޸�login�����ԲŻ�ˢ��
	// ParamerChange();
	 
	int  flag=1;
	if(row==0)
		JOptionPane.showMessageDialog(null, "Can't Update!");
	if(row==1)
	{    dev.setName(usernameField.getText());
        StaticContext.devList.get(dev.getId()).setName(usernameField.getText());
		//JOptionPane.showMessageDialog(null, "change Name");
	}
		
	if(row==2)
		JOptionPane.showMessageDialog(null, "Can't Update!");
	if(row==3)
		JOptionPane.showMessageDialog(null, "Can't Update!");
	if(row==4)
	{
		dev.setYlan(Double.valueOf(usernameField.getText()));
		StaticContext.devList.get(dev.getId()).setYlan(Double.valueOf(usernameField.getText()));
		//JOptionPane.showMessageDialog(null, "lan");
	}
		
	if(row==5)
	{
	  dev.setYlat(Double.valueOf(usernameField.getText()));
	  StaticContext.devList.get(dev.getId()).setYlat(Double.valueOf(usernameField.getText()));
	}
	if(row==6)
		JOptionPane.showMessageDialog(null, "Can't Update!");
	if(row==7)
	{
		//dev.setGPS(usernameField.);
	
		if(box.getState())
		{
			dev.setGPS(1);	
			System.out.println("GPStest"+StaticContext.devList.get(dev.getId()).getGPS());
		//	StaticContext.devList.get(row).setGPS(1);
		}else
		{
			dev.setGPS(0);	
		}
	//	JOptionPane.showMessageDialog(null, "GPS");
	}
	try {
	if(row==1||row==4||row==5||row==7){	
		if(Request.updateDevPar(dev, row))
		{
			if(row==1){
				StaticContext.pms.getDps().get(dev.getId()).setLabelText(dev.getName());
				 Point2D p = StaticContext.siteMenu.layout.getFrame().getScreenPoint();
				 Point pp = new Point((int)p.getX(),(int)p.getY());
				 StaticContext.siteMenu.setPosition(pp);
		  	     StaticContext.siteMenu.getLayer().removeAllRenderables();
		  	   StaticContext.pms.getDps().get(dev.getId()).getIconlayer().removeAllIcons();
		  	   StaticContext.pms.getDps().get(dev.getId()).getLayer().removeAllRenderables();
		  	 StaticContext.pms.getDps().clear();//clear pms
			   StaticContext.siteMenu.getLayer().addRenderable(StaticContext.siteMenu.Renderdraw());
		  	   StaticContext.pms.loadPlaceMark();
		  	 StaticContext.pms.judgeAndAlarmOnce();
		  	 JOptionPane.showMessageDialog(null, "Sucess!");
			}
			if(row==4||row==5){
//			DevicePlacemark p=StaticContext.pms.getDps().get(dev.getId());
//			p.setPosition(dev.getPosition());
//            Movable dragObject = (Movable) p;
//            Position refPos = new Position(dev.getPosition().latitude,
//           	dev.getPosition().longitude,
//             dragObject.getReferencePosition().elevation);
//             dragObject.moveTo(refPos);  
//             if( StaticContext.pms.getDps().get(dev.getId()).isAlarm)
//             StaticContext.pms.getDps().get(dev.getId()).ai.setPosition(dev.getPosition());
		    // StaticContext.wwd.redraw();
				try {
					StaticContext.devList=Request.getDevList();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
//				 Point2D p = StaticContext.siteMenu.layout.getFrame().getScreenPoint();
//				 Point pp = new Point((int)p.getX(),(int)p.getY());
//				 StaticContext.siteMenu.setPosition(pp);
//		  	     StaticContext.siteMenu.getLayer().removeAllRenderables();//delect sitemenu
//				//delect plackmark and Alarm
//				for(int i=0;i<StaticContext.pms.getDps().size();i++)
//				{
//					StaticContext.pms.getDps().get(i).getIconlayer().removeAllIcons();
//					 StaticContext.pms.getDps().get(i).getLayer().removeAllRenderables();
//				}
//	           StaticContext.pms.getDps().clear();//clear pms
//	           try {
//				StaticContext.pms.loadPlaceMark();//reput Plackmarks
//				StaticContext.pms.judgeAndAlarmOnce();
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			} catch (JSONException e1) {
//				e1.printStackTrace();
//			}
				ReDrawPlaceMark.DrawPlaceMark();
	           StaticContext.siteMenu.getLayer().addRenderable(StaticContext.siteMenu.Renderdraw());
			    JOptionPane.showMessageDialog(null, "Sucess!");
			    setposition();
		}
		}
		else
			JOptionPane.showMessageDialog(null, "Error");
	}
		//1name,4lan,5lat,7gps
	} catch (IOException e) {
		e.printStackTrace();
	} catch (JSONException e) {
		e.printStackTrace();
	}
	
	//dev.setYlat(Double.valueOf(usernameField.getText()));
  loginFrame.setVisible(false);
  //loginFrame.dispose();
//	 while(model.getRowCount()>0){
//	      model.removeRow(model.getRowCount()-1);
//	 }
//	 StaticContext.window.buildTable();
	    return flag;

	
 }
	public void setposition()
	{
		Position pos = new Position(
				Angle.fromDegreesLatitude(dev.getYlat()),
				Angle.fromDegreesLongitude(dev.getYlan()), 0);
		StaticContext.wwd.getView().goTo(pos, 4000);
	}

}
 