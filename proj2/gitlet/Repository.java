package gitlet;

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
    /* TODO: fill in the rest of this class. */
    //管理提交的目录
    public Index index;
    //暂存区
    public HashMap<String,Blob> staged;

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
        Date date = new Date(0L);
        Commit init = new Commit("initial commit","n",date,null);
        staged = new HashMap<>();
        index = new Index();
        index.add(init);
        index.save();
        writeObject(STAGED,staged);
        this.save();
    }

    public void save() {
        writeObject(REPO,this);
    }

    public void load() {
        Repository saved = readObject(REPO,Repository.class);
        index = saved.index;
        staged = saved.staged;
    }

    public void commit(Commit c) throws IOException {
        staged = readObject(STAGED,staged.getClass());
        //没有暂存文件报错
        if(staged.isEmpty()) {
            message("No changes added to the commit.");
            System.exit(0);
        }
        //没有消息报错
        if(c.getMessage()==null) {
            message("Please enter a commit message.");
            System.exit(0);
        }

        for(String filename:staged.keySet()) {
            //同名文件(被修改的)更新追踪
            if(!staged.get(filename).equals(c.tracked.get(filename))) {
                c.tracked.replace(filename,staged.get(filename));
            }
            //追踪新文件
            if(!c.tracked.containsKey(filename)) {
                c.tracked.put(filename,staged.get(filename));
            }
        }

        index.load();
        //提交到树中，更新头指针
        index.add(c);
        index.save();

        //保存追踪文件到仓库。
        File trackedDir = join(SAVED_DIR,"%s".formatted(_sha1(c)));
        trackedDir.mkdir();

        //保存提交信息到文件
        File commitfile = join(COMMIT_DIR,"%s".formatted(_sha1(c)));
        commitfile.createNewFile();
        writeObject(commitfile,c);

        for(String filename:c.tracked.keySet()) {
            File file = join(SAVED_DIR,"%s".formatted(_sha1(c)),"%s".formatted(filename));
            file.createNewFile();
            writeObject(file,c.tracked.get(filename));
        }

        //清空暂存区
        staged.clear();
    }

    public void add(File file) {
        //读取文件
        staged = readObject(STAGED,staged.getClass());
        //文件不存在报错
        if(!file.exists()) {
            message("File does not exist.");
            System.exit(0);
        }

        //储存信息
        Blob blob = new Blob(file);

        //若该文件与当前提交的版本相同则不添加
        if(!index.getHead().tracked.isEmpty() && index.getHead().tracked.get(file.getName()).equals(blob)){
            //如果已经暂存则从暂存区删除
            staged.remove(file.getName());
            writeObject(STAGED,staged);
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
    }

    public void checkout1(String filename) throws IOException {
        index.load();
        //获取当前提交中的版本
        Blob blob = index.getHead().tracked.get(filename);
        //恢复文件（覆盖）
        File file = join(CWD,"%s".formatted(blob.getName()));
        file.createNewFile();
        writeContents(file,blob.getContent());
    }

    public void checkout2(String commitId,String filename) throws IOException {
        index.load();
        Commit chosen = index.get(commitId);
        Blob check = chosen.tracked.get(filename);
        File file = join(CWD,"%s".formatted(filename));
        file.createNewFile();
        writeContents(file,check.getContent());
    }

    public void checkout_b(String branchName) {

    }

    public void log() {
        ArrayList<Commit> logs = index.log();
        for(Commit i :logs) {
            System.out.println("===");
            Formatter formatter1 = new Formatter();
            formatter1.format("commit %s",_sha1(i));
            System.out.println(formatter1.toString());
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
}
