package whiteboard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JPanel;

public class Canvas extends JPanel {
    public int count = 0;
    private DShape selected;
    private Point pivotKnob;
    private Point movingKnob;
    private ConcurrentHashMap <String, DShape> shapes;
    private ArrayList <Point> knobs;
    private int x = 0;
    private int y = 0;
    private Whiteboard board;
    private MouseMotionAdapter dragListener = null;
    private int dragFlag = 0;


    public Canvas(Whiteboard board) {
        this.board = board;
        this.setBackground ( Color.WHITE );
        canvasClicked ();
        this.dragListener = new MouseMotionAdapter () {
            public void mouseDragged(MouseEvent e) {
                if (board.getMode () != 2) {
                    int dx = e.getX () - x;
                    int dy = e.getY () - y;

                    int newX = e.getX ();
                    int newY = e.getY ();
                    if (newX != x || newY != y) {
                        if (selected != null) {
                            selected.moveTo ( newX, newY );

                            board.updateTable ( selected );
                            repaint ();
                            try {
                                board.servercomInter.pubishMoveModel ( selected.getModelId (), newX, newY );
                            } catch (RemoteException ex) {
                                ex.printStackTrace ();
                            }
                        }
                        if (movingKnob != null) {
                            movingKnob.x += dx;
                            movingKnob.y += dy;
                            selected.resize ( pivotKnob, movingKnob );
                            try {
                                board.servercomInter.pubishDistortion ( selected.getModelId (), pivotKnob, movingKnob );
                            } catch (RemoteException ex) {
                                ex.printStackTrace ();
                            }
                        }
                    }
                    x = e.getX ();
                    y = e.getY ();
                }
            }
        };
        drag ();
//			setPreferredSize(new Dimension(800, 400));
        setBackground ( Color.WHITE );
        shapes = new ConcurrentHashMap <> ();
        selected = null;
        movingKnob = null;
        setVisible ( true );

    }


    public void canvasClicked() {
        addMouseListener ( new MouseAdapter () {
            public void mousePressed(MouseEvent e) {
                if (board.getMode () != 2) {
                    Point pt = e.getPoint ();
                    x = e.getX ();
                    y = e.getY ();
                    movingKnob = null;
                    pivotKnob = null;

                    if (selected != null) {
                        if (!selected.getColor ().equals ( Color.WHITE ))
                            getSelection ( pt );

                    }

                    if (movingKnob == null) {
                        selected = null;
                        for (Map.Entry <String, DShape> entry : shapes.entrySet ()) {
//							System.out.println(entry.getKey()+"："+entry.getValue());
                            if (entry.getValue ().containsPoint ( pt ) && !entry.getValue ().getColor ().equals ( Color.WHITE )) {
                                selected = entry.getValue ();
                            }
                        }
//	                    for(int i = 0; i < shapes.size(); i++){
//	                    	if(shapes.get(i).containsPoint(pt)&&!shapes.get(i).getColor().equals(Color.WHITE))
//	                            selected = shapes.get(i);
//	                    }

                    }

                    repaint ();
                }

            }

        } );

    }

    public void drag() {
        if (this.dragFlag == 0) {
            this.addMouseMotionListener ( this.dragListener );
            this.dragFlag=1;
        }

    }

    public void removeDrag(){
        if (this.dragFlag == 1) {
            this.removeMouseMotionListener ( this.dragListener );
            this.dragFlag = 0;
        }
    }

    public void drawMove(String modelId, int x, int y) {
        DShape dShape = shapes.get ( modelId );
        dShape.moveTo ( x, y );
        board.updateTable ( dShape );
        repaint ();
    }

    public void drawDistortion(String modelId, Point pivotKnob, Point movingKnob) {
        DShape dShape = shapes.get ( modelId );
        dShape.resize ( pivotKnob, movingKnob );
        repaint ();
    }

    public void getSelection(Point pt) {

        knobs = selected.getKnobs ();
        for (int i = 0; i < knobs.size (); i++) {
            Rectangle knob = new Rectangle ( knobs.get ( i ).x - 4, knobs.get ( i ).y - 4, 9, 9 );
            if (knob.contains ( pt )) {
                int j = 0;
                movingKnob = new Point ( knobs.get ( i ) );
                if (knobs.size () == 2) {
                    if (i == 0) j = 1;
                    else if (i == 1) j = 0;
                } else {
                    if (i == 0) j = 3;
                    else if (i == 1) j = 2;
                    else if (i == 2) j = 1;
                    else if (i == 3) j = 0;
                }
                pivotKnob = new Point ( knobs.get ( (j) ) );
                break;
            }
        }
    }


    public void recolorShape(Color color) {
        if (selected != null) {
            selected.setColor ( color );
            repaint ();

        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent ( g );

        for (Map.Entry <String, DShape> entry : shapes.entrySet ()) {
//				System.out.println(entry.getKey()+"："+entry.getValue());
            DShape shape = entry.getValue ();
            shape.draw ( g, (selected == shape) );
        }

//	        for(int i = 0; i < shapes.size(); i++){
//	        	DShape shape = shapes.get(i);
//	        	shape.draw(g, (selected == shape));
//	        }

    }

    public String addShape(DShapeModel model, String id) {
        if (id == null) {
            id = Integer.toString ( board.userId ) + Integer.toString ( count++ );
        }
        if (board.getMode () != 2) {
            DShape shape = null;
            if (model instanceof DOvalModel)
                shape = new DOval ( model, id );
            else if (model instanceof DTextModel)
                shape = new DText ( model, id );
            else if (model instanceof DRectModel)
                shape = new DRect ( model, id );
            else if (model instanceof DLineModel)
                shape = new DLine ( model, id );

            shapes.put ( id, shape );
            selected = shape;
            board.add ( shape );

            repaint ();
        }
        return id;
    }

    public void removeShape() {

        if (selected ()) {
            shapes.remove ( selected );
            board.delete ( selected );
            repaint ();
        }

    }


    public DShape getSelected() {
        return selected;
    }

    public ConcurrentHashMap <String, DShape> getShapes() {
        return shapes;
    }

    public void setText(String text) {
        if (selected ()) {
            ((DText) selected).setText ( text );
            repaint ();
        }

    }

    public void setFont(String fontName) {
        if (selected ()) {
            ((DText) selected).setFont ( fontName );
            repaint ();
        }
    }

    public void setNull() {

        shapes.clear ();
        board.clear ();
        repaint ();
    }


    public boolean selected() {
        if (selected != null) return true;
        else return false;
    }

}



