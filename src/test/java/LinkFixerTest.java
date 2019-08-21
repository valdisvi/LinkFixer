import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.apache.commons.io.FileUtils;

public class LinkFixerTest {
	
	public static String noMatch = " does not match expected";
	public static String noChange = " should not have changed text";
	public static String defaultLink = "http://www.vardene.lv/index.php/Lokaliz%c4%93%c5%a1anas_rokasgr%c4%81mata";
	
	@Before
	public void setUp() {
		LinkFixer.setInput(FileManipulation.readTestFile());
	}

	private void assertFiles(String name, String user, String message) {
		FileManipulation.writeTestFile(LinkFixer.getInput(), name);
		try {
			assertEquals(user + message,
					FileUtils.readFileToString(new File("TestTxt/Correct" + user + ".txt"), "utf-8"),
					FileUtils.readFileToString(new File("TestTxt/" + name), "utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	@Test
	public void testImplicit() {
		String name = "implicit.txt";
		
		//fix needed
		LinkFixer.fixImplicit("https://www.xwiki.org");
		assertFiles(name, "Implicit", noMatch);
		
		//fix not needed
		LinkFixer.fixImplicit(defaultLink);
		assertFiles(name, "Implicit", noChange);
	}
	
	@Test
	public void testLabel() {
		String name = "label.txt";
		//fix needed
		LinkFixer.fixLabel(defaultLink);
		assertFiles(name, "Label", noMatch);

		//fix not needed
		LinkFixer.fixLabel(defaultLink);
		assertFiles(name, "Label", noChange);
	}

}
