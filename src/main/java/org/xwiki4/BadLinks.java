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

}
