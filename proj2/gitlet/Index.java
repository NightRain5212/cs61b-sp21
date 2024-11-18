package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import static gitlet.Repository.*;
import static gitlet.Utils.*;

public class Index implements Serializable {
    private HashMap<String,Commit> CommitSet;
    private HashMap<Commit,String> IdSet;
    private HashMap<String,Node> NodeSet;
    private class Node implements Serializable {
        public Commit item;
        public List<Node> parent;
        public Node son;

        public Node(Commit c) {
            item = c;
            parent = new ArrayList<>();
            son = null;
        }

        public Node getParent() {
            if(this.parent.isEmpty()) {
                return null;
            }
            return this.parent.get(0);
        }

        public Node getsParent() {
            if(this.parent.size() != 2) {
                return null;
            }
            return this.parent.get(1);
        }
    }

    private Node head;
    private String curBranch;
    private HashMap<String,Node> branches;

    //初始化
    public Index() {
        CommitSet = new HashMap<>();
        branches = new HashMap<>();
        NodeSet = new HashMap<>();
        IdSet = new HashMap<>();
        Node master = new Node(null);
        branches.put("master",master);
        head = master;
        curBranch = "master";
        NodeSet.put("init",master);
        this.save();
    }

    //添加提交
    public void add(Commit c) {
        load();
        CommitSet.put(_sha1(c),c);
        IdSet.put(c,_sha1(c));
        Node newnode = new Node(c);
        newnode.parent.add(head);
        NodeSet.put(_sha1(c),newnode);
        head.son = newnode;
        head = newnode;
        branches.replace(curBranch,head);
        save();
    }

    public void addParent(String branch) {
        load();
        head.parent.add(getBranchHead(branch));
        save();
    }

    //维护节点
    public void update() {
        load();
        for(Node n:NodeSet.values()) {
            if(n.item == null) {
                continue;
            }
            File savedDir = join(SAVED_DIR,"%s".formatted(IdSet.get(n.item)));
            //考虑init commit的情况
            if(plainFilenamesIn(savedDir) == null) {
                continue;
            }
            n.item.tracked.clear();
            for (String filename : (Objects.requireNonNull(plainFilenamesIn(savedDir)))) {
                File updated = join(savedDir,"%s".formatted(filename));
                Blob blob = new Blob(updated);
                n.item.tracked.put(filename,blob);
            }
        }
        save();
    }
    //保存到文件
    public void save() {
        writeObject(INDEX,this);
    }

    public void load() {
        Index saved = readObject(INDEX,Index.class);
        this.head = saved.head;
        this.branches = saved.branches;
        this.IdSet = saved.IdSet;
        this.CommitSet = saved.CommitSet;
        this.NodeSet = saved.NodeSet;
    }

    //获取父提交
    public Commit getParent(int n) {
        if(head.parent.get(n) == null) {
            return null;
        }
        return head.parent.get(n).item;
    }

    public Commit getParent(String id,int n) {
        Node p = NodeSet.get(id);
        return p.parent.get(n).item;
    }

    public String getParentId(String id,int n) {
        Node p = NodeSet.get(id);
        return IdSet.get(p.parent.get(n).item);
    }

    //获取当前提交
    public Commit getHead() {
        return head.item;
    }


    //获取对应id的提交
    public Commit getCommit(String id){
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

    public ArrayList<Commit> getAllCommits() {
        ArrayList<Commit> allCommits = new ArrayList<>();
        for(Commit c:CommitSet.values()) {
            allCommits.add(c);
        }
        return allCommits;
    }

    public ArrayList<String> getBranches() {
        ArrayList<String> allBranches = new ArrayList<>();
        for(String str:branches.keySet()) {
            allBranches.add(str);
        }
        return allBranches;
    }

    public String getCurrentBranch() {
        return curBranch;
    }

    public void newBranch(String name) {
        //已经存在名称报错
        if(branches.containsKey(name)) {
            message("A branch with that name already exists.");
            System.exit(0);
        }
        Node newBranch = head;
        branches.put(name,newBranch);
    }

    public void changeBranch(String name) {
        curBranch = name;
        head = branches.get(name);
    }

    public boolean containsBranch(String name) {
        return branches.containsKey(name);
    }

    public void removeBranch(String name) {
        //不存在分支报错
        if(!branches.containsKey(name)){
            message("A branch with that name does not exist.");
            System.exit(0);
        }
        //删除当前分支报错
        if(name.equals(curBranch)) {
            message("Cannot remove the current branch.");
            System.exit(0);
        }
        branches.remove(name);
    }

    public void setHead(String commitId) {
        Node target = NodeSet.get(commitId);
        head = target;
        branches.replace(curBranch,head);
    }

    public boolean containsCommit(String commitId) {
        return CommitSet.containsKey(commitId);
    }

    public HashMap<String,Blob> CwdAllFiles() {
        HashMap<String,Blob> allFiles = new HashMap<>();
        for(String filename: Objects.requireNonNull(plainFilenamesIn(CWD))) {
            File file = join(CWD,"%s".formatted(filename));
            Blob blob = new Blob(file);
            allFiles.put(filename,blob);
        }
        return allFiles;
    }

    public void merge(String branchName) throws IOException {
        Repository repo = new Repository();
        repo.load();
        update();
        Node commonParent = getCommonParent(branchName);
        Node bHead = getBranchHead(branchName);
        boolean isConflict = false;
        //有暂存文件则报错
        if(!repo.staged.isEmpty()) {
            message("You have uncommitted changes.");
            System.exit(0);
        }
        //不存在分支
        if(!branches.containsKey(branchName)) {
            message("A branch with that name does not exist.");
            System.exit(0);
        }
        //合并自身
        if(curBranch.equals(branchName)) {
            message("Cannot merge a branch with itself.");
            System.exit(0);
        }

        //分割点与给定分支相同
        if (commonParent != null && commonParent.equals(bHead)) {
            message("Given branch is an ancestor of the current branch.");
            System.exit(0);
        }
        //分割点与当前分支相同
        if (commonParent != null && commonParent.equals(head)) {
            repo.checkout_b(branchName);
            repo.save();
            message("Current branch fast-forwarded.");
            System.exit(0);
        }

        //仅在给定分支存在的文件检出暂存
        for(String filename:bHead.item.tracked.keySet()) {

            if(!Objects.requireNonNull(commonParent).item.tracked.containsKey(filename) && !getHead().tracked.containsKey(filename)) {
                //覆盖未追踪文件报错
                if(CwdAllFiles().containsKey(filename) ) {
                    message("There is an untracked file in the way; delete it, or add and commit it first.");
                    System.exit(0);
                }
                File file = join(CWD,"%s".formatted(filename));
                Blob b = getBranchHead(branchName).item.tracked.get(filename);
                file.createNewFile();
                writeContents(file,b.getContent());
                repo.staged.put(filename,b);
            }
            //给定分支不修改，当前分支不存在，不管
            if(commonParent.item.tracked.containsKey(filename) && bHead.item.tracked.get(filename).equals(commonParent.item.tracked.get(filename)) && !getHead().tracked.containsKey(filename)) {
                continue;
            }
        }

        ArrayList<String> filenames = new ArrayList<>();
        for(Blob b : getHead().tracked.values()) {
            filenames.add(b.getName());
        }
        for(String filename: filenames) {
            //当前分支中未修改，给定分支不存在的文件被删除
            if(commonParent.item.tracked.containsKey(filename) && getHead().tracked.get(filename).equals(commonParent.item.tracked.get(filename)) && !getBranchHead(branchName).item.tracked.containsKey(filename)) {
                File file = join(CWD,"%s".formatted(filename));
                Blob b = new Blob(file);
                head.item.tracked.remove(filename);
                file.delete();
            }
            //当前分支未修改，给定分支修改的文件检出暂存
            if(getBranchHead(branchName).item.tracked.containsKey(filename) && commonParent.item.tracked.containsKey(filename) && getHead().tracked.get(filename).equals(commonParent.item.tracked.get(filename)) && !getHead().tracked.get(filename).equals(getBranchHead(branchName).item.tracked.get(filename))) {
                File file = join(CWD,"%s".formatted(filename));
                Blob b = getBranchHead(branchName).item.tracked.get(filename);
                file.createNewFile();
                writeContents(file,b.getContent());
                repo.staged.put(filename,b);
            }
            //当前分支中修改，给定分支不修改的情况不管
            if (commonParent.item.tracked.containsKey(filename) && getBranchHead(branchName).item.tracked.containsKey(filename) && !getHead().tracked.get(filename).equals(commonParent.item.tracked.get(filename)) && getBranchHead(branchName).item.tracked.get(filename).equals(commonParent.item.tracked.get(filename))) {
                continue;
            }
            //仅存在当前分支的文件保持不变
            if(!commonParent.item.tracked.containsKey(filename) && !getBranchHead(branchName).item.tracked.containsKey(filename)) {
                continue;
            }
        }

        //合并冲突
        for(String filename: Objects.requireNonNull(commonParent).item.tracked.keySet()) {
            //1.给定分支修改，当前分支删除
            if(getBranchHead(branchName).item.tracked.containsKey(filename) && !getHead().tracked.containsKey(filename) && !getBranchHead(branchName).item.tracked.get(filename).equals(commonParent.item.tracked.get(filename))) {
                isConflict = true;
                File file = join(CWD,"%s".formatted(filename));
                Blob b = getBranchHead(branchName).item.tracked.get(filename);
                StringBuilder conflict1 = new StringBuilder();
                conflict1.append("<<<<<<< HEAD");
                conflict1.append(System.lineSeparator()).append("=======").append(System.lineSeparator());
                conflict1.append(b.getContent()).append(System.lineSeparator()).append(">>>>>>>");
                file.createNewFile();
                writeContents(file,conflict1.toString());
                Blob newb = new Blob(file);
                repo.staged.put(filename,newb);
            }
            //2.当前分支修改，给定分支删除
            if(getHead().tracked.containsKey(filename) && !getBranchHead(branchName).item.tracked.containsKey(filename) && !getHead().tracked.get(filename).equals(commonParent.item.tracked.get(filename))) {
                isConflict = true;
                File file = join(CWD,"%s".formatted(filename));
                Blob b = getHead().tracked.get(filename);
                StringBuilder conflict2 = new StringBuilder();
                conflict2.append("<<<<<<< HEAD").append(System.lineSeparator());
                conflict2.append(b.getContent()).append(System.lineSeparator());
                conflict2.append("=======").append(System.lineSeparator());
                conflict2.append(">>>>>>>");
                file.createNewFile();
                writeContents(file,conflict2.toString());
                Blob newb = new Blob(file);
                repo.staged.put(filename,newb);
            }
            //3.当前与给定分支均修改，修改方式不同。
            if(getHead().tracked.containsKey(filename) && getBranchHead(branchName).item.tracked.containsKey(filename) && !getHead().tracked.get(filename).equals(commonParent.item.tracked.get(filename)) && !getBranchHead(branchName).item.tracked.get(filename).equals(commonParent.item.tracked.get(filename))) {
               if(!getHead().tracked.get(filename).equals(getBranchHead(branchName).item.tracked.get(filename))) {
                   isConflict = true;
                   File file = join(CWD,"%s".formatted(filename));
                   Blob b1 = getHead().tracked.get(filename);
                   Blob b2 = getBranchHead(branchName).item.tracked.get(filename);
                   StringBuilder conflict3 = new StringBuilder();
                   conflict3.append("<<<<<<< HEAD").append(System.lineSeparator());
                   conflict3.append(b1.getContent()).append(System.lineSeparator());
                   conflict3.append("=======").append(System.lineSeparator());
                   conflict3.append(b2.getContent()).append(System.lineSeparator());
                   conflict3.append(">>>>>>>");
                   file.createNewFile();
                   writeContents(file,conflict3.toString());
                   Blob newb = new Blob(file);
                   repo.staged.put(filename,newb);
               }
           }
        }
        repo.saveStaged();
        save();
        repo.save();
        if(isConflict) {
            message("Encountered a merge conflict.");
        }

    }

    private Node getBranchHead(String name) {
        return branches.get(name);
    }

    public Commit getBranchHeadCommit(String name) {
        return branches.get(name).item;
    }

    private Node getCommonParent(String name) {
        load();
        return getCommonParent2(head,name,queue);
    }
    private final Queue<Node> queue = new ArrayDeque<>();
    private Node getCommonParent2(Node node,String name,Queue<Node> q) {
        load();
        ArrayList<Node> bLogs = new ArrayList<>();
        Node bHead = getBranchHead(name);
        while (bHead != null) {
            bLogs.add(bHead);
            bHead = bHead.getParent();
        }
        if(node == null) {
            return null;
        }
        for(Node n:bLogs) {
            if(n.item == null) {continue;}
            if(n.item.getMessage().equals(node.item.getMessage())) {
                q.clear();
                return n;
            }
        }
        if(node.getParent() != null) {
            queue.add(node.getParent());
        }
        if(node.parent.size() == 2) {
            queue.add(node.getsParent());
        }
        return getCommonParent2(q.poll(),name,q);

    }

    public HashMap<String,Commit> getCommitSet() {
        return CommitSet;
    }

    public HashMap<Commit,String> getIdSet() {
        return IdSet;
    }
}
