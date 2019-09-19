import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;


public class DRect extends DShape{
	
	
	public DRect(DShapeModel model) {
		super(model);
		
	}
	
	public DRectModel getModel() { 
        return (DRectModel) model; 
    } 

	public void draw(Graphics g, boolean selected){
		
		Rectangle bounds = model.getBounds(); 
		g.setColor(model.getColor());
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
		if(selected) {
			drawKnobs(g); 
			
		}
		
		
		
	}

	@Override
	public void modelChanged(DShapeModel model) {
		// TODO Auto-generated method stub
		
	}
	
}
