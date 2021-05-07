package org.xwiki4.test;

import static org.junit.Assert.assertEquals;
import static org.xwiki4.LinkFixer.readFrom;
import static org.xwiki4.LinkFixer.writeTo;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.xwiki4.LinkFixer;

public class LinkFixerLargeTest {

	public static final String noMatch = " does not match expected";
	public static final String noChange = " should not have changed text";
	public static final String defaultLink = "http://www.vardene.lv/index.php/Lokaliz%c4%93%c5%a1anas_rokasgr%c4%81mata";

	@Before
	public void setUp() {
		LinkFixer.setInput(readFrom("TestTxt/BibleTest.txt"));
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
		assertFiles(name, "Bible", noMatch);
	}

	public static void assertFiles(String name, String user, String message) {
		writeTo("TestTxt/" + name, LinkFixer.getInput());
		try {
			assertEquals(user + message,
					FileUtils.readFileToString(new File("TestTxt/Correct" + user + ".txt"), StandardCharsets.UTF_8),
					FileUtils.readFileToString(new File("TestTxt/" + name), StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
