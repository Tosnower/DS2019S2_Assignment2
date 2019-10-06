package db;
import java.util.ArrayList;

public interface opdbInt {
    Boolean createnewboard(String name);
    ArrayList readboardlist();
    void selectboard(String name);
    Boolean insertnewop(String id,String x,String y,String w,String h,String c);
    Boolean modifyop(String id,String x,String y,String w,String h,String c);
    Boolean removeop(String id);
    Boolean insertnewco(String username,String content);
    ArrayList readboardhistory(String name);
    ArrayList readcontenthistory(String name);

}