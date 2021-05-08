package org.xwiki4.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.xwiki4.Database;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DatabaseTest {
	Database db = new Database();
	private static String content1, content2, content3;

	@Test
	public void test01getDocument() {
		content1 = db.getDocument("Main.WebHome", "lv");
		content2 = db.getDocument("Main.WebHome", "en");
		content3 = db.getDocument("Main.WebHome", "ru");
		assertTrue(content1.contains("=== Sveicināti Odo.lv vietnē! ==="));
		assertTrue(content2.contains("=== Welcome  to odo.lv site! ==="));
		assertTrue(content3.contains("=== Добро пожаловать в Odo.lv! ==="));
	}

	@Test
	public void test02putDocument() {
		db.putDocument("Main.WebHome", "lv", content1);
		assertEquals(content1, db.getDocument("Main.WebHome", "lv"));
		db.putDocument("Main.WebHome", "en", content2);
		assertEquals(content2, db.getDocument("Main.WebHome", "en"));
		// Test get/put without language
		content3 = db.getDocument("Blog.201207", "");
		db.putDocument("Blog.201207", "", content3);
		assertEquals(content3, db.getDocument("Blog.201207", ""));
	}

}
