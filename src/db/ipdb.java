package db;
import java.sql.*;
import java.io.*;
import java.util.ArrayList;
public class ipdb implements opdbInt{
    static Connection conn;
    static Statement statement;
    ArrayList<String> boardlist;
    ArrayList<String> currboardhistory;
    ArrayList<String> currcontenthistory;
    String currenttable;

    public ipdb(String filepath){
        File file = new File(filepath);
        if (!file.exists()) {
            System.out.println("file doesn't exist");
            System.exit(-1);
        }
        try {

            conn = DriverManager.getConnection("jdbc:sqlite:" + filepath);
            statement = conn.createStatement();
            statement.setQueryTimeout(10);
        }
        catch (SQLException e){
            System.out.println("the file isn't a sqlite databasefile, please use another");
        }
        finally {
            System.out.println("read file successfully");
        }

    }
    public Boolean createnewboard(String name){
        setcurrcreateboaed(name);
        try {
            //statement.executeUpdate("create table if not exists user (id integer primary key autoincrement,name string not null)");
            statement.executeUpdate("create table if not exists whiteboard_"+currenttable+" (modelid text primary key autoincrement, model_x integer not null, model_y integer not null, model_w integer not null, model_h integer not null, model_color text not null)");
            
            
            //statement.executeUpdate("create table if not exists chathistory_"+currenttable+" (id integer primary key autoincrement,name text not null, content text not null)");
            return true;
        }
        catch (SQLException e){
            System.out.println("create new board database failed");
        }
        return false;
    }

    public ArrayList readboardlist()
    {
        boardlist = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery("select name from sqlite_master where type='table' order by name");
            while (rs.next()) {
                boardlist.add(rs.getString("name"));
            }

        }
        catch (SQLException e){
            System.out.println("cannot read tables");
        }
        return boardlist;
    }

    void setcurrcreateboaed(String name)
    {
        currenttable=name;

    }

    public void selectboard(String name)
    {
        setcurrcreateboaed(name);
    }

    public Boolean insertnewop(String id,String x,String y,String w,String h,String c) {
        try{
        statement.executeUpdate("insert into whiteboard_"+currenttable+"(modelid, model_x, model_y, model_w, model_h, model_color) values('"+id+"', '"+x+"', '"+y+"', '"+w+"', '"+h+"', '"+c+"')");
        }
        catch (SQLException e){
        System.out.println("cannot read board operation history from current table");
    }
        return true;
    }
    
    public Boolean modifyop(String id,String x,String y,String w,String h,String c) {
        try{
        statement.executeUpdate("update whiteboard_"+currenttable+"set model_x='"+x+"', model_y='"+y+"', model_w='"+w+"', model_h='"+h+"', model_color='"+c+"' where modelid='"+id+"'");
        }
        catch (SQLException e){
        System.out.println("cannot read board operation history from current table");
    }
        return true;
    }
    
    public Boolean removeop(String id) {
        try{
        statement.executeUpdate("delete from whiteboard_"+currenttable+" where modelid='"+id+"'");
        }
        catch (SQLException e){
        System.out.println("cannot read board operation history from current table");
    }
        return true;
    }
    

    public Boolean insertnewco(String username,String content)  {
        try{
        statement.executeUpdate("insert into chathistory_"+currenttable+"(userid,content) values('"+username+"', '"+content+ "')");
        }
        catch (SQLException e){
            System.out.println("cannot read board operation history from current table");
        }
        return true;
    }

    public ArrayList readboardhistory(String name)
    {
        currboardhistory = new ArrayList<>();
        setcurrcreateboaed(name);
        try {
            ResultSet rs = statement.executeQuery("select * from whiteboard_"+currenttable);
            while (rs.next()) {
                currboardhistory.add(rs.getString("operation"));
            }

        }
        catch (SQLException e){
            System.out.println("cannot read board operation history from current table");
        }
        return currboardhistory;
    }

    public ArrayList readcontenthistory(String name)
    {
        currcontenthistory = new ArrayList<>();
        setcurrcreateboaed(name);
        try {
            ResultSet rs = statement.executeQuery("select * from chathistory_"+currenttable);
            while (rs.next()) {
                currcontenthistory.add(rs.getString("operation"));
            }

        }
        catch (SQLException e){
            System.out.println("cannot read content history from current table");
        }
        return currcontenthistory;
    }


}
