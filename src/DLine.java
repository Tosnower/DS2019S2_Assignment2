import java.awt.*;
import java.util.*; 
 

public class DLine extends DShape { 
 
   
    public DLine(DShapeModel model) { 
        super(model); 
    } 
 

  
    public void draw(Graphics g, boolean selected) { 
        DLineModel model= getModel(); 
        g.setColor(getColor()); 
        
        ((Graphics2D) g).setStroke(new BasicStroke(10));
        g.drawLine(model.getPoint1().x, model.getPoint1().y, model.getPoint2().x, model.getPoint2().y); 
        if(selected) {
        	drawKnobs(g); 
        }
    } 
 
  
    public DLineModel getModel() { 
        return (DLineModel) model; 
    } 
     
    public ArrayList<Point> getKnobs() { 
       
            knobs = new ArrayList<Point>(); 
            DLineModel line = (DLineModel) model; 
            knobs.add(new Point(line.getPoint1())); 
            knobs.add(new Point(line.getPoint2())); 
            needKnobs = false; 
        
        return knobs; 
    }
    
    



	@Override
	public void modelChanged(DShapeModel model) {
		// TODO Auto-generated method stub
		
	} 
 
}