package org.xwiki4;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class LinkFixer {
	
	private static StringBuffer input;
	private static ArrayList<String> badLinksList;
	private static ArrayList<String> locationsList;
				
	//fix url
	//plain implicit url
	//remove it
	public static void fixImplicit(String badLink) {
		matchAndReplace("([\\h\n\r\t]){1}(" + correctInput(badLink) + "){1}([\\h\n\r\t]){1}", " ");
	}
	
	//fix explicit
	//url, but surrounded by [[]]
	//remove it
	public static void fixExplicit(String badLink) {
		matchAndReplace("(\\h)*(\\[)\\2{1}(" + correctInput(badLink) + "){1}(\\])\\4{1}(\\h)*", " ");
	}
	
	//fix label file
	//of the form [[label>>link]]
	//replace with label
	//also deals with the form [[image:link>>link]]
	//delete that one
	public static void fixLabel(String badLink) {
		matchAndReplace("((\\h)*(\\[){1}\\3{1}(image:){1}([^\\[]*?)(>>" + correctInput(badLink) + "){1}(\\]){1}\\7{1}(\\h)*){1}", " ");
		matchAndReplace("((\\h)*(\\[){1}\\3{1}([^\\[]*?)(>>" + correctInput(badLink) + "){1}(\\]){1}\\6{1}(\\h)*){1}", "4");
	}
	
	//fix image
	//of the form [[image:link]]
	//remove it
	public static void fixImage(String badLink) {
		matchAndReplace("((\\h)*(\\[){1}\\3{1}(image:){1}(" + correctInput(badLink) + "){1}(\\]){1}\\6{1}(\\h)*){1}", " ");
	}
	
	//fix attach
	//of the form [[attach:img.png]]
	//replace it with part before .png
	public static void fixAttach(String badLink) {
		//match the part before .png
		Pattern pattern = Pattern.compile("(.*)(\\.)(.*)");
		Matcher matcher = pattern.matcher(badLink);
		matcher.find();
		matchAndReplace("((\\h)*(\\[){1}\\3{1}(attach:){1}(" + correctInput(badLink) + "){1}(\\]){1}\\6{1}(\\h)*){1}", " " + matcher.group(1) + " ");
	}
	
	//fix new window
	//of the form [[link||target="_blank"]]
	//remove it
	public static void fixNewWindow(String badLink) {
		matchAndReplace("(\\h)*(\\[)\\2{1}(" + correctInput(badLink) + "){1}(||){1}([^\\[]*?)(\\])\\6{1}(\\h)*", " ");
	}
	
	//decides where the string should go
	public static void fixAny(String badLink) {
		Pattern pattern = Pattern.compile("(" + correctInput(badLink) + "){1}");
				
		try {
			Matcher matcher = pattern.matcher(input);
			matcher.find();
			System.out.println("Found:" + matcher.group().toString());
			if(input.substring(matcher.start()-1, matcher.start()).equals(">")) 		fixLabel(badLink);
			else if (input.substring(matcher.start()-1, matcher.start()).equals("[") && !input.substring(matcher.start(), matcher.start()+1).equals("|")) 	fixExplicit(badLink);
			else if(input.substring(matcher.start()-1, matcher.start()).equals("[") && input.substring(matcher.start(), matcher.start()+1).equals("|")) fixNewWindow(badLink);
			else if (input.substring(matcher.start()-1, matcher.start()).equals(":")) {
				fixAttach(badLink);
				fixImage(badLink);
			} 	
			else fixImplicit(badLink);
		} catch(Exception e) {
			fixNewWindow(badLink);
			fixAttach(badLink);
			fixImage(badLink);
			fixLabel(badLink);
			fixExplicit(badLink);
			fixImplicit(badLink);
		}
			
			fixNewWindow(badLink);
			fixAttach(badLink);
			fixImage(badLink);
			fixLabel(badLink);
			fixExplicit(badLink);
			fixImplicit(badLink);
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
		
		result.replaceAll("/", "\\/");
		
		return result;
	}
	
	public static void getLinkFixer() {
		BadLinks badLinks = new BadLinks();
		String restLink;
		String restTail = "";
		String[] split;
		try {
			ClassLoader classLoader = new LinkFixer().getClass().getClassLoader();
			//File file = new File(classLoader.getResource("badlinks.html").getFile());
			File file = new File("src/test/resources/badlinks.html");	
			
			badLinks.findLinksLocal(file);
			badLinksList = new ArrayList(badLinks.getParentLinks());
			locationsList = new ArrayList(badLinks.getRealLinks());
			
			System.out.println("Current error count:" + badLinks.getErrorCount());
			
			//do the actual fixing			
			for(int i = 0; i < badLinksList.size(); i++) {
				
				//form the rest link
				split = locationsList.get(i).split("/");
				restLink = split[0] + "//" + split[2] + "/" + split[3] + "/" + "rest/wikis/xwiki";
				restTail = "";
				for(int k = 6; k < split.length; k++) {
					if(split[k].charAt(0) != '?')
						restLink += "/spaces/" + split[k];
					else 
						restTail = split[k];
				}
				
				//System.out.println("Fixing:" + badLinksList.get(i));
				
				restLink += "/pages/WebHome" + restTail;
				input = new StringBuffer(XWikiController.getPage(restLink));
				//System.out.print(input);
				
				fixAny(badLinksList.get(i));
				
				//write the changes to text file
				FileManipulation.writeTo(input, "src/main/resources/fixResult.txt");
				
				//push the changes to XWiki
				XWikiController.setPage(restLink, "src/main/resources/fixResult.txt");
				
			}
			
			System.out.println("Done fixing!");
						
		} catch (IOException e) {
			System.err.println();
			e.printStackTrace();
		}
	}
	
	public static StringBuffer getInput() {
		return input;
	}
	
	public static void setInput(StringBuffer inputIn) {
		input = inputIn;
	}
			
}
