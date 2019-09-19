import java.rmi.*;
import java.util.*;

public interface ServercomInter extends Remote{
    public boolean login (ClientcomInter a)throws RemoteException ;
    public void publish (String s)throws RemoteException ;
    public Vector getConnected() throws RemoteException ;
    public boolean logout(ClientcomInter a) throws RemoteException;
    public boolean creatorlogin(ClientcomInter a) throws RemoteException;
}
