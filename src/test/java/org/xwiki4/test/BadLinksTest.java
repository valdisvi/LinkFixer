package org.xwiki4.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.xwiki4.BadLinks;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BadLinksTest {

	@Test
	public void test01FindLinks() {
		BadLinks badLinks = new BadLinks();
		try {
			badLinks.findLinks("https://odo.lv/xwiki/bin/download/Challenges/WebHome/linkchecker.html");
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals(958, badLinks.getErrorCount());
	}

	@Test
	public void test02FindLinksLocal() {
		BadLinks badLinks = new BadLinks();
		try {
			File file = new File("src/test/resources/badlinks.html");
			badLinks.findLinks(file);
			System.out.println(badLinks);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals(18, badLinks.getErrorCount());
		String badLinksString = "" //
				+ "http://localhost:8080/xwiki/bin/view/TestPageLinkChecker/SimplePage/ http://www.eclipse.org/gmt/ http://www.eclipse.org/gmt/ http://www.eclipse.org/gmt/\n"
				+ "http://localhost:8080/xwiki/bin/view/TestPageLinkChecker/SimplePage/ http://www.eclipse.org/gmt/ http://www.eclipse.org/gmt/ http://www.eclipse.org/gmt/\n"
				+ "http://localhost:8080/xwiki/bin/view/TestPageLinkChecker/SimplePage/ http://www.eclipse.org/gmt/ http://www.eclipse.org/gmt/ http://www.eclipse.org/gmt/\n"
				+ "http://localhost:8080/xwiki/bin/view/TestPageLinkChecker/SimplePage/ http://www.eclipse.org/gmt/ http://www.eclipse.org/gmt/ http://www.eclipse.org/gmt/\n"
				+ "http://localhost:8080/xwiki/bin/view/TestPageLinkChecker/SimplePage/ http://www.eclipse.org/gmt/ http://www.eclipse.org/gmt/ http://www.eclipse.org/gmt/\n"
				+ "http://localhost:8080/xwiki/bin/view/TestPageLinkChecker/SimplePage/ http://www.eclipse.org/gmt/ http://www.eclipse.org/gmt/ http://www.eclipse.org/gmt/\n"
				+ "http://localhost:8080/xwiki/bin/view/TestPageLinkChecker/SimplePage/ http://www.eclipse.org/gmt/ http://www.eclipse.org/gmt/ http://www.eclipse.org/gmt/\n"
				+ "http://localhost:8080/xwiki/bin/view/TestPageLinkChecker/SimplePage/ http://www.eclipse.org/gmt/ http://www.eclipse.org/gmt/ label\n"
				+ "http://localhost:8080/xwiki/bin/view/TestPageLinkChecker/SimplePage/ http://www.eclipse.org/gmt/ http://www.eclipse.org/gmt/ label\n"
				+ "http://localhost:8080/xwiki/bin/view/TestPageLinkChecker/SimplePage/ http://www.eclipse.org/gmt/ http://www.eclipse.org/gmt/ bold label\n"
				+ "http://localhost:8080/xwiki/bin/view/TestPageLinkChecker/SimplePage/ http://www.eclipse.org/gmt/ http://www.eclipse.org/gmt/ http://www.eclipse.org/gmt/\n"
				+ "http://localhost:8080/xwiki/bin/view/TestPageLinkChecker/SimplePage/ http://www.eclipse.org/gmt/ http://www.eclipse.org/gmt/ http://www.eclipse.org/gmt/\n"
				+ "http://localhost:8080/xwiki/bin/view/TestPageLinkChecker/SimplePage/ https://precinso.ga https://precinso.ga https://precinso.ga\n"
				+ "http://localhost:8080/xwiki/bin/view/TestPageLinkChecker/SimplePage/ https://precinso.ga/ https://precinso.ga/ https://precinso.ga/\n"
				+ "http://localhost:8080/xwiki/bin/view/TestPageLinkChecker/SimplePage/ https://svn.xwiki.org/ https://svn.xwiki.org/ https://svn.xwiki.org/\n"
				+ "http://localhost:8080/xwiki/bin/view/TestPageLinkChecker/SimplePage/ https://www.bpmi.org/bpml-spec.htm https://www.bpmi.org/bpml-spec.htm https://www.bpmi.org/bpml-spec.htm\n"
				+ "http://localhost:8080/xwiki/bin/view/TestPageLinkChecker/SimplePage/ https://www.vardene.lv/index.php/Lokaliz%C4%93%C5%A1anas_rokasgr%C4%81mata/ https://www.vardene.lv/index.php/Lokaliz%c4%93%c5%a1anas_rokasgr%c4%81mata/ https://www.vardene.lv/index.php/Lokaliz%c4%93%c5%a1anas_rokasgr%c4%81mata/\n"
				+ "http://localhost:8080/xwiki/bin/view/TestPageLinkChecker/SimplePage/ https://www.xwiki.org/xwiki/webjars/wiki%3Axwiki/xwiki-platform-tree-webjar/8.4.4/throbber https://www.xwiki.org/xwiki/webjars/wiki%3Axwiki/xwiki-platform-tree-webjar/8.4.4/throbber https://www.xwiki.org/xwiki/webjars/wiki%3Axwiki/xwiki-platform-tree-webjar/8.4.4/throbber\n";
		assertEquals(badLinksString, badLinks.toString());
	}

}
