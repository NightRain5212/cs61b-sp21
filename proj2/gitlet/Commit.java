package gitlet;

import java.io.Serializable;
import java.util.*;

public class Commit implements Serializable {
    private String message;
    private String author;
    private Date date;
    private List<Commit> parent;
    private HashMap<String, Blob> tracked;

    public List<Commit> getParent() {
        return parent;
    }

    public HashMap<String, Blob> getTracked() {
        return tracked;
    }

    public Commit(String msg, String aut, Date dt, Commit p) {
        message = msg;
        author = aut;
        date = dt;
        parent = new ArrayList<>();
        parent.add(p);
        tracked = new HashMap<>();
        if (parent.get(0) != null) {
            //默认追踪父提交追踪的文件(注意是值传递，不是引用传递)
            for (String filename:parent.get(0).tracked.keySet()) {
                tracked.put(filename, parent.get(0).tracked.get(filename));
            }
        }
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }
}
