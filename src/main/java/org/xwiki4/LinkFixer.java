package org.xwiki4;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/*
 * @author Edmunds Ozolins
 * Uses regex to fix links, which are passed as an html file
 */

public class LinkFixer {

	private static StringBuffer input;
	private static ArrayList<String> badLinksList;
	private static ArrayList<String> locationsList;
	private static ArrayList<String> urlsList;
	private static ArrayList<String> namesList;
	private static boolean found; // whether a match has been found
	private static boolean fixed; // whether the match has been fixed
	private static boolean verbose = false; // whether to print out execution info
	private static boolean dontChange = false;
	private static String resultFileLocation = ""; // this is the result of the LinkFixer

	// fix url
	// plain implicit url
	// remove it
	public static void fixImplicit(String badLink) {
		String replacement = " ";

		try {
			Pattern pattern = Pattern.compile(
					"((\\h)*([^\\[]){1}(" + correctInput(badLink) + "){1}([^\\]]){1}(\\h)*){1}",
					Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(input);
			matcher.find();

			if (matcher.group(3).toString().equals("\n") || matcher.group(5).toString().equals("\n")
					|| matcher.group(3).toString().equals("\r") || matcher.group(5).toString().equals("\r")) {
				replacement = "\n";
			}
			;
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
			Matcher matcher = pattern.matcher(input);
			if (matcher.find()) {
				if (verbose)
					System.out.println("Found:" + badLink);
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
			Matcher matcher = pattern.matcher(input);
			if (matcher.find()) {
				if (verbose)
					System.out.println("Found:" + badLink);
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
			Matcher matcher = pattern.matcher(input);
			matcher.find();

			// restore newlines
			if (matcher.group(1) != null && matcher.group(1).toString().equals("\n"))
				replacement = "\n ";
			if (matcher.group(3) != null && matcher.group(3).toString().equals("\n"))
				replacement = " \n";
			if (matcher.group(1) != null && matcher.group(1).toString().equals("\r"))
				replacement = "\r ";
			if (matcher.group(3) != null && matcher.group(3).toString().equals("\r"))
				replacement = " \r";

			// if replacement is actually int replace with group
			try {
				Integer.parseInt(replacement);
				input = new StringBuffer(matcher.replaceAll(" " + matcher.group(Integer.parseInt(replacement)) + " "));
				found = true;
				if (verbose)
					System.out.println("Fixed!");
				fixed = true;
			} catch (NumberFormatException e) {
				// not a number just replace
				input = new StringBuffer(matcher.replaceAll(replacement));
				found = true;
				if (verbose)
					System.out.println("Fixed!");
				fixed = true;
			}
		} catch (IllegalStateException e) {
			// doesn't match
		}
	}

	// escape regex special characters from input string
	private static String correctInput(String input) {
		String result = input;
		String specials = "[^$.|?*+(){}";

		for (int i = 0; i < specials.length(); i++) {
			result = result.replaceAll("\\" + specials.charAt(i), "\\\\\\" + specials.charAt(i));
		}

		result.replaceAll("/", "\\/");

		return result;
	}

	public static void getLinkFixer(String inputFile, String username, String password) {
		BadLinks badLinks = new BadLinks();
		String restLink = ""; // ends with webhome
		String restLink2 = ""; // ends with last entry in link
		String restTail = "";
		String[] split;
		String[] split2;
		File resultFile;
		try {
			// don't do anything if file doesn't exist
			if (FileManipulation.fileExists(inputFile) == false)
				return;

			File file = new File(inputFile);
			badLinks.findLinks(file);
			badLinksList = new ArrayList(badLinks.getParentLinks());
			locationsList = new ArrayList(badLinks.getRealLinks());
			urlsList = new ArrayList(badLinks.getUrls());
			namesList = new ArrayList(badLinks.getNames());

			if (verbose)
				System.out.println("XWiki broken links count:" + badLinks.getErrorCount());

			// do the actual fixing
			for (int i = 0; i < badLinksList.size(); i++) {

				// form the rest link
				split = locationsList.get(i).split("/");
				restLink = split[0] + "//" + split[2] + "/" + split[3] + "/" + "rest/wikis/xwiki";
				restLink2 = split[0] + "//" + split[2] + "/" + split[3] + "/" + "rest/wikis/xwiki";
				restTail = "";
				for (int k = 6; k < split.length; k++) {
					// note that there are two types of restlinks
					// split2 is used to append parameters
					// put with ?
					// restLink2 mirrors restLink most of the time
					if (split[k].contains("?") && split[k].charAt(0) != '?') {
						split2 = split[k].split("\\?");
						restLink += "/spaces/" + split2[0];
						restLink2 += "/spaces/" + split2[0];
						restTail += "?" + split2[1];
					} else if (split[k].charAt(0) != '?') {
						restLink += "/spaces/" + split[k];
						restLink2 += "/spaces/" + split[k];
					} else {
						restTail = split[k];
					}
				}

				restLink += "/pages/WebHome" + restTail;
				restLink2 += restTail;

				// double reverse to replace last
				restLink2 = StringUtils.reverse(StringUtils.reverse(restLink2).replaceFirst("secaps", "segap"));

				found = false;

				processData(inputFile, restLink, username, password, i);
				// try with differently formed page if found == false
				if (found == false) {
					processData(inputFile, restLink2, username, password, i);
				}

			}

			if (verbose)
				System.out.println("Done fixing!");

		} catch (IOException e) {
			System.err.println("LinkFixer exception!");
			e.printStackTrace();
		} finally {
			// clean up the results
			resultFile = new File(resultFileLocation);
			resultFile.delete();
		}

	}

	// a combination of reading/link fixing/writing
	public static void processData(String inputFile, String restLink, String username, String password, int index) {

		String[] split;
		String translationLink;

		input = new StringBuffer(XWikiController.getPage(restLink));

		fixed = false;

		// switches between url and real url
		// if no fix has been made
		if (fixed == false) {
			input = new StringBuffer(XWikiController.getPage(restLink));
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
		if (input.length() > 0 && fixed == true && dontChange == false) {
			// write the changes to text file
			FileManipulation.writeTo(input, resultFileLocation);
			// push the changes to XWiki
			XWikiController.setPage(restLink, resultFileLocation, username, password);
		}

	}

	public static StringBuffer getInput() {
		return input;
	}

	public static void setInput(StringBuffer inputIn) {
		input = inputIn;
	}

	public static boolean getVerbose() {
		return verbose;
	}

	public static void setVerbose(boolean value) {
		verbose = value;
	}

	public static boolean getDontChange() {
		return dontChange;
	}

	public static void setDontChange(boolean value) {
		dontChange = value;
	}

}
