package ui.add;
import java.awt.Dimension;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.json.JSONException;

import szy.context.StaticContext;
import szy.request.Device;
import szy.request.Request;
import szy.worldwind.main.MainWindow;


public class AddDevList {

	private JTable table_1;
//	private JTable table;
	private JFrame frame;
	static int flag=0;
	static String a;
	static String d;
	int b;
	String c;
	ArrayList<Device> NewDevs;
//	private static String yh;
//	private static String yh1;
	PopupMenu popupMenu1 = new PopupMenu();
	MenuItem menuItem1 = new MenuItem();
//	MenuItem menuItem2 = new MenuItem();
//	MenuItem menuItem3 = new MenuItem();

	int row_value;
	String idName_Meg;
	JScrollPane scrollPane;
	private DefaultTableModel model;
	 //ParamerChange par;
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

	/**
	 * Create the application
	 */
	public AddDevList() {
		createContents();
	}

	/**
	 * Initialize the contents of the frame
	 */
	private void createContents() {
	
		frame = new JFrame("AddList");
		//frame.setModal
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
//	 	Thread thread=new Thread(show);
//		thread.start();
		
		     buildTable();
			menuItem1.setLabel("Add this Device");
			popupMenu1.add(menuItem1);
//			popupMenu1.add(menuItem2);
//			popupMenu1.add(menuItem3);
			
		menuItem1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
					try {
						menuItem1_actionPerformed(e);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		
			}
		});

//		menuItem2.setLabel("菜单2");
//		menuItem3.setLabel("菜单3");


	
	}
//	public boolean isCellEditable(int row, int column) {
//		 return false;
//		 } 
  public void buildTable(){
		String[] colnames={"ID","DEVICE","SENSORS"};
		int row = 0;
		try {
			NewDevs=Request.getNewDevList();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (JSONException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	
			row = NewDevs.size();
	
			//int row=2;
		 model=new DefaultTableModel(colnames,row);
		table_1=new JTable(model);
		scrollPane.setViewportView(table_1);

		//Vector<Object> vec = new Vector<Object>();
			
//		DefaultTableModel tableModel = (DefaultTableModel) table_1.getModel();
//		tableModel.addRow(new Object[]{});
//		 while(model.getRowCount()>0){
//	      model.removeRow(model.getRowCount()-1);
//	 }	
		for(int i=0;i<row;i++)
		{
		table_1.setValueAt(i+1,i,0);
		table_1.setValueAt(NewDevs.get(i).getDevName(),i,1);
		if(NewDevs.get(i).isLeaf()==1)
		table_1.setValueAt("On line",i,2);
		else
			table_1.setValueAt("Off Line",i,2);
	//	Thread.sleep(100);
		}

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
	void menuItem1_actionPerformed(ActionEvent e) throws IOException, JSONException {

//	     table_1.removeAll();
//	     StaticContext.devList.remove(row_value);
//			 // StaticContext.devList.get(row_value).setName("SSSSS");
//		 buildTable();
//		 Point2D p = StaticContext.siteMenu.layout.getFrame().getScreenPoint();
//		 Point pp = new Point((int)p.getX(),(int)p.getY());
//		 StaticContext.siteMenu.setPosition(pp);
//  	     StaticContext.siteMenu.getLayer().removeAllRenderables();//delect sitemenu
//		 if(StaticContext.pms.getDps().get(row_value).isAlarm)
//		    	// StaticContext.pms.getDps().get(_itemInfo.getDevId()).ai.layer.setEnabled(false);;
//		 StaticContext.pms.getDps().get(row_value).removeAlarm();//delect Icon
//		 StaticContext.pms.getDps().get(row_value).removePlaceMark();
//		 StaticContext.pms.getDps().remove(row_value);
//  	     StaticContext.siteMenu.getLayer().addRenderable(StaticContext.siteMenu.Renderdraw());//add sitemenu
//		 StaticContext.wwd.redraw();
		new AddDevice(Request.getNewDevList().get(row_value));
		frame.dispose();
		
	}

//	public void exit(){
//	//	frame.setVisible(false);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setVisible(false);
//		//frame.dispose();
//		//System.exit(-1);
//	}
}
