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
import org.junit.Test;

public class LinkFixer {

	private static Logger log = Logger.getLogger(LinkFixer.class);
	private static StringBuilder content; // Is needed to reuse existing tests and implementation!
	private static final String pagePrefix = "odo.lv";
	private static final String sourceLog = "logs/source.log";
	private static final String targetLog = "logs/target.log";
	private static final String[] excludePages = { //
			"Training.JavaSecurityWorkshop", //
			"Training.NewInformaticsLectures", //
			"Recipes.Apache", //
			"Recipes.Bubba", //
			"Recipes.Gitweb", //
			"Recipes.HTSEngine", //
			"Recipes.IP6", //
			"Recipes.Jforum", //
			"Recipes.Limesurvey", //
			"Recipes.Piwik", //
			"Recipes.Postfix", //
			"Recipes.SquidReverseProxy", //
			"Main.qtp_home_en", //
			"Main.LatvianKeyboard4", //
			"Recipes.JdeveloperWindows" //
	};

	/**
	 * fix plain implicit url — remove it
	 * 
	 * @param badLink
	 */
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

	/**
	 * fix explicit url surrounded by [[]] remove it
	 * 
	 * @param badLink
	 */
	public static void fixExplicit(String badLink) {
		matchAndReplace("(\\h)*(\\[)\\2{1}(" + correctInput(badLink) + "){1}(\\])\\4{1}(\\h)*", " ");
	}

	/**
	 * fix label [[label>>link]] — replace with label also deals with the form
	 * [[image:link>>link]] — delete that one
	 * 
	 * @param badLink
	 */
	public static void fixLabel(String badLink) {
		matchAndReplace("((\\h)*(\\[){1}\\3{1}(image:){1}([^\\[]*?)(>>([^\\[]*?)" + correctInput(badLink)
				+ "){1}(\\]){1}\\8{1}(\\h)*){1}", " ");
		matchAndReplace(
				"((\\h)*(\\[){1}\\3{1}([^\\[]*?)(>>([^\\[]*?)" + correctInput(badLink) + "){1}(\\]){1}\\7{1}(\\h)*){1}",
				"4");
	}

	/**
	 * fix image [[image:link]] — remove it
	 * 
	 * @param badLink
	 */
	public static void fixImage(String badLink) {
		matchAndReplace("((\\h)*(\\[){1}\\3{1}(image:){1}(" + correctInput(badLink) + "){1}(\\]){1}\\6{1}(\\h)*){1}",
				" ");
	}

	/**
	 * fix attach [[attach:img.png]] replace it with part before .png
	 * 
	 * @param badLink
	 */
	public static void fixAttach(String badLink) {
		// match the part before .png
		Pattern pattern = Pattern.compile("(.*)(\\.)(.*)", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(badLink);
		matcher.find();
		matchAndReplace("((\\h)*(\\[){1}\\3{1}(attach:){1}(" + correctInput(badLink) + "){1}(\\]){1}\\6{1}(\\h)*){1}",
				" " + matcher.group(1) + " ");
	}

	/**
	 * fix url [[url:link]] — delete it
	 * 
	 * @param badLink
	 */
	public static void fixUrl(String badLink) {
		matchAndReplace("((\\h)*(\\[){1}\\3{1}(url:){1}(" + correctInput(badLink) + "){1}(\\]){1}\\6{1}(\\h)*){1}",
				" ");
	}

	/**
	 * fix new window [[link||target="_blank"]] — remove it
	 * 
	 * @param badLink
	 */
	public static void fixNewWindow(String badLink) {
		matchAndReplace(
				"(\\h)*(\\[)\\2{1}([^\\[]*?)(" + correctInput(badLink) + "){1}(||){1}([^\\[]*?)(\\])\\7{1}(\\h)*", " ");
	}

	/**
	 * fix ftp {{some file="location"}} — replace with name
	 * 
	 * @param badLink
	 */
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

	/**
	 * decide where the string should go
	 * 
	 * @param badLink
	 */
	public static boolean fixAny(String badLink) {
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
				// TODO fixFTP(badLink);
				return true;
			}
		} catch (Exception e) {
			// nothing
		}
		return false;
	}

	/**
	 * does replacements and matching - needs the pattern and the replacement for
	 * the pattern can pass an int X as a number to specify that X found group is
	 * the replacement
	 * 
	 * @param stringPattern
	 * @param replacement
	 */
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

	private static void patchResult() {
		Pattern p;
		Matcher m;
		// delete _.
		p = Pattern.compile(" +\\.(\\W)");
		m = p.matcher(content);
		content = new StringBuilder(m.replaceAll(".$1"));
		// delete _,
		p = Pattern.compile(" +,");
		m = p.matcher(content);
		content = new StringBuilder(m.replaceAll(","));
		// delete _)
		p = Pattern.compile(" +\\)");
		m = p.matcher(content);
		content = new StringBuilder(m.replaceAll(")"));
		// delete (_
		p = Pattern.compile("\\( +");
		m = p.matcher(content);
		content = new StringBuilder(m.replaceAll("("));
		// delete _]
		p = Pattern.compile(" +\\]");
		m = p.matcher(content);
		content = new StringBuilder(m.replaceAll("]"));
		// delete [_
		p = Pattern.compile("\\[ +");
		m = p.matcher(content);
		content = new StringBuilder(m.replaceAll("["));
		// Fix link https:~~/~~/
		p = Pattern.compile("https:~+/~+/");
		m = p.matcher(content);
		content = new StringBuilder(m.replaceAll("https://"));
		// Fix link http:~~/~~/
		p = Pattern.compile("http:~+/~+/");
		m = p.matcher(content);
		content = new StringBuilder(m.replaceAll("http://"));
		// Fix link http:~/
		p = Pattern.compile("http:~+/");
		m = p.matcher(content);
		content = new StringBuilder(m.replaceAll("http:/"));
		// delete ^_
		p = Pattern.compile("^ +", Pattern.MULTILINE);
		m = p.matcher(content);
		content = new StringBuilder(m.replaceAll(""));
		// delete _$
		p = Pattern.compile(" +$", Pattern.MULTILINE);
		m = p.matcher(content);
		content = new StringBuilder(m.replaceAll(""));
		// delete ^*_$
		p = Pattern.compile("^\\* *?$", Pattern.MULTILINE);
		m = p.matcher(content);
		content = new StringBuilder(m.replaceAll(""));

	}

	/**
	 * escape regex special characters from content string
	 * 
	 * @param input
	 * @return
	 */
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
		writeTo(sourceLog, "");
		writeTo(targetLog, "");
		try {
			// don't do anything if file doesn't exist
			File file = new File(inputFile);
			if (!file.exists() || file.isDirectory())
				return;
			badLinks.findLinks(file);
			log.trace(badLinks);
			linkList = badLinks.getLinks();
			log.debug("XWiki broken links count:" + badLinks.getErrorCount());
			processData(linkList);
			log.info("Done fixing!");
		} catch (IOException e) {
			log.error("LinkFixer exception!" + e);
		}

	}

	/**
	 * Get language of the document from link
	 * 
	 * @param link
	 * @return
	 */
	static String getLanguage(String link) {
		String language = "";
		if (link.contains("?language=")) {
			try {
				language = link.split("\\?language=")[1];
			} catch (Exception e) {
				log.error("Couldn't get language for link:" + link);
			}
		}
		return language;
	}

	/**
	 * Get full document name from link
	 * 
	 * @param link
	 * @return
	 */
	static String getFullName(String link) {
		String[] parts = link.split("(/|\\?|#)");
		String fullName = null;
		for (int i = 1; i < parts.length; i++) {
			if (!parts[i].contains("=")) {
				fullName = parts[i - 1] + "." + parts[i];
			}
		}
		// Fix pages with hidden Main space
		fullName = fullName.replace(pagePrefix, "Main");
		return fullName;
	}

	static boolean isExcludedPage(String fullName) {
		for (String excluded : excludePages) {
			if (fullName.equals(excluded))
				return true;
		}
		return false;
	}

	/**
	 * combination of reading/link fixing/writing
	 * 
	 * @param linkList
	 */
	public static void processData(List<LinkStruct> linkList) {
		Database database = new Database();
		String currLang = "";
		String prevLang = null;
		String fullName = null;
		String prevName = null;
		int i = 0;
		for (LinkStruct clink : linkList) {
			fullName = getFullName(clink.parentLink);
			if (isExcludedPage(fullName)) {
				log.warn(fullName + " page is excluded");
				continue;
			}
			currLang = getLanguage(clink.parentLink);
			if (prevName == null) {
				prevName = fullName;
				prevLang = currLang;
				content = new StringBuilder(database.getDocument(fullName, currLang)); // get content for first entry
				appendTo(sourceLog, "\n" + fullName + "--------------------\n");
				appendTo(sourceLog, content.toString());
				continue;
			}
			if (fullName == null) {
				log.error("Couldn't get fullName for: " + clink.parentLink);
				continue;
			}
			if (clink.parentLink.contains("#Comments")) {// don't deal with comments
				log.warn("Skipped fixing for: " + clink.parentLink);
				continue;
			}
			// continue with current document
			if (fullName.equals(prevName)) {
				log.debug(fullName + ": " + i + ": " + clink.realLink);
				if (!fixAny(clink.url))
					fixAny(clink.realLink);
				/*- TODO
				if (clink.realLink.contains("ftp") || clink.realLink.contains(".")) {
					fixFTP(clink.realLink);
				}
				*/
				patchResult();
				i++;
			} else { // Finish previous and start new document
				i = 0;
				// Write back previous document to database
				database.putDocument(prevName, prevLang, content.toString());
				// Log changes
				appendTo(targetLog, "\n" + prevName + "--------------------\n");
				appendTo(targetLog, content.toString()); // modified content
				// New document
				log.debug("New: " + fullName + ":" + currLang);
				content = new StringBuilder(database.getDocument(fullName, currLang)); // get new content
				appendTo(sourceLog, "\n" + fullName + "--------------------\n");
				appendTo(sourceLog, content.toString());
			}
			prevName = fullName;
			prevLang = currLang;
		}
		// Deal with last entry
		// Write back previous document to database
		database.putDocument(prevName, prevLang, content.toString());
		appendTo(targetLog, "\n" + prevName + "--------------------\n");
		appendTo(targetLog, content.toString()); // modified content
	}

	public static StringBuilder getInput() {
		return content;
	}

	public static void setInput(StringBuilder inputIn) {
		content = inputIn;
	}

	/**
	 * write to file
	 * 
	 * @param name
	 * @param result
	 */
	public static void writeTo(String name, StringBuilder result) {
		writeTo(name, result.toString());
	}

	/**
	 * write to file
	 * 
	 * @param name
	 * @param result
	 */
	public static void writeTo(String name, String result) {
		File file = new File(name);
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			writer.write(result);
			writer.flush();
		} catch (Exception e) {
			log.error(e);
		}
	}

	/**
	 * append to file
	 * 
	 * @param name
	 * @param content
	 */
	public static void appendTo(String name, String content) {
		File file = new File(name);
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
			writer.append(content);
			writer.flush();
		} catch (Exception e) {
			log.error(e);
		}
	}

	/**
	 * read from file
	 * 
	 * @param name
	 * @return
	 */
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

	/**
	 * Start fixing links here!
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		LinkFixer.getLinkFixer("/home/valdis/Lejupielādes/linkchecker.html");
	}
}
