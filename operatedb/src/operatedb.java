public class operatedb {
    public static void main(String[] args){
        ipdb db = new ipdb("board.db");
        db.createnewboard("first");
        db.readboardlist();
    }
}
