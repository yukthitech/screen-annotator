package com.yukthitech.screen.annotator.shape;

import java.awt.Graphics2D;

import com.yukthitech.screen.annotator.AnnotatorContext;

public interface IAction
{
	public default void draw(Graphics2D g, AnnotatorContext settings)
	{}
	
	/**
	 * If true is returned, then desktop image also will be adjusted as per factors.
	 * @param x
	 * @param y
	 * @param button
	 * @param context
	 * @return
	 */
	public default boolean finalizeLocation(int x, int y, int button, AnnotatorContext context)
	{
		return false;
	}
	
	public default  void startFrom(int x, int y)
	{}
	
	public default void moveEndPointTo(int x, int y)
	{}
}
