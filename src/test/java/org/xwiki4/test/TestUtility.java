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
		writeTestFile(LinkFixer.getInput(), name);
		try {
			assertEquals(user + message,
					FileUtils.readFileToString(new File("TestTxt/Correct" + user + ".txt"), StandardCharsets.UTF_8),
					FileUtils.readFileToString(new File("TestTxt/" + name), StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// a simple write
	public static void writeTestFile(StringBuilder result, String name) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File("TestTxt/" + name)))) {
			writer.write(result.toString());
			writer.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	// a write to specific place
	public static void writeTo(StringBuilder result, String name) {
		File file = new File(name);
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			writer.write(result.toString());
			writer.flush();
		} catch (Exception e) {
			log.error("Writing failed." + e);
		}
	}

	// a simple read
	public static StringBuilder readTestFile(String name) {
		StringBuilder result = new StringBuilder();
		int c = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(name)))) {
			while ((c = reader.read()) != -1) {
				result.append((char) c);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}