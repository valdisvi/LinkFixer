package org.xwiki4;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BadLinks {

	private class BadLinksStruct {
		String parentLink;
		String realLink;
		String url;
		String name;

		public BadLinksStruct(String parentLink, String realLink, String url, String name) {
			this.parentLink = parentLink;
			this.realLink = realLink;
			this.url = url;
			this.name = name;
		}

		@Override
		public String toString() {
			return parentLink + " " + realLink + " " + url + " " + name;
		}

	}

	private List<BadLinksStruct> linkList = new LinkedList<>();

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
		String realLink = "";
		String url = "";
		String name = "";
		boolean first = true;
		for (Element link : links) {
			if (links.indexOf(link) % 2 != 0) {
				if (first) {
					System.err.println("!= 0");
					first = false;
				}
				realLink = link.attr("abs:href");
				// ---------------
				url = urlel.get(index).text().replaceAll("[`']", "");
				name = namel.get(index).text().replaceAll("[`']", "");
				BadLinksStruct badLinkStruct = new BadLinksStruct(parentLink, realLink, url, name);
				linkList.add(badLinkStruct);
				System.err.println(badLinkStruct);
			} else {
				if (first) {
					System.err.println("else");
					first = false;
				}
				parentLink = link.attr("abs:href");
				index++;
			}
		}
	}

	public int getErrorCount() {
		return linkList.size();
	}

	public List<String> getParentLinks() {
		List<String> list = new LinkedList<>();
		for (BadLinksStruct struct : linkList)
			list.add(struct.parentLink);
		return list;
	}

	public List<String> getRealLinks() {
		List<String> list = new LinkedList<>();
		for (BadLinksStruct struct : linkList)
			list.add(struct.realLink);
		return list;
	}

	public List<String> getUrls() {
		List<String> list = new LinkedList<>();
		for (BadLinksStruct struct : linkList)
			list.add(struct.url);
		return list;
	}

	public List<String> getNames() {
		List<String> list = new LinkedList<>();
		for (BadLinksStruct struct : linkList)
			list.add(struct.name);
		return list;
	}

	@Override
	public String toString() {
		return linkList.toString().replace("[", "").replace("]", "").replace(", ", "\n").replaceAll("$", "\n");
	}

}
