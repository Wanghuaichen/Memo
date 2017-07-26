package ui;
/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

import gov.nasa.worldwind.*;
import gov.nasa.worldwind.event.*;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.pick.PickedObject;
import gov.nasa.worldwind.util.Logging;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.json.JSONException;

import szy.context.StaticContext;
import szy.request.Request;
import ui.PlaceMarks.DevicePlacemark;

/**
 * @author Patrick Murris
 * @version $Id: BasicDragger.java 1171 2013-02-11 21:45:02Z dcollins $
 */
public class MyBasicDrager implements SelectListener
{
    private final WorldWindow wwd;
    private boolean dragging = false;
    private boolean useTerrain = true;
    private boolean isPmMoved = false;
    private Point dragRefCursorPoint;
    private Vec4 dragRefObjectPoint;
    private double dragRefAltitude;
	private int componentSize;
	

    public MyBasicDrager(WorldWindow wwd)
    {
        if (wwd == null)
        {
            String msg = Logging.getMessage("nullValue.WorldWindow");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }

        this.wwd = wwd;
    }

    public MyBasicDrager(WorldWindow wwd, boolean useTerrain)
    {
        if (wwd == null)
        {
            String msg = Logging.getMessage("nullValue.WorldWindow");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }

        this.wwd = wwd;
        this.setUseTerrain(useTerrain);
    }

    public boolean isUseTerrain()
    {
        return useTerrain;
    }
    public boolean isPmMoved()
    {
        return isPmMoved;	
    }

    public void setUseTerrain(boolean useTerrain)
    {
        this.useTerrain = useTerrain;
    }

    public boolean isDragging()
    {
        return this.dragging;
    }
     public boolean isMoved(Object topObject)
     {      boolean flag=false;
			int n=JOptionPane.showConfirmDialog(null, "You want to change the position of"+StaticContext.devList.get(((DevicePlacemark) topObject).devId).getName()+"？","WARNING",JOptionPane.WARNING_MESSAGE);//���ж��϶�����ʲô
			if(n==JOptionPane.YES_OPTION){//ȷ���޸���������
				DevicePlacemark dp=(DevicePlacemark)topObject;//�϶��ر�		
				Position position=dp.getPosition();
				//StaticContext.devList.get(dp.devId).setPosition(position);
			//	JOptionPane.showMessageDialog(null, JOptionPane.WARNING_MESSAGE);
				try {
					StaticContext.devList.get(dp.devId).UpdatePosition(position);
					//wwd.redrawNow();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				flag=true;
			    // StaticContext.pms.judgeAndAlarmOnce();
				if (StaticContext.pms.getDps().get(dp.devId).isAlarm)
				{
				StaticContext.pms.getDps().get(dp.devId).removeAlarm();
	        	StaticContext.pms.getDps().get(dp.devId).reputAlarm();
				}
			}
			else{			//û���޸������ԭ��ԭ��λ��         		
		            if (topObject == null)
		                flag=false;
		            if (!(topObject instanceof Movable))
		                flag=false;
		            Movable dragObject = (Movable) topObject;
		            Position refPos = new Position(StaticContext.devList.get(((DevicePlacemark) topObject).devId).getPosition().latitude,
		            		StaticContext.devList.get(((DevicePlacemark) topObject).devId).getPosition().longitude,
		            		dragObject.getReferencePosition().elevation);
		            //�ƶ�
		            dragObject.moveTo(refPos);     
		            flag=false;
			}
			return flag;
     }
    public void selected(SelectEvent event)
    {
        if (event == null)
        {
            String msg = Logging.getMessage("nullValue.EventIsNull");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }

        if (event.getEventAction().equals(SelectEvent.DRAG_END))//�϶������¼��������
        {
               this.dragging = false;
				PickedObject po=event.getTopPickedObject();
				Object topObject=po.getObject();
				if(topObject instanceof DevicePlacemark)//��ȡ�����ǵر������
				{	
					if(StaticContext.Line==1&&StaticContext.devList.get(((DevicePlacemark) topObject).devId).getGPS()==0){
		            isPmMoved=isMoved(topObject);
					}else{
						 if(StaticContext.Line==0)
							 JOptionPane.showMessageDialog(null, "Please login the system first");  
						 else
						     JOptionPane.showMessageDialog(null, "This equipment use GPS positioning!");  
						
				            Movable dragObject = (Movable) topObject;
				          
				            Position refPos = new Position(StaticContext.devList.get(((DevicePlacemark) topObject).devId).getPosition().latitude,
				            		StaticContext.devList.get(((DevicePlacemark) topObject).devId).getPosition().longitude,
				            		dragObject.getReferencePosition().elevation);
				            
				            dragObject.moveTo(refPos);     
					}
					}
			//	StaticContext.wwd.redraw();
            event.consume();
        }
        
        
        else if (event.getEventAction().equals(SelectEvent.DRAG))
        {
            DragSelectEvent dragEvent = (DragSelectEvent) event;
            Object topObject = dragEvent.getTopObject();
       //     if(topObject instanceof DevicePlacemark){}
            if (topObject == null)
                return;

            if (!(topObject instanceof Movable))
                return;

            Movable dragObject = (Movable) topObject;
            View view = wwd.getView();
            Globe globe = wwd.getModel().getGlobe();

            // Compute dragged object ref-point in model coordinates.
            // Use the Icon and Annotation logic of elevation as offset above ground when below max elevation.
            Position refPos = dragObject.getReferencePosition();
            if (refPos == null)
                return;

            Vec4 refPoint = globe.computePointFromPosition(refPos);

            if (!this.isDragging())   // Dragging started
            {
                // Save initial reference points for object and cursor in screen coordinates
                // Note: y is inverted for the object point.
                this.dragRefObjectPoint = view.project(refPoint);
                
                // Save cursor position
                this.dragRefCursorPoint = dragEvent.getPreviousPickPoint();
                // Save start altitude
                this.dragRefAltitude = globe.computePositionFromPoint(refPoint).getElevation();
            }

            // Compute screen-coord delta since drag started.
            int dx = dragEvent.getPickPoint().x - this.dragRefCursorPoint.x;
            int dy = dragEvent.getPickPoint().y - this.dragRefCursorPoint.y;
           
            // Find intersection of screen coord (refObjectPoint + delta) with globe.
            double x = this.dragRefObjectPoint.x + dx;
           //���ֵ����dragRefObjectPoint
            double y = event.getMouseEvent().getComponent().getSize().height - this.dragRefObjectPoint.y + dy - 1;
            componentSize=event.getMouseEvent().getComponent().getSize().height;
             //���ֵ����event.getMouseEvent().getComponent().getSize().height
     
            Line ray = view.computeRayFromScreenPoint(x, y);
            Position pickPos = null;
            // Use intersection with sphere at reference altitude.
            Intersection inters[] = globe.intersect(ray, this.dragRefAltitude);
            if (inters != null)
                pickPos = globe.computePositionFromPoint(inters[0].getIntersectionPoint());

            if (pickPos != null)
            {
                // Intersection with globe. Move reference point to the intersection point,
                // but maintain current altitude.
                Position p = new Position(pickPos, dragObject.getReferencePosition().getElevation());
                dragObject.moveTo(p);
            }
            this.dragging = true;
            event.consume();
        }
    }
}