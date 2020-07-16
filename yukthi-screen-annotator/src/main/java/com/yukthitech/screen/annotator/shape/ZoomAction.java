package com.yukthitech.screen.annotator.shape;

import java.awt.Point;
import java.awt.event.MouseEvent;

import com.yukthitech.screen.annotator.AnnotatorContext;

public class ZoomAction implements IAction
{
	@Override
	public boolean finalizeLocation(int x, int y, int button, AnnotatorContext context)
	{
		//adjust x & y according to current zoom factor
		x = x / context.getZoomFactor();
		y = y / context.getZoomFactor();
		
		//zoom
		return context.zoom(button == MouseEvent.BUTTON1, new Point(x, y));
	}
}
