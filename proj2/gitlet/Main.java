package gitlet;

/**
 * Driver class for Gitlet, a subset of the Git version-control system.
 *
 * @author no
 */

import java.io.File;
import java.io.IOException;
import java.util.Date;

import static gitlet.Repository.CWD;
import static gitlet.Repository.GITLET_DIR;
import static gitlet.Utils.*;

public class Main {
    static Repository repo = new Repository();

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */

    public static void main(String[] args) {
        try {
            //没有参数的情况报错
            if (args.length == 0) {
                message("Please enter a command.");
                System.exit(0);
            }

            String firstArg = args[0];
            switch (firstArg) {
                case "init":
                    repo.init();
                    break;
                case "add":
                    //未初始化报错
                    if (!GITLET_DIR.exists()) {
                        message("Not in an initialized Gitlet directory.");
                        System.exit(0);
                    }
                    String filename = args[1];
                    File file = join(CWD, "%s".formatted(filename));
                    repo.load();
                    repo.add(file);
                    repo.save();
                    break;
                case "commit":
                    //未初始化报错
                    if (!GITLET_DIR.exists()) {
                        message("Not in an initialized Gitlet directory.");
                        System.exit(0);
                    }
                    repo.load();
                    String msg = args[1];
                    if (msg.isEmpty()) {
                        message("Please enter a commit message.");
                        System.exit(0);
                    }
                    Date date = new Date();
                    Commit p = repo.index.getHead();
                    Commit newCommit = new Commit(msg, "n", date, p);
                    repo.commit(newCommit);
                    repo.save();
                    break;
                case "rm":
                    //未初始化报错
                    if (!GITLET_DIR.exists()) {
                        message("Not in an initialized Gitlet directory.");
                        System.exit(0);
                    }
                    String filename3 = args[1];
                    repo.load();
                    repo.remove(filename3);
                    repo.save();
                    break;
                case "log":
                    //未初始化报错
                    if (!GITLET_DIR.exists()) {
                        message("Not in an initialized Gitlet directory.");
                        System.exit(0);
                    }
                    repo.load();
                    repo.log();
                    break;
                case "global-log":
                    //未初始化报错
                    if (!GITLET_DIR.exists()) {
                        message("Not in an initialized Gitlet directory.");
                        System.exit(0);
                    }
                    repo.load();
                    repo.globalLog();
                    break;
                case "find":
                    //未初始化报错
                    if (!GITLET_DIR.exists()) {
                        message("Not in an initialized Gitlet directory.");
                        System.exit(0);
                    }
                    String filename4 = args[1];
                    repo.load();
                    repo.find(filename4);
                    break;
                case "status":
                    //未初始化报错
                    if (!GITLET_DIR.exists()) {
                        message("Not in an initialized Gitlet directory.");
                        System.exit(0);
                    }
                    repo.load();
                    repo.status();
                    break;
                case "checkout":
                    //未初始化报错
                    if (!GITLET_DIR.exists()) {
                        message("Not in an initialized Gitlet directory.");
                        System.exit(0);
                    }
                    repo.load();
                    if (args.length == 3) {
                        String filename1 = args[2];
                        repo.checkout1(filename1);
                    } else if (args.length == 4) {
                        String commitId = args[1];
                        String filename2 = args[3];
                        //不合法参数
                        if (!args[2].equals("--")) {
                            message("Incorrect operands.");
                            System.exit(0);
                        }
                        repo.checkout2(commitId, filename2);
                    } else if (args.length == 2) {
                        String branchname = args[1];
                        repo.checkoutB(branchname);
                    }
                    repo.save();
                    break;
                case "branch":
                    //未初始化报错
                    if (!GITLET_DIR.exists()) {
                        message("Not in an initialized Gitlet directory.");
                        System.exit(0);
                    }
                    repo.load();
                    String branchname1 = args[1];
                    repo.branch(branchname1);
                    repo.save();
                    break;
                case "rm-branch":
                    //未初始化报错
                    if (!GITLET_DIR.exists()) {
                        message("Not in an initialized Gitlet directory.");
                        System.exit(0);
                    }
                    repo.load();
                    String branchName2 = args[1];
                    repo.removeBranch(branchName2);
                    repo.save();
                    break;
                case "reset":
                    //未初始化报错
                    if (!GITLET_DIR.exists()) {
                        message("Not in an initialized Gitlet directory.");
                        System.exit(0);
                    }
                    repo.load();
                    String commitId = args[1];
                    repo.reset(commitId);
                    repo.save();
                    break;
                case "merge":
                    //未初始化报错
                    if (!GITLET_DIR.exists()) {
                        message("Not in an initialized Gitlet directory.");
                        System.exit(0);
                    }
                    repo.load();
                    String branchName = args[1];
                    repo.merge(branchName);
                    repo.save();
                    break;
                case "add-remote":
                    String name = args[1];
                    String dir = args[2];
                    dir.replace("/",File.separator);
                    repo.load();
                    repo.addRemote(name, dir);
                    repo.save();
                    break;
                case "rm-remote":
                    String name1 = args[1];
                    repo.load();
                    repo.removeRemote(name1);
                    repo.save();
                    break;
                case "push":
                    break;
                case "fetch":
                    break;
                case "pull":
                    break;
                default:
                    //不存在此命令的情况
                    message("No command with that name exists.");
                    System.exit(0);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
