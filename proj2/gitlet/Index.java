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
            return;
        }
        //分割点与当前分支相同
        if (commonParent != null && commonParent.equals(head)) {
            repo.checkout_b(branchName);
            repo.save();
            message("Current branch fast-forwarded.");
            return;
        }

        //检出暂存分割点不存在，且仅仅在给定分支存在的文件
        for(String filename:bHead.item.tracked.keySet()) {
            if (commonParent != null) {
                if(!commonParent.item.tracked.containsKey(filename) && !CwdAllFiles().containsKey(filename)) {
                    //当前提交中的未跟踪文件将被合并覆盖
                    if(!bHead.item.tracked.containsKey(filename) && !head.item.tracked.containsKey(filename)) {
                        message("There is an untracked file in the way; delete it, or add and commit it first.");
                        System.exit(0);
                    }
                    //恢复提交文件
                    File file = join(CWD,"%s".formatted(filename));
                    Blob blob = getBranchHead(branchName).item.tracked.get(filename);
                    file.createNewFile();
                    writeContents(file,blob.getContent());
                    repo.loadStaged();
                    repo.staged.put(filename,blob);
                    repo.saveStaged();
                }
            }
        }
        //任何在分割点存在的文件，在当前分支中未修改且在给定分支中不存在的文件应该被删除（并且不被跟踪）
        for (String filename:CwdAllFiles().keySet()) {
            if(commonParent != null && commonParent.item.tracked.containsKey(filename) && !bHead.item.tracked.containsKey(filename) && CwdAllFiles().get(filename).equals(commonParent.item.tracked.get(filename))) {
                //当前提交中的未跟踪文件将被合并删除
                if(!head.item.tracked.containsKey(filename)) {
                    message("There is an untracked file in the way; delete it, or add and commit it first.");
                    System.exit(0);
                }
                File file = join(CWD,"%s".formatted(filename));
                Blob blob = new Blob(file);
                head.item.tracked.remove(filename);
                file.delete();
                repo.loadRemoved();
                repo.removed.put(filename,blob);
                repo.saveRemoved();
            }
            //任何自分叉点以来在给定分支中已修改但自分叉点以来在当前分支中未修改的文件应更改为给定分支中的版本,暂存
            if(bHead.item.tracked.containsKey(filename) && !bHead.item.tracked.get(filename).equals(commonParent.item.tracked.get(filename)) && CwdAllFiles().get(filename).equals(commonParent.item.tracked.get(filename))) {
                //当前提交中的未跟踪文件将被合并覆盖
                if(!head.item.tracked.containsKey(filename)) {
                    message("There is an untracked file in the way; delete it, or add and commit it first.");
                    System.exit(0);
                }
                Blob b = bHead.item.tracked.get(filename);
                File file = join(CWD,"%s".formatted(filename));
                file.createNewFile();
                writeContents(file,b.getContent());
                repo.loadStaged();
                repo.staged.put(filename,b);
                repo.saveStaged();
            }
        }


        boolean isConflict = false;
        //合并冲突
        for(String filename:CwdAllFiles().keySet()) {
            //1.如果在给定分支中删除
            if(!bHead.item.tracked.containsKey(filename)) {
                //当前提交中的未跟踪文件将被合并覆盖
                if(!head.item.tracked.containsKey(filename)) {
                    message("There is an untracked file in the way; delete it, or add and commit it first.");
                    System.exit(0);
                }
                //文件未发生变化
                if(CwdAllFiles().get(filename).equals(getHead().tracked.get(filename))) {
                    continue;
                }
                isConflict = true;
                File file = join(CWD,"%s".formatted(filename));
                Blob blob = head.item.tracked.get(filename);
                String content;
                content = "<<<<<<< HEAD\n" + blob.getContent() + "=======\n" + ">>>>>>>";
                file.createNewFile();
                writeContents(file,content);
                Blob newblob = new Blob(file);
                repo.loadStaged();
                repo.staged.put(filename,newblob);
                repo.saveStaged();
            }
            //2.都存在且修改方式不同
            if(bHead.item.tracked.containsKey(filename) && !bHead.item.tracked.get(filename).equals(CwdAllFiles().get(filename))) {
                //当前提交中的未跟踪文件将被合并覆盖
                if(!head.item.tracked.containsKey(filename)) {
                    message("There is an untracked file in the way; delete it, or add and commit it first.");
                    System.exit(0);
                }
                isConflict = true;
                Blob b1 = head.item.tracked.get(filename);
                Blob b2 = bHead.item.tracked.get(filename);
                File file = join(CWD,"%s".formatted(filename));
                file.createNewFile();
                String content = "<<<<<<< HEAD\n" + b1.getContent() + "=======\n" + b2.getContent() + ">>>>>>>";
                writeContents(file,content);
                Blob newblob = new Blob(file);
                repo.loadStaged();
                repo.staged.put(filename,newblob);
                repo.saveStaged();
            }
        }
        //3.在当前分支删除而给定分支存在
        for(String filename:bHead.item.tracked.keySet()) {
            if(!CwdAllFiles().containsKey(filename)) {
                //任何在分割点存在的文件，在给定分支中未修改，并且在当前分支中缺失的文件应保持缺失。
                if (commonParent != null && commonParent.item.tracked.containsKey(filename) && commonParent.item.tracked.get(filename).equals(bHead.item.tracked.get(filename))) {
                    continue;
                }
                //文件未发生变化
                if(CwdAllFiles().get(filename).equals(bHead.item.tracked.get(filename))) {
                    continue;
                }
                //一个文件的内容发生了变化而另一个文件被删除导致冲突
                isConflict = true;
                Blob b = bHead.item.tracked.get(filename);
                File file = join(CWD,"%s".formatted(filename));
                String content = "<<<<<<< HEAD\n" + "=======\n" + b.getContent() + ">>>>>>>";
                file.createNewFile();
                writeContents(file,content);
                Blob newblob = new Blob(file);
                repo.loadStaged();
                repo.staged.put(filename,newblob);
                repo.saveStaged();
            }
        }
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
        Node curHead = head;
        Node branchHead = getBranchHead(name);
        ArrayList<Node> curLogs = new ArrayList<>();
        ArrayList<Node> bLogs = new ArrayList<>();
        while (curHead != null) {
            curLogs.add(curHead);
            curHead = curHead.getParent();
        }
        while (branchHead != null) {
            bLogs.add(branchHead);
            branchHead = branchHead.getParent();
        }
        for(Node n:curLogs) {
            if(bLogs.contains(n)) {
                return n;
            }
        }
        return null;
    }
}
