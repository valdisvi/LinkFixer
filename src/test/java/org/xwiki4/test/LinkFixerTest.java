package org.xwiki4.test;

import org.junit.Before;
import org.junit.Test;
import org.xwiki4.FileManipulation;
import org.xwiki4.LinkFixer;

public class LinkFixerTest {
	
	public static String noMatch = " does not match expected";
	public static String noChange = " should not have changed text";
	public static String defaultLink = "http://www.vardene.lv/index.php/Lokaliz%c4%93%c5%a1anas_rokasgr%c4%81mata";
	
	@Before
	public void setUp() {
		LinkFixer.setInput(FileManipulation.readTestFile("TestTxt/Test1.txt"));
	}
	
	@Test
	public void testImplicit() {
		String name = "implicit.txt";
		
		//fix needed
		LinkFixer.fixImplicit("https://www.xwiki.org");
		TestUtility.assertFiles(name, "Implicit", noMatch);
		
		//fix not needed
		LinkFixer.fixImplicit(defaultLink);
		TestUtility.assertFiles(name, "Implicit", noChange);
	}
		
	@Test
	public void testExplicit() {
		String name = "explicit.txt";
		//fix needed
		LinkFixer.fixExplicit("https://www.google.com/");
		TestUtility.assertFiles(name, "Explicit", noMatch);
		
		//fix not needed
		LinkFixer.fixExplicit(defaultLink);
		TestUtility.assertFiles(name, "Explicit", noChange);
	}

	@Test
	public void testLabel() {
		String name = "label.txt";
		//fix needed
		LinkFixer.fixLabel(defaultLink);
		TestUtility.assertFiles(name, "Label", noMatch);

		//fix not needed
		LinkFixer.fixLabel("https://www.xwiki.org");
		TestUtility.assertFiles(name, "Label", noChange);
	}
	
	//should be fixed by label fixer
	@Test
	public void testEmail() {
		String name = "email.txt";
		//fix needed
		LinkFixer.fixLabel("mailto:someone@somewhere.com");
		TestUtility.assertFiles(name, "Email", noMatch);
		
		//fix not needed
		LinkFixer.fixLabel("mailto:noone@nowhere.com");
		TestUtility.assertFiles(name, "Email", noChange);
	}
	
	@Test
	public void testImage() {
		String name = "image.txt";
		//fix needed
		LinkFixer.fixImage("https://www.w3schools.com/js/tryit.asp?filename=tryjs_randomlink");
		TestUtility.assertFiles(name, "Image", noMatch);
		
		//fix not needed
		LinkFixer.fixImage(defaultLink);
		TestUtility.assertFiles(name, "Image", noChange);
	}
	
	@Test
	public void testLinkOnImage() {
		String name = "linkImage.txt";
		
		//fix needed
		LinkFixer.fixLabel("https://odo.lv/Blog/?language=en");
		TestUtility.assertFiles(name, "LinkImage", noMatch);
		
		//fix not needed
		LinkFixer.fixLabel("https://www.w3schools.com/js/tryit.asp?filename=tryjs_randomlink");
		TestUtility.assertFiles(name, "LinkImage", noChange);
	}
	
	@Test
	public void testAttach() {
		String name = "attach.txt";
		
		//fix needed
		LinkFixer.fixAttach("cat.jpg");
		TestUtility.assertFiles(name, "Attach", noMatch);
		
		//fix not needed
		LinkFixer.fixAttach(defaultLink);
		TestUtility.assertFiles(name, "Attach", noChange);
	}
	
	@Test
	public void testNewWindow() {
		String name = "new";
		
		//fix needed
		LinkFixer.fixNewWindow("https://www.cnet.com/how-to/nine-tools-that-let-you-randomly-browse-the-web/");
		TestUtility.assertFiles(name, "New", noMatch);
		
		//fix not needed
		LinkFixer.fixNewWindow("https://www.xwiki.org");
		TestUtility.assertFiles(name, "New", noChange);
	}
		
}
