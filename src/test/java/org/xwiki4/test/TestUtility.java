package org.xwiki4.test;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.xwiki4.LinkFixer;

/*
 * @author Edmunds Ozolins
 */

public class TestUtility {

	public static void assertFiles(String name, String user, String message) {
		writeTestFile(LinkFixer.getInput(), name);
		try {
			assertEquals(user + message,
					FileUtils.readFileToString(new File("TestTxt/Correct" + user + ".txt"), "utf-8"),
					FileUtils.readFileToString(new File("TestTxt/" + name), "utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// a simple write
	public static void writeTestFile(StringBuffer result, String name) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("TestTxt/" + name)));
			writer.write(result.toString());
			writer.flush();
			writer.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	// a write to specific place
	public static void writeTo(StringBuffer result, String name) {
		File file = new File(name);
		BufferedWriter writer = null;

		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(result.toString());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			System.err.println("Writing failed");
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				System.err.println("Writing failed");
				e.printStackTrace();
			}
		}
	}

	// a simple read
	public static StringBuffer readTestFile(String name) {
		BufferedReader reader;
		StringBuffer result = new StringBuffer();
		int c = 0;

		try {
			reader = new BufferedReader(new FileReader(new File(name)));

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