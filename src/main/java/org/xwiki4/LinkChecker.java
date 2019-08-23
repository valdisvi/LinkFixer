package org.xwiki4;
import java.io.IOException;

public class LinkChecker {

    public static void main(final String[] args) throws IOException, InterruptedException {
    	
        String[] cmd = {"linkchecker", "--no-warnings", "-p", "-usuperadmin", "--ignore-url=^mailto --ignore-url=^http://localhost --ignore-url=^apt --ignore-url=.*?viewer=.*"
        		, "--no-follow-url=.*\\?viewer=.*", "--check-extern", "-r3", "-ohtml", "-t5", "-Fhtml//home/student/Team4_XWiki/XWiki/src/test/resources/badlinks.html"
        		, "http://192.168.8.150:8080/xwiki/bin/view/TestPageLinkChecker/SimplePage/"};
        
        Utils.executeCmd(cmd);
    }
        
}