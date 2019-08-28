package org.xwiki4;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class BadLinks {
	
	private ArrayList<String> parentLinks = new ArrayList<String>();
	private ArrayList<String> realLinks = new ArrayList<String>();
	private ArrayList<String> urls = new ArrayList<String>();
	private ArrayList<String> names = new ArrayList<String>();

	public void findLinks (String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a:nth-child(1)[target=top][href]");
        for (Element link : links) {
        	if(links.indexOf(link)%2 != 0)
        		parentLinks.add(link.attr("abs:href"));
        	else
        		realLinks.add(link.attr("abs:href"));
        }
        findUrls(url);
        findNames(url);
	}
	public void findLinksLocal (File url) throws IOException {
        Document doc = Jsoup.parse(url, "UTF-8", "");
        Elements links = doc.select("a:nth-child(1)[target=top][href]");
        for (Element link : links) {
        	if(links.indexOf(link)%2 != 0)
        		parentLinks.add(link.attr("abs:href"));
        	else
        		realLinks.add(link.attr("abs:href"));
        }
        findUrlsLocal(url);
        findNamesLocal(url);
	}
	
	public void findUrls (String html) throws IOException {
        Document doc = Jsoup.connect(html).get();
        Elements urls = doc.select("td.url:nth-child(2)");
        for (Element url : urls) {
        	this.urls.add(url.text().replaceAll("[`']", ""));
        }
	}
	
	public void findUrlsLocal (File html) throws IOException {
		Document doc = Jsoup.parse(html, "UTF-8", "");
        Elements urls = doc.select("td.url:nth-child(2)");
        for (Element url : urls) {
        	this.urls.add(url.text().replaceAll("[`']", ""));
        }
	}
	
	public void findNames (String html) throws IOException {
        Document doc = Jsoup.connect(html).get();
        Elements names = doc.select("tr:nth-child(2) td:nth-child(2)");
        for (Element name : names) {
        	this.names.add(name.text().replaceAll("[`']", ""));
        }
	}
	
	public void findNamesLocal (File html) throws IOException {
		Document doc = Jsoup.parse(html, "UTF-8", "");
        Elements names = doc.select("tr:nth-child(2) td:nth-child(2)");
        for (Element name : names) {
        	this.names.add(name.text().replaceAll("[`']", ""));
        }
	}
	
	public int getErrorCount() {
		return parentLinks.size();
	}
	
	public ArrayList<String> getParentLinks () {
		return parentLinks;
	}
	
	public ArrayList<String> getRealLinks () {
		return realLinks;
	}
	
	public ArrayList<String> getUrls () {
		return urls;
	}
	
	public ArrayList<String> getNames () {
		return names;
	}
}
