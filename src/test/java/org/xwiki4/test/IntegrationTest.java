package org.xwiki4.test;

import org.junit.Test;
import org.xwiki4.LinkFixer;

public class IntegrationTest {

	@Test
	public void testGetFixer() {
		LinkFixer.getLinkFixer("TestTxt/" + "linkchecker.html", "", "");

		// TestUtility.writeTo(LinkFixer.getInput(), "TestTxt/" +
		// "getFixerResult.txt");
		// TestUtility.assertFiles("getFixerResult.txt", "GetFixer",
		// "GetFixer test failed, check that Main.GetFixer.WebHome.xar is set up on the
		// local XWiki");
		// LinkFixer.setDontChange(false);
	}

}
