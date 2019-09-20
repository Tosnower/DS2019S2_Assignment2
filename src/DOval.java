import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


public class DOval extends DShape{
	
	
	 
	public DOval(DShapeModel model) 
	{
		super(model);
		
	}

	public void draw(Graphics g, boolean selected){
		
		Rectangle bounds = model.getBounds(); 
		g.setColor(model.getColor());
		if(!hollow)
			g.fillOval(bounds.x, bounds.y, bounds.width, bounds.height);
		else
			g.drawOval(bounds.x, bounds.y, bounds.width, bounds.height);
		if(selected) drawKnobs(g); 
	
	
	}

	@Override
	public void modelChanged(DShapeModel model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DShapeModel getModel() {
		return (DOvalModel) model; 
	}

}
