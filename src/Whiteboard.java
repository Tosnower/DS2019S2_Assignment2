import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.sun.glass.ui.Window;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.event.MenuEvent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

public class Whiteboard extends JFrame
{
	
	private static int normal = 0;
	private static int server = 1;
	private static int client = 2;
	
	private JFrame frmBoard;
	private Canvas canvas; 
	private JScrollPane scrollPane;
	private JButton addOval;
	private JButton addRectangle;
	private JButton addLine;
	private JButton addText;
	private JTable table;
	private TableModel tableModel;
	private JButton addCircle;
	private JButton addPencil;
	private JButton addEraser;
	private JMenu mnSaveAs;
	private JMenu mnOpen;
	private JMenu mnClose;
	/**
	 * Launch the application.
	 */
	private int x1=-999;
	private int x2=-999;
	private int y1=-999;
	private int y2=-999;
	private int locx=-999;
	private int locy=-999;
	private int erasersize=0;
	private int drawoval=0;
	private int drawcircle=0;
	private int drawrect=0;
	private int drawsquare=0;
	private int drawline=0;
	private int drawtext=0;
	public static Color pencilcolor=null;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Whiteboard window = new Whiteboard();
					window.frmBoard.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Whiteboard() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBoard = new JFrame();
		frmBoard.setTitle("Board");
		frmBoard.setBounds(100, 100, 581, 556);
		frmBoard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBoard.getContentPane().setLayout(null);
		frmBoard.setResizable(false);
		
		canvas = new Canvas(this);
		canvas.setBounds(34, 0, 541, 416);
		frmBoard.getContentPane().add(canvas);
		
		tableModel = new TableModel();
		table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(380, 400));
		scrollPane.setBounds(0, 421, 575, 96);
		frmBoard.getContentPane().add(scrollPane);
		
		
		MouseMotionAdapter I1=new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) 
			{
				x2=e.getX();
				y2=e.getY();
				Point p1;
				Point p2;
				if(x2<0) x2=0;
				else if(x2>541) x2=541;
				if(y2<0) y2=0;
				else if(y2>416) y2=416;
				p2=new Point(x2,y2);
				
					
				if(x1!=-999&&y1!=-999)
					p1=new Point(x1,y1);
				else 
					p1=new Point(p2);
				
				if(p1!=p2)
				{
					DLineModel model = new DLineModel(p1,p2);
					canvas.recolorShape(pencilcolor);
					canvas.addShape(model);
					canvas.repaint();
				}
				x1=x2;
				y1=y2;
			}
            
        };
        MouseMotionAdapter I2=new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) 
			{
				int x=e.getX();
				int y=e.getY();
				if(x<0) x=0;
				else if(x+erasersize>541) x=541-erasersize-1;
				if(y<0) y=0;
				else if(y+erasersize>416) y=416-erasersize-1;
				DRectModel model = new DRectModel(x,y,erasersize,erasersize,Color.WHITE);
				
				canvas.addShape(model);
				
				canvas.repaint();
			}
            
        };
        MouseListener I3=new MouseListener(){
        	@Override
        	public void mouseReleased(MouseEvent e)
        	{
        		x1=-999;
        		y1=-999;
        		canvas.removeMouseMotionListener(I1);
		        canvas.removeMouseMotionListener(I2);
		        pencilcolor=null;
        	}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
        	
			
        };
        
        MouseListener I4_Oval=new MouseListener(){
        	@Override
        	public void mouseReleased(MouseEvent e)
        	{
        		
        	}

			@Override
			public void mouseClicked(MouseEvent e) 
			{
				// TODO Auto-generated method stub
				if(drawoval!=0) return;
				drawoval=1;
				locx=e.getX();
				locy=e.getY();
				DOvalModel model = new DOvalModel(locx,locy,80,50,Color.BLACK);
				Color color = JColorChooser.showDialog(frmBoard, "Set Color", model.getColor());
				while(color==null||color.equals(Color.white))
				{
					JOptionPane.showMessageDialog(null, "please choose a valid color!","Error",JOptionPane.INFORMATION_MESSAGE);
					color = JColorChooser.showDialog(frmBoard, "Set Color", model.getColor());
				}
				int ret=JOptionPane.showConfirmDialog(null, "Filled Shape?", "Please choose an option",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
				if(ret==JOptionPane.YES_OPTION)
					model.setHollow(false);
				else
					model.setHollow(true);
				canvas.addShape(model);
				canvas.recolorShape(color);
				canvas.repaint();
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
        	
			
        };
        
        MouseListener I4_Circle=new MouseListener(){
        	@Override
        	public void mouseReleased(MouseEvent e)
        	{
        		
        	}

			@Override
			public void mouseClicked(MouseEvent e) 
			{
				// TODO Auto-generated method stub
				if(drawcircle!=0) return;
				drawcircle=1;
				locx=e.getX();
				locy=e.getY();
				DOvalModel model = new DOvalModel(locx,locy,50,50,Color.BLACK);
				Color color = JColorChooser.showDialog(frmBoard, "Set Color", model.getColor());
				while(color==null||color.equals(Color.white))
				{
					JOptionPane.showMessageDialog(null, "please choose a valid color!","Error",JOptionPane.INFORMATION_MESSAGE);
					color = JColorChooser.showDialog(frmBoard, "Set Color", model.getColor());
				}
				int ret=JOptionPane.showConfirmDialog(null, "Filled Shape?", "Please choose an option",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
				if(ret==JOptionPane.YES_OPTION)
					model.setHollow(false);
				else
					model.setHollow(true);
				canvas.addShape(model);
				canvas.recolorShape(color);
				canvas.repaint();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
        	
			
        };
        
        MouseListener I4_Rect=new MouseListener(){
        	@Override
        	public void mouseReleased(MouseEvent e)
        	{
        		
        	}

			@Override
			public void mouseClicked(MouseEvent e) 
			{
				// TODO Auto-generated method stub
				if(drawrect!=0) return;
				drawrect=1;
				locx=e.getX();
				locy=e.getY();
				DRectModel model = new DRectModel(locx,locy,50,80,Color.BLACK);
				Color color = JColorChooser.showDialog(frmBoard, "Set Color", model.getColor());
				while(color==null||color.equals(Color.white))
				{
					JOptionPane.showMessageDialog(null, "please choose a valid color!","Error",JOptionPane.INFORMATION_MESSAGE);
					color = JColorChooser.showDialog(frmBoard, "Set Color", model.getColor());
				}
				int ret=JOptionPane.showConfirmDialog(null, "Filled Shape?", "Please choose an option",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
				if(ret==JOptionPane.YES_OPTION)
					model.setHollow(false);
				else
					model.setHollow(true);
				canvas.addShape(model);
				canvas.recolorShape(color);
				canvas.repaint();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
        	
			
        };
        
        MouseListener I4_Square=new MouseListener(){
        	@Override
        	public void mouseReleased(MouseEvent e)
        	{
        		
        	}

			@Override
			public void mouseClicked(MouseEvent e) 
			{
				// TODO Auto-generated method stub
				if(drawsquare!=0) return;
				drawsquare=1;
				locx=e.getX();
				locy=e.getY();
				DRectModel model = new DRectModel(locx,locy,50,50,Color.BLACK);
				Color color = JColorChooser.showDialog(frmBoard, "Set Color", model.getColor());
				while(color==null||color.equals(Color.white))
				{
					JOptionPane.showMessageDialog(null, "please choose a valid color!","Error",JOptionPane.INFORMATION_MESSAGE);
					color = JColorChooser.showDialog(frmBoard, "Set Color", model.getColor());
				}
				int ret=JOptionPane.showConfirmDialog(null, "Filled Shape?", "Please choose an option",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
				if(ret==JOptionPane.YES_OPTION)
					model.setHollow(false);
				else
					model.setHollow(true);
				canvas.addShape(model);
				canvas.recolorShape(color);
				canvas.repaint();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
        	
			
        };
        
        MouseListener I4_Line=new MouseListener(){
        	@Override
        	public void mouseReleased(MouseEvent e)
        	{
        		
        	}

			@Override
			public void mouseClicked(MouseEvent e) 
			{
				// TODO Auto-generated method stub
				if(drawline!=0) return;
				drawline=1;
				locx=e.getX();
				locy=e.getY();
				Point p1 = new Point(locx-15,locy-15);
				Point p2 = new Point(locx+15,locy+15);
				DLineModel model = new DLineModel(p1,p2);
				Color color = JColorChooser.showDialog(frmBoard, "Set Color", model.getColor());
				while(color==null||color.equals(Color.white))
				{
					JOptionPane.showMessageDialog(null, "please choose a valid color!","Error",JOptionPane.INFORMATION_MESSAGE);
					color = JColorChooser.showDialog(frmBoard, "Set Color", model.getColor());
				}
				canvas.addShape(model);
				canvas.recolorShape(color);
				canvas.repaint();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
        	
			
        };

        MouseListener I4_Text=new MouseListener(){
        	@Override
        	public void mouseReleased(MouseEvent e)
        	{
        		
        	}

			@Override
			public void mouseClicked(MouseEvent e) 
			{
				// TODO Auto-generated method stub
				if(drawtext!=0) return;
				drawtext=1;
				locx=e.getX();
				locy=e.getY();
				SetText st=new SetText(canvas,locx,locy);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
        	
			
        };
        
        
        
        
        Icon iconoval=new ImageIcon("img/oval.jpg");
        addOval = new JButton("",iconoval);
		addOval.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		addOval.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				drawoval=0;
				canvas.removeMouseMotionListener(I1);
		        canvas.removeMouseMotionListener(I2);
		        canvas.removeMouseListener(I3);
		        canvas.removeMouseListener(I4_Line);
				canvas.removeMouseListener(I4_Text);
				canvas.removeMouseListener(I4_Square);
				canvas.removeMouseListener(I4_Rect);
				canvas.removeMouseListener(I4_Circle);
				canvas.addMouseListener(I4_Oval);
				
			}
		});
		addOval.setBounds(0, 0, 33, 23);
		frmBoard.getContentPane().add(addOval);
		Icon iconrect=new ImageIcon("img/rect.jpg");
		addRectangle = new JButton("",iconrect);
		addRectangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				drawrect=0;
				canvas.removeMouseMotionListener(I1);
		        canvas.removeMouseMotionListener(I2);
		        canvas.removeMouseListener(I3);
		        canvas.removeMouseListener(I4_Line);
				canvas.removeMouseListener(I4_Text);
				canvas.removeMouseListener(I4_Square);
				canvas.removeMouseListener(I4_Oval);
				canvas.removeMouseListener(I4_Circle);
				canvas.addMouseListener(I4_Rect);
			}
		});
		addRectangle.setFont(new Font("Times New Roman", Font.PLAIN, 10));
		addRectangle.setBounds(0, 40, 33, 23);
		frmBoard.getContentPane().add(addRectangle);
		Icon iconline=new ImageIcon("img/line.jpg");
		addLine = new JButton("",iconline);
		addLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				drawline=0;
				canvas.removeMouseMotionListener(I1);
		        canvas.removeMouseMotionListener(I2);
		        canvas.removeMouseListener(I3);
		        canvas.removeMouseListener(I4_Oval);
				canvas.removeMouseListener(I4_Text);
				canvas.removeMouseListener(I4_Square);
				canvas.removeMouseListener(I4_Rect);
				canvas.removeMouseListener(I4_Circle);
				canvas.addMouseListener(I4_Line);
			}
		});
		addLine.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		addLine.setBounds(0, 82, 33, 23);
		frmBoard.getContentPane().add(addLine);
		Icon icontext=new ImageIcon("img/text.jpg");
		addText = new JButton("",icontext);
		addText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				drawtext=0;
				canvas.removeMouseMotionListener(I1);
		        canvas.removeMouseMotionListener(I2);
		        canvas.removeMouseListener(I3);
		        canvas.removeMouseListener(I4_Line);
				canvas.removeMouseListener(I4_Oval);
				canvas.removeMouseListener(I4_Square);
				canvas.removeMouseListener(I4_Rect);
				canvas.removeMouseListener(I4_Circle);
		        canvas.addMouseListener(I4_Text);
				
				
			}
		});
		addText.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		addText.setBounds(0, 104, 33, 23);
		frmBoard.getContentPane().add(addText);
		Icon iconcircle=new ImageIcon("img/circle.jpg");
		addCircle = new JButton("",iconcircle);
		addCircle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				drawcircle=0;
				canvas.removeMouseMotionListener(I1);
		        canvas.removeMouseMotionListener(I2);
		        canvas.removeMouseListener(I3);
		        canvas.removeMouseListener(I4_Line);
				canvas.removeMouseListener(I4_Text);
				canvas.removeMouseListener(I4_Square);
				canvas.removeMouseListener(I4_Rect);
				canvas.removeMouseListener(I4_Oval);
				canvas.addMouseListener(I4_Circle);
			}
		});
		addCircle.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		addCircle.setBounds(0, 21, 33, 23);
		frmBoard.getContentPane().add(addCircle);
		Icon iconsquare=new ImageIcon("img/square.jpg");
		JButton addSquare = new JButton("",iconsquare);
		addSquare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				drawsquare=0;
				canvas.removeMouseMotionListener(I1);
		        canvas.removeMouseMotionListener(I2);
		        canvas.removeMouseListener(I3);
		        canvas.removeMouseListener(I4_Line);
				canvas.removeMouseListener(I4_Text);
				canvas.removeMouseListener(I4_Oval);
				canvas.removeMouseListener(I4_Rect);
				canvas.removeMouseListener(I4_Circle);
				canvas.addMouseListener(I4_Square);
			}
		});
		addSquare.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		addSquare.setBounds(0, 61, 33, 23);
		frmBoard.getContentPane().add(addSquare);
		
		
		Icon iconpencil=new ImageIcon("img/pencil.jpg");		
		addPencil = new JButton("",iconpencil);
		addPencil.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		addPencil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{		
		        canvas.removeMouseMotionListener(I2);
		        canvas.removeMouseListener(I4_Line);
				canvas.removeMouseListener(I4_Text);
				canvas.removeMouseListener(I4_Oval);
				canvas.removeMouseListener(I4_Rect);
				canvas.removeMouseListener(I4_Circle);
				canvas.removeMouseListener(I4_Square);
				pencilcolor=null;
				pencilcolor = JColorChooser.showDialog(frmBoard, "Set Color", Color.BLACK);
				while(pencilcolor==null||pencilcolor.equals(Color.white))
				{
					JOptionPane.showMessageDialog(null, "please choose a valid color!","Error",JOptionPane.INFORMATION_MESSAGE);
					pencilcolor = JColorChooser.showDialog(frmBoard, "Set Color", Color.BLACK);
				}
		        canvas.addMouseMotionListener(I1);
		        canvas.addMouseListener(I3);
			}
		});
		addPencil.setBounds(0, 125, 33, 23);
		frmBoard.getContentPane().add(addPencil);
		Icon iconeraser=new ImageIcon("img/eraser.jpg");
		addEraser = new JButton("",iconeraser);
		addEraser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				canvas.removeMouseMotionListener(I1);
				canvas.removeMouseListener(I4_Line);
				canvas.removeMouseListener(I4_Text);
				canvas.removeMouseListener(I4_Oval);
				canvas.removeMouseListener(I4_Rect);
				canvas.removeMouseListener(I4_Circle);
				canvas.removeMouseListener(I4_Square);
				Object[] Esizes={"very small","small","medium","big","very big"};
				int ret=JOptionPane.showOptionDialog(null, "Please select the size of eraser", "option", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, Esizes, Esizes[0]);
				switch(ret)
				{
				case 0:
					erasersize=10;
					break;
				case 1:
					erasersize=25;
					break;
				case 2:
					erasersize=50;
					break;
				case 3:
					erasersize=65;
					break;
				case 4:
					erasersize=85;
					break;
				}
					
		        canvas.addMouseMotionListener(I2);
		        canvas.addMouseListener(I3);
			}
		});
		addEraser.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		addEraser.setBounds(0, 146, 33, 23);
		frmBoard.getContentPane().add(addEraser);
		
		Icon iconclear=new ImageIcon("img/clear.jpg");
		JButton clearbtn = new JButton("", iconclear);
		clearbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				canvas.setNull();
	            repaint();
			}
		});
		clearbtn.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		clearbtn.setBounds(0, 167, 33, 23);
		frmBoard.getContentPane().add(clearbtn);
		
		JMenuBar menuBar = new JMenuBar();
		frmBoard.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		//mnFile.addSeparator();
		JMenuItem mntmNew = new JMenuItem("New");
		mnFile.add(mntmNew);
		mntmNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	canvas.setNull();
	            repaint();
            }
        });

		mnFile.addSeparator();
		JMenuItem mntmOpen = new JMenuItem("Open");
		mnFile.add(mntmOpen);
		mntmOpen.addActionListener(new  ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser fc=new JFileChooser();
				fc.setDialogTitle("Open");
				int result = fc.showOpenDialog(frmBoard);

		        if (result == JFileChooser.APPROVE_OPTION) 
		        {
		           
		            File f = fc.getSelectedFile();
		            //open(f);
		            clear();
		            BoardThread bt=new BoardThread();
		            bt.init(2, f,canvas);
		            bt.start();
		        }
			}
		});
		mnFile.addSeparator();
		JMenuItem mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);
		mntmSave.addActionListener(new  ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser fc=new JFileChooser();
				fc.setDialogTitle("Save");
				int result = fc.showSaveDialog(frmBoard);

		        if (result == JFileChooser.APPROVE_OPTION) 
		        {
		           
		            File f = fc.getSelectedFile();
		            //save(f);
		            BoardThread bt=new BoardThread();
		            bt.init(1, f,canvas);
		            bt.start();
		        }
			}
		});
		mnFile.addSeparator();
		JMenuItem mntmSaveAs = new JMenuItem("Save As Img");
		mnFile.add(mntmSaveAs);
		mntmSaveAs.addActionListener(new  ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				
				JFileChooser fc=new JFileChooser();
				fc.setDialogTitle("Save As Image");
				int result = fc.showSaveDialog(frmBoard);

		        if (result == JFileChooser.APPROVE_OPTION) 
		        {
		           
		            File f = fc.getSelectedFile();
		            if(f.getName().matches("^.+\\.jpg$"))
		            {
		            	BoardThread bt=new BoardThread();
		            	bt.init(3, f,canvas);
			            bt.start();
		            }
		            else
		            	JOptionPane.showMessageDialog(null, "only jpg format is accepted!", "Error", JOptionPane.INFORMATION_MESSAGE);
		        }
		
			}
		});
		mnFile.addSeparator();
		JMenuItem mntmClose = new JMenuItem("Close");
		mnFile.add(mntmClose);
		mntmClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	System.exit(0);
            }
        });
		
	}
	
	
	
	public void updateTable(DShape selectedShape) 
	{ 
        table.clearSelection(); 
        if(selectedShape != null) 
        { 
           int index = tableModel.getRow(selectedShape.getModel()); 
            table.setRowSelectionInterval(index, index); 
        } 
    }
	
	public void add(DShape shape) {
		tableModel.add(shape.getModel());
 
	} 
	
	public void delete(DShape shape) {
		tableModel.delete(shape.getModel());
		
	} 
	
	
	public void clear() {
		tableModel.clear();
	} 
	
	public void save(File file){
		try 
		{
			XMLEncoder xmlout =new XMLEncoder(new BufferedOutputStream(new FileOutputStream(file)));

			List<DShape> shapes = canvas.getShapes();
	    	DShapeModel[] models = new DShapeModel[shapes.size()];
	    	
	    	for(int i = 0; i < models.length; i ++){
	    		models[i] = shapes.get(i).getModel();
	    		
	    	}
	    	
			xmlout.writeObject(models);
			xmlout.close();
			JOptionPane.showMessageDialog(null, "Successfully Save!", "Message", JOptionPane.INFORMATION_MESSAGE);
			
		} 
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.INFORMATION_MESSAGE);
		}
    }
	
	public void saveImage(File file) {
		 
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
	
	public void open(File file){
		try 
		{
			XMLDecoder xmlin = new XMLDecoder(new FileInputStream(file));
			DShapeModel[] models = (DShapeModel[]) xmlin.readObject();
			xmlin.close();
			
			
			clear();
			
			for(int i = 0; i < models.length; i++)
			{
				canvas.addShape(models[i]);
			}
			JOptionPane.showMessageDialog(null, "Successfully Open!", "Message", JOptionPane.INFORMATION_MESSAGE);	
			
		} 
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.INFORMATION_MESSAGE);
			
		}    	
    }
}
