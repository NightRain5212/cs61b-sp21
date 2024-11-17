package gitlet;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File COMMIT_DIR = join(CWD, ".gitlet","commits");
    public static final File INDEX = join(CWD,".gitlet","index");
    public static final File STAGED = join(CWD,".gitlet","staged");
    public static final File REPO = join(CWD,".gitlet","repo");
    public static final File SAVED_DIR = join(CWD,".gitlet","saved");
    public static final File REMOVED = join(CWD,".gitlet","removed");

    /* TODO: fill in the rest of this class. */
    //管理提交的目录
    public Index index;
    //暂存区
    public HashMap<String,Blob> staged;
    public HashMap<String,Blob> removed;

    /*tree：
    * - .gitlet/
    *   -commits
    *       -commit1
    *       -commit2
    *   -saved
    *       -sha1(commit1)
    *           -tracked files
    *       -sha1(commit2)
    *           -tracked files
    *   -index
    *   -staged
    *   -repo
    *   -removed
    * - working trees
    *
    *
    * */

    //初始化
    public void init() throws IOException {
        if(GITLET_DIR.exists()) {
            message("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        GITLET_DIR.mkdir();
        COMMIT_DIR.mkdir();
        SAVED_DIR.mkdir();
        REPO.createNewFile();
        INDEX.createNewFile();
        STAGED.createNewFile();
        REMOVED.createNewFile();
        Date date = new Date(0L);
        Commit init = new Commit("initial commit","n",date,null);
        staged = new HashMap<>();
        index = new Index();
        removed = new HashMap<>();
        index.add(init);
        index.save();
        writeObject(STAGED,staged);
        writeObject(REMOVED,removed);
        this.save();
    }

    public void save() {
        writeObject(REPO,this);
    }

    public void load() {
        Repository saved = readObject(REPO,Repository.class);
        index = saved.index;
        staged = saved.staged;
        removed = saved.removed;
    }

    public void loadStaged() {
        staged = readObject(STAGED,staged.getClass());
    }

    public void saveStaged() {
        writeObject(STAGED,staged);
    }

    public void loadRemoved() {
        removed = readObject(REMOVED,removed.getClass());
    }

    public void saveRemoved() {
        writeObject(REMOVED,removed);
    }

    public void commit(Commit c) throws IOException {
        index.load();
        staged = readObject(STAGED,staged.getClass());
        loadRemoved();
        //没有暂存文件报错
        if(staged.isEmpty() && removed.isEmpty()) {
            message("No changes added to the commit.");
            System.exit(0);
        }
        //没有消息报错
        if(c.getMessage()==null || c.getMessage().isEmpty()) {
            message("Please enter a commit message.");
            System.exit(0);
        }


        for(String filename:staged.keySet()) {
            //同名文件(被修改的)更新追踪
            if(c.tracked.containsKey(filename) && !staged.get(filename).equals(c.tracked.get(filename))) {
                c.tracked.replace(filename,staged.get(filename));
            }
            //追踪新文件
            if(!c.tracked.containsKey(filename)) {
                c.tracked.put(filename,staged.get(filename));
            }
        }

        //提交到树中，更新头指针
        index.add(c);
        index.setHead(_sha1(c));
        index.save();

        //保存追踪文件到仓库。
        File trackedDir = join(SAVED_DIR,"%s".formatted(_sha1(c)));
        trackedDir.mkdir();

        //保存提交信息到文件
        File commitfile = join(COMMIT_DIR,"%s".formatted(_sha1(c)));
        commitfile.createNewFile();
        writeObject(commitfile,c);

        //保存信息
        for(String filename:c.tracked.keySet()) {
            File file = join(SAVED_DIR,"%s".formatted(_sha1(c)),"%s".formatted(filename));
            file.createNewFile();
            writeContents(file,c.tracked.get(filename).getContent());
        }

        //清空暂存区
        staged.clear();
        removed.clear();
        //保存
        writeObject(STAGED,staged);
        saveRemoved();
    }

    public void add(File file) {
        //读取文件
        index.load();
        staged = readObject(STAGED,staged.getClass());
        //文件不存在报错
        if(!file.exists()) {
            message("File does not exist.");
            System.exit(0);
        }

        //储存信息
        Blob blob = new Blob(file);

        //更新删除区
        loadRemoved();
        removed.remove(file.getName());
        saveRemoved();

        //若该文件与当前提交的版本相同则不添加
        if(!index.getHead().tracked.isEmpty() && index.getHead().tracked.containsKey(file.getName()) && index.getHead().tracked.get(file.getName()).equals(blob)){
            //如果已经暂存则从暂存区删除
            staged.remove(file.getName());
            writeObject(STAGED,staged);
            saveStaged();
            index.save();
            return;
        }

        //已经暂存覆盖原有文件
        if(staged.containsKey(file.getName())) {
            staged.replace(file.getName(),blob);
        } else {
            //未暂存直接添加文件
            staged.put(file.getName(),blob);
        }
        //保存文件
        writeObject(STAGED,staged);
        index.save();

    }

    public void checkout1(String filename) throws IOException {
        index.load();
        //不存在文件报错
        if(!index.getHead().tracked.containsKey(filename)) {
            message("File does not exist in that commit.");
            System.exit(0);
        }
        //获取当前提交中的版本
        Blob blob = index.getHead().tracked.get(filename);
        //恢复文件（覆盖）
        File file = join(CWD,"%s".formatted(blob.getName()));
        file.createNewFile();
        writeContents(file,blob.getContent());
        index.save();
    }

    public void checkout2(String commitId,String filename) throws IOException {
        index.load();
        //缩写id的情况
        if(commitId.length() == 8) {
            for(String id:index.getCommitSet().keySet()) {
                if(commitId.equals("%.8s".formatted(id))) {
                    commitId = id;
                }
            }
        }
        //不存在提交报错
        if(!index.containsCommit(commitId)) {
            message("No commit with that id exists.");
            System.exit(0);
        }
        Commit chosen = index.getCommit(commitId);
        //不存在文件报错
        if(!chosen.tracked.containsKey(filename)) {
            message("File does not exist in that commit.");
            System.exit(0);
        }
        Blob check = chosen.tracked.get(filename);
        File file = join(CWD,"%s".formatted(filename));
        file.createNewFile();
        writeContents(file,check.getContent());
        index.save();
    }

    public void checkout_b(String branchName) throws IOException {
        index.load();
        staged = readObject(STAGED,staged.getClass());
        removed = readObject(REMOVED,removed.getClass());
        if(!index.containsBranch(branchName)) {
            message("No such branch exists.");
            System.exit(0);
        }
        if(index.getCurrentBranch().equals(branchName)) {
            message("No need to checkout the current branch.");
            System.exit(0);
        }
        //检查是否有未追踪文件被覆盖
        boolean isuntracked = false;
        for(String filename: Objects.requireNonNull(plainFilenamesIn(CWD))) {
            if(!index.getHead().tracked.containsKey(filename) && index.getBranchHeadCommit(branchName).tracked.containsKey(filename)) {
                isuntracked = true;
                break;
            }
        }
        if(isuntracked) {
            message("There is an untracked file in the way; delete it, or add and commit it first.");
            System.exit(0);
        }
        //切换分支
        index.changeBranch(branchName);
        //删除在当前分支追踪而不在检出分支的文件
        for(String filename: Objects.requireNonNull(plainFilenamesIn(CWD))) {
            if(!index.getHead().tracked.containsKey(filename)) {
                File file = join(CWD,"%s".formatted(filename));
                Blob blob = new Blob(file);
                file.delete();
                removed.put(filename,blob);
            }
        }
        //更新工作目录
        for(String filename:index.getHead().tracked.keySet()) {
            File file = join(CWD,"%s".formatted(filename));
            Blob blob = index.getHead().tracked.get(filename);
            file.createNewFile();
            writeContents(file,blob.getContent());
        }


        //清除暂存区
        staged.clear();
        //保存文件
        index.save();
        writeObject(STAGED,staged);
        writeObject(REMOVED,removed);
    }

    public void log() {
        index.load();
        ArrayList<Commit> logs = index.log();
        for(Commit i :logs) {
            System.out.println("===");
            Formatter formatter1 = new Formatter();
            formatter1.format("commit %s",index.getIdSet().get(i));
            System.out.println(formatter1.toString());
            //处理合并提交
            String id = index.getIdSet().get(i);
            if(i.parent.size() == 2) {
                Formatter formatter2 = new Formatter();
                formatter2.format("Merge: %.7s %.7s",index.getParentId(id,0),index.getParentId(id,1));
                System.out.println(formatter2.toString());
            }
            //设置日期格式
            Instant instant = i.getDate().toInstant();
            ZonedDateTime zdt = instant.atZone(ZoneId.of("Asia/Shanghai"));
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy Z",Locale.ENGLISH);
            String dateMsg = zdt.format(dtf);
            System.out.println("Date: "+dateMsg);
            System.out.println(i.getMessage());
            System.out.println();
        }
    }

    public void remove(String filename) {
        //读取文件
        staged = readObject(STAGED,staged.getClass());
        removed = readObject(REMOVED,removed.getClass());
        //既没有暂存也没有追踪时报错
        if(!staged.containsKey(filename) && !index.getHead().tracked.containsKey(filename)) {
            message("No reason to remove the file.");
            System.exit(0);
        }
        //如果被当前提交追踪则删除
        if(index.getHead().tracked.containsKey(filename)) {
            Blob blob = index.getHead().tracked.get(filename);
            staged.put(filename,blob);
            index.getHead().tracked.remove(filename);
            removed.put(filename,blob);
            //删除工作区的文件
            File file = join(CWD,"%s".formatted(filename));
            file.delete();
        }
        //从暂存区删除
        staged.remove(filename);

        //保存文件
        index.save();
        writeObject(STAGED,staged);
        writeObject(REMOVED,removed);

    }

    public void global_log() {
        index.load();
        for(Commit c:index.getAllCommits()) {
            if(c == null){
                continue;
            }
            //打印日志
            System.out.println("===");
            Formatter formatter1 = new Formatter();
            formatter1.format("commit %s",index.getIdSet().get(c));
            System.out.println(formatter1.toString());
            //处理合并提交
            String id = index.getIdSet().get(c);
            if(c.parent.size() == 2) {
                Formatter formatter2 = new Formatter();
                formatter2.format("Merge: %.7s %.7s",index.getParentId(id,0),index.getParentId(id,1));
                System.out.println(formatter2.toString());
            }
            //设置日期格式
            Instant instant = c.getDate().toInstant();
            ZonedDateTime zdt = instant.atZone(ZoneId.of("Asia/Shanghai"));
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy Z",Locale.ENGLISH);
            String dateMsg = zdt.format(dtf);
            System.out.println("Date: "+dateMsg);
            System.out.println(c.getMessage());
            System.out.println();
        }
    }

    public void find(String msg){
        index.load();
        boolean isExist = false;
        for(Commit c:index.getAllCommits()) {
            if(msg.equals(c.getMessage()) ) {
                isExist = true;
                System.out.println(_sha1(c));
            }
        }
        //若不存在相应的文件报错
        if(!isExist) {
            message("Found no commit with that message.");
            System.exit(0);
        }

    }

    public void status() {
        index.load();
        staged = readObject(STAGED,staged.getClass());
        removed = readObject(REMOVED,removed.getClass());
        //显示分支
        System.out.println("=== Branches ===");
        String cur = "*" + index.getCurrentBranch();
        System.out.println(cur);
        for(String str:index.getBranches()) {
            if(str.equals(index.getCurrentBranch())) {
                continue;
            }
            System.out.println(str);
        }
        System.out.println();
        //显示暂存文件
        System.out.println("=== Staged Files ===");
        for(String str:staged.keySet()) {
            System.out.println(str);
        }
        System.out.println();
        //显示删除文件
        System.out.println("=== Removed Files ===");
        for(String str:removed.keySet()) {
            System.out.println(str);
        }
        System.out.println();
        //修改但未暂存文件
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        //未跟踪文件
        System.out.println("=== Untracked Files ===");
        System.out.println();
    }

    public void branch(String name) {
        index.load();
        index.newBranch(name);
        index.save();
    }

    public void removeBranch(String name) {
        index.load();
        index.removeBranch(name);
        index.save();
    }

    public void reset(String commitId) throws IOException {
        index.load();
        //不存在对应的提交
        if(!index.containsCommit(commitId)) {
            message("No commit with that id exists.");
            System.exit(0);
        }
        //存在未跟踪的文件被覆盖的情况(删除的情况未考虑)
        Commit target = index.getCommit(commitId);
        for(String filename: Objects.requireNonNull(plainFilenamesIn(CWD))) {
            File file = join(CWD,"%s".formatted(filename));
            Blob blob = new Blob(file);
            //跳过冗余文件
            if(!index.getHead().tracked.containsKey(filename) && !target.tracked.containsKey(filename)) {
                continue;
            }
            if(index.getHead().tracked.containsKey(filename)) {
                continue;
            }
            //删除的情况
            if(target.tracked.containsKey(filename) && !index.getHead().tracked.containsKey(filename)) {
                continue;
            }
            if(target.tracked.containsKey(filename)) {
                message("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
        }



        //checkout
        index.setHead(commitId);
        index.save();
        for(String filename: index.getHead().tracked.keySet()) {
            checkout1(filename);
        }
        index.save();
        //清楚暂存区
        loadStaged();
        loadRemoved();
        staged.clear();
        removed.clear();
        saveStaged();
        saveRemoved();
    }

    public void merge(String branchName) throws IOException {
        index.load();
        index.merge(branchName);
        Date date = new Date();
        Commit mergeCommit = new Commit("Merged %s into %s.".formatted(branchName,index.getCurrentBranch()),"n",date,index.getHead());
        mergeCommit.parent.add(index.getBranchHeadCommit(branchName));
        commit(mergeCommit);
        index.addParent(branchName);
        index.save();
    }

}
