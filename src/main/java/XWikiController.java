import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.xwiki.rest.model.jaxb.Page;

public class XWikiController {


	public static void main(String[] args) {

		try {
			HttpClient httpClient = new HttpClient();
			JAXBContext context = JAXBContext.newInstance("org.xwiki.rest.model.jaxb");
			Unmarshaller unmarshaller = context.createUnmarshaller();

			GetMethod getMethod = new GetMethod("http://localhost:8080/xwiki/rest/wikis/xwiki/spaces/WebHome/pages/WebHome");
			getMethod.addRequestHeader("Accept", "application/xml");
			httpClient.executeMethod(getMethod);
			Page page = (Page) unmarshaller.unmarshal(getMethod.getResponseBodyAsStream());
			System.out.println(page.toString());
			System.out.println(page.getFullName());
			System.out.println(page.getContent());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
