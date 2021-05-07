package org.xwiki4;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
	}

	private List<BadLinksStruct> linkList = new LinkedList<>();

	private ArrayList<String> parentLinks = new ArrayList<String>();
	private ArrayList<String> realLinks = new ArrayList<String>();
	private ArrayList<String> urls = new ArrayList<String>();
	private ArrayList<String> names = new ArrayList<String>();

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
		for (Element link : links) {
			if (links.indexOf(link) % 2 != 0) {
				realLinks.add(link.attr("abs:href"));
			} else {
				parentLinks.add(link.attr("abs:href"));
				index++;
				this.urls.add(urlel.get(index).text().replaceAll("[`']", ""));
				this.names.add(namel.get(index).text().replaceAll("[`']", ""));
			}
		}
	}

	public int getErrorCount() {
		return realLinks.size();
	}

	public ArrayList<String> getParentLinks() {
		return parentLinks;
	}

	public ArrayList<String> getRealLinks() {
		return realLinks;
	}

	public ArrayList<String> getUrls() {
		return urls;
	}

	public ArrayList<String> getNames() {
		return names;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < parentLinks.size(); i++) {
			builder.append(parentLinks.get(i) + " " + realLinks.get(i) + " " + urls.get(i) + " " + names.get(i) + "\n");
		}
		return builder.toString();
	}

}
