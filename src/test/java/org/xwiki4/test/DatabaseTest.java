package org.xwiki4.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.xwiki4.Database;

public class DatabaseTest {
	Database db = new Database();

	@Test
	public void testGetDocument() {
		assertTrue(db.getDocument("Main.WebHome", "lv").contains("=== Sveicināti Odo.lv vietnē! ==="));
		assertTrue(db.getDocument("Main.WebHome", "en").contains("=== Welcome  to odo.lv site! ==="));
		assertTrue(db.getDocument("Main.WebHome", "ru").contains("=== Добро пожаловать в Odo.lv! ==="));
	}

	
}
