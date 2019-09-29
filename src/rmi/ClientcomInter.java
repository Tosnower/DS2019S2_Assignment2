package rmi;

import whiteboard.DShape;
import whiteboard.DShapeModel;

import java.awt.*;
import java.rmi.*;

public interface ClientcomInter extends Remote{
    public void tell(String name)throws RemoteException ;
    public String getName()throws RemoteException ;
    public void addModel(String modelId, DShapeModel model, Color color)throws RemoteException;
    public void moveModel(String modelId, int dx, int dy) throws RemoteException;
    public void resizeModel(String modelId, Point p1, Point p2) throws RemoteException;
}