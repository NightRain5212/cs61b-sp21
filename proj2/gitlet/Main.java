package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
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

    public static void main(String[] args)  {
        try {
            // TODO: what if args is empty?
            //没有参数的情况报错
            if (args.length == 0) {
                message("Please enter a command.");
                System.exit(0);
            }
            //未初始化报错
            if(!GITLET_DIR.exists()) {
                message("Not in an initialized Gitlet directory.");
                System.exit(0);
            }

            String firstArg = args[0];
            switch (firstArg) {
                case "init":
                    // TODO: handle the `init` command
                    repo.init();
                    break;
                case "add":
                    // TODO: handle the `add [filename]` command
                    String filename = args[1];
                    File file = join(CWD, "%s".formatted(filename));
                    repo.load();
                    repo.add(file);
                    repo.save();
                    break;
                // TODO: FILL THE REST IN
                case "commit":
                    repo.load();
                    String msg = args[1];
                    if(msg.isEmpty()) {
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
                    String filename3 = args[1];
                    repo.load();
                    repo.remove(filename3);
                    repo.save();
                    break;
                case "log":
                    repo.load();
                    repo.log();
                    break;
                case "global-log":
                    repo.load();
                    repo.global_log();
                    break;
                case "find":
                    String filename4 = args[1];
                    repo.load();
                    repo.find(filename4);
                    break;
                case "status":
                    repo.load();
                    repo.status();
                    break;
                case "checkout":
                    repo.load();
                    if (args.length == 3) {
                        String filename1 = args[2];
                        repo.checkout1(filename1);
                    } else if (args.length == 4) {
                        String commitId = args[1];
                        String filename2 = args[3];
                        repo.checkout2(commitId, filename2);
                    } else if (args.length == 2) {
                        String branchname = args[1];
                        repo.checkout_b(branchname);
                    }
                    repo.save();
                    break;
                case "branch":
                    repo.load();
                    String branchname1 = args[1];
                    repo.branch(branchname1);
                    break;
                case "rm-branch":
                    repo.load();
                    String branchName2 = args[1];
                    repo.removeBranch(branchName2);
                    repo.save();
                    break;
                case "reset":
                    repo.load();
                    String commitId = args[1];
                    repo.reset(commitId);
                    repo.save();
                    break;
                case "merge":
                    repo.load();
                    String branchName = args[1];
                    repo.merge(branchName);
                    repo.save();
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
