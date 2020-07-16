package com.yukthitech.screen.annotator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import com.yukthitech.screen.annotator.shape.IAction;
import com.yukthitech.screen.annotator.shape.LineAction;
import com.yukthitech.screen.annotator.shape.RectAction;
import com.yukthitech.screen.annotator.shape.ZoomAction;

public class DesktopAnnotator
{
	private static Logger logger = LogManager.getLogger(DesktopAnnotator.class);
	
	private Robot robot;

	private JFrame desktopFrame;

	private JPanel contentPane;

	private JPanel drawingPane;

	private Rectangle screenSize;

	private BufferedImage currentSpanshot;

	private DrawingHandler drawingHandler;
	
	private AnnotatorContext context = new AnnotatorContext();
	
	public DesktopAnnotator() throws Exception
	{
		robot = new Robot();

		screenSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

		desktopFrame = new JFrame();

		contentPane = new JPanel()
		{
			private static final long serialVersionUID = 1L;

			public void paint(Graphics g)
			{
				super.paint(g);
				paintContentPane((Graphics2D) g);
			}
		};

		contentPane.setLayout(new BorderLayout());

		drawingPane = new JPanel()
		{
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				paintDrawingPane((Graphics2D) g);
			}
		};

		drawingPane.setOpaque(false);

		desktopFrame.setContentPane(contentPane);
		desktopFrame.setGlassPane(drawingPane);

		desktopFrame.setUndecorated(true);
		desktopFrame.setBounds(screenSize);
		desktopFrame.setVisible(false);

		/*
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		baseCursorIcon = ImageIO.read(DesktopAnnotator.class.getResourceAsStream("/cursor.png"));
		Cursor c = toolkit.createCustomCursor(baseCursorIcon, new Point(0, 0), "img");
		drawingPane.setCursor(c);
		*/

		drawingPane.setBorder(new LineBorder(Color.black, 2));
		drawingPane.setVisible(true);

		drawingHandler = new DrawingHandler(drawingPane, contentPane, context);
	}

	private void paintContentPane(Graphics g)
	{
		if(currentSpanshot == null)
		{
			return;
		}

		if(context.getZoomFactor() <= 1)
		{
			g.drawImage(currentSpanshot, 0, 0, null);
		}
		else
		{
			int zoomFactor = context.getZoomFactor();
			int w = currentSpanshot.getWidth() * zoomFactor, h = currentSpanshot.getHeight() * zoomFactor;
			
			int centerX = w / 2, centerY = h / 2;
			int zoomX = zoomFactor * context.getZoomPoint().x, zoomY = context.getZoomPoint().y;
			
			int finalX = centerX - zoomX;
			int finalY = centerY - zoomY;
			
			finalX = finalX > 0 ? 0 : finalX;
			finalY = finalY > 0 ? 0 : finalY;
					
			g.drawImage(currentSpanshot, finalX, finalX, w, h, null);
		}
	}

	private void paintDrawingPane(Graphics2D g)
	{
		List<IAction> shapes = drawingHandler.getActions();

		for(IAction shape : shapes)
		{
			shape.draw(g, context);
		}
	}

	public void displayAnnotationScreen()
	{
		currentSpanshot = robot.createScreenCapture(screenSize);
		desktopFrame.setVisible(true);
	}
	
	private void displaySettings()
	{
		
	}

	private void registerTrayIcon() throws Exception
	{
		PopupMenu popup = new PopupMenu();
		Image trayImage = ImageIO.read(DesktopAnnotator.class.getResourceAsStream("/annotation.png"));
		
		TrayIcon trayIcon = new TrayIcon(trayImage);
		SystemTray tray = SystemTray.getSystemTray();

		// Create a pop-up menu components
		MenuItem settingsItem = new MenuItem("Settings");
		MenuItem exitItem = new MenuItem("Exit");
		
		settingsItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				displaySettings();
			}
		});
		
		exitItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				logger.debug("Exiting the application");
				System.exit(0);
			}
		});

		// Add components to pop-up menu
		popup.add(settingsItem);
		popup.add(exitItem);

		trayIcon.setPopupMenu(popup);
		tray.add(trayIcon);
	}
	
	private void executeOption(AnnotationOption option)
	{
		logger.debug("Executing option: {}", option);
		
		drawingPane.setCursor(option.getCursor());
		
		switch(option)
		{
			case ESCAPE:
			{
				desktopFrame.setVisible(false);
				break;
			}
			case UNDO:
			{
				drawingHandler.removeLastShape();
				break;
			}
			case RECT:
			{
				displayAnnotationScreen();
				drawingHandler.setCurrentActionType(RectAction.class);
				break;
			}
			case LINE:
			{
				displayAnnotationScreen();
				drawingHandler.setCurrentActionType(LineAction.class);
				break;
			}
			case ZOOM:
			{
				displayAnnotationScreen();
				drawingHandler.setCurrentActionType(ZoomAction.class);
				break;
			}
		}
	}

	public static void main(String[] args) throws Exception
	{
		final DesktopAnnotator annotator = new DesktopAnnotator();
		//annotator.displayAnnotationScreen();
		annotator.registerTrayIcon();
		
		Runtime.getRuntime().addShutdownHook(new Thread() 
		{
			public void run()
			{
				logger.debug("Cleaning up the listeners...");
				JIntellitype.getInstance().cleanUp();
			}
		});
		
		JIntellitype.getInstance().addHotKeyListener(new HotkeyListener() 
		{
			@Override
			public void onHotKey(int identifier) 
			{
				annotator.executeOption(AnnotationOption.getById(identifier));
			}
		});
		
		for(AnnotationOption opt : AnnotationOption.values())
		{
			JIntellitype.getInstance().registerHotKey(opt.getId(), opt.getKeyStroke());
		}
	}
}
