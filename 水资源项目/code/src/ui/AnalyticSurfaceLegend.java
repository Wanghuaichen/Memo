/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */
package ui;
import gov.nasa.worldwind.render.*;
import gov.nasa.worldwind.util.*;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.text.Format;
import java.util.ArrayList;

/**
 * @author dcollins
 * @version $Id: AnalyticSurfaceLegend.java 1171 2013-02-11 21:45:02Z dcollins $
 */
public class AnalyticSurfaceLegend implements Renderable
{
    public interface LabelAttributes
    {
        double getValue();

        String getText();

        Font getFont();

        Color getColor();

        Point2D getOffset();
    }

    protected static final Font DEFAULT_FONT = Font.decode("Arial-PLAIN-12");
    protected static final Color DEFAULT_COLOR = Color.WHITE;
    protected static final int DEFAULT_WIDTH = 32;
    protected static final int DEFAULT_HEIGHT = 256;

    protected boolean visible = true;
    protected ScreenImage screenImage;
    protected Iterable<? extends Renderable> labels;

    public static AnalyticSurfaceLegend fromColorGradient(int width, int height, double minValue, double maxValue,
        double minHue, double maxHue, Color borderColor, Iterable<? extends LabelAttributes> labels,
        LabelAttributes titleLabel)
    {
        AnalyticSurfaceLegend legend = new AnalyticSurfaceLegend();
        legend.screenImage = new ScreenImage();
      legend.screenImage.setImageSource(legend.createColorGradientLegendImage(width, height, minHue, maxHue,  borderColor));
        //legend.labels = legend.createColorGradientLegendLabels(width, height, minValue, maxValue, labels, titleLabel);

        return legend;
    }

    public static AnalyticSurfaceLegend fromColorGradient(double minValue, double maxValue, double minHue,
        double maxHue, Iterable<? extends LabelAttributes> labels, LabelAttributes titleLabel)
    {
        return fromColorGradient(DEFAULT_WIDTH, DEFAULT_HEIGHT, minValue, maxValue, minHue, maxHue, DEFAULT_COLOR,
            labels,
            titleLabel);
    }

    protected AnalyticSurfaceLegend()
    {
    }

    public boolean isVisible()
    {
        return this.visible;
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    public double getOpacity()
    {
        return this.screenImage.getOpacity();
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

    public Point getScreenLocation(DrawContext dc)
    {
        return this.screenImage.getScreenLocation(dc);
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

    public int getWidth(DrawContext dc)
    {
        if (dc == null)
        {
            String message = Logging.getMessage("nullValue.DrawContextIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        return this.screenImage.getImageWidth(dc);
    }

    public int getHeight(DrawContext dc)
    {
        if (dc == null)
        {
            String message = Logging.getMessage("nullValue.DrawContextIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        return this.screenImage.getImageHeight(dc);
    }

    public void render(DrawContext dc)
    {
        if (dc == null)
        {
            String message = Logging.getMessage("nullValue.DrawContextIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        if (!this.isVisible())
            return;

        this.doRender(dc);
    }

    //**************************************************************//
    //********************  Legend Utilities  **********************//
    //**************************************************************//

    public static Iterable<? extends AnalyticSurfaceLegend.LabelAttributes> createDefaultColorGradientLabels(
        double minValue, double maxValue, Format format)
    {
        if (format == null)
        {
            String message = Logging.getMessage("nullValue.Format");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        ArrayList<AnalyticSurfaceLegend.LabelAttributes> labels
            = new ArrayList<AnalyticSurfaceLegend.LabelAttributes>();

        int numLabels = 6;
        Font font = Font.decode("Arial-BOLD-12");

        for (int i = 0; i < numLabels; i++)
        {
            double value =i;

            String text = format.format(value);
            if (!WWUtil.isEmpty(text))
            {
                labels.add(createLegendLabelAttributes(value, text, font, Color.WHITE, 5d, 0d));
            }
        }

        return labels;
    }

    public static AnalyticSurfaceLegend.LabelAttributes createDefaultTitle(String text)
    {
        if (text == null)
        {
            String message = Logging.getMessage("nullValue.StringIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        Font font = Font.decode("Arial-BOLD-16");
        return createLegendLabelAttributes(0d, text, font, Color.WHITE, 0d, -20d);
    }

    public static AnalyticSurfaceLegend.LabelAttributes createLegendLabelAttributes(final double value,
        final String text, final Font font, final Color color, final double xOffset, final double yOffset)
    {
        return new AnalyticSurfaceLegend.LabelAttributes()
        {
            public double getValue()
            {
                return value;
            }

            public String getText()
            {
                return text;
            }

            public Font getFont()
            {
                return font;
            }

            public Color getColor()
            {
                return color;
            }

            public Point2D getOffset()
            {
                return new Point2D.Double(xOffset, yOffset);
            }
        };
    }

    //**************************************************************//
    //********************  Legend Rendering  **********************//
    //**************************************************************//

    protected void doRender(DrawContext dc)
    {
        this.screenImage.render(dc);

        if (!dc.isPickingMode() && this.labels != null)
        {
            for (Renderable renderable : this.labels)
            {
                if (renderable != null)
                    renderable.render(dc);
            }
        }
    }
    //**************************************************************//
    //********************  Hue Gradient Legend  *******************//
    //**************************************************************//

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
