import java.awt.*;
import java.util.*; 
 

public class DLine extends DShape { 
 
	public DLine()
	{
		super();
	}
   
    public DLine(DShapeModel model) { 
        super(model); 
    } 
 

  
    public void draw(Graphics g, boolean selected) { 
        DLineModel model= getModel();
        Rectangle bounds = model.getBounds();
        g.setColor(getColor()); 
        
        ((Graphics2D) g).setStroke(new BasicStroke(8));
        //g.drawLine(model.getPoint1().x, model.getPoint1().y, model.getPoint2().x, model.getPoint2().y); 
        g.drawLine(bounds.x, bounds.y, bounds.x+bounds.width, bounds.y+bounds.height);
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
            //knobs.add(new Point(line.getPoint1())); 
            knobs.add(new Point(model.getBounds().x,model.getBounds().y));
            //knobs.add(new Point(line.getPoint2())); 
            knobs.add(new Point(model.getBounds().x+model.getBounds().width, model.getBounds().y+model.getBounds().height));
            needKnobs = false; 
        
        return knobs; 
    }
    
    



	@Override
	public void modelChanged(DShapeModel model) {
		// TODO Auto-generated method stub
		
	} 
 
}