package org.xwiki4.test;

import org.junit.Before;
import org.junit.Test;
import org.xwiki4.LinkFixer;

/*
 * @author Edmunds Ozolins
 */
public class LinkFixerLargeTest {

	public static final String noMatch = " does not match expected";
	public static final String noChange = " should not have changed text";
	public static final String defaultLink = "http://www.vardene.lv/index.php/Lokaliz%c4%93%c5%a1anas_rokasgr%c4%81mata";

	@Before
	public void setUp() {
		LinkFixer.setInput(TestUtility.readTestFile("TestTxt/BibleTest.txt"));
	}

	// large test
	// test some link fixing in Bible
	@Test
	public void testLarge() {
		String name = "bible.txt";

		// need fixing
		LinkFixer.fixAny(defaultLink);
		LinkFixer.fixAny("https://www.xwiki.org");
		LinkFixer.fixAny("https://www.google.com");
		LinkFixer.fixAny("https://www.w3schools.com/js/tryit.asp?filename=tryjs_randomlink");
		// don't need fixing
		LinkFixer.fixExplicit(defaultLink);
		LinkFixer.fixImplicit(defaultLink);
		LinkFixer.fixImage(defaultLink);
		TestUtility.assertFiles(name, "Bible", noMatch);
	}

}
