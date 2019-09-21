import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class DShapeModel {
	
	private int x;
	private int y;
	private int width;
	private int height;
	//protected Point p1, p2;
	protected Color color;
	protected Rectangle bounds;
	private boolean hollow=false;
	
	public DShapeModel(int x, int y, int width, int height, Color color) {
		this.bounds = new Rectangle(x, y, width, height);
		this.color = color;

	}
	
	public DShapeModel(int x, int y) { 
        this(x, y, 0, 0, Color.gray); 
    } 
	
	public DShapeModel() { 
		this.bounds = new Rectangle(0, 0, 0, 0);
		this.color = Color.gray; 
    } 
	
	public boolean getHollow()
	{
		return hollow;
	}
	
	public void setHollow(boolean h)
	{
		hollow=h;
	}
	
	public Rectangle getBounds(){
		return bounds;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public Color getColor(){
		if(color == null){
			return Color.gray;
		}
		return color;
		
	}
	
	public void setX(int x){
		bounds.x = x;
	
	}

	public void setY(int y){
		bounds.y = y;
		
	}
	
	public void setWidth(int width){
		bounds.width = width;
		
	}
	
	public void setHeight(int height){
		bounds.height = height;
		
	}
	
	public void setColor(Color c){
		this.color = c;
		
	}
	
	public void setBounds(int x, int y, int width, int height) { 
        this.bounds = new Rectangle(x, y, width, height); 
       
    }
	
	public void setBounds(Rectangle bounds){
		
		this.bounds = bounds;
		
	}
	
	public void setLocation(int x, int y) { 
		bounds.x = x;
		bounds.y = y;
    } 
	
	public void moveBy(int dx, int dy){
		bounds.x += dx;
		bounds.y += dy;
	}
	
	public void setPoints(Point p1, Point p2) { 
		int x, y;
		
		if (p1.x < p2.x) {
			x = p1.x;
		} else {
			x = p2.x;
		}
		if (p1.y < p2.y) {
			y = p1.y;
		} else {
			y = p2.y;
		}
		
		int width = Math.abs(p1.x - p2.x); 
		int height = Math.abs(p1.y - p2.y); 

		setBounds(new Rectangle(x, y, width, height));
	}
	
	public void resize(Point pivotKnob, Point movingKnob) {
		
		
		
		if(pivotKnob.x < movingKnob.x){
			x = pivotKnob.x;
		}
		else x = movingKnob.x;
		if(pivotKnob.y < movingKnob.y){
			y = pivotKnob.y;
		}
		else y = movingKnob.y;
		if(this.bounds.getHeight()!=this.bounds.getWidth())
        {
			int width = Math.abs(pivotKnob.x - movingKnob.x); 
			int height = Math.abs(pivotKnob.y - movingKnob.y); 
			if(width==height) width=height+1;
			setBounds(x, y, width, height); 
        }
		else
		{
			int width = Math.abs(pivotKnob.x - movingKnob.x); 
			setBounds(x, y, width, width);
		}
    
	}
	
	public String toString(){
		return "Shape is " + width + " by " + height + " and located at " + x + " " + y;
	}

	

}
