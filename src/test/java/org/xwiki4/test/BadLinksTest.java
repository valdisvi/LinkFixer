package org.xwiki4.test;

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
		Assert.assertEquals(958, badLinks.getErrorCount());
		Assert.assertEquals(badLinks.getUrls().size(), badLinks.getNames().size());
		Assert.assertEquals(badLinks.getParentLinks().size(), badLinks.getRealLinks().size());
	}

	@Test
	public void test02FindLinksLocal() {
		BadLinks badLinks = new BadLinks();
		try {
			File file = new File("src/test/resources/badlinks.html");
			badLinks.findLinks(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Assert.assertEquals(18, badLinks.getErrorCount());
		Assert.assertEquals(badLinks.getUrls().size(), badLinks.getErrorCount());
		Assert.assertEquals(badLinks.getUrls().size(), badLinks.getNames().size());
		Assert.assertEquals(badLinks.getParentLinks().size(), badLinks.getRealLinks().size());
	}

}
