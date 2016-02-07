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
import java.io.*;
import storage.main.*;
/**
 * Created by Frank on 16/1/26.
 */
public class tokenGen {
	private  TokenStorage tokenstore;
	public tokenGen(){
		tokenstore= new TokenStorage(TokenStorage.ICS_URI);
	}
	public TokenStorage gettokenstore(){
		return tokenstore;
	}
	public static void main(String[] args) throws Exception {
	   	tokenGen tokengen = new tokenGen();
	   	//tokengen.gettokenstore().reset();
	   	FileStorage fs = new FileStorage(FileStorage.ICS_URI);
		PrintWriter writer = new PrintWriter("pageURLs.txt");
		PrintWriter uniwriter = new PrintWriter("uniqueSubdomain.txt");
		HashSet<String> hashset = new HashSet<String>();
    	int totalPages = 0;  
    	System.out.println("---testing getPage---");
    	fs.resetPagesIterator();
    	Map.Entry<String,String> page = fs.getNextPage();
    	System.out.println("---printing out page URL---");
    	while ( page!=null ) {
    		totalPages++;
    		writer.println(page.getKey());
    		System.out.println("The following is the document content:");
    		System.out.println(page.getValue());
    		tokengen.tokenAnd3gram(page.getValue(),page.getKey());
    		String[] arry = page.getKey().split("//|\\s+|\\n+|\\.");
    		if(!hashset.contains(arry[1])){
    			hashset.add(arry[1]);
    			System.out.printf("subdomain is %s\n", arry[1]);
    		}
    		//System.out.println("Page URL: " + page.getKey());
    		page = fs.getNextPage();
    	}
    	hashset.remove("www");
    	Iterator<String> ite = hashset.iterator();
    	int unicount = 0;
    	while(ite.hasNext()){
    		uniwriter.printf("%d: %s\n",++unicount,ite.next());
    	}
    	uniwriter.close();
    	writer.println("Total Pages: "+totalPages);
    	writer.close();
    	System.out.println("---getPage() function test complete---");
	}
	public int tokenAnd3gram(String input, String URL){
		int result = 0;
		List<String> list = TextProcessor.tokenizeFile(input);
		Pair[] pairfreq = TextProcessor.computeWordFrequencies(list);
		List<Pair> listfreq = new ArrayList<Pair>(Arrays.asList(pairfreq));
		if(!tokenstore.insertToken(listfreq, URL)){
			System.out.println("Insert token list fail: "+URL);
			result = -1;
		}
		Pair[] pair3g = TextProcessor.computeThreeGramFrequencies(list);
		List<Pair> list3g = new ArrayList<Pair>(Arrays.asList(pair3g));
		System.out.printf("list3g's size is %d\n", list3g.size());
		if(!tokenstore.insert3G(list3g, URL)){
			System.out.println("Insert 3g list fail: "+URL);
			result = -1;
		}
		return result;

	}
}
