import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.sun.glass.ui.Window;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JColorChooser;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
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
import javax.swing.event.MenuEvent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class Whiteboard extends JFrame
{
	private int mode;
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
		
		canvas = new Canvas(this);
		canvas.setBounds(79, 0, 486, 416);
		frmBoard.getContentPane().add(canvas);
		
		tableModel = new TableModel();
		table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(380, 400));
		scrollPane.setBounds(79, 414, 486, 103);
		frmBoard.getContentPane().add(scrollPane);
		
		MouseMotionAdapter I1=new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) 
			{
				int x=e.getX();
				int y=e.getY();
				Point p=new Point(x,y);
				DLineModel model = new DLineModel(p,p);
				canvas.recolorShape(Color.BLACK);
				canvas.addShape(model);
				
				canvas.repaint();
			}
            
        };
        MouseMotionAdapter I2=new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) 
			{
				int x=e.getX();
				int y=e.getY();
				
				DRectModel model = new DRectModel(x,y,50,50,Color.WHITE);
				
				canvas.addShape(model);
				
				canvas.repaint();
			}
            
        };
		
		addOval = new JButton("Oval");
		addOval.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		addOval.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				canvas.removeMouseMotionListener(I1);
		        canvas.removeMouseMotionListener(I2);
				DOvalModel model = new DOvalModel(25,25,80,50,Color.BLACK);
				Color color = JColorChooser.showDialog(frmBoard, "Set Color", model.getColor());
				canvas.addShape(model);
				canvas.recolorShape(color);
				canvas.repaint();
			}
		});
		addOval.setBounds(0, 0, 81, 23);
		frmBoard.getContentPane().add(addOval);
		
		addRectangle = new JButton("Rectangle");
		addRectangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				canvas.removeMouseMotionListener(I1);
		        canvas.removeMouseMotionListener(I2);
				DRectModel model = new DRectModel(25,25,50,80,Color.BLACK);
				Color color = JColorChooser.showDialog(frmBoard, "Set Color", model.getColor());
				canvas.addShape(model);
				canvas.recolorShape(color);
				canvas.repaint();
			}
		});
		addRectangle.setFont(new Font("Times New Roman", Font.PLAIN, 10));
		addRectangle.setBounds(0, 40, 81, 23);
		frmBoard.getContentPane().add(addRectangle);
		
		addLine = new JButton("Line");
		addLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				canvas.removeMouseMotionListener(I1);
		        canvas.removeMouseMotionListener(I2);
				Point p1 = new Point(5,5);
				Point p2 = new Point(35,35);
				DLineModel model = new DLineModel(p1,p2);
				Color color = JColorChooser.showDialog(frmBoard, "Set Color", model.getColor());
				canvas.addShape(model);
				canvas.recolorShape(color);
				canvas.repaint();
			}
		});
		addLine.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		addLine.setBounds(0, 82, 81, 23);
		frmBoard.getContentPane().add(addLine);
		
		addText = new JButton("Text");
		addText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				canvas.removeMouseMotionListener(I1);
		        canvas.removeMouseMotionListener(I2);
				SetText st=new SetText(canvas);
				
			}
		});
		addText.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		addText.setBounds(0, 104, 81, 23);
		frmBoard.getContentPane().add(addText);
		
		addCircle = new JButton("Circle");
		addCircle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				canvas.removeMouseMotionListener(I1);
		        canvas.removeMouseMotionListener(I2);
				DOvalModel model = new DOvalModel(25,25,50,50,Color.BLACK);
				Color color = JColorChooser.showDialog(frmBoard, "Set Color", model.getColor());
				canvas.addShape(model);
				canvas.recolorShape(color);
				canvas.repaint();
			}
		});
		addCircle.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		addCircle.setBounds(0, 21, 81, 23);
		frmBoard.getContentPane().add(addCircle);
		
		JButton addSquare = new JButton("Square");
		addSquare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				canvas.removeMouseMotionListener(I1);
		        canvas.removeMouseMotionListener(I2);
				DRectModel model = new DRectModel(25,25,50,50,Color.BLACK);
				Color color = JColorChooser.showDialog(frmBoard, "Set Color", model.getColor());
				canvas.addShape(model);
				canvas.recolorShape(color);
				canvas.repaint();
			}
		});
		addSquare.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		addSquare.setBounds(0, 61, 81, 23);
		frmBoard.getContentPane().add(addSquare);
		
		
		
		
		addPencil = new JButton("Pencil");
		addPencil.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		addPencil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				
						
		        canvas.removeMouseMotionListener(I2);
		        canvas.addMouseMotionListener(I1);
			}
		});
		addPencil.setBounds(0, 126, 81, 23);
		frmBoard.getContentPane().add(addPencil);
		
		addEraser = new JButton("Eraser");
		addEraser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				canvas.removeMouseMotionListener(I1);
		        canvas.addMouseMotionListener(I2);
			}
		});
		addEraser.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		addEraser.setBounds(0, 148, 81, 23);
		frmBoard.getContentPane().add(addEraser);
		
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
				String result = JOptionPane.showInputDialog("File Name", null);
				if (result != null) {
					File f = new File(result);
					open(f);
				}
			}
		});
		mnFile.addSeparator();
		JMenuItem mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);
		mntmSave.addActionListener(new  ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				String result = JOptionPane.showInputDialog("File Name", null);
				if (result != null) {
					File f = new File(result);
					save(f);
				}
			}
		});
		mnFile.addSeparator();
		JMenuItem mntmSaveAs = new JMenuItem("Save As Img");
		mnFile.add(mntmSaveAs);
		mntmSaveAs.addActionListener(new  ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				String result = JOptionPane.showInputDialog("File Name", null);
				if (result != null) {
					File f = new File(result);
					saveImage(f);
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
	
	public int getMode()
	{
		return mode;
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
		try {
			XMLEncoder xmlout =new XMLEncoder(new BufferedOutputStream(new FileOutputStream(file)));

			List<DShape> shapes = canvas.getShapes();
	    	DShapeModel[] models = new DShapeModel[shapes.size()];
	    	
	    	for(int i = 0; i < models.length; i ++){
	    		models[i] = shapes.get(i).getModel();
	    	}
			xmlout.writeObject(models);
			xmlout.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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
		{
			f.createNewFile();
			//System.out.println(123);
		}
		ImageIO.write(image, "jpg",f);
	    }
		catch(Exception e)
		{
			e.printStackTrace();
		}
	
		
	}
	
	public void open(File file){
		try {
			XMLDecoder xmlin = new XMLDecoder(new FileInputStream(file));
			DShapeModel[] models = (DShapeModel[]) xmlin.readObject();
			xmlin.close();
			
			
			clear();
			
			for(int i = 0; i < models.length; i++){
				canvas.addShape(models[i]);
			}
				
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ClassCastException e) {
			e.printStackTrace();
			
		}    	
    }
}
