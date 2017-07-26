package ui;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.layers.IconLayer;
import gov.nasa.worldwind.render.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;


public class AlarmIcon extends UserFacingIcon {

	 public  enum AlarmLevel {
	        yellow,orange,red;
	    }
	
	public IconLayer layer;
	WorldWindow wwd;
	PulsingAlarmAction alarmAction;
	PulsingAlarmAction gpsAction;


	public AlarmIcon( Position _pos,WorldWindow _wwd, IconLayer _layer) {
		super("img/redCircle.png", _pos);
		this.setSize(new Dimension(30, 30));
		
		wwd = _wwd;
		layer = _layer;
		layer.addIcon(this);
		layer.setPickEnabled(false);

	}

	private BufferedImage createBitmap(String pattern, Color color) {
		// Create bitmap with pattern
		BufferedImage image = PatternFactory.createPattern(pattern,
				new Dimension(128, 128), 0.7f, color,
				new Color(color.getRed(), color.getGreen(),
						color.getBlue(), 0));
		// Blur a lot to get a fuzzy edge
		image = PatternFactory.blur(image, 13);
		image = PatternFactory.blur(image, 13);
		image = PatternFactory.blur(image, 13);
		image = PatternFactory.blur(image, 13);
		return image;
	}
	private void setAlarmAction(AlarmLevel alarmLevel)// �����ȼ�1,2,3�ơ��ȡ� ��
	{
		BufferedImage circleColor;
		String alarmName;
		switch (alarmLevel) {
		case yellow:
			circleColor = createBitmap(PatternFactory.PATTERN_CIRCLE,Color.YELLOW);
			
			alarmName = "ˮ����Ⱦ";
			break;
		case orange:
			circleColor = createBitmap(PatternFactory.PATTERN_CIRCLE,Color.ORANGE);
			alarmName = "orange alarm";
			break;
		case red:
			circleColor = createBitmap(PatternFactory.PATTERN_CIRCLE,
					Color.RED);
			alarmName = "ˮ����Ⱦ";
			break;
		default:
			return;

		}
		this.setImageSource(circleColor);
		//alarmAction = new PulsingAlarmAction(alarmName,	circleColor, this, wwd,new double[] {2, 0.5});
		alarmAction = new PulsingAlarmAction(alarmName,	circleColor, this, wwd);
		
	}
	public void setGpsAction(AlarmLevel alarmLevel) {
		BufferedImage circleColor;
		String alarmName;
		switch (alarmLevel) {
		case yellow:
			circleColor = createBitmap(PatternFactory.PATTERN_CIRCLE,Color.YELLOW);
			
			alarmName = "ˮ����Ⱦ";
			break;
		case orange:
			circleColor = createBitmap(PatternFactory.PATTERN_CIRCLE,Color.ORANGE);
			alarmName = "orange alarm";
			break;
		case red:
			circleColor = createBitmap(PatternFactory.PATTERN_CIRCLE,
					Color.RED);
			alarmName = "ˮ����Ⱦ";
			break;
		default:
			return;

		}
		this.setImageSource(circleColor);
		this.gpsAction =new PulsingAlarmAction(alarmName,	circleColor, this, wwd,new double[] {2, 0.5});
	}
	
	public Action getAlarmAction(AlarmLevel alarmLevel)// �����ȼ�1,2,3�ơ��ȡ� ��
	{
		this.setAlarmAction(alarmLevel);
		return alarmAction;
	}
	public PulsingAlarmAction getGpsAction(AlarmLevel alarmLevel) {
		this.setGpsAction(alarmLevel);
		return gpsAction;
	}


	public void alarm(AlarmLevel alarmLevel)// �����ȼ�1,2,3�ơ��ȡ� ��
	{
		this.setAlarmAction(alarmLevel);
		alarmAction.actionPerformed(null);

	}
	public void stopAlarm()
	{
		alarmAction.stop();
		layer.removeIcon(this);
		wwd.redraw();
		
	}
	
	/*
	 * ��������
	 */
	protected class PulsingAlarmAction extends AbstractAction {
		
		protected final Object bgIconPath;
		protected int frequency = 80;
		protected int scaleIndex = 0;
		protected double[] scales = new double[] { 1.25, 1.5, 1.75, 2, 2.25,
				2.5, 2.75, 3, 3.25, 3.5, 3.25, 3, 2.75, 2.5, 2.25, 2, 1.75, 1.5 };
		protected Timer timer;
		protected UserFacingIcon icon;
		protected WorldWindow wwd;

		public PulsingAlarmAction(String name, BufferedImage bgp,
				UserFacingIcon _icon, WorldWindow _wwd) {
			super(name);
			this.bgIconPath = bgp;
			this.icon = _icon;
			wwd=_wwd;
		}
		public PulsingAlarmAction(String name, BufferedImage bgp,
				UserFacingIcon _icon, WorldWindow _wwd,double[] scales) {
//			super(name);
//			this.bgIconPath = bgp;
//			this.icon = _icon;
//			wwd=_wwd;
            this(name, bgp, _icon,_wwd);
            this.scales = scales;
		}
		

		public void actionPerformed(ActionEvent e) {
			if (timer == null) {
				timer = new Timer(frequency, new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						icon.setBackgroundScale(scales[++scaleIndex
								% scales.length]);
						wwd.redraw();
					}
				});

			}
			icon.setBackgroundImage(bgIconPath);
			scaleIndex = 0;
			timer.start();
		}
		public void stop()
		{
			timer.stop();
			
		
		}
	}

	public void removeAlarm() {
		layer.removeIcon(this);
		
	}
	
	
	
	

}


