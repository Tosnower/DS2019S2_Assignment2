package chat;

import util.Document;
import util.httpURLConectionGET;

import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class ClientChat {

    // 定义成员变量

    public static String serverIP;
    public static int serverPort1;
    private final ButtonGroup buttonGroup = new ButtonGroup();
    public Socket socket;
    JFrame frmtcp;
    /**
     * 客户端通信线程
     */
    private ClientThread clientThread;
    private JTextField textServerIP;
    private JTextField textServerPort;
    private JTextArea textAreaRecord;
    private JButton btnSend;
    private JButton btnConnect;
    private JTextField textUsername;
    private JList<String> listUsers;
    private JRadioButton rdbtnBrocast;
    private JRadioButton rdbtnPrivateChat;
    private final ButtonGroup buttonGroup2 = new ButtonGroup();
    private JTextArea textAreaMsg;
    private DefaultListModel<String> modelUsers;
    public JPanel left;
    public JPanel right;
    public static String username;
    public static JPanel whiteboard;
    private JRadioButton rdbtnen;
    private JRadioButton rdbtnch;
    private JButton btnTrans;
    private JButton btnEmoji;
    public ExecutorService threadPool;
    private JTextArea tx;
    /**
     * Launch the application.
     */
//    public static void main(String[] args) {
//        EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                try {
//                    chat.ClientChat window = new chat.ClientChat();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    /**
     * Create the application.
     */
    
	public void logout() {
        try {
        	
            modelUsers.removeAllElements();
            clientThread.isLogged=false;
            //clientThread.stop();
            socket.close();
        }
        catch (IOException e){
            System.out.println("exit failed");
        }

    }

    
    
	public ClientChat(JPanel jPanel, JPanel wb, JTextArea ja, String name, String ip, int port, ExecutorService threadPool) {
		serverIP =ip;
        		//"localhost";        
		username = name;
        serverPort1 = port;
        		//8000;
        whiteboard = wb;
        tx=ja;
        btnConnect=connect;
        try {
            socket = new Socket(serverIP, serverPort1);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Cant Find Server");

        }
        this.threadPool = threadPool;

        initialize(jPanel);
        // 初始化成员变量和随机用户名
        modelUsers = new DefaultListModel<String>();
        listUsers.setModel(modelUsers);

    }

    public static Document Connect(String ip) {
        Document request = new Document();
        request.append("command", "Connect");
        request.append("ip", ip);
        return request;
    }

    public static Document beenpoped() {
        Document request = new Document();
        request.append("command", "beenpoped");
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

    /**
     * Initialize the contents of the frame.
     */
    private void initialize(JPanel jPanel) {
//        frmtcp = new JFrame();
//        frmtcp.setResizable(false);
        // frmtcp.setTitle("TCP\u804A\u5929\u5BA4\u5BA2\u6237\u7AEF");
//        frmtcp.setBounds(100, 100, 715, 476);
//        frmtcp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frmtcp.getContentPane().setLayout(null);

        // btnConnect = new JButton("\u767B\u5F55");
        // btnConnect.setBounds(596, 20, 95, 27);
        // frmtcp.getContentPane().add(btnConnect);
        // 连接按钮事件处理程序

        jPanel.setLayout(new BorderLayout(5, 5));
        left = new JPanel();
        left.setLayout(new BorderLayout(5, 5));

        // left north
        JScrollPane scrollPane = new JScrollPane(); //信息显示
//        scrollPane.setBounds(14, 72, 518, 228);
        textAreaRecord = new JTextArea();
        textAreaRecord.setEditable(false);
        scrollPane.setViewportView(textAreaRecord);
        left.add(scrollPane, BorderLayout.CENTER);

        //left meddle
        JPanel bottom = new JPanel();
        bottom.setLayout(new GridLayout(3, 1, 5, 0));
        bottom.setPreferredSize(new Dimension(300, 150));
        JPanel bottomtop = new JPanel();
        bottomtop.setLayout(new GridLayout(1, 2, 5, 0));
        rdbtnBrocast = new JRadioButton("Send To All");
        buttonGroup.add(rdbtnBrocast);
//        rdbtnBrocast.setBounds(226, 309, 132, 22);
        bottomtop.add(rdbtnBrocast);
        rdbtnPrivateChat = new JRadioButton("Send To One");
        buttonGroup.add(rdbtnPrivateChat);
        rdbtnBrocast.setSelected(true);
        bottomtop.add(rdbtnPrivateChat);
//        rdbtnPrivateChat.setBounds(356, 309, 157, 22);
        bottom.add(bottomtop);

        JPanel bottomButton = new JPanel();
        //加入聊天信息输入框
        JScrollPane scrollPane_1 = new JScrollPane();   //信息输入框
//        scrollPane_1.setBounds(14, 332, 518, 73);
        bottom.add(scrollPane_1);
        textAreaMsg = new JTextArea();
        scrollPane_1.setViewportView(textAreaMsg);
         
        //添加button
        rdbtnen = new JRadioButton("en");
        rdbtnch = new JRadioButton("ch");
        rdbtnch.setSelected(true);
        buttonGroup2.add(rdbtnen);
        buttonGroup2.add(rdbtnch);
        JPanel buttonG = new JPanel (  );
        buttonG.setLayout ( new BorderLayout ( 0,0 ) );
        buttonG.add ( rdbtnen, BorderLayout.NORTH );
        buttonG.add ( rdbtnch, BorderLayout.SOUTH );
        bottomButton.add(buttonG);
        btnTrans = new JButton("Trans(en->ch)");
        btnTrans.setEnabled(true);
        btnTrans.setPreferredSize ( new Dimension ( 100,20 ) );
        bottomButton.add(btnTrans);

        btnSend = new JButton("Send");
        btnSend.setEnabled(true);
        btnSend.setPreferredSize ( new Dimension ( 50,20 ) );
        bottomButton.add(btnSend);
//        btnSend.setBounds(444, 409, 88, 27);
        btnEmoji = new JButton("Emoji");
        btnEmoji.setEnabled(true);
        btnEmoji.setPreferredSize ( new Dimension ( 50,20 ) );
        bottomButton.add(btnEmoji);
        bottom.add(bottomButton);
        JTable table;
        //定义二维数组作为表格数据
        Object[][] tableData =
                {
                        new Object[]{"(▼ _ ▼)" , " ┑(￣Д ￣)┍ " , "↖(▔＾▔)↗"},
                        new Object[]{" (●′ω`●) ", "（●>∀<●）" , " (≥﹏ ≤)"},
                        new Object[]{" °(°ˊДˋ°) °", " (つ﹏⊂) " , "  (￣ε ￣) "},
                        new Object[]{" (◍'౪`◍)ﾉﾞ", " (๑´ڡ`๑) " , "ℰ⋆‿⋆ℰ "},
                        new Object[]{"╰(*´︶`*)╯" , "(；′⌒`)" , "(/ω＼) "}
                };
        //定义一维数据作为列标题
        Object[] columnTitle = {"", "", ""};
        JWindow jWindow = new JWindow();
        //以二维数组和一维数组来创建一个JTable对象
        table = new JTable(tableData, columnTitle);
        jWindow.add(table);
        jWindow.setSize(300, 250);

        table.getTableHeader().setVisible(false);
        table.setSelectionBackground(new Color(24, 242, 255));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        table.setFont(new Font("Menu.font", Font.PLAIN, 20));
        table.setRowHeight(50);
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, r);
        //FitTableColumns(table);
        table.setEnabled(false);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int row = ((JTable) e.getSource()).rowAtPoint(e.getPoint()); //获得行位置
                int col = ((JTable) e.getSource()).columnAtPoint(e.getPoint()); //获得列位置
                String cellVal = (String) (table.getValueAt(row, col)); //获得点击单元格数据
                jWindow.setVisible(false);
                textAreaMsg.setText(textAreaMsg.getText() + cellVal);
            }
        });

        btnEmoji.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jWindow.isVisible()) {
                    jWindow.setVisible(false);
                } else {
                    jWindow.setVisible(true);
                }
            }
        });

        rdbtnch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnTrans.setText("Trans(en->ch)");

            }
        });

        rdbtnen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnTrans.setText("Trans(ch->en)");

            }
        });
        btnTrans.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String out;

                boolean containEmoji=false;
                for(int i =0; i < 5; i++){

                    if(containEmoji){
                        break;
                    }
                    for(int j =0; j<3;j++){

                        //System.out.println(tableData[i][j].toString());
                        if(containEmoji){
                            break;
                        }
                        if(textAreaMsg.getText().contains(tableData[i][j].toString())){
                            containEmoji=true;
                        }
                    }
                }

                if(containEmoji){

                    JOptionPane.showMessageDialog(null, "Translation messages cannot contain emoticons");


                }else{
                    //if (rdbtnen.isSelected()){
                    //    out =  httpURLConectionGET.Translate("ch","en",textAreaMsg.getText());
                    //}else{
                    //    out =  httpURLConectionGET.Translate("en","ch",textAreaMsg.getText());
                    //}
                    //if(out.equals("error")){
                    //    JOptionPane.showMessageDialog(null, "Translation ERROR");

                    //}else{
                    //    textAreaMsg.setText(out);
                    //}
                	TransThread tt=new TransThread();
                	tt.init(rdbtnen, rdbtnch, textAreaMsg);
                	tt.start();
                }

            }
        });

        btnSend.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (null != clientThread)
                    clientThread.sendChatMag();
            }
        });

        left.add(bottom, BorderLayout.SOUTH);

        jPanel.add(left, BorderLayout.CENTER);

        //right
        right = new JPanel();
        JScrollPane scrollPane_2 = new JScrollPane();
//        scrollPane_2.setBounds(0, 0, 149, 334);
        scrollPane_2.setPreferredSize(new Dimension(100, 500));
        right.add(scrollPane_2);
        listUsers = new JList<String>();
        listUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane_2.setViewportView(listUsers);
        jPanel.add(right, BorderLayout.EAST);

        jWindow.setVisible(false);

        jPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                super.componentMoved(e);
                Point xy = btnEmoji.getLocationOnScreen();
                jWindow.setLocation((int) xy.getX() + 25, (int) xy.getY() + 25);

            }
        });

        clientThread = new ClientThread();
        threadPool.execute ( clientThread );
//        clientThread.start();

    }

    /**
     * 添加消息到文本框textAreaRecord
     *
     * @param msg，要添加的消息
     */
    private void addMsg(String msg) {
        // 在文本区中添加一条消息，并加上换行
        textAreaRecord.append(msg + "\n");
        // 自动滚动到文本区的最后一行
        textAreaRecord.setCaretPosition(textAreaRecord.getText().length());
    }

    class ClientThread extends Thread {


        /**
         * 基本数据输入流
         */
        private DataInputStream dis;

        /**
         * 基本数据输出流
         */
        private DataOutputStream dos;

        /**
         * 是否登录
         */
        private boolean isLogged;

        /**
         * 连接服务器并登录
         */


        void sendChatMag() {
            String msgChat = null;

            if(textAreaMsg.getText().length()==0){

                JOptionPane.showMessageDialog(null, "enter something to send");

            }
            if (rdbtnBrocast.isSelected()) {

                // msgChat="TALKTO_ALL#"+textAreaMsg.getText();
                msgChat = Send_All(textAreaMsg.getText()).toJson();
                addMsg("I Say:" + textAreaMsg.getText());
                textAreaMsg.setText("");

            }

            if (rdbtnPrivateChat.isSelected()) {

                String toUsername = listUsers.getSelectedValue();

                if (toUsername == null) {

                    JOptionPane.showMessageDialog(null, "Please Select a person");

                }
                else if(toUsername.equals(username))
                {
                    JOptionPane.showMessageDialog(null, "You cannot sent something to yourself");
                }

                else {
                    // msgChat="TALKTO#"+toUsername+"#"+textAreaMsg.getText();
                    msgChat = Send_One(textAreaMsg.getText(), toUsername).toJson();
                    addMsg("From me To " + toUsername + " :" + textAreaMsg.getText());
                    System.out.println(msgChat);
                    textAreaMsg.setText("");

                }


            }


            if (null != msgChat) {
                try {
                    dos.writeUTF(msgChat);
                    dos.flush();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }


//        void logout()
//        {
//            try {
//
//
//              //  String msgLogout="LOGOUT";
//                String myIp = InetAddress.getLocalHost().getHostAddress();
//                int myPort = socket.getLocalPort();
//                String send_str = myIp+":"+myPort;
//                String loginStr = Login_Out(send_str).toJson();
//
//                dos.writeUTF(loginStr);
//                dos.flush();
//                isLogged=false;
//                socket.close();
//                modelUsers.clear();
//                btnSend.setEnabled(false);
//                btnConnect.setText("登录");
//                addMsg("已经退出聊天室");
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//


        public void login() {


            // 连接服务器，获取套接字IO流
            try {
                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());
                String myIp = InetAddress.getLocalHost().getHostAddress();
                int myPort = socket.getLocalPort();
                String send_str = username;
//                String send_str = myIp + ":" + myPort;
                // 获取用户名，构建、发送登录报文

                String loginStr = Connect(send_str).toJson();
                // String msgLogin = "LOGIN#" + username;

                dos.writeUTF(loginStr);
                dos.flush();
                // 读取服务器返回的信息，判断是否登录成功
                String response = dis.readUTF();
                System.out.println(response);
                Document getresponse = Document.parse(response);
                String command = getresponse.getString("command");
                // 登录失败
                if (command.equals("Login_False")) {
                    addMsg("Login Fail");
                    // 登录失败，断开连接，结束客户端线程
                    isLogged = false;
                    socket.close();
                    return;
                }
                // 登录成功
                if (command.equals("Login_Success")) {
                    addMsg("Login Successful");
                    isLogged = true;
                    //btnConnect.setText("退出");
                    btnSend.setEnabled(true);
                }
            } catch (IOException | NullPointerException e2) {

                addMsg("There has some exception, Please Check Server");

                //e.printStackTrace();
            }


        }

        @Override
        public void run() {
            // 连接服务器并登录
            login();

            while (isLogged) {


                try {
                    String msg = dis.readUTF();
                    //String[] parts = msg.split("#");
                    Document getresponse = Document.parse(msg);
                    String command = getresponse.getString("command");


                    System.out.println(msg);

                    switch (command) {
                        // 处理服务器发来的用户列表报文

                        case "User_List":
                            System.out.println("In userlist");
                            ArrayList<String> revicewordname = (ArrayList<String>) getresponse.get("User_List");

                            System.out.println(revicewordname.toString());
                            modelUsers.removeAllElements();
                            for (int i = 0; i < revicewordname.size(); i++) {
                                modelUsers.addElement(revicewordname.get(i));
                            }

                            break;
                        // 处理服务器发来的新用户登录表报文
                        case "New_Login":
                            String getnewUser = (String) getresponse.get("New_Login");

                            modelUsers.addElement(getnewUser);
                            break;

                        case "Login_out":
                            getnewUser = (String) getresponse.get("Login_out");
                            modelUsers.removeElement(getnewUser);
                            break;

                        case "Send_All":
                            getnewUser = (String) getresponse.get("sendname");
                            String getMsg = (String) getresponse.get("text");

                            addMsg("(" + getnewUser + ")To EveryOne: " + getMsg);
                            break;

                        case "Send_One":
                            getnewUser = (String) getresponse.get("user");
                            getMsg = (String) getresponse.get("text");

                            addMsg("(" + getnewUser + ")To Me: " + getMsg);
                            break;

                        case "beenpoped":
                            
                            getnewUser = (String) getresponse.get("user");
                            getMsg = (String) getresponse.get("text");
                            btnSend.setEnabled(false);
                            btnTrans.setEnabled(false);
                            btnEmoji.setEnabled(false);
                            rdbtnen.setEnabled(false);
                            rdbtnch.setEnabled(false);
                            rdbtnBrocast.setEnabled(false);
                            rdbtnPrivateChat.setEnabled(false);
                            textAreaMsg.setEnabled(false);
                            whiteboard.removeAll();
                            tx.setVisible(false);
                            left.setVisible(false);
                            right.setVisible(false);
                            modelUsers.removeAllElements();
                            btnConnect.setText("connect");
                            //addMsg("(" + getnewUser + ")To Me: " + getMsg);
                            JOptionPane.showMessageDialog(null, "You have been poped up by manager!");
                            
                            //System.exit(1);
                            return;

                        case "Send_Fail":

                            addMsg("(System)To Me: Not Found Users");
                            break;
                        default:
                            break;
                    }
                } catch (IOException e) {
                    // TODO 处理异常
                    if(isLogged)
                    {
                    	JOptionPane.showMessageDialog(null, "The manager has left the chat room!");
                    	System.exit(1);
                    }
                    isLogged = false;
                    
                }
            }
        }

    }

}
