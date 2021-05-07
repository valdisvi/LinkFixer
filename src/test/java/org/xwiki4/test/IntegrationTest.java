package org.xwiki4.test;

import static org.junit.Assert.assertEquals;
import static org.xwiki4.LinkFixer.appendTo;
import static org.xwiki4.LinkFixer.readFrom;
import static org.xwiki4.LinkFixer.writeTo;

import org.junit.Test;
import org.xwiki4.LinkFixer;

public class IntegrationTest {

	@Test
	public void testReadWrite() {
		writeTo("logs/test.log", "");
		appendTo("logs/test.log", "test");
		String result = readFrom("logs/test.log").toString();
		assertEquals("test", result);
	}

	@Test
	public void testGetFixer() {
		LinkFixer.getLinkFixer("TestTxt/" + "linkchecker.html");

		// TestUtility.writeTo(LinkFixer.getInput(), "TestTxt/" +
		// "getFixerResult.txt");
		// TestUtility.assertFiles("getFixerResult.txt", "GetFixer",
		// "GetFixer test failed, check that Main.GetFixer.WebHome.xar is set up on the
		// local XWiki");
		// LinkFixer.setDontChange(false);
	}

}
