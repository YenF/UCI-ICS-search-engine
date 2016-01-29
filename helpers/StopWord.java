package helpers;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class StopWord {

	private static final String stopListFile = System.getProperty("user.dir") + File.separator + "StopWordList.txt";
	private static HashSet<String> stopWords = new HashSet<String>(); 

	static {
		
		// Read file and load list              
		File file = new File(stopListFile);
		try {
			BufferedReader input =  new BufferedReader(new FileReader(file));

			try {
				String line = null;

				while ((line = input.readLine()) != null){
					if (line != null)
						stopWords.add(line.trim().toLowerCase());
				}
			} finally {
				input.close();
			}
		} catch (FileNotFoundException e) {
			System.err.println("Unable to locate stop word: " + file.getAbsolutePath());
			System.exit(0);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(0);
		}
	}

	// Checks whether or not a given word is part of the stop word list.
	public static boolean isStopWord(String word) {
		return stopWords.contains(word.trim());
	}
}

