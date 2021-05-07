package org.xwiki4.test;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.xwiki4.LinkFixer;

/*
 * @author Edmunds Ozolins
 */

public class TestUtility {
	private static Logger log = Logger.getLogger(TestUtility.class);

	public static void assertFiles(String name, String user, String message) {
		writeTo(LinkFixer.getInput(), "TestTxt/" + name);
		try {
			assertEquals(user + message,
					FileUtils.readFileToString(new File("TestTxt/Correct" + user + ".txt"), StandardCharsets.UTF_8),
					FileUtils.readFileToString(new File("TestTxt/" + name), StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// a write to file
	public static void writeTo(StringBuilder result, String name) {
		File file = new File(name);
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			writer.write(result.toString());
			writer.flush();
		} catch (Exception e) {
			log.error("Writing failed." + e);
		}
	}

	// read from file
	public static StringBuilder readFrom(String name) {
		StringBuilder result = new StringBuilder();
		int c = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(name)))) {
			while ((c = reader.read()) != -1) {
				result.append((char) c);
			}
		} catch (IOException e) {
			log.error(e);
		}
		return result;
	}

}
