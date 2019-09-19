import java.awt.Point;
import java.awt.Rectangle; 
 



public class DLineModel extends DShapeModel { 
   
	private Point p1, p2;
	protected Rectangle bounds;
	
	public DLineModel() { 
        super(); 
        p1 = new Point(bounds.x, bounds.y); 
        p2 = new Point(bounds.x + bounds.width, bounds.y + bounds.height); 
    } 
    
    public DLineModel(Point p1, Point p2){
    	super();
    	this.p1 = p1;
    	this.p2 = p2;
    	this.bounds = new Rectangle(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y);
    }
     
  
    public void moveBy(int dx, int dy) { 
    	
        p1.x += dx; 
        p1.y += dy; 
         
        p2.x += dx; 
        p2.y += dy; 
         
        super.moveBy(dx, dy); 
        this.bounds = new Rectangle(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y);
    } 
    
    public void setBounds(Rectangle bounds){
		super.setBounds(bounds);
	}
    
    public Rectangle getBounds() { 
        return bounds.getBounds(); 
    } 
     
    public Point getPoint1() { 
        return p1; 
    } 
     
    public void setPoints(Point p1, Point p2) { 
        p1 = new Point(p1); 
        p2 = new Point(p2);
        super.setPoints(p1, p2);
       
    } 
     
    public Point getPoint2() { 
        return p2; 
    } 
     
     
    public void resize(Point pivotKnob, Point movingKnob) { 
        p1 = new Point(pivotKnob); 
        p2 = new Point(movingKnob); 
        super.resize(pivotKnob, movingKnob); 
        this.bounds = super.getBounds();
        
    } 
     
}