package data.text_processing;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import IRUtilities.*;
import Pair.*;
import edu.smu.tspell.wordnet.*;
import java.sql.*;

public class TextProcessor {
	public static Connection c = null;
	public static Statement stmt = null;
	public static List<String> tokenizeFile(String filename){
		List<String> list = new LinkedList<String>();
		try{
			FileReader filereader = new FileReader(filename);
			BufferedReader bufferReader = new BufferedReader(filereader);
			
			String nextline;
			StringBuilder builder = new StringBuilder();
			Porter porter = new Porter();
			while((nextline = bufferReader.readLine())!=null){
				String[] strarry = nextline.split(" |, |\\.");
				for(int i=0;i<strarry.length;i++){
					if(strarry[i].length()>0 && !Stopwords.isStopword(strarry[i])) list.add(porter.stripAffixes(strarry[i]));
				}
			}

			bufferReader.close();
			System.out.println("file is closing\n");
			return list;
		}
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file" );                
        }
        catch(IOException ex) {
            System.out.println(
                "Error for FileIO");                  
        }
		return list; 
	}
	public static void print(List<String> list){
		Iterator<String> ite = list.iterator();
		while(ite.hasNext()){
			System.out.println(ite.next());
		}
		return;
	}
	public static Pair[] computeWordFrequencies(List<String> list){
		Hashtable<String,Integer> hash = new Hashtable<String,Integer>();
		Iterator<String> ite = list.iterator();
		String tempstr;
		Integer tempint;
		while(ite.hasNext()){
			tempstr = ite.next();
			tempint = hash.get(tempstr);
			if(tempint==null){
				hash.put(tempstr, new Integer(1));
			}else{
				hash.put(tempstr, tempint+1);
			}
		}
		Iterator<String>  keyite = hash.keySet().iterator();
		Pair[] pairs = new Pair[hash.size()];
		int i = 0;
		while(keyite.hasNext()){
			String str = keyite.next();
			Integer integer = hash.get(str);
			pairs[i++] = new Pair(str,integer);
		}
		Arrays.sort(pairs);
		return pairs;
	}
	public static void print(Pair[] pairarry){
		
		for(int i=0;i<pairarry.length;i++){
			System.out.printf("%s %d\n", pairarry[i].getT(),pairarry[i].getE());
		}
	}
	public static Pair[] computeThreeGramFrequencies(List<String> list){
		String[] strarry = list.toArray(new String[0]);
		Hashtable<String,Integer> hash = new Hashtable<String,Integer>();
		String tempstr;
		Integer tempint;
		for(int i=0;i<(strarry.length-2);i++){
			tempstr = strarry[i]+" "+strarry[i+1]+" "+strarry[i+2];
			tempint = hash.get(tempstr);
			if(tempint==null){
				hash.put(tempstr, new Integer(1));
			}else{
				hash.put(tempstr, tempint+1);
			}
		}
		Iterator<String>  keyite = hash.keySet().iterator();
		Pair[] pairs = new Pair[hash.size()];
		int i = 0;
		while(keyite.hasNext()){
			String str = keyite.next();
			Integer integer = hash.get(str);
			pairs[i++] = new Pair(str,integer);
		}
		Arrays.sort(pairs);
		return pairs;
	}
	public static void print3g(Pair[] pairarry){
		
		for(int i=0;i<pairarry.length;i++){
			System.out.printf("%s %d\n", pairarry[i].getT(),pairarry[i].getE());
		}
	}
	public static  List<PairE<String,List<String>>> detecAnagrams(List<String> list){
		Hashtable<String,List<String>> hashana = new Hashtable<String,List<String>>();
		List<PairE<String,List<String>>> pairlist = new LinkedList<PairE<String,List<String>>>();
		HashSet<String> hashset = new HashSet<String>();
		Pair[] pairarry = computeWordFrequencies(list);
		char[] chararry;
		String[] strarry = new String[pairarry.length];
		for(int i=0;i<strarry.length;i++) strarry[i] = pairarry[i].getT();
		List<String> templist;
		Arrays.sort(strarry);
		for(int j=0;j<strarry.length;j++){
			chararry = strarry[j].toCharArray();
			Arrays.sort(chararry);
			String indexstr = new String(chararry);
			templist = hashana.get(indexstr);
			//if(hashset.contains(strarry[j])) continue;
			//hashset.add(strarry[j]);
			//System.out.printf("strarry[%d] is %s\n", j,strarry[j]);
			if( templist==null){
				templist = detectAnagrams(indexstr);
				hashana.put(indexstr, templist);
				pairlist.add(new PairE<String,List<String>>(strarry[j],templist));
				
			}else{
				pairlist.add(new PairE<String,List<String>>(strarry[j],templist));
			}
		}
		try{
			stmt.close();
			c.close();
		}
		catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		    }
		c = null;
		stmt = null;
		return pairlist;

	}
	private static List<String> detectAnagrams(String indexstr){
		char[] chararry = indexstr.toCharArray();
		String index = new String(chararry);
		Arrays.sort(chararry);
		boolean jump = false;
		for(int j=0;j<chararry.length;j++){
			//System.out.printf("chararry[%d] is %c\n",j,chararry[j]);
			if('z'<chararry[j] || 'a'>chararry[j]){ 
				jump = true;
				break;
			}
		}
		List<String> list = new LinkedList<String>();
		if(jump) return list;
		try{
		    Class.forName("org.sqlite.JDBC");
		    if(c==null)c = DriverManager.getConnection("jdbc:sqlite:dict.db");
		    c.setAutoCommit(false);
		    if(stmt==null)stmt = c.createStatement();
			String query = "SELECT *"+
					"FROM DICTIONARY"+
					" WHERE sortedIndex='"+
					index+"';";
			ResultSet rs = stmt.executeQuery(query);
			String[] strarry;
			int i = 0;
			while(rs.next()){
				i++;
				strarry = rs.getString("wordList").split("\\s+|\n+");
				for(String pattern:strarry){
				list.add(pattern);
				}
			}
			rs.close();
		}
		catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		    }
		return list;
	}
	public static void printAna(List<PairE<String,List<String>>> list){
		Iterator< PairE<String,List<String>>> ite = list.iterator();
		PairE<String,List<String>> temppair;
		while(ite.hasNext()){
			temppair = ite.next();
			System.out.printf("token %s:\n", temppair.getT());
			Iterator<String> itestr = temppair.getE().iterator();
			while(itestr.hasNext()){
				System.out.printf("%s\n", itestr.next());
			}
			System.out.printf("\n");
		}
	}
	/*private static void generatePattern(char[] pattern,int[] stock,int level,List<String> list){
		//System.out.printf("level is %d pattern.length is %d\n", level,pattern.length);
		if(level==pattern.length){
			WordNetDatabase database = WordNetDatabase.getFileInstance();
			String patternstr = new String(pattern);
			//System.out.printf("pattern is %s\n", patternstr);
			Synset[] synsets = database.getSynsets(patternstr,null,true);
			if(synsets.length>0){ 
				//System.out.printf("Synsets[0] is %s\n",(synsets[0].getWordForms())[0]);
				list.add(patternstr);
			}
			return;
		}
		for(int i=0;i<stock.length;i++){
			if(stock[i]>0){
				pattern[level] = (char)(i+'a');
				stock[i]--;
				generatePattern(pattern,stock,level+1,list);
				stock[i]++;
			}
		}
		return;
	}*/
	
}