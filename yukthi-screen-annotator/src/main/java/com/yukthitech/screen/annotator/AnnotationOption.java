package com.yukthitech.screen.annotator;

import java.awt.Cursor;
import java.util.HashMap;
import java.util.Map;

import com.yukthitech.screen.annotator.shape.IAction;
import com.yukthitech.screen.annotator.shape.LineAction;
import com.yukthitech.screen.annotator.shape.OvalAction;
import com.yukthitech.screen.annotator.shape.RectAction;
import com.yukthitech.screen.annotator.shape.ZoomAction;

public enum AnnotationOption 
{
	RECT(1, "CTRL + ALT + R", ICursors.CROSS_HAIR, RectAction.class),
	
	LINE(2, "CTRL + ALT + L", ICursors.CROSS_HAIR, LineAction.class),
	
	ESCAPE(3, "SHIFT + ESCAPE", ICursors.DEFAULT, null),
	
	UNDO(4, "CTRL + ALT + BACK_SPACE", ICursors.DEFAULT, null),
	
	ZOOM(5, "CTRL + ALT + Z", ICursors.DEFAULT, ZoomAction.class),
	
	OVAL(6, "CTRL + ALT + O", ICursors.CROSS_HAIR, OvalAction.class)
	
	;
	
	private static Map<Integer, AnnotationOption> idToOption;
	
	private int id;
	
	private String keyStroke;
	
	private Cursor cursor;
	
	private Class<? extends IAction> actionType;
	
	private AnnotationOption(int id, String keyStroke, Cursor cursor, Class<? extends IAction> actionType)
	{
		this.id = id;
		this.keyStroke = keyStroke;
		this.cursor = cursor;
		this.actionType = actionType;
	}

	public int getId() 
	{
		return id;
	}

	public String getKeyStroke() 
	{
		return keyStroke;
	}
	
	public Cursor getCursor()
	{
		return cursor;
	}
	
	public Class<? extends IAction> getActionType()
	{
		return actionType;
	}
	
	public static AnnotationOption getById(int id)
	{
		if(idToOption == null)
		{
			Map<Integer, AnnotationOption> map = new HashMap<Integer, AnnotationOption>();
			
			for(AnnotationOption opt : AnnotationOption.values())
			{
				map.put(opt.getId(), opt);
			}
			
			idToOption = map;
		}
		
		return idToOption.get(id);
	}
}
