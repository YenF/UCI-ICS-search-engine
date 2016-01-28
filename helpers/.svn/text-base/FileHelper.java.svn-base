package ir.assignments.three.helpers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileHelper {
	public static final String lineSeparator = System.getProperty("line.separator");

	public static String readFirstLine(File file) throws IOException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			return reader.readLine();
		}
		finally {
			if (reader != null)
				reader.close();
		}
	}

	public static String readFile(File file, int linesToSkip) throws IOException, FileNotFoundException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));

			// Skip lines
			while (linesToSkip > 0) {
				reader.readLine();
				linesToSkip--;
			}

			// Read the rest
			StringBuilder content = new StringBuilder();
			String currentLine = "";
			while ((currentLine = reader.readLine()) != null) {
				content.append(currentLine);
				if (reader.ready())
					content.append(lineSeparator);
			}

			return content.toString();
		}
		finally {
			if (reader != null)
				reader.close();
		}
	}

	public static void writeFile(File file, String[] lines) throws IOException {
		FileWriter stream = new FileWriter(file);
		BufferedWriter writer = new BufferedWriter(stream);

		try {
			// Write each line
			for (int i = 0; i < lines.length; i++) {
				writer.write(lines[i]);
				if (i + 1 < lines.length)
					writer.write(lineSeparator);
			}
		}
		finally {
			writer.flush();
			writer.close();
		}
	}
	
	public static void appendToFile(File file, String line) throws IOException {
		FileWriter stream = new FileWriter(file, true);
		BufferedWriter writer = new BufferedWriter(stream);

		try {
			writer.write(lineSeparator + line);
		}
		finally {
			writer.flush();
			writer.close();
		}
	}
}
