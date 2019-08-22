package org.xwiki4;
import java.io.IOException;

public class LinkChecker {

    public static void main(final String[] args) throws IOException, InterruptedException {
    	
        String[] cmd = {"linkchecker", "--no-warnings", "--ignore-url=^mailto --ignore-url=^http://localhost --ignore-url=^apt --ignore-url=.*?viewer=.*"
        		, "--no-follow-url=.*\\?viewer=.*", "--check-extern", "-r6", "-ohtml", "-t5", "-Fhtml//home/student/workspace/XWiki/src/main/java/badlinks.html"
        		, "http://192.168.8.113:8080/xwiki/bin/view/TestPageLinkChecker/SimplePage/"};
        
        Utils.executeCmd(cmd);
    }
        
}