package org.xwiki4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class LinkFixer {

	private static Logger log = Logger.getLogger(LinkFixer.class);
	private static StringBuilder content; // Is needed to reuse existing tests and implementation!
	private static final String pagePrefix = "odo.lv";
	private static final String sourceLog = "logs/source.log";
	private static final String targetLog = "logs/target.log";

	// fix url
	// plain implicit url
	// remove it
	public static void fixImplicit(String badLink) {
		String replacement = " ";

		try {
			Pattern pattern = Pattern.compile(
					"((\\h)*([^\\[]){1}(" + correctInput(badLink) + "){1}([^\\]]){1}(\\h)*){1}",
					Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(content);
			matcher.find();
			if (matcher.group(3).equals("\n") || matcher.group(5).equals("\n") || matcher.group(3).equals("\r")
					|| matcher.group(5).equals("\r")) {
				replacement = "\n";
			}
		} catch (Exception e) {

		}

		matchAndReplace("((\\h)*([^\\[]){1}(" + correctInput(badLink) + "){1}([^\\]]){1}(\\h)*){1}", replacement);
	}

	// fix explicit
	// url, but surrounded by [[]]
	// remove it
	public static void fixExplicit(String badLink) {
		matchAndReplace("(\\h)*(\\[)\\2{1}(" + correctInput(badLink) + "){1}(\\])\\4{1}(\\h)*", " ");
	}

	// fix label file
	// of the form [[label>>link]]
	// replace with label
	// also deals with the form [[image:link>>link]]
	// delete that one
	public static void fixLabel(String badLink) {
		matchAndReplace("((\\h)*(\\[){1}\\3{1}(image:){1}([^\\[]*?)(>>([^\\[]*?)" + correctInput(badLink)
				+ "){1}(\\]){1}\\8{1}(\\h)*){1}", " ");
		matchAndReplace(
				"((\\h)*(\\[){1}\\3{1}([^\\[]*?)(>>([^\\[]*?)" + correctInput(badLink) + "){1}(\\]){1}\\7{1}(\\h)*){1}",
				"4");
	}

	// fix image
	// of the form [[image:link]]
	// remove it
	public static void fixImage(String badLink) {
		matchAndReplace("((\\h)*(\\[){1}\\3{1}(image:){1}(" + correctInput(badLink) + "){1}(\\]){1}\\6{1}(\\h)*){1}",
				" ");
	}

	// fix attach
	// of the form [[attach:img.png]]
	// replace it with part before .png
	public static void fixAttach(String badLink) {
		// match the part before .png
		Pattern pattern = Pattern.compile("(.*)(\\.)(.*)", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(badLink);
		matcher.find();
		matchAndReplace("((\\h)*(\\[){1}\\3{1}(attach:){1}(" + correctInput(badLink) + "){1}(\\]){1}\\6{1}(\\h)*){1}",
				" " + matcher.group(1) + " ");
	}

	// fix url
	// of the form [[url:link]]
	// delete it
	public static void fixUrl(String badLink) {
		matchAndReplace("((\\h)*(\\[){1}\\3{1}(url:){1}(" + correctInput(badLink) + "){1}(\\]){1}\\6{1}(\\h)*){1}",
				" ");
	}

	// fix new window
	// of the form [[link||target="_blank"]]
	// remove it
	public static void fixNewWindow(String badLink) {
		matchAndReplace(
				"(\\h)*(\\[)\\2{1}([^\\[]*?)(" + correctInput(badLink) + "){1}(||){1}([^\\[]*?)(\\])\\7{1}(\\h)*", " ");
	}

	// fix ftp
	// of the form {{some file="location"}}
	// replace with name
	public static void fixFTP(String badLink) {
		Pattern pattern = Pattern.compile("(" + correctInput(badLink) + "){1}", Pattern.CASE_INSENSITIVE);
		try {
			Matcher matcher = pattern.matcher(content);
			if (matcher.find()) {
				log.debug("Found:" + badLink);
				String[] split;
				split = badLink.split("/");
				matchAndReplace("(\\h)*(\\{)\\2{1}([^{]*?)=\"([^{]*?)(" + correctInput(badLink)
						+ "){1}\"(\\/)?(\\})\\7{1}(\\h)*", " " + split[split.length - 1] + " ");
			}
		} catch (Exception e) {
			// nothing
		}
	}

	// decides where the string should go
	public static void fixAny(String badLink) {

		Pattern pattern = Pattern.compile("(" + correctInput(badLink) + "){1}", Pattern.CASE_INSENSITIVE);
		try {
			Matcher matcher = pattern.matcher(content);
			if (matcher.find()) {
				log.debug("Found:" + badLink);
				fixLabel(badLink);
				fixNewWindow(badLink);
				fixAttach(badLink);
				fixImage(badLink);
				fixUrl(badLink);
				fixExplicit(badLink);
				fixImplicit(badLink);
			}
		} catch (Exception e) {
			// nothing
		}

	}

	// does replacements and matching - needs the pattern and the replacement for
	// the pattern
	// can pass an int X as a number to specify that X found group is the
	// replacement
	private static void matchAndReplace(String stringPattern, String replacement) {
		Pattern pattern = Pattern.compile(stringPattern, Pattern.CASE_INSENSITIVE);

		try {
			Matcher matcher = pattern.matcher(content);
			matcher.find();

			// restore newlines
			if (matcher.group(1) != null && matcher.group(1).equals("\n"))
				replacement = "\n ";
			if (matcher.group(3) != null && matcher.group(3).equals("\n"))
				replacement = " \n";
			if (matcher.group(1) != null && matcher.group(1).equals("\r"))
				replacement = "\r ";
			if (matcher.group(3) != null && matcher.group(3).equals("\r"))
				replacement = " \r";

			// if replacement is actually int replace with group
			try {
				Integer.parseInt(replacement);
				content = new StringBuilder(
						matcher.replaceAll(" " + matcher.group(Integer.parseInt(replacement)) + " "));
				log.info("Fixed!");
			} catch (NumberFormatException e) {
				// not a number just replace
				content = new StringBuilder(matcher.replaceAll(replacement));
				log.info("Fixed!");
			}
		} catch (IllegalStateException e) {
			// doesn't match
		}
	}

	// escape regex special characters from content string
	private static String correctInput(String input) {
		String result = input;
		String specials = "[^$.|?*+(){}";
		for (int i = 0; i < specials.length(); i++) {
			result = result.replaceAll("\\" + specials.charAt(i), "\\\\\\" + specials.charAt(i));
		}
		return result.replace("/", "\\/");
	}

	public static void getLinkFixer(String inputFile) {
		BadLinks badLinks = new BadLinks();
		List<LinkStruct> linkList;
		Database database = new Database();
		writeTo(sourceLog, "");
		writeTo(targetLog, "");
		try {
			// don't do anything if file doesn't exist
			File f = new File(inputFile);
			if (!f.exists() || f.isDirectory())
				return;

			File file = new File(inputFile);
			badLinks.findLinks(file);
			log.trace(badLinks);
			linkList = badLinks.getLinks();
			log.debug("XWiki broken links count:" + badLinks.getErrorCount());

			// do the actual fixing
			String prevParent = null;
			for (LinkStruct clink : linkList) {
				if (prevParent == null) {
					prevParent = clink.parentLink;
					continue;
				}
				if (clink.parentLink.equals(prevParent)) {
					// Update changes
					processData(clink, database);
				} else {
					prevParent = clink.parentLink;
					// COMMIT changes
				}

			}

			log.info("Done fixing!");

		} catch (IOException e) {
			log.error("LinkFixer exception!" + e);
		}

	}

	// a combination of reading/link fixing/writing
	public static StringBuilder processData(LinkStruct links, Database database) {
		String language = "";
		boolean fixed = false;
		boolean comment = false;
		if (links.parentLink.contains("?language=")) {
			try {
				language = links.parentLink.split("\\?language=")[1];
			} catch (Exception e) {
				log.error("Couldn't get language for link:" + links.parentLink);
			}
		}
		if (links.parentLink.contains("#Comments"))
			comment = true;
		String info = "";
		if (!"".equals(language))
			info = " lang:" + language;
		if (comment)
			info = info + ", comment";
		// Get document name
		String[] parts = links.parentLink.split("(/|\\?|#)");
		String fullName = null;
		for (int i = 1; i < parts.length; i++) {
			if (!parts[i].contains("=")) {
				fullName = parts[i - 1] + "." + parts[i];
			}
		}
		// Fix pages with hidden Main space
		fullName = fullName.replace(pagePrefix, "Main");

		log.debug(fullName + " : " + links.parentLink + info + " : " + links.realLink);
		content = new StringBuilder(database.getDocument(fullName, language));
		appendTo(sourceLog, fullName + "--------------------");
		appendTo(targetLog, fullName + "--------------------");
		appendTo(sourceLog, content.toString());

		fixAny(links.url);
		fixAny(links.realLink);
		if (links.realLink.contains("ftp") || links.realLink.contains(".")) {
			fixFTP(links.realLink);
		}
		appendTo(targetLog, content.toString());
		appendTo(targetLog, "--------------------");
		appendTo(sourceLog, "--------------------");
		return null;
		/*-
		//StringBuffer content = new StringBuffer(XWikiController.getPage(restLink));
		
		fixed = false;
		
		// switches between url and real url
		// if no fix has been made
		if (fixed == false) {
			// TODO content = new StringBuffer(XWikiController.getPage(restLink));
			fixAny(urlsList.get(index));
			if (fixed == false)
				fixAny(badLinksList.get(index));
		}
		
		if (fixed == false) {
			if (badLinksList.get(index).contains("ftp") || badLinksList.get(index).contains(".")) {
				fixFTP(namesList.get(index));
			}
		}
		
		split = inputFile.split("\\/");
		
		resultFileLocation = "/";
		
		for (int k = 0; k < split.length; k++) {
			if (!split[k].isEmpty() && k != split.length - 1) {
				resultFileLocation = resultFileLocation.concat(split[k] + "/");
			}
		}
		
		resultFileLocation = resultFileLocation.concat("fixResult.txt");
		
		// don't write if empty
		if (content.length() > 0 && fixed == true && dontChange == false) {
			// write the changes to text file
			// TODO TestUtility.writeTo(content, resultFileLocation);
			// push the changes to XWiki
			// TODO XWikiController.setPage(restLink, resultFileLocation, username,
			// password);
		}
		*/

	}

	public static StringBuilder getInput() {
		return content;
	}

	public static void setInput(StringBuilder inputIn) {
		content = inputIn;
	}

	// write to file
	public static void writeTo(String name, StringBuilder result) {
		writeTo(name, result.toString());
	}

	public static void writeTo(String name, String result) {
		File file = new File(name);
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			writer.write(result);
			writer.flush();
		} catch (Exception e) {
			log.error(e);
		}
	}

	// append to file
	public static void appendTo(String name, String content) {
		File file = new File(name);
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
			writer.append(content);
			writer.flush();
		} catch (Exception e) {
			log.error(e);
		}
	}

	// read from file
	public static StringBuilder readFrom(String name) {
		StringBuilder result = new StringBuilder();
		int c = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(name)))) {
			while ((c = reader.read()) != -1) {
				result.append((char) c);
			}
		} catch (IOException e) {
			log.error(e);
		}
		return result;
	}
}
