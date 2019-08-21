package org.xwiki4.test;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.xwiki4.FileManipulation;
import org.xwiki4.LinkFixer;

public class LinkFixerLargeTest {

	public static String noMatch = " does not match expected";
	public static String noChange = " should not have changed text";
	public static String defaultLink = "http://www.vardene.lv/index.php/Lokaliz%c4%93%c5%a1anas_rokasgr%c4%81mata";
	
	@Before
	public void setUp() {
		LinkFixer.setInput(FileManipulation.readTestFile("TestTxt/BibleTest.txt"));
	}
	
	//large test
	//test some link fixing in Bible
	@Test
	public void testLarge() {
		String name = "bible.txt";
		
		LinkFixer.fixLabel(defaultLink);
		LinkFixer.fixExplicit("https://www.xwiki.org");
		LinkFixer.fixImplicit("https://www.google.com");
		TestUtility.assertFiles(name, "Bible", noMatch);
	}

}
