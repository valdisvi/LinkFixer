package org.xwiki4;

import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.xwiki.rest.model.jaxb.Page;

/*
 * @author Edmunds Ozolins
 * Manages input/output to the XWiki
 * input is done by Jaxb
 * output is done by Curl
 */
public class XWikiController {

	public static String defaultPage = "http://10.0.0.40:8080/xwiki/rest/wikis/xwiki/spaces/TestPageLinkChecker/spaces/SimplePage/pages/WebHome";

	// get page using JAXB
	public static StringBuffer getPage(String url) {

		StringBuffer result = new StringBuffer();

		try {
			HttpClient httpClient = new HttpClient();
			JAXBContext context = JAXBContext.newInstance("org.xwiki.rest.model.jaxb");
			Unmarshaller unmarshaller = context.createUnmarshaller();
			GetMethod getMethod = new GetMethod(url);
			getMethod.addRequestHeader("Accept", "application/xml");
			httpClient.executeMethod(getMethod);
			Page page = (Page) unmarshaller.unmarshal(getMethod.getResponseBodyAsStream());

			result = new StringBuffer(page.getContent());

			return result;
		} catch (Exception e) {
			if (LinkFixer.getVerbose())
				System.out.println("Couldn't open:" + url);
			return new StringBuffer();
		}

	}

	// set page using curl
	public static void setPage(String url, String place, String username, String password) {
		if (username.isEmpty() || password.isEmpty()) {
			String[] parameters = { "curl", "-X", "PUT", "-T", place, "-H", "Content-Type: text/plain", url };
			Utils.executeCmd(parameters);
		} else {
			String[] parameters = { "curl", "-u " + username + ":" + password, "-X", "PUT", "-T", place, "-H",
					"Content-Type: text/plain", url };
			Utils.executeCmd(parameters);
		}
	}

}
