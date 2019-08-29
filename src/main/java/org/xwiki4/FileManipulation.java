package org.xwiki4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/*
 * A variety of different file manipulation utilities
 */
public class FileManipulation {

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

	// a simple read from string
	public static StringBuffer readString(String name) {
		BufferedReader reader;
		StringBuffer result = new StringBuffer();
		int c = 0;

		try {
			reader = new BufferedReader(new FileReader(name));

			while ((c = reader.read()) != -1) {
				result.append((char) c);
			}

			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	// check if exists
	public static boolean fileExists(String name) {
		File f = new File(name);
		if (f.exists() && !f.isDirectory()) {
			return true;
		} else {
			return false;
		}
	}

}
