package org.xwiki4;
import java.io.IOException;

public class LinkChecker {

    public static void check (String url) throws IOException, InterruptedException {
    	
        String[] cmd = {"linkchecker", "--no-warnings", "--ignore-url=^mailto --ignore-url=^http://localhost --ignore-url=^apt --ignore-url=.*?viewer=.*"
        		, "--no-follow-url=.*\\?viewer=.*", "--check-extern", "-r1", "-ohtml", "-t5", "-Fhtml//home/student/workspace/XWiki/linkchecker.html"
        		, url};
        
        Utils.executeCmd(cmd);
    }
        
}