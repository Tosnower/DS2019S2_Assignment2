
	
	import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;
	import java.awt.event.MouseAdapter;
	import java.awt.event.MouseEvent;
	import java.awt.event.MouseMotionAdapter;
	import java.util.ArrayList;
	import java.util.LinkedList;
	import java.util.List;
	import java.util.Random;

	import javax.swing.JButton;
	import javax.swing.JColorChooser;
	import javax.swing.JFrame;
	import javax.swing.JPanel;

	public class Canvas extends JPanel {
		
		private DShape selected; 
		private Point pivotKnob; 
		private Point movingKnob; 
		private ArrayList<DShape> shapes;
		private ArrayList<Point> knobs; 
		private int x = 0;
		private int y = 0;
		private Whiteboard board;
		
		
		
		public Canvas(Whiteboard board){
			this.board = board;
			this.setBackground(Color.WHITE);
			canvasClicked();
			drag();
			setPreferredSize(new Dimension(400, 400));
			setBackground(Color.WHITE);
			shapes = new ArrayList<DShape>();
			selected = null; 
			movingKnob = null; 
	        setVisible(true); 
			
		}
		
		
		public void canvasClicked(){	
			addMouseListener( new MouseAdapter() {
	            public void mousePressed(MouseEvent e) {
	            	if(board.getMode() != 2){
	            	Point pt = e.getPoint();
	            	x = e.getX(); 
	                y = e.getY(); 
	                movingKnob = null; 
	                pivotKnob = null; 
	            	
	                if(selected != null) {
	                	
	                		getSelection(pt);
	                	
	                }
	                
	                if(movingKnob == null) { 
	                    selected = null; 
	                    for(int i = 0; i < shapes.size(); i++){
	                    	if(shapes.get(i).containsPoint(pt)) 
	                            selected = shapes.get(i); 
	                    }
	                        
	                }
	                  
	                repaint(); 
	            	}
	            	                   
	            }
	            
	        });
			
		}
		
		public void drag(){	
			addMouseMotionListener( new MouseMotionAdapter() {
	            public void mouseDragged(MouseEvent e) {
	            	if(board.getMode() != 2){
	            	int dx = e.getX()-x;
	            	int dy = e.getY()-y;
	            	x = e.getX();
	            	y = e.getY();
	            	
	            	if(selected != null)
	            	{
	            		
	            			selected.moveBy(dx, dy);
	            			board.updateTable(selected);
		            		repaint();
		            		
	            		
	            		
	            	}
	            	
	            	if(movingKnob != null){
	            		movingKnob.x += dx;
	            		movingKnob.y += dy;
	            		
	            		selected.resize(pivotKnob,movingKnob);
	            		
	            	}
	            }
	            }
	        });
			
		}
		
		public void getSelection(Point pt){
			knobs = selected.getKnobs();
			for(int i = 0; i < knobs.size(); i++){
				Rectangle knob = new Rectangle(knobs.get(i).x - 4, knobs.get(i).y - 4, 9, 9);
	        	if(knob.contains(pt)){
	        		int j = 0;
	        		movingKnob = new Point(knobs.get(i));
	        		if(knobs.size() == 2){
	        			if(i == 0) j = 1;
	        			else if(i == 1) j = 0; 
	        		}
	        		else {
	        			if(i == 0) j = 3;
	        			else if(i == 1) j = 2; 
	        			else if(i == 2) j = 1; 
	        			else if(i == 3) j = 0; 
	        		}
	        		pivotKnob = new Point(knobs.get((j)));
	                break; 
	        	}
			}
		}
		
		
		public void recolorShape(Color color) 
		{
			if (selected != null) 
			{
				selected.setColor(color);
				repaint();
				
			}
		}
		
		protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        
	        for(int i = 0; i < shapes.size(); i++){
	        	DShape shape = shapes.get(i);
	        	shape.draw(g, (selected == shape));
	        }
	        
	    }
		
		public void addShape(DShapeModel model) {
			if(board.getMode() != 2){
			DShape shape = null;
			if (model instanceof DOvalModel)
				shape = new DOval(model);
			else if (model instanceof DTextModel)
				shape = new DText(model);
			else if (model instanceof DRectModel)
				shape = new DRect(model);
			else if (model instanceof DLineModel)
				shape = new DLine(model);
			
			shapes.add(shape);
			selected = shape;
			board.add(shape); 
			
			repaint();
			}
			
		}
		
		public void removeShape() { 
	        
			if(selected()) {
			shapes.remove(selected); 
			board.delete(selected);	
	        repaint();
			}
	    
	    } 
		
		
		
		public DShape getSelected() { 
	        return selected; 
	    } 
		
		public List<DShape> getShapes()
		{
			return shapes;
		}
		
		public void setText(String text) { 
	        if(selected()) {
	            ((DText)selected).setText(text);
	            repaint();
	        }
	        
	    } 
		
		public void setFont(String fontName) { 
	        if(selected()) {
	            ((DText)selected).setFont(fontName); 
	            repaint();
	        }
	    } 
		
		public void setNull(){
			
			shapes.clear();
			board.clear();
			repaint();
		}
		
		
		
		public boolean selected(){
			if (selected != null) return true;
			else return false;
		}
		
	}



