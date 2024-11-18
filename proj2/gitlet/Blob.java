package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.*;

public class Blob implements Serializable {
    private String filecontent;
    private String filename;

    public Blob(File file) {
        filename = file.getName();
        filecontent = readContentsAsString(file);
    }

    public String getName() {
        return filename;
    }

    public String getContent() {
        return filecontent;
    }

    public boolean equal(Object b) {
        Blob b1 = (Blob) b;
        return msha1(this).equals(msha1(b1));
    }
}
