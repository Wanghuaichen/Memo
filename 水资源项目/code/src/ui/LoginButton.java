
package ui;
import javax.swing.JButton;


import gov.nasa.worldwind.WorldWindow;

import gov.nasa.worldwind.layers.*;
import gov.nasa.worldwind.render.Renderable;

import gov.nasa.worldwindx.examples.ApplicationTemplate;
import gov.nasa.worldwindx.examples.util.ScreenShotAction;
/**
 * @author tag
 * @version $Id: Compass.java 1171 2013-02-11 21:45:02Z dcollins $
 */
public class LoginButton 
{ private WorldWindow wwd;
	private JButton button;
	private RenderableLayer layer;
	public LoginButton(WorldWindow wwd) {
		this.wwd=wwd;
		button.setText("截图");
		button.setLocation(100, 150);
		layer = new RenderableLayer();
		button.addActionListener(new ScreenShotAction(this.wwd));
		layer.addRenderable((Renderable) button);
		ApplicationTemplate.insertBeforeCompass(this.wwd, this.layer);
	}
	  
	
}
