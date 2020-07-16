package com.yukthitech.screen.annotator;

import java.awt.Color;
import java.awt.Point;

public class AnnotatorContext
{
	private int thickness = 3;
	
	private Color color = Color.red;
	
	private int zoomFactor = 1;
	
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
		if(!increment && zoomFactor <= 1)
		{
			return false;
		}
		
		if(increment)
		{
			zoomFactor ++;
		}
		else
		{
			zoomFactor--;
		}
		
		this.zoomPoint = zoomPoint;
		
		return true;
	}
	
	public int getZoomFactor()
	{
		return zoomFactor;
	}
	
	public Point getZoomPoint()
	{
		return zoomPoint;
	}
}
