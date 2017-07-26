package ui.set;

import java.awt.Dimension;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import szy.request.Device;


public class SetSite {

	private JTable table_1;
//	private JTable table;
	public JFrame frame;
	static int flag=0;
	static String a;
	static String d;
	int b;
	String c;
//	private static String yh;
//	private static String yh1;
	Device dev;
	PopupMenu popupMenu1 = new PopupMenu();
	MenuItem menuItem1 = new MenuItem();
//	MenuItem menuItem2 = new MenuItem();
//	MenuItem menuItem3 = new MenuItem();

	int row_value;
	String idName_Meg;
	JScrollPane scrollPane;
	private DefaultTableModel model;
	 ParamerChange par;
	private String Gps;
	private String state;
//	public void data(String name,String sjj){
//		yh=name;
//		yh1=sjj;
//		final JLabel label_1 = new JLabel();
//		label_1.setBounds(77, 46, 66, 23);
//		frame.getContentPane().add(label_1);
//		label_1.setText(yh);
//		
//		final JLabel label_67 = new JLabel();
//		label_67.setBounds(0, 48, 66, 18);
//		frame.getContentPane().add(label_67);
//		label_67.setText(yh1);
//		a=label_1.getText();
//         d=label_67.getText();
//		System.out.println(yh+"aaaaa");
//		System.out.println(yh);
//	}
	/**
	 * Launch the application
	 * @param args
	 */
//	public static void main(String args[]) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					SetSite window = new SetSite(StaticContext.devList.get(1));
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
	private String ab;

	/**
	 * Create the application
	 */
	public SetSite(Device _dev) {
		dev=_dev;
		createContents();
	}

	/**
	 * Initialize the contents of the frame
	 */
	private void createContents() {
	
		frame = new JFrame("Update Parameter");
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		  Toolkit kit=Toolkit.getDefaultToolkit();
		  Dimension ScreenSize=kit.getScreenSize();
		  int ScreenHeight=ScreenSize.height;
		  int ScreenWidth=ScreenSize.width;
         frame.setBounds(ScreenWidth/2, ScreenHeight/3, ScreenWidth/5, ScreenHeight/5);
       //   frame.setSize(width, height);
	//	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		//frame.setAlwaysOnTop(true);
	//	  frame.setLayout(new GridLayout(1,1));

		 scrollPane = new JScrollPane();
	//	scrollPane.setBounds(0, 0, ScreenWidth/8, ScreenHeight/8);
		scrollPane.setSize(frame.getSize());
		frame.getContentPane().add(scrollPane);

		
		   buildTable();
			menuItem1.setLabel("UPDATE");
			popupMenu1.add(menuItem1);
//			popupMenu1.add(menuItem2);
//			popupMenu1.add(menuItem3);
			
		menuItem1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuItem1_actionPerformed(e);
			}
		});

//		menuItem2.setLabel("菜单2");
//		menuItem3.setLabel("菜单3");


	
	}
//	public boolean isCellEditable(int row, int column) {
//		 return false;
//		 } 
  public void buildTable(){
		String[] colnames={"ID","PROPERTY","PARAMETER","EDITABLE"};
		 model=new DefaultTableModel(colnames,8);
		table_1=new JTable(model);
		scrollPane.setViewportView(table_1);

		//Vector<Object> vec = new Vector<Object>();
			
//		DefaultTableModel tableModel = (DefaultTableModel) table_1.getModel();
//		tableModel.addRow(new Object[]{});
		table_1.setValueAt(1, 0, 0);
		table_1.setValueAt(2, 1, 0);
		table_1.setValueAt(3, 2, 0);
		table_1.setValueAt(4, 3, 0);
		table_1.setValueAt(5, 4, 0);
		table_1.setValueAt(6, 5, 0);
		table_1.setValueAt(7, 6, 0);
		table_1.setValueAt(8, 7, 0);
		
		table_1.setValueAt("Number", 0, 1);
		table_1.setValueAt("Name", 1, 1);
		table_1.setValueAt("Abnormal", 2, 1);
		table_1.setValueAt("State", 3, 1);
		//table_1.setValueAt("DeviceConnect", 4, 1);
		table_1.setValueAt("Longitude", 4, 1);
		table_1.setValueAt("Latitude", 5, 1);
		table_1.setValueAt("Power", 6, 1);
		table_1.setValueAt("GPS", 7, 1);
		table_1.setValueAt(dev.getDevName(), 0, 2);
		table_1.setValueAt(dev.getName(), 1, 2);
		if(dev.getAbnormal()==1)
			ab="Warning";
		else 
		    ab="Normal";
		table_1.setValueAt(ab,2, 2);	
		if(dev.getActive()==1)
			state="Opened";
		else
			state="Closed";
		table_1.setValueAt(state, 3, 2);	
		//table_1.setValueAt(dev.get, 4, 2);
		table_1.setValueAt(dev.getYlan(), 4, 2);	
		table_1.setValueAt(dev.getYlat(), 5, 2);	
		table_1.setValueAt(dev.getPower()+"%", 6, 2);
		if(dev.getGPS()==1)
			Gps="YES";
		else
			Gps="NO";
		table_1.setValueAt(Gps, 7, 2);	
		table_1.setValueAt("F", 0, 3);
		table_1.setValueAt("T", 1, 3);
		table_1.setValueAt("F", 2, 3);
		table_1.setValueAt("F", 3, 3);
		table_1.setValueAt("T", 4, 3);
		table_1.setValueAt("T", 5, 3);
		table_1.setValueAt("F", 6, 3);
		table_1.setValueAt("T", 7, 3);
		table_1.add(popupMenu1);
		table_1.addMouseListener(new java.awt.event.MouseAdapter() {
			 public void mouseClicked(MouseEvent e)   
		      {   
		           processEvent(e);   
		      }       
		      public void mousePressed(MouseEvent e) 
		      {   
		           processEvent(e);   
		      }    
			public void mouseReleased(MouseEvent e) {
				int mods = e.getModifiers();
				// 鼠标右键
				 processEvent(e); 
				// if(table_1.getSelectedRow()!=-1)
				 //&& !e.isControlDown() && !e.isShiftDown()
				if ((mods & MouseEvent.BUTTON3_MASK) != 0 ) {
					
					// 弹出菜单
					row_value = table_1.getSelectedRow();
			        idName_Meg = table_1.getModel().getValueAt(table_1.getSelectedRow(),0)+"";
			       //  popup.show(tableRows, e.getX(), e.getY()); 
			        System.out.println("row:"+row_value+"idName:"+idName_Meg);
			       System.out.println("ttt"+table_1.getSelectedRow());
					popupMenu1.show(table_1, e.getX(), e.getY());
					table_1.clearSelection();
				}
			}
			 public void mouseEntered(MouseEvent e) 
			    { 
			          processEvent(e); 
			    } 

			   public void mouseExited(MouseEvent e) 
			   { 
			         processEvent(e); 
			   }   
			   public void mouseDragged(MouseEvent e) 
			   { 
			         processEvent(e); 
			    }   
			    public void mouseMoved(MouseEvent e) 
			    { 
			         processEvent(e); 
			    }   
		});

	//	table_1.setEnabled(false);
		//table_1.isEditing();
	//	table_1.isValid();

  }
	    
   
	private void processEvent(MouseEvent e) 
    { 
        if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) 
        { 
              int modifiers = e.getModifiers(); 
              modifiers -= MouseEvent.BUTTON3_MASK; 
              modifiers |= MouseEvent.BUTTON1_MASK; 
              MouseEvent ne = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), modifiers, e.getX(), e .getY(), e.getClickCount(), false); 
              table_1.dispatchEvent(ne); 
        } 
     } 
	void menuItem1_actionPerformed(ActionEvent e) {
		// 菜单事件
		if(row_value==0||row_value==2||row_value==3||row_value==6)
		{
			JOptionPane.showMessageDialog(null, "This property can't update!");
		}else{
		 par=new ParamerChange(row_value,dev);
		//if(par.login()==1)
		 par.submit.addActionListener(new ButtonListener());
		 par.cancel.addActionListener(new ButtonListener());
		}
	}
	 private class ButtonListener implements ActionListener {
		//public ButtonListener(WorldWindow wwd){
		//
		//	
		//}
		  public void actionPerformed(ActionEvent event) {
		   
		   String command = event.getActionCommand();
		   
		   if("update".equals(command)) {
			  
		       par.change();
		   //    frame.repaint();
		       table_1.removeAll();
//				 while(model.getRowCount()>0){
//				      model.removeRow(model.getRowCount()-1);
//				 }
			  buildTable();
			  frame.dispose();
		   }
		   else if("Cancel".equals(command)) {
		    par.usernameField.setText("");
		    par.loginFrame.setVisible(false);
		   }
		  }
		  
		 }
//	public void exit(){
//	//	frame.setVisible(false);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setVisible(false);
//		//frame.dispose();
//		//System.exit(-1);
//	}
}
