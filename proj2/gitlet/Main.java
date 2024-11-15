package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */

import java.io.File;
import java.io.IOException;
import java.util.Date;

import static gitlet.Repository.CWD;
import static gitlet.Utils.*;
public class Main {
    static Repository repo = new Repository();

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) throws IOException {
        // TODO: what if args is empty?
        //没有参数的情况报错
        if(args.length == 0) {
            message("Please enter a command.");
            System.exit(0);
        }

        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                repo.init();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                String filename = args[1];
                File file = join(CWD,"%s".formatted(filename));
                repo.load();
                repo.add(file);
                repo.save();
                break;
            // TODO: FILL THE REST IN
            case "commit":
                repo.load();
                String msg = args[1];
                Date date = new Date();
                Commit p = repo.index.getParent();
                Commit newCommit = new Commit(msg,"n",date,p);
                repo.commit(newCommit);
                repo.save();
                break;
            case "rm":
                break;
            case "log":
                repo.load();
                repo.log();
                break;
            case "global-log":
                break;
            case "find":
                break;
            case "status":
                break;
            case "checkout":
                repo.load();
                if(args.length == 3){
                    String filename1 = args[2];
                    repo.checkout1(filename1);
                }else if(args.length == 4) {
                    String commitId = args[1];
                    String filename2 = args[3];
                    repo.checkout2(commitId,filename2);
                }else if(args.length == 2) {

                }
                break;
            case "branch":
                break;
            case "rm-branch":
                break;
            case "reset":
                break;
            case "merge":
                break;
            default:
                //不存在此命令的情况
                message("No command with that name exists.");
                System.exit(0);
        }
    }
}
