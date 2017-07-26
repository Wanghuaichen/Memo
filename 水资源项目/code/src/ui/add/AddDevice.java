package ui.add;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;

import javax.swing.*;

import org.json.JSONException;

import szy.context.ReDrawPlaceMark;
import szy.context.StaticContext;
import szy.request.Device;
import szy.request.Request;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.io.IOException;
public class AddDevice {

 private JFrame AddFrame = null;
 private JPanel loginPanel = null;
 private JPanel devicenamePanel = null;
 private JPanel ylatPanel = null;
 private JPanel ylanPanel = null;
 private JPanel StatePanel = null;
 private JPanel GPSPanel = null;
 private JPanel IDPanel = null;
 
 private JPanel buttonPanel = null;
 
 private JTextField devicenameField = null;
 private JTextField ylatField = null;
 private JTextField ylanField = null;
private JLabel idLabel=null;
 public JTextField idName=null;
 private JLabel usernameLabel = null;
 private JLabel ylatLabel = null;
 private JLabel ylanLabel = null;
 private JLabel StateLabel = null;
 private JLabel GPSLabel = null;
 
 private JButton submit = null;
 private JButton cancel = null;
 private JRadioButton StateO;
 private JRadioButton StateC;
 private JRadioButton GPSO;
 private JRadioButton GPSC;

private ButtonGroup StateGroup;

private ButtonGroup GPSGroup;
Device dev;

 public  AddDevice(Device par) {
 // this.wwd=_wwd;
	 dev=par;

	 AddFrame = new JFrame("ADD");
	// AddFrame.setModal(true);
 // loginFrame.setUndecorated(true);
	 AddFrame.setBackground(Color.BLACK);
 // loginFrame.setOpacity((float) 0);
  loginPanel = new JPanel();
 // loginPanel.setOpaque(true);
  devicenamePanel = new JPanel();
  IDPanel=new JPanel();
  ylatPanel = new JPanel();
  ylanPanel = new JPanel();
  StatePanel=new JPanel();
  GPSPanel=new JPanel();
  buttonPanel = new JPanel();
  devicenameField = new JTextField(10);
  devicenameField.setText(dev.getName());
  ylatField = new JTextField(10);
  ylatField.setText(String.valueOf(dev.getYlat()));
 // ylatField.setText("0");
  ylanField = new JTextField(10);
  ylanField.setText(String.valueOf(dev.getYlan()));
 // ylanField.setText("0");
  usernameLabel = new JLabel("Device   Name:");
      ylatLabel = new JLabel("Set Longitude:");
      ylanLabel = new JLabel("Set  Latitude:");
       StateLabel=new JLabel("Default State:");
         GPSLabel=new JLabel("Default GPS :");
  StateO=new JRadioButton("OPEN");
  StateC=new JRadioButton("CLOSE");
          idLabel=new JLabel("Device     ID  :");
  idName=new JTextField(10);
  idName.setEditable(false);
  idName.setText(dev.getDevName());
  StateGroup = new ButtonGroup();
  StateGroup.add(StateO);
  StateGroup.add(StateC);
  if(dev.getActive()==1)
      StateO.setSelected(true);
  else
	  StateC.setSelected(true);
  GPSO=new JRadioButton(" YES  ");
  GPSC=new JRadioButton(" NO   ");
  GPSGroup = new ButtonGroup();
  GPSGroup.add(GPSO);
  GPSGroup.add(GPSC);
  if(dev.getGPS()==1)
      GPSO.setSelected(true);
  else
	  GPSC.setSelected(true);
  submit = new JButton("Login");
  cancel = new JButton("Cancel");
  
  AddFrame.setLayout(new GridLayout(7,1));
  AddFrame.add(IDPanel);
  AddFrame.add(devicenamePanel);
  AddFrame.add(ylatPanel);
  AddFrame.add(ylanPanel);
  AddFrame.add(StatePanel);
  AddFrame.add(GPSPanel);
  AddFrame.add(buttonPanel);
  
  IDPanel.add(idLabel);
  IDPanel.add(idName);
  devicenamePanel.add(usernameLabel);
  devicenamePanel.add(devicenameField);
  ylatPanel.add(ylatLabel);
  ylanPanel.add(ylanLabel);
  ylatPanel.add(ylatField);
  ylanPanel.add(ylanField);
  StatePanel.add(StateLabel);
  StatePanel.add(StateO);
  StatePanel.add(StateC);
  GPSPanel.add(GPSLabel);
  GPSPanel.add(GPSO);
  GPSPanel.add(GPSC);
  buttonPanel.add(submit);
  buttonPanel.add(cancel);
  
  submit.addActionListener(new AddDevListener());
  this.AddFrame.getRootPane().setDefaultButton(submit);
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
  
  AddFrame.pack();
  Toolkit kit=Toolkit.getDefaultToolkit();
  Dimension ScreenSize=kit.getScreenSize();
  int ScreenHeight=ScreenSize.height;
  int ScreenWidth=ScreenSize.width;
  AddFrame.setLocation(ScreenWidth/2, ScreenHeight/2);
  AddFrame.setVisible(true);
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
   
   if("Login".equals(command)) {
	  
	   ReShowWindow();
   }
   else if("Cancel".equals(command)) {
    devicenameField.setText("");
    ylatField.setText("");
    AddFrame.setVisible(false);
   }
  }
  
 }
 public void ReShowWindow(){
	 int n=JOptionPane.showConfirmDialog(null, "Sure to add"+dev.getName()+"?","WARNING",JOptionPane.WARNING_MESSAGE);//���ж��϶�����ʲô
		if(n==JOptionPane.YES_OPTION){
//先提交表单到数据库
           dev.setName(devicenameField.getText());
           dev.setYlan(Double.valueOf(ylanField.getText()));
           dev.setYlat(Double.valueOf(ylatField.getText()));
           if(GPSO.isSelected())
        	   dev.setGPS(1);
           else
        	   dev.setGPS(0);
           if(StateO.isSelected())
        	   dev.setActive(1);
           else
        	   dev.setActive(0);

      	 //重新加载地图
      	  //  System.out.println(StaticContext.Line);
//         JOptionPane.showMessageDialog(null, "Reloading Window need to wait for a period of time!");
			try {
				if(Request.addNewDev(dev)){
//		      	 再更新StaticContext.devlist
			          
					try {
						StaticContext.devList=Request.getDevList();
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
					ReDrawPlaceMark.DrawPlaceMark();
		           StaticContext.siteMenu.getLayer().addRenderable(StaticContext.siteMenu.Renderdraw());
		           JOptionPane.showMessageDialog(null, "Sucess");
		           setposition();
		       	    AddFrame.dispose();
		           
				}else
				{
					 JOptionPane.showMessageDialog(null, "Error");
						AddFrame.dispose();
				}
					
			} catch (HeadlessException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			


 }
	public void setposition()
	{
		Position pos = new Position(
				Angle.fromDegreesLatitude(dev.getYlat()),
				Angle.fromDegreesLongitude(dev.getYlan()), 0);
		StaticContext.wwd.getView().goTo(pos, 4000);
	}
// public static void main(String[] args) {
//	 AddDevice ad=  new AddDevice();
//	 }
 
}
