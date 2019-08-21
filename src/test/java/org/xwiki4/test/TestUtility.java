package org.xwiki4.test;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.xwiki4.FileManipulation;
import org.xwiki4.LinkFixer;

public class TestUtility {

	public static void assertFiles(String name, String user, String message) {
		FileManipulation.writeTestFile(LinkFixer.getInput(), name);
		try {
			assertEquals(user + message,
					FileUtils.readFileToString(new File("TestTxt/Correct" + user + ".txt"), "utf-8"),
					FileUtils.readFileToString(new File("TestTxt/" + name), "utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
}
