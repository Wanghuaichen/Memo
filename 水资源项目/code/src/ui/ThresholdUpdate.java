package ui;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;

import javax.swing.*;

import org.json.JSONException;

import szy.context.ReDrawPlaceMark;
import szy.context.StaticContext;
import szy.request.Device;
import szy.request.Request;
import szy.request.SensorRange;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
public class ThresholdUpdate {

 private JFrame UpdateFrame = null;
 private JPanel loginPanel = null;
 private JPanel devicenamePanel = null;
 private JPanel ylatPanel = null;
 private JPanel ylanPanel = null;
 
 private JPanel buttonPanel = null;
 
 private JTextField devicenameField = null;
 private JTextField ylatField = null;
 private JTextField ylanField = null;
 public JTextField idName=null;
 private JLabel usernameLabel = null;
 private JLabel ylatLabel = null;
 private JLabel ylanLabel = null;
 private JButton submit = null;
 private JButton cancel = null;
 private SensorRange SR;

 public  ThresholdUpdate(SensorRange _SR) {
 // this.wwd=_wwd;
	SR = _SR;

	 UpdateFrame = new JFrame("Update");
	// AddFrame.setModal(true);
 // loginFrame.setUndecorated(true);
	 UpdateFrame.setBackground(Color.BLACK);
 // loginFrame.setOpacity((float) 0);
  loginPanel = new JPanel();
 // loginPanel.setOpaque(true);
  devicenamePanel = new JPanel();
  ylatPanel = new JPanel();
  ylanPanel = new JPanel();
  buttonPanel = new JPanel();
  devicenameField = new JTextField(10);
  devicenameField.setText(Integer.toString(SR.getSensorId()));
  ylatField = new JTextField(10);
  ylatField.setText(Double.toString(SR.getUp()));
 // ylatField.setText("0");
  ylanField = new JTextField(10);
  ylanField.setText(Double.toString(SR.getDown()));
 // ylanField.setText("0");
  usernameLabel = new JLabel("Device   Name:");
      ylatLabel = new JLabel("Set Upper:");
      ylanLabel = new JLabel("Set  Lower:");

  submit = new JButton("update");
  cancel = new JButton("Cancel");
  
  UpdateFrame.setLayout(new GridLayout(7,1));
  UpdateFrame.add(devicenamePanel);
  UpdateFrame.add(ylatPanel);
  UpdateFrame.add(ylanPanel);
  UpdateFrame.add(buttonPanel);
  devicenamePanel.add(usernameLabel);
  devicenamePanel.add(devicenameField);
  ylatPanel.add(ylatLabel);
  ylanPanel.add(ylanLabel);
  ylatPanel.add(ylatField);
  ylanPanel.add(ylanField);
  buttonPanel.add(submit);
  buttonPanel.add(cancel);
  
  submit.addActionListener(new AddDevListener());
  this.UpdateFrame.getRootPane().setDefaultButton(submit);
//  this.loginFrame.addKeyListener(new KeyAdapter()
//  {
//	  public void keyPressed(KeyEvent event)
//	  {
//	 // if(event.getKeyText(event.getKeyCode()).compareToIgnoreCase("Enter")==0)
//		  if(event.getKeyChar()==event.VK_ENTER)
//		  {
//		  login();
//	  }
//  }
//	  });
  
  cancel.addActionListener(new AddDevListener());
  
  UpdateFrame.pack();
  Toolkit kit=Toolkit.getDefaultToolkit();
  Dimension ScreenSize=kit.getScreenSize();
  int ScreenHeight=ScreenSize.height;
  int ScreenWidth=ScreenSize.width;
  UpdateFrame.setLocation(ScreenWidth/2, ScreenHeight/2);
  UpdateFrame.setVisible(true);
  //loginFrame.setAlwaysOnTop(true);
 // loginFrame.setResizable(false);
 }
 
 private class AddDevListener implements ActionListener {
//public ButtonListener(WorldWindow wwd){
//
//	
//}
  public void actionPerformed(ActionEvent event) {
   
   String command = event.getActionCommand();
   
   if("update".equals(command)) {
	  
	   ReShowWindow();
   }
   else if("Cancel".equals(command)) {
    devicenameField.setText("");
    ylatField.setText("");
    UpdateFrame.setVisible(false);
   }
  }
  
 }
 public void ReShowWindow(){
	 int n=JOptionPane.showConfirmDialog(null, "Sure to update"+"?","WARNING",JOptionPane.WARNING_MESSAGE);//���ж��϶�����ʲô
		if(n==JOptionPane.YES_OPTION){
//先提交表单到数据库


      	 //重新加载地图
      	  //  System.out.println(StaticContext.Line);
//         JOptionPane.showMessageDialog(null, "Reloading Window need to wait for a period of time!");
			try {
			try {
				if(!check())
				{
					 JOptionPane.showMessageDialog(null, "illgeal");
				}else
				 if(Double.valueOf(ylatField.getText())>=Double.valueOf(ylanField.getText())){
			           SR.setSensorId(Integer.valueOf(devicenameField.getText()));
			           SR.setUp(Double.valueOf(ylatField.getText())); 
			           SR.setDown(Double.valueOf(ylanField.getText()));
				    Request.setThreshold(SR);
					StaticContext.senR=Request.getSenRange();
					ReDrawPlaceMark.DrawPlaceMark();
			           StaticContext.siteMenu.getLayer().addRenderable(StaticContext.siteMenu.Renderdraw());
				    JOptionPane.showMessageDialog(null, "Sucess");
				       //  setposition();
				         UpdateFrame.dispose();
				   }
				   else{
					   JOptionPane.showMessageDialog(null, "Illegal input!");
				   }
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
//					 Point2D p = StaticContext.siteMenu.layout.getFrame().getScreenPoint();
//					 Point pp = new Point((int)p.getX(),(int)p.getY());
//					 StaticContext.siteMenu.setPosition(pp);
//			  	     StaticContext.siteMenu.getLayer().removeAllRenderables();//delect sitemenu
//					//delect plackmark and Alarm
//					for(int i=0;i<StaticContext.pms.getDps().size();i++)
//					{
//						StaticContext.pms.getDps().get(i).getIconlayer().removeAllIcons();
//						 StaticContext.pms.getDps().get(i).getLayer().removeAllRenderables();
//					}
//		           StaticContext.pms.getDps().clear();//clear pms
//		           try {
//					StaticContext.pms.loadPlaceMark();//reput Plackmarks
//					StaticContext.pms.judgeAndAlarmOnce();
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				} catch (JSONException e1) {
//					e1.printStackTrace();
//				}
				//ReDrawPlaceMark.DrawPlaceMark();
        // StaticContext.siteMenu.getLayer().addRenderable(StaticContext.siteMenu.Renderdraw());
     
					
			} catch (HeadlessException e) {
				e.printStackTrace();
			} 
		}
//		}else
//		{
//			JOptionPane.showMessageDialog(null, "Error!");
//		}
			


 }
//	boolean Digit(String cc){
//		String s=ylatField.getText();
//		char[] ch=s.toCharArray();
//		for(character c:ch){
//		if(c.isDigit()){
//		return false;
//		}
//		return true;
//		}
//	}
 boolean check()
 {
	 try{
		 Double.valueOf(ylatField.getText());
		 Double.valueOf(ylanField.getText());//是否可以轮换，否则就抛出异常
		 }catch(Exception e)
		 {
		 return false;
		 }
		 return true;
		 }
 
// public static void main(String[] args) {
//	 AddDevice ad=  new AddDevice();
//	 }
 
}
