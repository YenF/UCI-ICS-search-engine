import java.io.*;
import java.lang.Object.*;
import java.math.BigInteger;
import java.util.*;
import IRUtilities.*;
import Pair.Pair;
import text_processing.*;
import java.sql.*;

public class Hello
{

	
	public static  void main(String args[])
    {
    	//System.setProperty("wordnet.database.dir", "C:\\Program Files (x86)\\WordNet\\2.1\\dict");
		List<String> list = TextProcessor.tokenizeFile(".\\"+args[0]);
		if(args[1].equals("A")) TextProcessor.print(list);
		if(args[1].equals("B")) TextProcessor.print(TextProcessor.computeWordFrequencies(list));
		if(args[1].equals("C")) TextProcessor.print3g(TextProcessor.computeThreeGramFrequencies(list));
		if(args[1].equals("D")) TextProcessor.printAna(TextProcessor.detecAnagrams(list));
		String teststr = "www.com.test";
		String[] arry = teststr.split(" |\n+");
		System.out.println(teststr.contains(".com.test"));

		//constructDict();

		return;
    }
	private static void constructDict(){
		try{
			FileReader filereaderword = new FileReader("words.txt");
			BufferedReader bufferReaderword = new BufferedReader(filereaderword);
			String tempstr;
			String[] strarry;
			Connection c = null;
		    Class.forName("org.sqlite.JDBC");
		    c = DriverManager.getConnection("jdbc:sqlite:dict.db");
		    c.setAutoCommit(false);
			Statement stmt = null;
			stmt = c.createStatement();
			String sql = "CREATE TABLE DICTIONARY"+
						 "(sortedIndex TEXT PRIMARY KEY NOT NULL,"+
						 "wordList TEXT NOT NULL);";
			stmt.executeUpdate(sql);
			
			/*String insert1 = "INSERT INTO DICTIONARY (sortedIndex,wordList)"+
					"VALUES ('"+"test"+"','"+"testtest"+"');";
			stmt.executeUpdate(insert1);
			System.out.println("Before query\n");
			String query1 = "SELECT *"+"FROM DICTIONARY"+" WHERE sortedIndex='"+"test"+"';";
			ResultSet rs1 = stmt.executeQuery(query1);
			System.out.println("sucessfully query\n");
			while(rs1.next()){
				System.out.printf("sortedIndex is %s\n", rs1.getString("sortedIndex"));
				System.out.printf("wordList is %s\n", rs1.getString("wordList"));
			}*/
			
			//write the dictionary file
			char[] chararry;
			while((tempstr = bufferReaderword.readLine())!=null){
				strarry = tempstr.split("\\s+|\n+");
				for(int i=0;i<strarry.length;i++){
					//System.out.printf("string is %s\n", strarry[i]);
					chararry = strarry[i].toCharArray();
					Arrays.sort(chararry);
					String indexstr = new String(chararry);
					boolean jump = false;
					for(int j=0;j<chararry.length;j++){
						//System.out.printf("chararry[%d] is %c\n",j,chararry[j]);
						if('z'<chararry[j] || chararry[j]<'a'){ 
							jump = true;
							break;
						}
					}
					if(jump) break;
					String query = "SELECT *"+
									"FROM DICTIONARY"+
									" WHERE sortedIndex='"+
									indexstr+"';";
					ResultSet rs = stmt.executeQuery(query);
					String insertpattern = strarry[i];
					int i1=0;
					while(rs.next()){
						i1++;
						System.out.printf("sortedIndex is %s\n", rs.getString("sortedIndex"));
						insertpattern = rs.getString("wordList")+" "+insertpattern;
						System.out.printf("wordList is %s\n", rs.getString("wordList"));
					}
					rs.close();
					if(i1==0){
						String insert = "INSERT INTO DICTIONARY (sortedIndex,wordList)"+
										"VALUES ('"+indexstr+"','"+insertpattern+"');";
						stmt.executeUpdate(insert);
					}else if(i1>0){
						String update = "UPDATE DICTIONARY set wordList='"+insertpattern+"' WHERE sortedIndex='"+indexstr+"';";
						stmt.executeUpdate(update);
					}
					
				}
			}
			c.commit();
			stmt.close();
			c.close();
		}
		catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		    }

	}
    
    
    
}


