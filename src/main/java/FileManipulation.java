import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManipulation {
	
	//a simple write
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
	
	//a simple read
	public static StringBuffer readTestFile(String name) {	
		BufferedReader reader;
		StringBuffer result = new StringBuffer();
		int c = 0;
		
		try {
			reader = new BufferedReader(new FileReader(new File(name)));
		
			while((c = reader.read()) != -1) {
				result.append((char)c);
			}
			
			reader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
