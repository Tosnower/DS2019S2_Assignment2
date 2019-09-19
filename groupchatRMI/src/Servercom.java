import javax.swing.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Servercom extends UnicastRemoteObject implements ServercomInter {

    private Vector v=new Vector();
    public Servercom() throws RemoteException{}

    public boolean login(ClientcomInter a) throws RemoteException{
        System.out.println(a.getName() + "  got connected....");
        int allowed =JOptionPane.showConfirmDialog(Startclient.frame, a.getName()+" want to connect, are you allow?");
        if(0==allowed)
        {
            a.tell("You have Connected successfully.");
        }
        else
        {
            a.tell("You are not allowed to connect.");
            return false;
        }

        publish(a.getName()+ " has just connected.");

        v.add(a);
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
        publish(a.getName()+ " has just exit.");
        v.remove(a);
        return true;
    }

    public void publish(String s) throws RemoteException{
        System.out.println(s);
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


    public Vector getConnected() throws RemoteException{
        return v;
    }
}