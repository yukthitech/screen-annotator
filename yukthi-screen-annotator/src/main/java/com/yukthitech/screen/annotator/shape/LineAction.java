package com.yukthitech.screen.annotator.shape;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;

import com.yukthitech.screen.annotator.AnnotatorContext;

public class LineAction implements IAction
{
	private Point start;

	private Point end = new Point();

	@Override
	public void startFrom(int x, int y)
	{
		start = new Point(x, y);
		end = new Point(x, y);
	}

	@Override
	public void moveEndPointTo(int x, int y)
	{
		end.setLocation(x, y);
	}

	@Override
	public void draw(Graphics2D g, AnnotatorContext settings)
	{
		g.setColor(settings.getColor());
		g.setStroke(new BasicStroke(settings.getThickness()));

		g.drawLine(start.x, start.y, end.x, end.y);
	}
}
