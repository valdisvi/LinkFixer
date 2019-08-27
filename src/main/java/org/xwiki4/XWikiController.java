package org.xwiki4;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.xwiki.rest.model.jaxb.Page;

public class XWikiController {

	public static String defaultPage = "http://localhost:8080/xwiki/rest/wikis/xwiki/spaces/TestPageLinkChecker/spaces/SimplePage/pages/WebHome";
	
	public static void main(String[] args) {
		getPage("");
	}
	
	//get page using JAXB
	public static StringBuffer getPage(String url) {
		
		StringBuffer result;
		
		try {
			HttpClient httpClient = new HttpClient();
			JAXBContext context = JAXBContext.newInstance("org.xwiki.rest.model.jaxb");
			Unmarshaller unmarshaller = context.createUnmarshaller();
			GetMethod getMethod = new GetMethod(
					url);
			getMethod.addRequestHeader("Accept", "application/xml");
			httpClient.executeMethod(getMethod);
			Page page = (Page) unmarshaller.unmarshal(getMethod.getResponseBodyAsStream());
				
			result = new StringBuffer(page.getContent());
									
			return result;
		} catch (Exception e) {
			System.out.println("Couldn't open:" + url);
			return new StringBuffer();
		}	
		
		
	}
	
	//set page using curl
	public static void setPage(String url, String place) {
		String[] parameters = {"curl", "-X", "PUT", "-T", place,
				"-H", "Content-Type: text/plain", url};	
		Utils.executeCmd(parameters);
	}

}
