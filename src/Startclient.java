import chat.ClientChat;
import chat.ServerChat;
import rmi.ClientcomInter;
import rmi.ServercomInter;
import util.Document;
import whiteboard.Whiteboard;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.rmi.Naming;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Startclient {
    static ClientCom client;
    private ServercomInter server;
    private ClientChat clientChat;
    private ServerChat serverChat;
    private Whiteboard whiteboard;
    public ExecutorService threadPool = Executors.newFixedThreadPool(10);
    Boolean issuper=false;
    public void doConnect(){
        if (connect.getText().equals("Connect")){
            if (name.getText().length()<2){JOptionPane.showMessageDialog(frame, "You need to type a name."); return;}
            if (ip.getText().length()<2){JOptionPane.showMessageDialog(frame, "You need to type an IP."); return;}
            try{
                meddle.setVisible(true);
                client=new ClientCom(name.getText());
                client.setGUI(this);
                server=(ServercomInter)Naming.lookup("rmi://"+ip.getText()+"/myabc");
                Boolean loginsuccess = server.login(client);
                if(loginsuccess) {
                    updateUsers(server.getConnected());

                    connect.setText("Disconnect");
                    name.setEditable(false);
                    ip.setEditable(false);
                    port.setEditable(false);
                    whiteboard = new Whiteboard ( whiteBoard, server, false );
                    //TODO userid 由服务端分配
                    whiteboard.setUserId ( 1 );
                    clientChat = new ClientChat (chat,whiteBoard,tx,name.getText(),ip.getText(),Integer.parseInt(port.getText()), threadPool);
                    client.setWhiteboard ( whiteboard );
                    server.resumemodelhistory(client);
                    clientChat.left.setVisible(true);
                    clientChat.right.setVisible(true);
                    tx.setVisible(true);
                    frame.setSize(1400,600);
                }else {
                    JOptionPane.showMessageDialog(frame, "You are not allowed or cannot connect to the service.");

                }
            }catch(Exception e){e.printStackTrace();JOptionPane.showMessageDialog(frame, "ERROR, we wouldn't connect....");}
        }else{
            try {
            
            	
            	meddle.setVisible(false);
            	tx.setVisible(false);
            	
            	clientChat.logout();
                server.logout(client);
                chat.removeAll();
                whiteBoard.removeAll();
                
                clientChat=null;
                client.setWhiteboard(null);
                tx.setText("");
                name.setEditable(true);
                ip.setEditable(true);
                port.setEditable(true);
                //server.logout(client);
                frame.setSize(1400,100);
                
            }
            catch(Exception e){e.printStackTrace();JOptionPane.showMessageDialog(frame, "ERROR, cannot discoonnect bconnect....");}

            updateUsers(null);
            connect.setText("Connect");
            //Better to implement Logout ....
        }
    }

    public void createBoad(){
        try{
            if (name.getText().length()<2){JOptionPane.showMessageDialog(frame, "You need to type a name."); return;}
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            ServercomInter b=new Servercom ();
            Naming.rebind("rmi://localhost/myabc", b);
            System.out.println("[System] Chat Server is ready.");

            try{
                client=new ClientCom(name.getText());
                client.setGUI(this);
                server=(ServercomInter)Naming.lookup("rmi://"+ip.getText()+"/myabc");
                Boolean loginsuccess = server.creatorlogin(client);
                if(loginsuccess) {
                    updateUsers(server.getConnected());
                    connect.setText("Disconnect");
                    whiteboard = new Whiteboard ( whiteBoard, server, true );
                    whiteboard.setUserId ( 0 );
                    serverChat = new ServerChat (chat,name.getText(),Integer.parseInt(port.getText()), threadPool);
                    client.setWhiteboard ( whiteboard );
                }
                frame.setSize(1400,600);
                meddle.setVisible(true);
            }catch(Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "ERROR, we wouldn't create board");
            }
            issuper=true;
            createnewboard.setVisible(false);
            top.setVisible(false);
        }catch(Exception e){e.printStackTrace();JOptionPane.showMessageDialog(frame, "ERROR, we cannot create board");}

    }

    public void sendText(){
        if (connect.getText().equals("Connect")){
            JOptionPane.showMessageDialog(frame, "You need to connect first."); return;
        }
        String st=tf.getText();
        try{
            st="["+client.getName()+"] "+st;
        }catch(Exception e){e.printStackTrace();}
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
        catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "ERROR, updateuserlist");
        }
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间 
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();// 获取当前时间 
        st=sdf.format(date)+"  "+st; // 输出已经格式化的现在时间（24小时制） 
        if(tx.getText().length()!=0)
        	tx.setText(tx.getText()+"\n"+st);
        else
        	tx.setText(st);
        
    }

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
        Startclient c=new Startclient();
        
    }
    

    //User Interface code.
    public Startclient(){
    	
        frame=new JFrame("WhiteBoard");
        main =new JPanel();

        top =new JPanel();
        meddle = new JPanel (  );
        bottom =new JPanel();

        whiteBoard = new JPanel ();
        cn =new JPanel();

        ip=new JTextField("127.0.0.1");
        tf=new JTextField();
        name=new JTextField("anomousy");
        port=new JTextField("8000");
        tx=new JTextArea();
        tx.setEditable(false);
        tx.setBackground(frame.getBackground());
        JScrollPane jp = new JScrollPane(tx);  
        // 设置垂直滚动条  
        jp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);  
        // 设置水平滚动条  
        jp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);  
        
        connect=new JButton("Connect");
        createnewboard =new JButton("createnewboard");
        JButton bt=new JButton("Send");
        lst=new JList();
        chat = new JPanel ();



        top.setLayout(new GridLayout(1,0,5,5));
        top.add(createnewboard);
        top.add(new JLabel("Your name: "));
        top.add(name);
        top.add(new JLabel("Server Address: "));
        top.add(ip);
        top.add(new JLabel("Port Number: "));
        top.add(port);
        top.add(connect);
        


        whiteBoard.setLayout ( new BorderLayout(0,0) );
        whiteBoard.setPreferredSize ( new Dimension ( 1200, 600 ) );
        cn.setLayout(new BorderLayout(5,5));
//        cn.add(new JScrollPane(tx), BorderLayout.CENTER);
//        cn.add(lst, BorderLayout.EAST);
        cn.add ( chat, BorderLayout.WEST);
        bottom.setLayout(new BorderLayout(10,10));
        //bottom.add(tx, BorderLayout.CENTER);
        bottom.add(jp, BorderLayout.CENTER);
        bottom.setPreferredSize(new Dimension(100,100));
        bottom.setVisible(true);
        
        meddle.setLayout(new BorderLayout ( 5,0 ));
        meddle.add ( whiteBoard, BorderLayout.CENTER);
        meddle.add ( cn, BorderLayout.EAST );
        meddle.add(bottom,BorderLayout.SOUTH);
        meddle.setVisible(false);

        
//        bottom.add(tf, BorderLayout.CENTER);
//        bottom.add(bt, BorderLayout.EAST);

        main.setLayout(new BorderLayout(0,0));
        main.add(top, BorderLayout.NORTH);
        main.add ( meddle, BorderLayout.CENTER );
//        main.add(cn, BorderLayout.CENTER);
//        main.add(bottom, BorderLayout.SOUTH);
        main.setBorder(new EmptyBorder(10, 5, 10, 10) );
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
        frame.setSize(1400,100);
        frame.setVisible(true);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
 
 
        public void windowClosing(WindowEvent e) {
        		super.windowClosing(e);
        		try{
            	clientChat.logout();
                server.logout(client);             
        		}
        		catch(Exception e1){}
        		
        		System.exit(0);

        	}
 
        }); 
    }
    JTextArea tx;
    JTextField tf,ip, name,port;
    JButton connect;
    JButton createnewboard;
    JList lst;
    JPanel top;
    JPanel meddle;
    JPanel cn;
    JPanel bottom;
    JPanel whiteBoard;
    JPanel main;
    JPanel chat;
    static public JFrame frame;
}

