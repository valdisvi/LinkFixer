package org.xwiki4;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import static org.xwiki4.LinkFixer.getFullName;
import static org.xwiki4.LinkFixer.isInList;

public class BadLinks {

	private List<LinkStruct> linkList = new LinkedList<>();

	public void findLinks(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();
		findLinks(doc);
	}

	public void findLinks(File file) throws IOException {
		Document doc = Jsoup.parse(file, "UTF-8", "");
		findLinks(doc);
	}

	private void findLinks(Document doc) {
		Elements links = doc.select("a:nth-child(1)[target=top][href]");
		Elements urlel = doc.select("td.url:nth-child(2)");
		Elements namel = doc.select("tr:nth-child(2) td:nth-child(2)");
		int index = -1;
		String parentLink = "";
		for (Element link : links) {
			if (links.indexOf(link) % 2 == 0) {
				parentLink = link.attr("abs:href");
			} else {
				index++;
				String realLink = link.attr("abs:href");
				String url = urlel.get(index).text().replaceAll("[`']", "");
				String name = namel.get(index).text().replaceAll("[`']", "");
				LinkStruct badLinkStruct = new LinkStruct(parentLink, realLink, url, name);
				linkList.add(badLinkStruct);
			}
		}
		Collections.sort(linkList);
	}

	public int getErrorCount() {
		return linkList.size();
	}

	public List<LinkStruct> getLinks() {
		return linkList;
	}

	@Override
	public String toString() {
		return linkList.toString().replace("[", "").replace("]", "\n").replace(", ", "\n");
	}

	public static void main(String[] args) {
		String[] badPages = { "Main.LatvianKeyboard4", //
				"Main.qtp_home_en", //
				"Recipes.Apache", //
				"Recipes.Bubba", //
				"Recipes.Gitweb", //
				"Recipes.IP6", //
				"Recipes.JdeveloperWindows", //
				"Recipes.Jforum", //
				"Recipes.Limesurvey", //
				"Recipes.Piwik", //
				"Recipes.Postfix", //
				"Recipes.SquidReverseProxy", //
				"Training.NewInformaticsLectures", //
				"Blog.", //
				"Blog.Darbi", //
				"Challenges.", //
				"Main.LedgerSMB", //
				"Main.Recipes" //
		};
		BadLinks badLinks = new BadLinks();
		try {
			File file = new File("/home/valdis/Lejupielādes/linkchecker.html");
			badLinks.findLinks(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String prevName = null;
		String currName = null;
		LinkStruct prevStr = null;
		for (LinkStruct currStr : badLinks.getLinks()) {
			currName = getFullName(currStr.parentLink);
			if (isInList(currName, badPages)) {
				System.out.println(">" + currName);
				if (!currName.equals(prevName)) {
					System.out.println("\t1 " + prevStr.parentLink + " " + prevStr.realLink);
					System.out.println("1" + currName + ": " + currStr.parentLink);
				} else {
					System.out.println("\t2 " + currStr.realLink);
				}
			}
			prevName = currName;
			prevStr = currStr;
		}
	}

}
