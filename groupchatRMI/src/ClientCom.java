import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

public class ClientCom extends UnicastRemoteObject implements ClientcomInter {

    private String name;
    private Startclient ui;
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

    public void setGUI(Startclient t){
        ui=t ;
    }

}