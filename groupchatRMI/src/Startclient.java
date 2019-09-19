import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;
import java.rmi.Naming;
import java.util.*;

public class Startclient {
    private ClientCom client;
    private ServercomInter server;
    public void doConnect(){
        if (connect.getText().equals("Connect")){
            if (name.getText().length()<2){JOptionPane.showMessageDialog(frame, "You need to type a name."); return;}
            if (ip.getText().length()<2){JOptionPane.showMessageDialog(frame, "You need to type an IP."); return;}
            try{
                client=new ClientCom(name.getText());
                client.setGUI(this);
                server=(ServercomInter)Naming.lookup("rmi://"+ip.getText()+"/myabc");
                Boolean loginsuccess = server.login(client);
                if(loginsuccess) {
                    updateUsers(server.getConnected());
                    connect.setText("Disconnect");
                }
            }catch(Exception e){e.printStackTrace();JOptionPane.showMessageDialog(frame, "ERROR, we wouldn't connect....");}
        }else{
            try {
                server.logout(client);
            }
            catch(Exception e){e.printStackTrace();JOptionPane.showMessageDialog(frame, "ERROR, cannot discoonnect bconnect....");}

            updateUsers(null);
            connect.setText("Connect");
            //Better to implement Logout ....
        }
    }
    public void createBoad(){
            try{
                java.rmi.registry.LocateRegistry.createRegistry(1099);
                ServercomInter b=new Servercom();
                Naming.rebind("rmi://localhost/myabc", b);
                System.out.println("[System] Chat Server is ready.");
                try{
                    client=new ClientCom("creator");
                    client.setGUI(this);
                    server=(ServercomInter)Naming.lookup("rmi://"+ip.getText()+"/myabc");
                    Boolean loginsuccess = server.creatorlogin(client);
                    if(loginsuccess) {
                        updateUsers(server.getConnected());
                        connect.setText("Disconnect");
                    }
                }catch(Exception e){e.printStackTrace();JOptionPane.showMessageDialog(frame, "ERROR, we wouldn't create board");}
                createnewboard.setVisible(false);
                top.setVisible(false);

            }catch(Exception e){e.printStackTrace();JOptionPane.showMessageDialog(frame, "ERROR, we cannot create board");}

    }

    public void sendText(){
        if (connect.getText().equals("Connect")){
            JOptionPane.showMessageDialog(frame, "You need to connect first."); return;
        }
        String st=tf.getText();
        st="["+name.getText()+"] "+st;
        tf.setText("");
        //Remove if you are going to implement for remote invocation
        try{
            server.publish(st);
        }catch(Exception e){e.printStackTrace();}
    }

    public void writeMsg(String st){
        try {
            updateUsers(server.getConnected());
        }
        catch(Exception e){e.printStackTrace();JOptionPane.showMessageDialog(frame, "ERROR, updateuserlist");}
        tx.setText(tx.getText()+"\n"+st);  }

    public void updateUsers(Vector v){
        DefaultListModel listModel = new DefaultListModel();
        if(v!=null) for (int i=0;i<v.size();i++){
            try{  String tmp=((ClientcomInter)v.get(i)).getName();
                listModel.addElement(tmp);
            }catch(Exception e){e.printStackTrace();}
        }
        lst.setModel(listModel);
    }

    public static void main(String [] args){
        System.out.println("Hello World !");
        Startclient c=new Startclient();
    }

    //User Interface code.
    public Startclient(){
        frame=new JFrame("commuincation test");
        JPanel main =new JPanel();
        top =new JPanel();
        JPanel cn =new JPanel();
        JPanel bottom =new JPanel();
        ip=new JTextField();
        tf=new JTextField();
        name=new JTextField();
        tx=new JTextArea();
        connect=new JButton("Connect");
        createnewboard =new JButton("createnewboard");
        JButton bt=new JButton("Send");
        lst=new JList();
        main.setLayout(new BorderLayout(5,5));
        top.setLayout(new GridLayout(1,0,5,5));
        cn.setLayout(new BorderLayout(5,5));
        bottom.setLayout(new BorderLayout(5,5));
        top.add(createnewboard);
        top.add(new JLabel("Your name: "));top.add(name);
        top.add(new JLabel("Server Address: "));top.add(ip);
        top.add(connect);
        cn.add(new JScrollPane(tx), BorderLayout.CENTER);
        cn.add(lst, BorderLayout.EAST);
        bottom.add(tf, BorderLayout.CENTER);
        bottom.add(bt, BorderLayout.EAST);
        main.add(top, BorderLayout.NORTH);
        main.add(cn, BorderLayout.CENTER);
        main.add(bottom, BorderLayout.SOUTH);
        main.setBorder(new EmptyBorder(10, 10, 10, 10) );
        //Events
        connect.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){ doConnect();   }  });
        bt.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){ sendText();   }  });
        tf.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){ sendText();   }  });
        createnewboard.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){ createBoad();   }  });
        frame.setContentPane(main);
        frame.setSize(900,600);
        frame.setVisible(true);
    }
    JTextArea tx;
    JTextField tf,ip, name;
    JButton connect;
    JButton createnewboard;
    JList lst;
    JPanel top;
    static public JFrame frame;
}