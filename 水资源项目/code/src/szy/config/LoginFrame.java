package szy.config;
import gov.nasa.worldwind.WorldWindow;

import javax.swing.*;

import org.json.JSONException;

import szy.context.StaticContext;
import szy.request.Request;
import szy.worldwind.main.MainWindow;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class LoginFrame {

 private JDialog loginFrame = null;
 
 private JPanel loginPanel = null;
 private JPanel usernamePanel = null;
 private JPanel passwordPanel = null;
 private JPanel buttonPanel = null;
 
 private JTextField usernameField = null;
 private JPasswordField passwordField = null;
 
 private JLabel usernameLabel = null;
 private JLabel passwordLabel = null;
 
 private JButton submit = null;
 private JButton cancel = null;
 protected final WorldWindow wwd;
// protected static class DoChange
// {
//
//     protected ScreenRelativeAnnotation up;
//
//     public DoChange(ScreenRelativeAnnotation _up)
//     {
//         this.up=_up;
//     }
//
//	public ScreenRelativeAnnotation getUp() {
//		return up;
//	}
//
//	public void setUp(ScreenRelativeAnnotation up) {
//		this.up = up;
//	}
//     
// }
 public  LoginFrame(WorldWindow _wwd) {
  this.wwd=_wwd;
  loginFrame = new JDialog();
  loginFrame.setModal(true);
 // loginFrame.setUndecorated(true);
  loginFrame.setBackground(Color.BLACK);
 // loginFrame.setOpacity((float) 0);
  loginPanel = new JPanel();
 // loginPanel.setOpaque(true);
  usernamePanel = new JPanel();
  passwordPanel = new JPanel();
  buttonPanel = new JPanel();
  usernameField = new JTextField(15);
  passwordField = new JPasswordField(15);
  usernameLabel = new JLabel("UserName:");
  passwordLabel = new JLabel("PassWord:");
  submit = new JButton("Login");
  cancel = new JButton("Cancel");
  
  loginFrame.setLayout(new GridLayout(3,1));
  loginFrame.add(usernamePanel);
  loginFrame.add(passwordPanel);
  loginFrame.add(buttonPanel);
  
  usernamePanel.add(usernameLabel);
  usernamePanel.add(usernameField);
  
  passwordPanel.add(passwordLabel);
  passwordPanel.add(passwordField);
  
  buttonPanel.add(submit);
  buttonPanel.add(cancel);
  
  submit.addActionListener(new ButtonListener());
  this.loginFrame.getRootPane().setDefaultButton(submit);
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
  
  cancel.addActionListener(new ButtonListener());
  
  loginFrame.pack();
  Toolkit kit=Toolkit.getDefaultToolkit();
  Dimension ScreenSize=kit.getScreenSize();
  int ScreenHeight=ScreenSize.height;
  int ScreenWidth=ScreenSize.width;
  loginFrame.setLocation(ScreenWidth/2, ScreenHeight/2);
  loginFrame.setVisible(true);
  //loginFrame.setAlwaysOnTop(true);
  loginFrame.setResizable(false);
 }
 
 private class ButtonListener implements ActionListener {
//public ButtonListener(WorldWindow wwd){
//
//	
//}
  public void actionPerformed(ActionEvent event) {
   
   String command = event.getActionCommand();
   
   if("Login".equals(command)) {
	  
       login();
   }
   else if("Cancel".equals(command)) {
    usernameField.setText("");
    passwordField.setText("");
    loginFrame.setVisible(false);
   }
  }
  
 }
 public void login(){
	 System.out.println("pass:"+String.valueOf(passwordField.getPassword()));
	 try {
		if(Request.loginCheck(usernameField.getText(), String.valueOf(passwordField.getPassword()))){
		   StaticContext.Line=1;
	    StaticContext.ly.upperRight.setText("Welcome:"+usernameField.getText());
	    StaticContext.ly.defaultAttributes.setTextColor(Color.GREEN);
	    //要直接修改login的属性才会刷新
	    loginFrame.setVisible(false);
	    wwd.redraw();
		}
		else{
			JOptionPane.showMessageDialog(null, "Login error!"); 
		    usernameField.setText("");
		    passwordField.setText("");
		}
	} catch (IOException | JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	    System.out.println(StaticContext.Line);
 }


 
}