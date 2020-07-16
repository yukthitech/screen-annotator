package com.yukthitech.screen.annotator;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yukthitech.screen.annotator.shape.IAction;

public class DrawingHandler implements MouseListener, MouseMotionListener
{
	private static Logger logger = LogManager.getLogger(DrawingHandler.class);
	
	private List<IAction> actions = new LinkedList<IAction>();
	
	private IAction currentAction;

	private JPanel drawingPane;
	
	private JPanel contentPane;
	
	private Class<? extends IAction> currentActionType;
	
	private AnnotatorContext context;
	
	public DrawingHandler(JPanel drawingPane, JPanel contentPane, AnnotatorContext context)
	{
		this.drawingPane = drawingPane;
		this.contentPane = contentPane;
		this.context = context;
		
		contentPane.addMouseListener(this);
		contentPane.addMouseMotionListener(this);
	}

	public void setCurrentActionType(Class<? extends IAction> currentActionType) 
	{
		this.currentActionType = currentActionType;
	}
	
	public List<IAction> getActions()
	{
		return actions;
	}
	
	public void removeLastShape()
	{
		if(actions.isEmpty())
		{
			logger.debug("As no shapes are present, ignoring undo..");
			return;
		}
		
		actions.remove(actions.size() - 1);
		drawingPane.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		try
		{
			currentAction = currentActionType.newInstance();
		}catch(Exception ex)
		{
			logger.error("An error occurred while creating shape of type: " + currentActionType.getName());
			return;
		}
		
		actions.add(currentAction);
		
		currentAction.startFrom(e.getX(), e.getY());
		drawingPane.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		currentAction.moveEndPointTo(e.getX(), e.getY());
		
		
		if(currentAction.finalizeLocation(e.getX(), e.getY(), e.getButton(), context))
		{
			actions.clear();
			contentPane.repaint();
		}
		
		drawingPane.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		currentAction.moveEndPointTo(e.getX(), e.getY());
		drawingPane.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
	}
}
