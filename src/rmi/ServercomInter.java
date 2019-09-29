package rmi;

import whiteboard.DShape;
import whiteboard.DShapeModel;

import java.awt.*;
import java.rmi.*;
import java.util.*;

public interface ServercomInter extends Remote{
    public boolean login(ClientcomInter a)throws RemoteException ;
    public void publish(String s)throws RemoteException ;
    public Vector getConnected() throws RemoteException ;
    public boolean logout(ClientcomInter a) throws RemoteException;
    public boolean creatorlogin(ClientcomInter a) throws RemoteException;
    public void resumehistory(ClientcomInter a) throws RemoteException;
    public void pubishAddModel(String modelId, DShapeModel model, Color color) throws RemoteException;
    public void pubishMoveModel(String modelId, int dx, int dy) throws RemoteException;
    public void pubishResizeModel(String modelId, Point p1, Point p2) throws RemoteException;
    public void removeallModel() throws RemoteException;
}
