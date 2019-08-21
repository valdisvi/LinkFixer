package org.xwiki4;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkFixer {
	
	private static StringBuffer input;
				
	//fix url
	//plain implicit url
	//remove it
	public static void fixImplicit(String badLink) {
		matchAndReplace("([\\s\n\r\t]){1}(" + correctInput(badLink) + "){1}([\\s\n\r\t]){1}", " ");
	}
	
	//fix explicit
	//url, but surrounded by [[]]
	//remove it
	public static void fixExplicit(String badLink) {
		matchAndReplace("(\\s)*(\\[)\\2{1}(" + correctInput(badLink) + "){1}(\\])\\4{1}(\\s)*", " ");
	}
	
	//fix label file
	//of the form [[label>>link]]
	//replace with label
	//also deals with the form [[image:link>>link]]
	//delete that one
	public static void fixLabel(String badLink) {
		matchAndReplace("((\\s)*(\\[){1}\\3{1}(image:){1}([^\\[]*?)(>>" + correctInput(badLink) + "){1}(\\]){1}\\7{1}(\\s)*){1}", " ");
		matchAndReplace("((\\s)*(\\[){1}\\3{1}([^\\[]*?)(>>" + correctInput(badLink) + "){1}(\\]){1}\\6{1}(\\s)*){1}", "4");
	}
	
	//fix image
	//of the form [[image:link]]
	//remove it
	public static void fixImage(String badLink) {
		matchAndReplace("((\\s)*(\\[){1}\\3{1}(image:){1}(" + correctInput(badLink) + "){1}(\\]){1}\\6{1}(\\s)*){1}", " ");
	}
	
	//decides where the string should go
	public static void fixAny(String badLink) {
		Pattern pattern = Pattern.compile("(" + correctInput(badLink) + "){1}");
				
		try {
			Matcher matcher = pattern.matcher(input);
			matcher.find();
			if(input.substring(matcher.start()-1, matcher.start()).equals(">")) 		fixLabel(badLink);
			else if (input.substring(matcher.start()-1, matcher.start()).equals("[")) 	fixExplicit(badLink);
			else if (input.substring(matcher.start()-1, matcher.start()).equals(":")) 	fixImage(badLink);
			else fixImplicit(badLink);
		} catch(Exception e) {
			fixImage(badLink);
			fixLabel(badLink);
			fixExplicit(badLink);
			fixImplicit(badLink);
		}
	}
	
	//does replacements and matching - needs the pattern and the replacement for the pattern
	//can pass an int X as a number to specify that X found group is the replacement 
	private static void matchAndReplace(String stringPattern, String replacement) {
		Pattern pattern = Pattern.compile(stringPattern);
				
		try {
			Matcher matcher = pattern.matcher(input);
			matcher.find();
						
			//restore newlines			
			if(matcher.group(1) != null && matcher.group(1).toString().equals("\n")) replacement = "\n ";
			if(matcher.group(3) != null && matcher.group(3).toString().equals("\n")) replacement = " \n";
			if(matcher.group(1) != null && matcher.group(1).toString().equals("\r")) replacement = "\r ";
			if(matcher.group(3) != null && matcher.group(3).toString().equals("\r")) replacement = " \r";
			
			//if replacement is actually int replace with group
			try {
				Integer.parseInt(replacement);
				input = new StringBuffer(matcher.replaceAll(" "+ matcher.group(Integer.parseInt(replacement)) + " "));
			} catch (NumberFormatException e) {
				//not a number just replace
				input = new StringBuffer(matcher.replaceAll(replacement));
			}
		} catch (IllegalStateException e) {
			//doesn't match
		}
	}
	
	//escape regex special characters from input string
	private static String correctInput(String input) {	
		String result = input;
		String specials = "[^$.|?*+(){}";
		
		for(int i = 0; i < specials.length(); i++) {
			result = result.replaceAll("\\" + specials.charAt(i), "\\\\\\" + specials.charAt(i));
		}
		
		return result;
	}
	
	public static StringBuffer getInput() {
		return input;
	}
	
	public static void setInput(StringBuffer inputIn) {
		input = inputIn;
	}
			
}
