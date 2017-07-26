/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */
package test;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.*;
import gov.nasa.worldwind.data.*;
import gov.nasa.worldwind.exception.WWRuntimeException;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.*;
import gov.nasa.worldwind.util.*;
import gov.nasa.worldwindx.examples.ApplicationTemplate;
import gov.nasa.worldwindx.examples.util.ExampleUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.*;
import java.util.ArrayList;

/**
 * Illustrates how to configure and display a 3D geographic grid of scalar data using the World Wind <code>{@link
 * AnalyticSurface}</code>. Analytic surface defines a grid over a geographic <code>{@link Sector}</code> at a specified
 * altitude, and enables the caller to specify the color and height at each grid point.
 * <p/>
 * This illustrates three key AnalyticSurface configurations: <ul> <li>Displaying a static data set where each grid
 * point uses color and height to indicate the data's magnitude.</li> <li>Displaying data that varies by color over time
 * on the terrain surface.</li> <li>Displaying data that varies by color and height over time at a specified
 * altitude.</li> </ul>
 *
 * @author dcollins
 * @version $Id: AnalyticSurfaceDemo.java 1171 2013-02-11 21:45:02Z dcollins $
 */
public class AnalyticSurfaceDemo extends ApplicationTemplate
{
  

    public static class AppFrame extends ApplicationTemplate.AppFrame
    {
    
        protected RenderableLayer analyticSurfaceLayer;

        public AppFrame()
        {
        	  this.analyticSurfaceLayer = new RenderableLayer();
              this.analyticSurfaceLayer.setPickEnabled(false);
              this.analyticSurfaceLayer.setName("Analytic Surfaces");
              insertBeforePlacenames(this.getWwd(), this.analyticSurfaceLayer);
              this.getLayerPanel().update(this.getWwd());
              createLegendSurface(40, 40, this.analyticSurfaceLayer);
        }

        
    }


   protected static void createLegendSurface( int width, int height,RenderableLayer outLayer)
    {
      
    	double minValue = 0;
        double maxValue = 5;


        Format legendLabelFormat = new DecimalFormat("# km");
        

        AnalyticSurfaceLegend legend = AnalyticSurfaceLegend.fromColorGradient();

        legend.setOpacity(0.8);
        legend.setScreenLocation(new Point(650, 300));
        outLayer.addRenderable( legend);
    }

 
   
 public static void main(String[] args)
    {
        ApplicationTemplate.start("World Wind Analytic Surface", AppFrame.class);
    }
}
