package storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import static java.util.Arrays.asList;

/**
 * Operation related to tokens, three-grams could be find here.
 * @author Yen
 *
 */
public class TokenStorage {
	
	private static final TokenStorage instance = null;
	private MongoClient client;
	private MongoDatabase db;
	private MongoCursor<Document> iter;
	
	public TokenStorage() {
		client = new MongoClient( 
	      		 new MongoClientURI(
	      				"mongodb://UCI_Handsomes:UCI_Handsomes@ds041633.mongolab.com:41633/cs221") 
	      		 );
	       // Now connect to your databases
	    db = client.getDatabase("cs221");
	    System.out.println("---MongoDB initialized---");
	}
	
	/*	use mono instance will occur concurrency problem
	public static TokenStorage newInstance() {
		if ( instance==null ) {
			TokenStorage ts = new TokenStorage();
	        return ts;
		}
		return instance;
    }
	*/
	
	/**
	 * remove all elements in DB
	 */
	public void reset() {
		db.getCollection("tokens").drop();
		db.getCollection("threegrams").drop();
	}
	
	/**
	    * Insert token into DB. 
	    * @param token
	    * @param frequency Should be total count of "this URL(page)"
	    * @param URL
	    * @return True = succeed, False = duplicated (token, URL) or something wrong (could be ignore)
	    */
	   public boolean insertToken( String token, int frequency, String URL ) {
		   try{
			   db.getCollection("tokens").insertOne( 
					   new Document("token", token).append("frequency", frequency).append("URL", URL)
					   );
		   } catch( Exception e ) {
			   return false;	//if duplicate or something wrong
		   }
		   return true;
	   }
	   
	   /**
	    * NOT DONE YET. Get count of specific token in collections
	    * @param token
	    * @return number of occurrence
	    */
	   public int getTokenFreq( final String token ) {
		   int ans=0;
		   AggregateIterable<Document> iterable = db.getCollection("cs221").aggregate(asList(
				   new Document( "$match", new Document("token", token) ),
			        new Document("$group", new Document("_id", "$token").append("count", new Document("$sum", 1)))));
		   iterable.forEach(new Block<Document>() {
			    public void apply(final Document document) {
			    	System.out.println(document.toJson());
			    }
			});
		   

		   return ans;
	   }
	   
	   /**
	    * NOT DONE YET. List highest frequency ranks of tokens. If num=10, list top 10 of tokens.
	    * @param num
	    * @return
	    */
	   public List<Map.Entry<String,Integer>> getHighestFreq_Token( int num ) {
		   AggregateIterable<Document> iterable = db.getCollection("cs221").aggregate(asList(
				   new Document( "$sort", new Document( "count", -1 ) ),
				   new Document( "$limit", num ),
			        new Document("$group", new Document("_id", "$token").append("count", new Document("$sum", 1)))));
		   iterable.forEach(new Block<Document>() {
			    public void apply(final Document document) {
			    	System.out.println(document.toJson());
			    }
			});
		   return new ArrayList<Map.Entry<String,Integer>>();
	   }
	   
	   //Three gram
	   
	   /**
	    * NOT DONE YET. Insert three-gram into DB. 
	    * @param token
	    * @param frequency Should be total count of "this URL(page)"
	    * @param URL
	    * @return True = succeed, False = duplicated (token, URL) or something wrong (could be ignore)
	    */
	   public boolean insert3G( String token, int frequency, String URL ) {
		   
		   return true;
	   }
	   
	   /**
	    * NOT DONE YET.
	    * @param token
	    * @return
	    */
	   public int get3GFreq( String token ) {
		   int ans=0;
		   
		   return ans;
	   }
}
