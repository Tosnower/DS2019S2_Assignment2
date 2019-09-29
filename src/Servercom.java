import rmi.ClientcomInter;
import rmi.ServercomInter;
import whiteboard.DShape;
import whiteboard.DShapeModel;

import javax.swing.*;
import java.awt.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Servercom extends UnicastRemoteObject implements ServercomInter {

    private Vector v=new Vector();
    public Servercom() throws RemoteException{}
    private Vector history=new Vector();

    public boolean login(ClientcomInter a) throws RemoteException{
        System.out.println(a.getName() + "  got connected....");
        int allowed =JOptionPane.showConfirmDialog( Startclient.frame, a.getName()+" want to connect, are you allow?");
        if(0==allowed)
        {
            a.tell("You have Connected successfully.");
        }
        else
        {
            a.tell("You are not allowed to connect.");
            return false;
        }
        v.add(a);
        publish(a.getName()+ " has just connected.");


        return true;
    }
    public boolean creatorlogin(ClientcomInter a) throws RemoteException{
        v.add(a);
        publish( " create board successfully.");
        return true;
    }

    public boolean logout(ClientcomInter a) throws RemoteException{
        System.out.println(a.getName() + "  disconnected....");
        a.tell("You have disconnected.");
        v.remove(a);
        publish(a.getName()+ " has just exit.");
        return true;
    }

    public void publish(String s) throws RemoteException{
        System.out.println(s);
        history.add(s);
        for(int i=0;i<v.size();i++){
            try{
                ClientcomInter tmp=(ClientcomInter)v.get(i);
                tmp.tell(s);
            }catch(Exception e){
                //problem with the client not connected.
                //Better to remove it
            }
        }
    }

    public void resumehistory(ClientcomInter a) throws RemoteException
    {
        for(int i=0;i<history.size();i++){
            try{
                a.tell((String) history.get(i));
            }catch(Exception e){
                //problem with the client not connected.
                //Better to remove it
            }
        }

    }


    public void pubishAddModel(String modelId, DShapeModel model, Color color) throws RemoteException {
//        System.out.println(s);
//        history.add(s);
        for(int i=0;i<v.size();i++){
            try{
                ClientcomInter tmp=(ClientcomInter)v.get(i);
                tmp.addModel (modelId, model,color);
            }catch(Exception e){
                //problem with the client not connected.
                //Better to remove it
            }
        }
    }

    @Override
    public void pubishMoveModel(String modelId, int dx, int dy) throws RemoteException {
        for(int i=0;i<v.size();i++){
            try{
                ClientcomInter tmp=(ClientcomInter)v.get(i);
                tmp.moveModel (modelId,dx, dy);
            }catch(Exception e){
                //problem with the client not connected.
                //Better to remove it
            }
        }
    }

    @Override
    public void pubishResizeModel(String modelId, Point p1, Point p2) throws RemoteException
    {
    	for(int i=0;i<v.size();i++){
            try{
                ClientcomInter tmp=(ClientcomInter)v.get(i);
                tmp.resizeModel(modelId, p1, p2);
            }catch(Exception e){
                //problem with the client not connected.
                //Better to remove it
            }
        }
    }
    
    public Vector getConnected() throws RemoteException{
        return v;
    }


}