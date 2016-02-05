package data;
import data.text_processing.*;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import data.IRUtilities.*;
import data.Pair.*;
import java.sql.*;
import storage.main.*;
/**
 * Created by Frank on 16/1/26.
 */
public class tokenGen {
	public static int tokenAnd3gram(String input, String URL){
		int result = 0;
		TokenStorage tokenstore = new TokenStorage(TokenStorage.MONGOLAB_URI);
		List<String> list = TextProcessor.tokenizeFile(input);
		Pair[] pairfreq = TextProcessor.computeWordFrequencies(list);
		List<Pair> listfreq = new ArrayList<Pair>(Arrays.asList(pairfreq));
		if(!tokenstore.insertToken(listfreq, URL)){
			System.out.println("Insert token list fail: "+URL);
			result = -1;
		}
		Pair[] pair3g = TextProcessor.computeThreeGramFrequencies(list);
		List<Pair> list3g = new ArrayList<Pair>(Arrays.asList(pair3g));
		if(!tokenstore.insert3G(list3g, URL)){
			System.out.println("Insert 3g list fail: "+URL);
			result = -1;
		}
		return result;

	}
}
