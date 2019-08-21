package org.xwiki4;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class BadLinks {

	public static void main(String[] args) throws IOException {
        String url = "https://odo.lv/xwiki/bin/download/Challenges/WebHome/linkchecker.html";

        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a:nth-child(1)[target=top][href]");
		ArrayList<String> parentLinks = new ArrayList<>();
		ArrayList<String> realLinks = new ArrayList<>();
        for (Element link : links) {
        	if(links.indexOf(link)%2 != 0)
        		parentLinks.add(link.attr("abs:href"));
        	else
        		realLinks.add(link.attr("abs:href"));
        }
        for(int i=0; i<parentLinks.size(); i++) {
        	System.out.println("Parent URL: " + parentLinks.get(i) + "  Real URL: " + realLinks.get(i));
        }
    }
}
