package org.xwiki4;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkFixer {
	
	private static StringBuffer input;
				
	//fix url
	//plain implicit url
	//remove it
	public static void fixImplicit(String badLink) {
		//triggers on anything so do some more serious matches first
		matchAndReplace("( )?(" + badLink + "){1}\\1?", " ");
		
	}
	
	//fix explicit
	//url, but surrounded by [[]]
	//remove it
	public static void fixExplicit(String badLink) {
		matchAndReplace("(\\[)\\1{1}(" + badLink + "){1}(\\])\\3{1}", "");
	}
	
	//fix label file
	//of the form [[label>>link]]
	//replace with label
	public static void fixLabel(String badLink) {
		matchAndReplace("((\\[){1}\\2{1}(.*?)(>>" + badLink + "){1}(]){1}\\5{1}){1}", "3");
	}
	
	//does replacements and matching - needs the pattern and the replacement for the pattern
	//can pass an int X as a number to specify that X found group is the replacement 
	//return true if replaced false if not
	//replace let's it run as a test without replacing anything
	private static void matchAndReplace(String stringPattern, String replacement) {
		Pattern pattern = Pattern.compile(stringPattern);
		
		try {
			Matcher matcher = pattern.matcher(input);
			matcher.find();
			//if replacement is actually int replace with group
			try {
				Integer.parseInt(replacement);
				input = new StringBuffer(matcher.replaceAll(matcher.group(Integer.parseInt(replacement))));
			} catch (NumberFormatException e) {
				//not a number just replace
				input = new StringBuffer(matcher.replaceAll(replacement));
			}
		} catch (IllegalStateException e) {
			//doesn't match
		}
	}
	
	public static StringBuffer getInput() {
		return input;
	}
	
	public static void setInput(StringBuffer inputIn) {
		input = inputIn;
	}
			
}
