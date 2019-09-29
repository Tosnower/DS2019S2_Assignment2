package chat;

//import sun.tools.jps.Jps;
import util.Document;

import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import static java.lang.Thread.sleep;

public class ServerChat {

    // 瀹氫箟鎴愬憳鍙橀噺
    /**
     * 瀹㈡埛绔�氫俊绾跨▼
     */
    //private ClientThread clientThread;
    public Socket socket;
//    JFrame frmtcp;
    public Thread serverThread;
    private JTextField textServerIP;
    private JTextField textServerPort;
    private JTextArea textAreaRecord;
    private JButton btnSend;
    private JButton btnConnect;
    private JTextField textUsername;
    private JList<String> listUsers;
    private JLabel lblRoomInfo;
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private JRadioButton rdbtnBrocast;
    private JRadioButton rdbtnPrivateChat;
    private JTextArea textAreaMsg;
    private DefaultListModel<String> modelUsers;
    private JPanel left;
    private JPanel right;

    public static String serverIP;
    public static int serverPort1;

    int getport;
    String gethost;
    // 娣诲姞鐢ㄤ簬鍔熻兘瀹炵幇鐨勬垚鍛樺彉閲�
    /**
     * 鏈嶅姟鍣ㄥ鎺ュ瓧
     */
    private ServerSocket server;

    /**
     * 鍒ゆ柇鏈嶅姟鍣ㄦ槸鍚﹀湪杩愯
     */
    private boolean isRunning;

    /**
     * 瀹㈡埛绔槧灏勶紝key -> String锛氬鎴风鍚嶇О锛� value -> ClientHandler锛� 瀹㈡埛绔鐞嗙嚎绋�
     */
    private HashMap<String, ClientHandler> clientHandlerMap = new HashMap<String,ClientHandler>();

    /**
     * Launch the application.
     */
    /**
     * Launch the application.
     */
//    public static void main(String[] args) {
//
//        EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                try {
//
//                    System.out.println( "\uD83D\uDE00");
//                    chat.ServerChat window = new chat.ServerChat();
//
//                    //window.frmtcp.setVisible(true);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }



    public ServerChat(JPanel jPanel) {
        serverIP = "localhost";
        serverPort1 = 8000;
        initialize(jPanel);
        modelUsers = new DefaultListModel<String>();
        listUsers.setModel(modelUsers);
        modelUsers.addElement("Manager");

    }
    /**
     * Initialize the contents of the frame.
     */
    private void  initialize(JPanel jPanel) {

//        frmtcp = new JFrame();
//        frmtcp.setResizable(false);
//        // frmtcp.setTitle("TCP\u804A\u5929\u5BA4\u5BA2\u6237\u7AEF");
//        frmtcp.setBounds(100, 100, 715, 476);
//        frmtcp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frmtcp.getContentPane().setLayout(null);
        jPanel.setLayout ( new BorderLayout (5,5 ));
        left = new JPanel (  );
        left.setLayout ( new BorderLayout ( 5,5 ) );

        // left north
        JScrollPane scrollPane = new JScrollPane(); //淇℃伅鏄剧ず
//        scrollPane.setBounds(14, 72, 518, 228);
        textAreaRecord = new JTextArea();
        textAreaRecord.setEditable(false);
        scrollPane.setViewportView(textAreaRecord);
        left.add(scrollPane,BorderLayout.CENTER);

        //left meddle
        JPanel bottom = new JPanel (  );
        bottom.setLayout ( new GridLayout(3,1,5,5) );
        bottom.setPreferredSize ( new Dimension ( 300,120 ) );
        JPanel bottomtop = new JPanel (  );
        bottomtop.setLayout ( new GridLayout ( 1,2,5,0 ) );
        rdbtnBrocast = new JRadioButton("Send To All");
        buttonGroup.add(rdbtnBrocast);
//        rdbtnBrocast.setBounds(226, 309, 132, 22);
        bottomtop.add( rdbtnBrocast );
        rdbtnPrivateChat = new JRadioButton("Send To One");
        buttonGroup.add(rdbtnPrivateChat);
        rdbtnBrocast.setSelected(true);
        bottomtop.add( rdbtnPrivateChat );
//        rdbtnPrivateChat.setBounds(356, 309, 157, 22);
        bottom.add( bottomtop );
//        left.add(meddle,BorderLayout.SOUTH);

        //left bottom


//        bottom.setPreferredSize ( new Dimension ( 300,150 ) );
        JPanel bottomButton = new JPanel (  );
        //鍔犲叆鑱婂ぉ淇℃伅杈撳叆妗�
        JScrollPane scrollPane_1 = new JScrollPane();   //淇℃伅杈撳叆妗�
//        scrollPane_1.setBounds(14, 332, 518, 73);
        bottom.add ( scrollPane_1 );
        textAreaMsg = new JTextArea();
        scrollPane_1.setViewportView(textAreaMsg);
        //娣诲姞button
        btnSend = new JButton("Send");
        btnSend.setEnabled(true);
        bottomButton.add ( btnSend );
//        btnSend.setBounds(444, 409, 88, 27);
        JButton btnEmoji = new JButton("Emoji");
        btnEmoji.setEnabled(true);
        bottomButton.add ( btnEmoji );
        bottom.add ( bottomButton );
//        btnEmoji.setBounds(344, 409, 88, 27);
//        jPanel.add(btnEmoji);

        // 鍒濆鍖� 琛ㄦ儏鍖�
        JTable table;
        //瀹氫箟浜岀淮鏁扮粍浣滀负琛ㄦ牸鏁版嵁
        Object[][] tableData =
                {
                        new Object[]{"\uD83D\uDE00" , "\uD83D\uDE01" , "\uD83D\uDE02"},
                        new Object[]{"\uD83D\uDE05", "\uD83D\uDE0C" , "\uD83D\uDC91"},
                        new Object[]{"\uD83D\uDE09", "\uD83D\uDE16" , "\uD83D\uDE2D"},
                        new Object[]{"\uD83D\uDE12", "\uD83D\uDE28" , "\uD83D\uDE35"},
                        new Object[]{"\uD83D\uDE1B" , "\uD83D\uDC75" , "\uD83D\uDE20"}
                };
        //瀹氫箟涓�缁存暟鎹綔涓哄垪鏍囬
        Object[] columnTitle = {"" , "" , ""};
        JWindow jWindow = new JWindow();
        //浠ヤ簩缁存暟缁勫拰涓�缁存暟缁勬潵鍒涘缓涓�涓狫Table瀵硅薄
        table = new JTable(tableData , columnTitle);
        jWindow.add(table);
        jWindow.setSize(300,250);

        table.getTableHeader().setVisible(false);
        table.setSelectionBackground(new Color(24, 242, 255));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        table.setFont(new Font("Menu.font", Font.PLAIN, 20));
        table.setRowHeight(50);
        DefaultTableCellRenderer r=new DefaultTableCellRenderer();
        r.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class,r);
        //FitTableColumns(table);
        table.setEnabled(false);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int row =((JTable)e.getSource()).rowAtPoint(e.getPoint()); //鑾峰緱琛屼綅缃�
                int  col=((JTable)e.getSource()).columnAtPoint(e.getPoint()); //鑾峰緱鍒椾綅缃�
                String cellVal=(String)(table.getValueAt(row,col)); //鑾峰緱鐐瑰嚮鍗曞厓鏍兼暟鎹�
                jWindow.setVisible(false);
                textAreaMsg.setText(textAreaMsg.getText()+cellVal);
            }
        });

        btnEmoji.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(jWindow.isVisible()){
                    jWindow.setVisible(false);
                }else{
                    jWindow.setVisible(true);
                }
            }
        });

        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (null != serverThread){

                    String msgChat=null;
                    if(rdbtnBrocast.isSelected()){

                        // msgChat="TALKTO_ALL#"+textAreaMsg.getText();
                        msgChat=Send_All(textAreaMsg.getText(),"Manager").toJson();
                        try {
                            broadcastMsg("Manager", msgChat);
                        } catch (IOException e) {
                            // e.printStackTrace();
                        }
                        addMsg("I Say: "+textAreaMsg.getText());
                        textAreaMsg.setText("");
                    }


                    if(rdbtnPrivateChat.isSelected()){

                        String toUsername= listUsers.getSelectedValue();

                        if (toUsername==null){

                            JOptionPane.showMessageDialog(null, "Please Select a person");

                        }else {

                            // msgChat= Send_One(textAreaMsg.getText(),toUsername).toJson();
                            ClientHandler clientHandler = clientHandlerMap.get(toUsername);
                            if (null != clientHandler) {

                                String msgTalkTo = Send_One(textAreaMsg.getText(), "Manager").toJson();

                                try {
                                    clientHandler.dos.writeUTF(msgTalkTo);
                                    clientHandler.dos.flush();
                                } catch (IOException e) {
                                    //e.printStackTrace();
                                }

                            }
                            addMsg("From me To " + toUsername + " :" + textAreaMsg.getText());
                            textAreaMsg.setText("");
                        }
                    }
                }
            }
        });

        left.add(bottom, BorderLayout.SOUTH);

        jPanel.add ( left, BorderLayout.CENTER );

        //right
        right = new JPanel (  );
        JScrollPane scrollPane_2 = new JScrollPane();
//        scrollPane_2.setBounds(0, 0, 149, 334);
        scrollPane_2.setPreferredSize ( new Dimension ( 100,500 ) );
        right.add ( scrollPane_2 );
        listUsers = new JList<String>();
        listUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane_2.setViewportView(listUsers);
        jPanel.add(right,BorderLayout.EAST);






//         btnConnect = new JButton("\u767B\u5F55");
//         btnConnect.setBounds(596, 20, 95, 27);
//         jPanel.add(btnConnect);
        // 杩炴帴鎸夐挳浜嬩欢澶勭悊绋嬪簭



//        JLabel lblNewLabel_2 = new JLabel("Select Model:");
//        meddle.add ( lblNewLabel_2 );
//        lblNewLabel_2.setBounds(14, 313, 172, 18);







//        JLabel label = new JLabel("Message");
//        label.setBounds(14, 55, 72, 18);
//        jPanel.add(label);






//        JLabel label_2 = new JLabel("Online");
//        label_2.setBounds(543, 55, 72, 18);
//        jPanel.add(label_2);




//        lblRoomInfo = new JLabel("Totol");
//        lblRoomInfo.setBounds(546, 413, 145, 18);
//        jPanel.add(lblRoomInfo);

        // 鏇存崲鏍峰紡
//        try {
//            String style = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
//            UIManager.setLookAndFeel(style);
//            // 鏇存柊绐椾綋鏍峰紡
//            SwingUtilities.updateComponentTreeUI(this.frmtcp);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        frmtcp.setVisible(true);


        jWindow.setVisible(false);

        jPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                super.componentMoved(e);
                Point xy = btnEmoji.getLocationOnScreen();
                jWindow.setLocation((int)xy.getX()+25,(int)xy.getY()+25);

            }
        });
        serverThread = new Thread(new ServerThread()) ;
        serverThread.start();

    }




    class ClientHandler implements Runnable {
        private Socket socket;
        private DataInputStream dis;
        private DataOutputStream dos;
        private boolean isConnected;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            getport = socket.getPort();
            gethost = socket.getLocalAddress().getHostAddress().toString();
            try {

                this.dis = new DataInputStream(socket.getInputStream());
                this.dos = new DataOutputStream(socket.getOutputStream());
                isConnected = true;
            } catch (IOException e) {
                isConnected = false;
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while(isRunning && isConnected) {
                try {
                    // 璇诲彇瀹㈡埛绔彂閫佺殑鎶ユ枃
                    String msg = dis.readUTF();
                    System.out.println(msg);
                    //String[] parts = msg.split("#");


                    Document response = Document.parse(msg);
                    String command = response.getString("command");

                    switch (command) {
                        // 澶勭悊鐧诲綍鎶ユ枃
                        case "Connect":
                            String loginUser =  response.getString("ip");
                            // 濡傛灉璇ョ敤鎴峰悕宸茬櫥褰曪紝鍒欒繑鍥炲け璐ユ姤鏂囷紝鍚﹀垯杩斿洖鎴愬姛鎶ユ枃
                            if(clientHandlerMap.containsKey(loginUser)) {
                                String send_str = Login_False().toJson();
                                dos.writeUTF(send_str);
                            } else {



                                String send_str = Login_Success().toJson();

                                dos.writeUTF(send_str);
                                // 灏嗘瀹㈡埛绔鐞嗙嚎绋嬬殑淇℃伅娣诲姞鍒癱lientHandlerMap涓�
                                clientHandlerMap.put(loginUser, this);
                                // 灏嗙幇鏈夌敤鎴风殑淇℃伅鍙戠粰鏂扮敤鎴�
                                // StringBuffer msgUserList = new StringBuffer();

                                ArrayList<String> userlist = new ArrayList<String>();

                                for(String username : clientHandlerMap.keySet()) {

                                    userlist.add(username);

                                }

                                send_str = User_List(userlist).toJson();


                                System.out.println(send_str);


                                //dos.writeUTF(msgUserList.toString());
                                dos.writeUTF(send_str);


                                // 灏嗘柊鐧诲綍鐨勭敤鎴蜂俊鎭箍鎾粰鍏朵粬鐢ㄦ埛
                                //String msgLogin = loginUser;
                                String msgLogin = New_Login(loginUser).toJson();

                                broadcastMsg(loginUser, msgLogin);
                                // 瀛樺偍鐧诲綍鐨勭敤鎴峰悕
                                this.username = loginUser;
                            }
                            break;

                        case "Send_All":

                            String getMsg =  response.getString("text");

                            //String msgTalkToAll="TALKTO_ALL#"+username+"#"+getMsg;
                            String msgTalkToAll=Send_All(getMsg,username).toJson();
                            System.out.println(msg);
                            //String getMsg = loginUser;
                            addMsg("("+username+") says :"+getMsg);
                            broadcastMsg(username, msgTalkToAll);
                            break;

                        case "Send_One":
                            System.out.println("Send_One");
                            loginUser =  response.getString("user");
                            String text =  response.getString("text");
                            System.out.println("Send_One:"+loginUser);

                            if (loginUser.equals("Manager")){

                                addMsg("("+username+") says :"+text);

                            }else{
                                ClientHandler clientHandler=clientHandlerMap.get(loginUser);
                                if(null!=clientHandler){

                                    //String msgTalkTo="TALKTO#"+username+"#"+ loginUser;
                                    String msgTalkTo = Send_One(text,username).toJson();

                                    try{
                                        clientHandler.dos.writeUTF(msgTalkTo);
                                        clientHandler.dos.flush();

                                    }catch(IOException e){
                                        clientHandlerMap.remove(loginUser);
                                        String send_str = Send_Fail().toJson();
                                        dos.writeUTF(send_str);

                                    }


                                }
                            }


                            break;


                        case "Login_Out":
                            loginUser =  response.getString("ip");
                            clientHandlerMap.remove(username);

                            //String msgLogout="LOGOUT#"+username;
                            String msgLogout = New_Login(loginUser).toJson();

                            broadcastMsg(username, msgLogout);
                            isConnected=false;
                            socket.close();
                            break;

                        default:
                            break;
                    }
                } catch (IOException e) {

                    isConnected = false;
                    clientHandlerMap.remove(username);

                    //e.printStackTrace();
                }
            }
        }

        /**
         * 灏嗘煇涓敤鎴峰彂鏉ョ殑娑堟伅骞挎挱缁欏叾瀹冪敤鎴�
         * @param fromUsername 鍙戞潵娑堟伅鐨勭敤鎴�
         * @param msg 闇�瑕佸箍鎾殑娑堟伅
         */
        private void broadcastMsg(String fromUsername, String msg) throws IOException{
            for(String toUserName : clientHandlerMap.keySet()) {
                if(fromUsername.equals(toUserName) == false) {
                    DataOutputStream dos = clientHandlerMap.get(toUserName).dos;
                    dos.writeUTF(msg);
                    dos.flush();
                }
            }
        }
    }


    /**
     * 娣诲姞娑堟伅鍒版枃鏈textAreaRecord
     *
     * @param msg锛岃娣诲姞鐨勬秷鎭�
     */
    private void addMsg(String msg) {
        // 鍦ㄦ枃鏈尯涓坊鍔犱竴鏉℃秷鎭紝骞跺姞涓婃崲琛�
        textAreaRecord.append(msg + "\n");
        // 鑷姩婊氬姩鍒版枃鏈尯鐨勬渶鍚庝竴琛�
        textAreaRecord.setCaretPosition(textAreaRecord.getText().length());
    }

    public static Document Connect(String ip) {
        Document request = new Document();
        request.append("command", "Connect");
        request.append("ip", ip);
        return request;
    }

    public static Document Login_Out(String ip) {
        Document request = new Document();
        request.append("command", "Login_Out");
        request.append("ip", ip);
        return request;
    }

    public static Document Send_All(String text) {
        Document request = new Document();
        request.append("command", "Send_All");
        request.append("text", text);
        return request;
    }

    public static Document Send_One(String text, String user) {
        Document request = new Document();
        request.append("command", "Send_One");
        request.append("text", text);
        request.append("user", user);
        return request;
    }

    public class ServerThread implements Runnable {

        /**
         * 鍚姩鏈嶅姟
         */
        private void startServer() {
            try {


                // 鍒涘缓濂楁帴瀛楀湴鍧�
                SocketAddress socketAddress = new InetSocketAddress(serverIP, serverPort1);
                // 鍒涘缓ServerSocket锛岀粦瀹氬鎺ュ瓧鍦板潃
                server = new ServerSocket();
                server.bind(socketAddress);
                // 淇敼鍒ゆ柇鏈嶅姟鍣ㄦ槸鍚﹁繍琛岀殑鏍囪瘑鍙橀噺
                isRunning = true;
                // 淇敼鍚姩鍜屽仠姝㈡寜閽姸鎬�
                //btnStart.setEnabled(false);
               // btnStop.setEnabled(true);
                addMsg("Star Server Successful");

                //String myinfo = socket.getLocalAddress()+ ":"+ socket.getLocalPort();

                Thread t = new Thread(() ->Synchronous_Userlist());
                t.start();



            } catch (IOException e) {

                addMsg("Fail, Please Check the port");
                e.printStackTrace();
                isRunning = false;
            }
        }

        /**
         * 绾跨▼浣�
         */
        @Override
        public void run() {
            startServer();
            // 褰撴湇鍔″櫒澶勪簬杩愯鐘舵�佹椂锛屽惊鐜洃鍚鎴风鐨勮繛鎺ヨ姹�
            while(isRunning) {
                try {
                    Socket socket = server.accept();
                    // 鍒涘缓涓庡鎴风浜や簰鐨勭嚎绋�
                    Thread thread = new Thread(new ClientHandler(socket));
                    thread.start();

                } catch (IOException e) {
                    System.out.println("Dont have connect");
                }
            }
        }

    }

    public void Synchronous_Userlist(){

        while(true) {

            try {
                sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            modelUsers.removeAllElements();
            ArrayList<String> userlist = new ArrayList<String>();
            userlist.add("Manager");
            modelUsers.addElement("Manager");
            for (String username : clientHandlerMap.keySet()) {
                userlist.add(username);
                modelUsers.addElement(username);

            }


            String send_str = User_List(userlist).toJson();
            for (String toUserName : clientHandlerMap.keySet()) {
                DataOutputStream dos = clientHandlerMap.get(toUserName).dos;
                try {
                    dos.writeUTF(send_str);
                    dos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    public static Document Login_False() {
        Document request = new Document();
        request.append("command", "Login_False");
        return request;
    }

    public static Document Login_Success() {
        Document request = new Document();
        request.append("command", "Login_Success");
        return request;
    }

    public static Document User_List(ArrayList userlist) {
        Document request = new Document();
        request.append("User_List", userlist);
        request.append("command", "User_List");
        return request;
    }

    public static Document New_Login(String newuser) {
        Document request = new Document();
        request.append("New_Login", newuser);
        request.append("command", "New_Login");
        return request;
    }

    public static Document Login_out(String newuser) {
        Document request = new Document();
        request.append("Login_out", newuser);
        request.append("command", "Login_out");
        return request;
    }

    public static Document Send_Fail() {
        Document request = new Document();
        request.append("command", "Send_Fail");
        return request;
    }

    public static Document Send_All(String text, String sendname) {
        Document request = new Document();
        request.append("command", "Send_All");
        request.append("text", text);
        request.append("sendname", sendname);
        return request;
    }

    /**
     * 灏嗘煇涓敤鎴峰彂鏉ョ殑娑堟伅骞挎挱缁欏叾瀹冪敤鎴�
     * @param fromUsername 鍙戞潵娑堟伅鐨勭敤鎴�
     * @param msg 闇�瑕佸箍鎾殑娑堟伅
     */
    private void broadcastMsg(String fromUsername, String msg) throws IOException{
        for(String toUserName : clientHandlerMap.keySet()) {
            if(fromUsername.equals(toUserName) == false) {
                DataOutputStream dos = clientHandlerMap.get(toUserName).dos;
                dos.writeUTF(msg);
                dos.flush();
            }
        }
    }


}