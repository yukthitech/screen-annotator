package com.yukthitech.screen.annotator;

import java.awt.Cursor;
import java.util.HashMap;
import java.util.Map;

public enum AnnotationOption 
{
	RECT(1, "CTRL + ALT + R", ICursors.CROSS_HAIR),
	
	LINE(2, "CTRL + ALT + L", ICursors.CROSS_HAIR),
	
	ESCAPE(3, "SHIFT + ESCAPE", ICursors.DEFAULT),
	
	UNDO(4, "CTRL + ALT + BACK_SPACE", ICursors.DEFAULT),
	
	ZOOM(5, "CTRL + ALT + Z", ICursors.ZOOM)
	
	;
	
	private static Map<Integer, AnnotationOption> idToOption;
	
	private int id;
	
	private String keyStroke;
	
	private Cursor cursor;
	
	private AnnotationOption(int id, String keyStroke, Cursor cursor)
	{
		this.id = id;
		this.keyStroke = keyStroke;
		this.cursor = cursor;
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
