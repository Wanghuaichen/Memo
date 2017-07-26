/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */
package test;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.*;
import gov.nasa.worldwind.event.*;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.*;
import gov.nasa.worldwindx.applications.worldwindow.util.Util;
import gov.nasa.worldwindx.examples.ApplicationTemplate;

import javax.swing.*;

import test.Control.ContextMenu;
import test.Control.ContextMenuController;
import test.Control.ContextMenuItemInfo;
import test.Control.ContextMenuInfo;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Illustrates how to attach context (popup) menus to shapes. The example creates several <code>{@link
 * PointPlacemark}s</code> and assigns each of them a context-menu definition. When the user presses the right mouse
 * button while the cursor is on a placemark, the placemark's context menu is shown and the user may select an item in
 * it.
 *
 * @author tag
 * @version $Id: ContextMenusOnShapes.java 1171 2013-02-11 21:45:02Z dcollins $
 */
public class test1 extends ApplicationTemplate
{
    /** The Controller listens for selection events and either highlights a selected item or shows its context menu. */
   

    // The code below makes and displays some placemarks. The context menu info for each placemark is also specified.

    public static class AppFrame extends ApplicationTemplate.AppFrame
    {
        public AppFrame()
        {
            RenderableLayer layer = new RenderableLayer();

            // Create and set an attribute bundle.
            PointPlacemarkAttributes attrs = new PointPlacemarkAttributes();
            attrs.setAntiAliasHint(Polyline.ANTIALIAS_FASTEST);
            attrs.setLineMaterial(Material.WHITE);
            attrs.setLineWidth(2d);
            attrs.setImageAddress("images/pushpins/push-pin-yellow.png");
            attrs.setScale(0.6);
            attrs.setImageOffset(new Offset(19d, 11d, AVKey.PIXELS, AVKey.PIXELS));

            PointPlacemarkAttributes highlightAttrs = new PointPlacemarkAttributes(attrs);
            highlightAttrs.setScale(0.7);

            ContextMenuItemInfo[] itemActionNames = new ContextMenuItemInfo[]
                {
                    new ContextMenuItemInfo("Do This"),
                    new ContextMenuItemInfo("Do That"),
                    new ContextMenuItemInfo("Do the Other Thing"),
                };

            PointPlacemark pp = new PointPlacemark(Position.fromDegrees(28, -102, 1e4));
            pp.setAttributes(attrs);
            pp.setHighlightAttributes(highlightAttrs);
            pp.setAltitudeMode(WorldWind.CLAMP_TO_GROUND);
            pp.setValue(ContextMenu.CONTEXT_MENU_INFO, new ContextMenuInfo("Placemark A", itemActionNames));
            layer.addRenderable(pp);

            pp = new PointPlacemark(Position.fromDegrees(29, -104, 2e4));
            pp.setAttributes(attrs);
            pp.setHighlightAttributes(highlightAttrs);
            pp.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
            pp.setValue(ContextMenu.CONTEXT_MENU_INFO, new ContextMenuInfo("Placemark B", itemActionNames));
            layer.addRenderable(pp);

            pp = new PointPlacemark(Position.fromDegrees(30, -104.5, 2e4));
            pp.setAttributes(attrs);
            pp.setHighlightAttributes(highlightAttrs);
            pp.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
            pp.setValue(ContextMenu.CONTEXT_MENU_INFO, new ContextMenuInfo("Placemark C", itemActionNames));
            layer.addRenderable(pp);

            pp = new PointPlacemark(Position.fromDegrees(28, -104.5, 2e4));
            pp.setAttributes(attrs);
            pp.setHighlightAttributes(highlightAttrs);
            pp.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
            pp.setValue(ContextMenu.CONTEXT_MENU_INFO, new ContextMenuInfo("Placemark D", itemActionNames));
            layer.addRenderable(pp);

            // Create a placemark that uses all default values.
            pp = new PointPlacemark(Position.fromDegrees(30, -103.5, 2e3));
            pp.setValue(ContextMenu.CONTEXT_MENU_INFO, new ContextMenuInfo("Placemark E", itemActionNames));
            layer.addRenderable(pp);

            // Add the layer to the model.
            insertBeforeCompass(getWwd(), layer);
            // Update layer panel.
            this.getLayerPanel().update(this.getWwd());

            // Set up the context menu
            ContextMenuController contextMenuController = new ContextMenuController();
            getWwd().addSelectListener(contextMenuController);
        }
    }

    public static void main(String[] args)
    {
        ApplicationTemplate.start("World Wind Context Menus on Shapes", AppFrame.class);
    }
}
