package test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Scanner;

import org.junit.BeforeClass;
import org.junit.Test;

import main.RecordProcessor;

public class TestRecordProcessor {
	private static String expectedFromData1;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		StringBuffer fileContents = new StringBuffer();
		Scanner fileInput = new Scanner(new File("expectedOutput.txt"));
		while(fileInput.hasNextLine())
			fileContents.append(fileInput.nextLine() + "\n");
		expectedFromData1 = fileContents.toString();
		fileInput.close();
	}

	@Test
	public void testFileData1() {
		assertEquals(expectedFromData1, RecordProcessor.processFile("inputData1.txt"));
	}

	@Test
	public void testFileData2() {
		assertEquals(null, RecordProcessor.processFile("inputData2.txt"));
	}

	@Test
	public void testFileData3() {
		assertEquals(null, RecordProcessor.processFile("inputData3.txt"));
	}
}
