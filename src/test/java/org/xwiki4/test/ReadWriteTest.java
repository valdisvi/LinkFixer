package org.xwiki4.test;

import static org.junit.Assert.assertEquals;
import static org.xwiki4.LinkFixer.appendTo;
import static org.xwiki4.LinkFixer.readFrom;
import static org.xwiki4.LinkFixer.writeTo;

import org.junit.Test;

public class ReadWriteTest {

	@Test
	public void testReadWrite() {
		writeTo("logs/test.log", "");
		appendTo("logs/test.log", "test");
		String result = readFrom("logs/test.log").toString();
		assertEquals("test", result);
	}


}
