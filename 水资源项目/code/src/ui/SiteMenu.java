package ui;
import szy.analytic.JudgeWaterQuality;
import szy.context.StaticContext;
import szy.request.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import gov.nasa.worldwindx.examples.ApplicationTemplate;
import gov.nasa.worldwindx.examples.util.HotSpotController;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.*;
import szy.utils.treeMenu.*;
public class SiteMenu {
    private  static String  ICON_PATH = "images/16x16water.png";
    private  static String  ICONRedx_PATH = "images/16x16waterred.png";
    private  static String  ICONBluex_PATH = "images/16x16water.png";
    private  static String  ICONGrayx_PATH = "images/waterhui.png";
   // private HotSpotController controller;
    private  RenderableLayer layer ;
    private WorldWindow wwd;
	private Point position; 
    private String title;
	 public BasicTreeLayout layout;
	ArrayList<BasicTreeNode> bt=new ArrayList<BasicTreeNode>();
	public  BasicTree tree;
	public BasicTreeModel model;
    public SiteMenu(WorldWindow _wwd,Point _position,String _title) throws IOException, JSONException
    {
    	wwd=_wwd;
    	position=_position;
    	title=_title;
        for(int i=0;i<StaticContext.devList.size();i++)
        {  	
        	 Device dev=StaticContext.devList.get(i);
                 if(dev.getActive()==1){       
        		try {
        			dev.loadData();
        				if(JudgeWaterQuality.getQualityFromDev(dev)!=null){
        					dev.setAbnormal(1);
        					System.out.println("kankan"+dev.getDevName()+dev.getAbnormal());
        				}
        				else{
        					dev.setAbnormal(2);
        					System.out.println("kankan"+dev.getDevName()+dev.getAbnormal());
        				}		
        		} catch (Exception e) {
        			e.printStackTrace();
        		System.out.println("获取不到sen数据");
        		//ICON_PATH="img/16x16water.png";
        		}
                 }
        }
    
    }
    public Point getPosition() {
		return position;
	}
	public void setPosition(Point position) {
		this.position = position;
	}
	/**
     * @throws IOException
     */
    public void init() throws IOException 
    {
    	
    
    	
    	layer = new RenderableLayer();

      //  controller = new HotSpotController(wwd);
       
        layer.addRenderable(Renderdraw());
     
      }
    public BasicTree Renderdraw(){
      	 tree = new BasicTree();//changgeshu
       	 
        layout = new BasicTreeLayout(tree, position.x, position.y);//treeMenu中最重要的类
        
     
       
       BasicFrameAttributes fa= new BasicFrameAttributes();
       
       Font font= new Font("宋体", Font.PLAIN, 14);
       
       fa.setFont(font);
       fa.setTextColor(Color.PINK);
       fa.setBackgroundOpacity(0.2);
      layout.getFrame().setAttributes(fa);
        layout.getFrame().setFrameTitle("站点设置");
         tree.setLayout(layout);
          model = new BasicTreeModel();
         BasicTreeNode root1 = new BasicTreeNode(-1,"站点设置", ICONBluex_PATH); 
         model.setRoot(root1);
         BasicTreeNode subChild;
         for(int i=0;i<StaticContext.devList.size();i++)
         {  	
         	 Device dev=StaticContext.devList.get(i);
                  if(dev.getActive()==1){
                 	 if(dev.getAbnormal()==1)
                 			ICON_PATH=ICONRedx_PATH;
                 	 if(dev.getAbnormal()==2)
                 			ICON_PATH=ICONBluex_PATH;
                  }
            
     			else{
     				ICON_PATH=ICONGrayx_PATH;
     			}

         		subChild=new BasicTreeNode(i,"定位至"+dev.getName(),ICON_PATH);
         		bt.add(subChild);
         	 root1.addChild(subChild);
         	//SiteMenu 的监听事件
         	 subChild.addPropertyChangeListener(new PropertyChangeListener(){  
 			@Override
 			public void propertyChange(PropertyChangeEvent evt) {
 				 DopropertyChange(evt);              
 			}
 			});  
         	 
            
         }
         

         tree.setModel(model);
         tree.expandPath(root1.getPath());
         return tree;
    }
    public void  DopropertyChange(PropertyChangeEvent evt)
    {
         BasicTreeNode tn=(BasicTreeNode)evt.getNewValue();
         Device dev=StaticContext.devList.get(tn.getIndex());
       Position pos=new Position(Angle.fromDegreesLatitude(dev.getYlat()),Angle.fromDegreesLongitude(dev.getYlan()),0);
        wwd.getView().goTo(pos,4000);
    }
    public BasicTreeLayout getLayout() {
		return layout;
	}
	public void setLayout(BasicTreeLayout layout) {
		this.layout = layout;
	}
	public RenderableLayer getLayer() {
		return layer;
	}
	public ArrayList<BasicTreeNode> getBt() {
		return bt;
	}
	public void setBt(ArrayList<BasicTreeNode> bt) {
		this.bt = bt;
	}
	public void draw()
    {
    	ApplicationTemplate.insertBeforeCompass(wwd, layer);
    	//ApplicationTemplate.insertAfterPlacenames(wwd, layer);
    	
    }

}
