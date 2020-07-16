package com.yukthitech.screen.annotator;

import java.awt.Color;
import java.awt.Point;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AnnotatorContext
{
	private static Logger logger = LogManager.getLogger(AnnotatorContext.class);
	
	private static final float ZOOM_STEP = 0.25f;
	
	private int thickness = 3;
	
	private Color color = Color.red;
	
	private float zoomFactor = 1;
	
	private Point zoomPoint;
	
	public int getThickness()
	{
		return thickness;
	}

	public void setThickness(int thickness)
	{
		this.thickness = thickness;
	}

	public Color getColor()
	{
		return color;
	}

	public void setColor(Color color)
	{
		this.color = color;
	}
	
	public boolean zoom(boolean increment, Point zoomPoint)
	{
		logger.debug("From zoom-factor '{}' {} zoom at point {}", 
				zoomFactor, 
				(increment ? "incrementing" : "decrementing"),
				zoomPoint);
		
		if(!increment && zoomFactor <= 1)
		{
			zoomFactor = 1;
			return true;
		}
		
		if(increment)
		{
			zoomFactor += ZOOM_STEP;
		}
		else
		{
			zoomFactor -= ZOOM_STEP;
		}
		
		this.zoomPoint = zoomPoint;
		
		return true;
	}
	
	public float getZoomFactor()
	{
		return zoomFactor;
	}
	
	public Point getZoomPoint()
	{
		return zoomPoint;
	}
	
	public void reset()
	{
		zoomFactor = 1;
		zoomPoint = null;
	}
}
