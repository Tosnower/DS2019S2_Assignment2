import java.awt.Point;
import java.awt.Rectangle; 
 



public class DLineModel extends DShapeModel { 
   
	
	
	
	public DLineModel() { 
        super(); 
        
        this.bounds = new Rectangle(0,0,0,0);
    } 
    
    public DLineModel(Point p1, Point p2){
    	super();
    	
    	this.bounds = new Rectangle(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y);
    	
    }
     
  
    public void moveBy(int dx, int dy) { 
    	
        
    	if((bounds.x + dx<0&&dx<0)||(bounds.x +bounds.width+ dx>541&&dx>0)) dx=0;
		if((bounds.y + dy<0&&dy<0)||(bounds.y +bounds.height+ dy>416&&dy>0)) dy=0;
        super.moveBy(dx, dy); 
        this.bounds = new Rectangle(bounds.x+dx, bounds.y+dy, bounds.width, bounds.height);
    } 
    
    public void setBounds(Rectangle bounds){
		super.setBounds(bounds);
	}
    
    public Rectangle getBounds() { 
        return bounds.getBounds(); 
    } 
     
    
    public void setPoints(Point p1, Point p2) { 
        p1 = new Point(p1); 
        p2 = new Point(p2);
        super.setPoints(p1, p2);
       
    } 
     
    
     
     
    public void resize(Point pivotKnob, Point movingKnob) { 

    	int maxX=bounds.x+bounds.width;
		int minX=bounds.x;
		int maxY=bounds.y+bounds.height;
		int minY=bounds.y;
		if(minX<0||maxX>541||minY<0||maxY>416) return;
        this.bounds=new Rectangle(pivotKnob.x, pivotKnob.y, movingKnob.x - pivotKnob.x, movingKnob.y - pivotKnob.y);
        super.resize(pivotKnob, movingKnob); 
        //this.bounds = super.getBounds();
        
    } 
     
}