package ir.assignments.three.helpers;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

/**
 * Original code structure borrowed from: http://code.google.com/p/jrawler/source/browse/trunk/src/com/barkerton/crawler/parser/StopWords.java?r=9
 * 
 * Class maintains an in-memory list of available stop words
 * used in removing unnecessary words form the parsing indexer.
 * Words we don't care to utilize for frequency weights in
 * determining probability.
 *
 * @author CJ_Barker
 *  
 *
 * Copyright (c) 2008 C. J. Barker. All rights reserved.
 */
public class StopWord {

	private static final String stopListFile = System.getProperty("user.dir") + File.separator + "StopWordList.txt";
	private static HashSet<String> stopWords = new HashSet<String>(); 

	/*
	 * Static class load - one-time file IO for reading in the
	 * stop words list and storing it in-memory when the application
	 * is launched.
	 */
	static {
		
		// Read file and load list              
		File f = new File(stopListFile);
		try {
			BufferedReader input =  new BufferedReader(new FileReader(f));

			try {
				String line = null;

				while ((line = input.readLine()) != null){
					if (line != null)
						stopWords.add(line.trim().toLowerCase());
				}
			}
			finally {
				input.close();
			}
		}
		catch (FileNotFoundException fnfe) {
			System.err.println("Unable to locate stop word list file for processing: " + f.getAbsolutePath());
			System.exit(0);
		}
		catch (IOException ioe) {
			System.err.println(ioe.getMessage());
			System.exit(0);
		}
	}

	// Checks whether or not a given word is part of the stop word list.
	public static boolean isStopWord(String word) {
		return stopWords.contains(word.trim());
	}
}

