import rmi.ClientcomInter;
import whiteboard.DShape;
import whiteboard.DShapeModel;
import whiteboard.Whiteboard;

import java.awt.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

public class ClientCom extends UnicastRemoteObject implements ClientcomInter {

    private String name;
    private Startclient ui;
    private Whiteboard whiteboard;
    public ClientCom(String n) throws RemoteException {
        name=n;
    }

    public void tell(String st) throws RemoteException{
        System.out.println(st);
        ui.writeMsg(st);
    }
    public String getName() throws RemoteException{
        return name;
    }

    @Override
    public void addModel(String modelId, DShapeModel model, Color color) throws RemoteException {
        whiteboard.drawModel ( model,color, modelId );
    }

    @Override
    public void moveModel(String modelId, int dx, int dy) throws RemoteException {
        whiteboard.canvas.drawMove ( modelId,dx,dy );
    }
    
    @Override
    public void resizeModel(String modelId, Point p1, Point p2) throws RemoteException
    {
    	whiteboard.canvas.drawresize(modelId, p1, p2);
    }

    public void setGUI(Startclient t){
        ui=t ;
    }

    public void setWhiteboard(Whiteboard whiteboard) {
        this.whiteboard = whiteboard;
    }


}