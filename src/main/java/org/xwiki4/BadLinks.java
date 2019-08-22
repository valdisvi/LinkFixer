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

	public void findLinks (String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a:nth-child(1)[target=top][href]");
        for (Element link : links) {
        	if(links.indexOf(link)%2 != 0)
        		parentLinks.add(link.attr("abs:href"));
        	else
        		realLinks.add(link.attr("abs:href"));
        }
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
	}
	public ArrayList<String> getParentLinks () {
		return parentLinks;
	}
	public ArrayList<String> getRealLinks () {
		return realLinks;
	}
	public void printLinks () {
        for(int i=0; i<realLinks.size(); i++) {
        	System.out.println("Parent URL: " + parentLinks.get(i) + "  Real URL: " + realLinks.get(i));
        }
    }
}
