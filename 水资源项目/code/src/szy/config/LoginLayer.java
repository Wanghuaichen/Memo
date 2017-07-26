package szy.config;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.*;
import gov.nasa.worldwind.util.Logging;

import java.awt.*;

	/**
	 * This layer displays onscreen view controls. Controls are available for pan, zoom, heading, pitch, tilt, field-of-view
	 * and vertical exaggeration. Each of the controls can be enabled or disabled independently.
	 * <p/>
	 * An instance of this class depends on an instance of {@link LoginSelectListener} to control it. The select
	 * listener must be registered as such via {@link gov.nasa.worldwind.WorldWindow#addSelectListener(gov.nasa.worldwind.event.SelectListener)}.
	 * <p/>
	 * <code>ViewControlsLayer</code> instances are not sharable among <code>WorldWindow</code>s.
	 *
	 * @author xiangfu shi
	 * @version $Id: ViewControlsLayer.java 1171 2013-02-11 21:45:02Z dcollins $
	 * @see LoginSelectListener
	 */
	public class LoginLayer extends RenderableLayer
	{
	    // The default images
	    protected final static String IMAGE_PAN = "img/redCircle.png";
	   

	    // The annotations used to display the controls.
	    protected ScreenAnnotation controlPan;
	    protected GlobeAnnotation a;

	    protected String position = AVKey.NORTHEAST;
	    protected String layout = AVKey.HORIZONTAL;
	    protected Vec4 locationCenter = null;
	    protected Vec4 locationOffset = null;
	    protected double scale =1;
	    protected int borderWidth = 20;
	    protected int buttonSize = 200;
	   // protected int panSize = 300;
	    protected boolean initialized = false;
	    protected Rectangle referenceViewport;
	    protected ScreenAnnotation currentControl;
	    protected boolean showPanControls = true;



	    public int getBorderWidth()
	    {
	        return this.borderWidth;
	    }

	    /**
	     * Sets the view controls offset from the viewport border.
	     *
	     * @param borderWidth the number of pixels to offset the view controls from the borders indicated by {@link
	     *                    #setPosition(String)}.
	     */

	    /**
	     * Get the controls display scale.
	     *
	     * @return the controls display scale.
	     */

	
	    protected int getButtonSize()
	    {
	        return buttonSize;
	    }


	    public String getPosition()
	    {
	        return this.position;
	    }

	    /**
	     * Sets the relative viewport location to display the view controls. Can be one of {@link AVKey#NORTHEAST}, {@link
	     * AVKey#NORTHWEST}, {@link AVKey#SOUTHEAST}, or {@link AVKey#SOUTHWEST} (the default). These indicate the corner of
	     * the viewport to place view controls.
	     *
	     * @param position the desired view controls position, in screen coordinates.
	     */
	    public void setPosition(String position)
	    {
	        if (position == null)
	        {
	            String message = Logging.getMessage("nullValue.PositionIsNull");
	            Logging.logger().severe(message);
	            throw new IllegalArgumentException(message);
	        }
	        this.position = position;
	        clearControls();
	    }

	    /**
	     * Returns the current layout. Can be one of {@link AVKey#HORIZONTAL} or {@link AVKey#VERTICAL}.
	     *
	     * @return the current layout.
	     */
	    public String getLayout()
	    {
	        return this.layout;
	    }

	    /**
	     * Sets the desired layout. Can be one of {@link AVKey#HORIZONTAL} or {@link AVKey#VERTICAL}.
	     *
	     * @param layout the desired layout.

	    /**
	     * Layer opacity is not applied to layers of this type. Opacity is controlled by the alpha values of the operation
	     * images.
	     *
	     * @param opacity the current opacity value, which is ignored by this layer.
	     */
	    @Override
	    public void setOpacity(double opacity)
	    {
	        super.setOpacity(opacity);
	    }

	    /**
	     * Returns the layer's opacity value, which is ignored by this layer. Opacity is controlled by the alpha values of
	     * the operation images.
	     *
	     * @return The layer opacity, a value between 0 and 1.
	     */
	    @Override
	    public double getOpacity()
	    {
	        return super.getOpacity();
	    }

	    /**
	     * Returns the current layer image location.
	     *
	     * @return the current location center. May be null.
	     */
	    public Vec4 getLocationCenter()
	    {
	        return locationCenter;
	    }


	
	    public boolean isShowPanControls()
	    {
	        return this.showPanControls;
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
	            this.currentControl.getAttributes().setImageOpacity(-1); // use default opacity
	            this.currentControl = null;
	        }

	        // Turn on highlight if object selected.
	        if (control != null && control instanceof ScreenAnnotation)
	        {
	            this.currentControl = (ScreenAnnotation) control;
	            this.currentControl.getAttributes().setImageOpacity(1);
	        }
	    }

	    @Override
	    public void doRender(DrawContext dc)
	    {
	        if (!this.initialized)
	            initialize(dc);

	        if (!this.referenceViewport.equals(dc.getView().getViewport()))
	            updatePositions(dc);

	        super.doRender(dc);
	    }

	    protected boolean isInitialized()
	    {
	        return initialized;
	    }

	    protected void initialize(DrawContext dc)
	    {
	        if (this.initialized)
	            return;
//
	        // Setup user interface - common default attributes
	        AnnotationAttributes ca = new AnnotationAttributes();
	        ca.setAdjustWidthToText(AVKey.SIZE_FIT_TEXT);
	        ca.setInsets(new Insets(0, 0, 0, 0));
	        ca.setBorderWidth(0);
	        ca.setCornerRadius(0);
	        ca.setSize(new Dimension(buttonSize, buttonSize));
	        ca.setBackgroundColor(new Color(0, 0, 0, 0));
	        ca.setImageOpacity(.5);
	       // ca.setFont(Font.decode("Arial-BOLD-24"));
	        ca.setFont(Font.decode("GBK"));
	       // ca.setBackgroundColor(Color.BLUE);
	        ca.setScale(scale);
//            AnnotationAttributes defaultAttributes = new AnnotationAttributes();
//            defaultAttributes.setBackgroundColor(new Color(0f, 0f, 0f, 0f));
//            defaultAttributes.setTextColor(Color.YELLOW);
//            defaultAttributes.setLeaderGapWidth(14);
//            defaultAttributes.setCornerRadius(0);
//            defaultAttributes.setSize(new Dimension(300, 0));
//            defaultAttributes.setAdjustWidthToText(AVKey.SIZE_FIT_TEXT); // use strict dimension width - 200
//            defaultAttributes.setFont(Font.decode("Arial-BOLD-24"));
//            defaultAttributes.setBorderWidth(0);
//            defaultAttributes.setHighlightScale(1);             // No highlighting either
//            defaultAttributes.setCornerRadius(0);
	       

	        final String NOTEXT = "请登录";
	        final Point ORIGIN = new Point(0, 0);
	        if (this.showPanControls)
	        {
	            // Pan
	        	

	            controlPan = new ScreenAnnotation(NOTEXT, ORIGIN,ca);
	        	//controlPan=new ScreenAnnotation(NOTEXT, ORIGIN, Font.decode("Arial-ITALIC-12"), Color.DARK_GRAY);
	            controlPan.setValue(AVKey.VIEW_OPERATION, AVKey.VIEW_PAN);
	          //  controlPan.getAttributes().setImageSource(getImageSource(AVKey.VIEW_PAN));
	           // controlPan.getAttributes().setImageOpacity(0);
	            controlPan.getAttributes().setTextAlign(AVKey.LEFT);
	          // controlPan.getAttributes().setBorderColor(Color.BLACK);
	           controlPan.getAttributes().setBackgroundColor(Color.YELLOW);
	            controlPan.getAttributes().setTextColor(new Color(255,255,255));
	        
	           // Font f=new Font("宋体",1,8);
	          //  controlPan.getAttributes().setFont(f);
	          //  controlPan.getAttributes().setSize(new Dimension(panSize, panSize));
	            this.addRenderable(controlPan);
//	            ScreenRelativeAnnotation controlPan = new ScreenRelativeAnnotation(NOTEXT, 0.99, 0.99);
//	            controlPan.setKeepFullyVisible(true);
//	            controlPan.setXMargin(5);
//	            controlPan.setYMargin(5);
//	            controlPan.getAttributes().setDefaults(defaultAttributes);
//	            this.addRenderable(controlPan);
	        }

	        updatePositions(dc);

	        this.initialized = true;
	    }

	
	    protected Object getImageSource(String control)
	    {
	        if (control.equals(AVKey.VIEW_PAN))
	            return IMAGE_PAN;
      return null;
	    }
	    // Set controls positions according to layout and viewport dimension
	    protected void updatePositions(DrawContext dc)
	    {
	        boolean horizontalLayout = this.layout.equals(AVKey.HORIZONTAL);

	        // horizontal layout: pan button + look button beside 2 rows of 4 buttons
	        int width = buttonSize;
	        int height = buttonSize;
	        width = (int) (width * scale);
	        height = (int) (height * scale);
	        int xOffset = 0;
	        int yOffset = (int) (buttonSize * scale);

	        if (!horizontalLayout)
	        {
	            // vertical layout: pan button above look button above 4 rows of 2 buttons
	            int temp = height;
	            //noinspection SuspiciousNameCombination
	            height = width;
	            width = temp;
	            xOffset = (int) (buttonSize * scale);
	            yOffset = 0;
	        }

	        int halfPanSize = (int) (buttonSize * scale / 2);
	        int halfButtonSize = (int) (buttonSize * scale / 2);

	        Rectangle controlsRectangle = new Rectangle(width, height);
	        Point locationSW = computeLocation(dc.getView().getViewport(), controlsRectangle);

	        // Layout start point
	        int x = locationSW.x;
	        int y = horizontalLayout ? locationSW.y : locationSW.y + height;

	        if (this.showPanControls)
	        {
	            if (!horizontalLayout)
	                y -= (int) (buttonSize * scale);
	            controlPan.setScreenPoint(new Point(x + halfPanSize, y));
	            if (horizontalLayout)
	                x += (int) (buttonSize * scale);
	        }

	        this.referenceViewport = dc.getView().getViewport();
	    }


	    protected Point computeLocation(Rectangle viewport, Rectangle controls)
	    {
	        double x;
	        double y;

	        if (this.locationCenter != null)
	        {
	            x = this.locationCenter.x - controls.width / 2;
	            y = this.locationCenter.y - controls.height / 2;
	        }
	        else if (this.position.equals(AVKey.NORTHEAST))
	        {
	            x = viewport.getWidth() - controls.width - this.borderWidth;
	            y = viewport.getHeight() - controls.height - this.borderWidth;
	        }
	        else if (this.position.equals(AVKey.SOUTHEAST))
	        {
	            x = viewport.getWidth() - controls.width - this.borderWidth;
	            y = 0d + this.borderWidth;
	        }
	        else if (this.position.equals(AVKey.NORTHWEST))
	        {
	            x = 0d + this.borderWidth;
	            y = viewport.getHeight() - controls.height - this.borderWidth;
	        }
	        else if (this.position.equals(AVKey.SOUTHWEST))
	        {
	            x = 0d + this.borderWidth;
	            y = 0d + this.borderWidth;
	        }
	        else // use North East as default
	        {
	            x = viewport.getWidth() - controls.width - this.borderWidth;
	            y = viewport.getHeight() - controls.height - this.borderWidth;
	        }

	        if (this.locationOffset != null)
	        {
	            x += this.locationOffset.x;
	            y += this.locationOffset.y;
	        }

	        return new Point((int) x, (int) y);
	    }

	    protected void clearControls()
	    {
	        this.removeAllRenderables();

	        this.controlPan = null;


	        this.initialized = false;
	    }


	}


