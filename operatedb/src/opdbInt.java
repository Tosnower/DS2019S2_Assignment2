import java.util.ArrayList;

public interface opdbInt {
    Boolean createnewboard(String name);
    ArrayList readboardlist();
    void selectboard(String name);
    Boolean insertnewop(String username,String operation);
    Boolean insertnewco(String username,String content);
    ArrayList readboardhistory(String name);
    ArrayList readcontenthistory(String name);

}
