package whiteboard;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class BoardThread extends Thread
{
	private Canvas canvas;
	private int action;
	private File file; 
	BoardThread()
	{
		
	}
	public void run()
	{
		if(action==1) save(file);
		else if(action==2) open(file);
		else if(action==3) saveImage(file);
	}
	public void init(int a,File f,Canvas c)
	{
		action=a;
		file=f;
		canvas=c;
	}
	public synchronized void save(File file)
	{
		try 
		{
			XMLEncoder xmlout =new XMLEncoder(new BufferedOutputStream(new FileOutputStream(file)));

			ConcurrentHashMap <String, DShape> shapes = canvas.getShapes();
	    	DShapeModel[] models = new DShapeModel[shapes.size()];

	    	int i=0;
			for(Map.Entry <String, DShape> entry : shapes.entrySet()) {
				System.out.println(entry.getKey()+"："+entry.getValue());
				models[i++] = entry.getValue ().getModel ();
			}
//	    	for(int i = 0; i < models.length; i ++){
//	    		models[i] = shapes.get(i).getModel();
//
//	    	}
	    	
			xmlout.writeObject(models);
			xmlout.close();
			JOptionPane.showMessageDialog(null, "Successfully Save!", "Message", JOptionPane.INFORMATION_MESSAGE);
			
		} 
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public synchronized void saveImage(File file)
	{
		Dimension imagesize = canvas.getSize();
		BufferedImage image = new BufferedImage(imagesize.width,imagesize.height,BufferedImage.TYPE_INT_RGB);	
		Graphics2D graphics = image.createGraphics();
	    canvas.paint(graphics);
	    graphics.dispose();
	    File f=file;
		try
	    {	if( !f.exists() )
				f.createNewFile();
			
			ImageIO.write(image, "jpg",f);
			JOptionPane.showMessageDialog(null, "Successfully Save Image!", "Message", JOptionPane.INFORMATION_MESSAGE);
	    }
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public synchronized void open(File file)
	{
		try 
		{
			XMLDecoder xmlin = new XMLDecoder(new FileInputStream(file));
			DShapeModel[] models = (DShapeModel[]) xmlin.readObject();
			xmlin.close();
			
			
			//clear();
			
			for(int i = 0; i < models.length; i++)
			{
				String id=canvas.addShape(models[i],null);
				canvas.board.servercomInter.pubishAddModel(id, models[i], models[i].getColor());
			}
			JOptionPane.showMessageDialog(null, "Successfully Open!", "Message", JOptionPane.INFORMATION_MESSAGE);	
			
		} 
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.INFORMATION_MESSAGE);
			
		}    	
	}
	
}
