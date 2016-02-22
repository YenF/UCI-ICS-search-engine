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
	private static int maxsize = 0;
	private static String maxsize_URL = null;
	public tokenGen(){
		
		tokenstore= new TokenStorage(TokenStorage.LOCAL_URI);
	}
	public TokenStorage gettokenstore(){
		return tokenstore;
	}
	public static void main(String[] args) throws Exception {
	   	tokenGen tokengen = new tokenGen();
	   	tokengen.gettokenstore().reset();
	   	FileStorage fs = new FileStorage(FileStorage.LOCAL_URI);
		PrintWriter writer = new PrintWriter("pageURLs.txt");
		PrintWriter uniwriter = new PrintWriter("Subdomains.txt");
		Hashtable<String,Integer> hashset = new Hashtable<String,Integer>();
    	int totalPages = 0;  
    	System.out.println("---testing getPage---");
    	fs.resetPagesIterator();
    	Map.Entry< List<String>, List< Map<String,String> > > page = fs.getNextPage();
    	List<String> page_list;
    	System.out.println("---printing out page URL---");
    	int count = 0;
    	while ( page!=null ) {
    		if(++count>90761) break;
    		totalPages++;
    		System.out.printf("pages count: %d\n",totalPages);
    		page_list = page.getKey();
    		/*System.out.println("URL:\n"+page_list.get(0));
    		System.out.println("The following is the document content:\n"+page_list.get(1)+"\n");
    		System.out.println("The following is the document title:\n"+page_list.get(2)+"\n");
    		System.out.println("The following is the document anchortxt:\n"+page_list.get(3)+"\n");*/
    		//System.out.println(page.getValue());
    		tokengen.tokenAnd3gramPosition(page_list);
    		String[] arry = page_list.get(0).split("//|\\s+|\\n+|\\.");
    		Integer tempint = hashset.get(arry[1]);
    		if(tempint==null){
    			hashset.put(arry[1],new Integer(1));
    			//System.out.printf("new subdomain is %s %d\n", arry[1],1);
    		}else{
    			tempint = tempint+1;
    			hashset.put(arry[1],tempint);
    			//System.out.printf("exising subdomain is %s %d\n", arry[1],tempint.intValue());
    		}
    		if(totalPages%50 ==0) System.out.printf("current subdomain is %s\n",arry[1]);
    		//System.out.println("Page URL: " + page.getKey());
    		try{
    			page = fs.getNextPage();
    		}
    		catch(Exception e){
    			System.out.println("error message"+e.getMessage());
    			//break;
    		}
    	}
    	hashset.remove("www");
    	Iterator<String> ite = hashset.keySet().iterator();
    	int unicount = 0;
    	int indexi = 0;
    	int subpagecount=0;
    	String[] strarry = new String[hashset.keySet().size()];
    	while(ite.hasNext()){
    		String nextStr = ite.next();
    		strarry[indexi++] = nextStr;
    		
    	}
    	Arrays.sort(strarry);
    	for(String tempstr: strarry){
    		unicount++;
    		subpagecount +=hashset.get(tempstr).intValue();
    		uniwriter.printf("http://%s.ics.uci.edu, %d\n",tempstr,hashset.get(tempstr).intValue());
    	}
    	uniwriter.printf("total subdomain number is %d the number of  pages in subdomain is %d\n", unicount,subpagecount);
    	uniwriter.close();
    	writer.println("Total Pages: "+totalPages);
    	writer.close();
    	System.out.println("---getPage() function test complete---");
	}
	
	public int tokenAnd3gramPosition(List<String> page_list){
		int result = 0;
		String URL = page_list.get(0);
		List<String> content_list = TextProcessor.tokenizeFile(page_list.get(1));
		List<String> title_list = TextProcessor.tokenizeFile(page_list.get(2));
		List<String> anchor_list = TextProcessor.tokenizeFile(page_list.get(3));


		Pair[] pairfreq = TextProcessor.computeWordPosition(content_list,anchor_list,title_list);
		List<Pair> listfreq = new ArrayList<Pair>(Arrays.asList(pairfreq));
		//TextProcessor.printWordPosition(pairfreq);
		try{
			if(!tokenstore.insertToken(listfreq, URL,TokenStorage.TOKEN_COLL_NAME)){
				System.out.println("Insert token list fail: "+URL);
				result = -1;
			}
		}
		 catch (Exception e) {
			    System.err.println("Caught Exception: from tokens");
			}
		Pair[] pair3g =  TextProcessor.computeThreeGramPosition(content_list,anchor_list,title_list);
		List<Pair> list3g = new ArrayList<Pair>(Arrays.asList(pair3g));
		//TextProcessor.printThreeGramPosition(pair3g);
		System.out.printf("list3g's size is %d\n", list3g.size());
		try{
			if(!tokenstore.insertToken(list3g, URL,TokenStorage.TGRAM_COLL_NAME)){
				System.out.println("Insert 3g list fail: "+URL);
				result = -1;
			}
		}
		 catch (Exception e) {
			    System.err.println("Caught Exception: from 3gram");
			}
		return result;

	}
	/*public int tokenAnd3gram(String input, String URL){
		int result = 0;
		List<String> list = TextProcessor.tokenizeFile(input);
		Pair[] pairfreq = TextProcessor.computeWordFrequencies(list);
		List<Pair> listfreq = new ArrayList<Pair>(Arrays.asList(pairfreq));
		try{
			if(!tokenstore.insertToken(listfreq, URL)){
				System.out.println("Insert token list fail: "+URL);
				result = -1;
			}
		}
		 catch (Exception e) {
			    System.err.println("Caught Exception: from tokens");
			}
		Pair[] pair3g = TextProcessor.computeThreeGramFrequencies(list);
		List<Pair> list3g = new ArrayList<Pair>(Arrays.asList(pair3g));
		System.out.printf("list3g's size is %d\n", list3g.size());
		try{
			if(!tokenstore.insert3G(list3g, URL)){
				System.out.println("Insert 3g list fail: "+URL);
				result = -1;
			}
		}
		 catch (Exception e) {
			    System.err.println("Caught Exception: from 3gram");
			}
		return result;

	}*/
}
