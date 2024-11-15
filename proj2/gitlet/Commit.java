package gitlet;

import java.io.Serializable;
import java.util.*;

public class Commit implements Serializable {
    private String message;
    private String author;
    private Date date;
    public List<Commit> parent;
    public HashMap<String,Blob> tracked;

    public Commit(String msg,String aut,Date dt,Commit p) {
        message = msg;
        author = aut;
        date = dt;
        parent = new ArrayList<>();
        parent.add(p);
        tracked = new HashMap<>();
        if(parent.get(0) != null) {
            //默认追踪父提交追踪的文件
            tracked = parent.get(0).tracked;
        }
    }

    public String getMessage(){
        return message;
    }

    public Date getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }
}
