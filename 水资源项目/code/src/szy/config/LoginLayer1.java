package szy.config;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;







import szy.context.StaticContext;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.AnnotationAttributes;
import gov.nasa.worldwind.render.ScreenAnnotation;
import gov.nasa.worldwind.render.ScreenRelativeAnnotation;


public class LoginLayer1 extends RenderableLayer {
	private String Name="Please Login!";
	private int login=StaticContext.Line;

	ScreenRelativeAnnotation upperRight;
	   protected ScreenAnnotation currentControl;
	AnnotationAttributes defaultAttributes;
    public LoginLayer1()
    {
    	
    	if(login==1){
    		Name="Admin";
    	}
    	
    	
        defaultAttributes = new AnnotationAttributes();
        defaultAttributes.setBackgroundColor(new Color(0f, 0f, 0f, 0f));
        defaultAttributes.setTextColor(Color.RED);
        defaultAttributes.setLeaderGapWidth(14);
        defaultAttributes.setCornerRadius(0);
        defaultAttributes.setSize(new Dimension(50, 0));
        defaultAttributes.setAdjustWidthToText(AVKey.SIZE_FIT_TEXT); // use strict dimension width - 200
        Font f=new Font("",1,20);
       
        defaultAttributes.setFont(Font.decode("UTF-8"));
        defaultAttributes.setFont(f);
        defaultAttributes.setBorderWidth(0);
        defaultAttributes.setHighlightScale(1);             // No highlighting either
        defaultAttributes.setCornerRadius(30);

//        ScreenRelativeAnnotation lowerLeft = new ScreenRelativeAnnotation("Lower Left", .1, 0.01);
//        lowerLeft.setKeepFullyVisible(true);
//        lowerLeft.setXMargin(5);
//        lowerLeft.setYMargin(5);
//        lowerLeft.getAttributes().setDefaults(defaultAttributes);


         upperRight = new ScreenRelativeAnnotation(Name, 0.99, 0.99);
        upperRight.setKeepFullyVisible(true);
        upperRight.setXMargin(12);
        upperRight.setYMargin(12);
        upperRight.setValue(AVKey.VIEW_OPERATION, AVKey.VIEW_PAN);
        upperRight.getAttributes().setDefaults(defaultAttributes);
        upperRight.getAttributes().setBackgroundColor(Color.GRAY);
        upperRight.getAttributes().setOpacity(0.6);
        upperRight.getAttributes().setBorderWidth(11);
        upperRight.getAttributes().setDistanceMaxScale(1);
      //  this.setValue(LoginLayer1.CHANGE, new DoChange(upperRight));
        
        
        /////////////////////////////////////
   

   //     this.addRenderable(upperLeft);
//        layer.addAnnotation(upperLeft);
        this.addRenderable(upperRight);
//        layer.addAnnotation(lowerRight);
//        layer.addAnnotation(center);
    }
    public AnnotationAttributes getDefaultAttributes() {
		return defaultAttributes;
	}
	public void setDefaultAttributes(AnnotationAttributes defaultAttributes) {
		this.defaultAttributes = defaultAttributes;
	}
	// --- Selection ---------------------------------------
	public ScreenRelativeAnnotation getUpperRight() {
		return upperRight;
	}
	public void setUpperRight(ScreenRelativeAnnotation upperRight) {
		this.upperRight = upperRight;
	}
	public Object getHighlightedObject()
    {
        return this.currentControl;
    }
    public void highlight(Object control)
    {
        // Manage highlighting of controls.
        if (this.currentControl == control)
            return; // same thing selected

        // Turn off highlight if on.
        if (this.currentControl != null)
        {
            this.currentControl.getAttributes().setOpacity(0.6); // use default opacity
            this.currentControl = null;
        }

        // Turn on highlight if object selected.
        if (control != null && control instanceof ScreenAnnotation)
        {
            this.currentControl = (ScreenAnnotation) control;
            this.currentControl.getAttributes().setOpacity(1);
        }
    }

}
