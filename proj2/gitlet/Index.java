package gitlet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static gitlet.Repository.INDEX;
import static gitlet.Utils.*;

public class Index implements Serializable {
    private HashMap<String,Commit> CommitSet;
    private class Node implements Serializable {
        public Commit item;
        public List<Node> parent;
        public Node son;

        public Node(Commit c) {
            item = c;
            parent = new ArrayList<>();
            son = null;
        }
    }

    private Node head;
    private HashMap<String,Node> branches;

    //初始化
    public Index() {
        CommitSet = new HashMap<>();
        branches = new HashMap<>();
        Node master = new Node(null);
        branches.put("master",master);
        head = master;
        this.save();
    }

    //添加提交
    public void add(Commit c) {
        CommitSet.put(_sha1(c),c);
        Node newnode = new Node(c);
        newnode.parent.add(head);
        head.son = newnode;
        head = newnode;
    }

    //保存到文件
    public void save() {
        writeObject(INDEX,this);
    }

    public void load() {
        Index saved = readObject(INDEX,Index.class);
        this.head = saved.head;
        this.branches = saved.branches;
    }

    //获取父提交
    public Commit getParent() {
        if(head.parent.get(0) == null) {
            return null;
        }
        return head.parent.get(0).item;
    }

    //获取当前提交
    public Commit getHead() {
        return head.item;
    }

    public Commit get(String id){
        return CommitSet.get(id);
    }

    public ArrayList<Commit> log() {
        ArrayList<Commit> logs = new ArrayList<>();
        Node ohead = head;
        while (head != null) {
            logs.add(head.item);
            if(!head.parent.isEmpty()){head = head.parent.get(0);}
            if(head.parent.isEmpty()){break;}
        }
        head = ohead;
        return logs;
    }
}
