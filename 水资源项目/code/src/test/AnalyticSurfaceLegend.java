package test;

import com.jogamp.opengl.util.awt.TextRenderer;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.render.*;
import gov.nasa.worldwind.util.*;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.text.Format;
import java.util.ArrayList;


public class AnalyticSurfaceLegend implements Renderable
{
 

    protected static final int DEFAULT_WIDTH = 32;
    protected static final int DEFAULT_HEIGHT = 256;
    protected static final double HUE_BLUE = 240d / 360d;
    protected static final double HUE_RED = 0d / 360d;

    protected ScreenImage screenImage;


    public static AnalyticSurfaceLegend fromColorGradient()
    {
        AnalyticSurfaceLegend legend = new AnalyticSurfaceLegend();
        legend.screenImage = new ScreenImage();
      legend.screenImage.setImageSource(legend.createColorGradientLegendImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, HUE_BLUE, HUE_RED,  Color.YELLOW));
      
      
        return legend;
    }

   

    protected AnalyticSurfaceLegend()
    {
    }

 

   

    public void setOpacity(double opacity)
    {
        if (opacity < 0d || opacity > 1d)
        {
            String message = Logging.getMessage("generic.OpacityOutOfRange", opacity);
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        this.screenImage.setOpacity(opacity);
    }

    
    public void setScreenLocation(Point point)
    {
        if (point == null)
        {
            String message = Logging.getMessage("nullValue.PointIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        this.screenImage.setScreenLocation(point);
    }

  

   

    public void render(DrawContext dc)
    {
        if (dc == null)
        {
            String message = Logging.getMessage("nullValue.DrawContextIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

       

        this.doRender(dc);
    }






    protected void doRender(DrawContext dc)
    {
        this.screenImage.render(dc);

     
    }


    protected BufferedImage createColorGradientLegendImage(int width, int height, double minHue, double maxHue,
        Color borderColor)
    {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2d = image.createGraphics();
        try
        {
            for (int y = 0; y < height; y++)
            {
                double hue = WWMath.mix(1d - y / (double) (height - 1), minHue, maxHue);
                g2d.setColor(Color.getHSBColor((float) hue, 1f, 1f));
                g2d.drawLine(0, y, width - 1, y);
            }

            if (borderColor != null)
            {
                g2d.setColor(borderColor);
                g2d.drawRect(0, 0, width - 1, height - 1);
            }
        }
        finally
        {
            g2d.dispose();
        }

        return image;
    }

}
