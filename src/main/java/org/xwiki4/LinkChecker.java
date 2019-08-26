package org.xwiki4;
import java.io.IOException;

public class LinkChecker {

    public static void check (String url, String saveLocation, String recursion) throws IOException, InterruptedException {
    	
    	String[] IP;
    	
    	IP = url.split("\\/");
    	    	
        String[] cmd = {"linkchecker", "-r" + recursion, "--no-warnings", "--ignore-url=^mailto --ignore-url=^http://localhost --ignore-url=^apt --ignore-url=.*?viewer=.*"
        		, "--no-follow-url=.*\\?viewer=.*", "--check-extern", "-ohtml", "-t5", "-Fhtml/" + saveLocation
        		,  url};
        
        Utils.executeCmd(cmd);
        
    }
        
}
