package org.xwiki4;
import java.io.IOException;

public class LinkChecker {

    public static void check (String url, String saveLocation, String recursion) throws IOException, InterruptedException {
    	
    	String[] IP;
    	
    	IP = url.split("\\/");
    	    	
        String[] cmd = {"linkchecker", "-r" + recursion, "--no-warnings", "--ignore-url=((" + IP[0] + "//" + IP[2] + "){1}(.*?)){1}"
        		, "--no-follow-url=.*\\?viewer=.*", "--check-extern", "-ohtml", "-t5", "-Fhtml/" + saveLocation
        		,  url};
        
        Utils.executeCmd(cmd);
        
    }
        
}
