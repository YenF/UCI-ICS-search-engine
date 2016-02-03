package storage.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;

import static java.util.Arrays.asList;

import java.util.AbstractMap;

/**
 * Operation related to tokens, three-grams could be find here.
 * @author Yen
 *
 */
public class TokenStorage {
	
	public final static String TOKEN_COLL_NAME = "tokens";
	public final static String TGRAM_COLL_NAME = "threeGrams";
	public final static String TOKEN_DB_NAME = "cs221";
	public final static String URI = "mongodb://UCI_Handsomes:UCI_Handsomes@ds041633.mongolab.com:41633/cs221"; 
	//private static final TokenStorage instance = null;
	//private MongoClient client;
	private MongoDatabase db;
	private MongoCursor<Document> iter;
	
	public TokenStorage() {
		MongoDB DB = new MongoDB();
		DB.init(URI, TOKEN_DB_NAME);
		
	       // Now connect to your databases
	    db = DB.db;
	    //System.out.println("---MongoDB initialized---");
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
	 * remove all tokens and 3-grams in DB. Use it wisely.
	 */
	public void reset() {
		MongoCollection coll;
		db.getCollection(TOKEN_COLL_NAME).drop();
		db.createCollection(TOKEN_COLL_NAME);
		coll = db.getCollection(TOKEN_COLL_NAME);
		IndexOptions IndOpt = new IndexOptions();
		IndOpt.unique(true);
		coll.createIndex ( new Document("token",1).append("URL", 1) , IndOpt);
		
		db.getCollection(TGRAM_COLL_NAME).drop();
		db.createCollection(TGRAM_COLL_NAME);
		coll = db.getCollection(TGRAM_COLL_NAME);
		coll.createIndex ( new Document("token",1).append("URL", 1) , IndOpt);
	}
	
	/**
	    * Insert token into DB.
	    * @param p 
	    * @param URL
	    * @return True = succeed, False = duplicated (token, URL) or something wrong (could be ignore)
	    */
	   public boolean insertToken( List<Pair> p, String URL ) {
		   for ( int i=0; i<p.size(); i++ ) {
			   insertToken( p.get(i).getT(), p.get(i).getE(), URL );
		   }
		   return true;
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
		   db.getCollection(TOKEN_COLL_NAME).insertOne( 
				   new Document("token", token).append("frequency", frequency).append("URL", URL)
				   );
	   } catch( Exception e ) {
		   return false;	//if duplicate or something wrong
	   }
	   return true;
   }
   
   /**
    * Get count of specific token in collections
    * @param token
    * @return number of occurrence in whole collection
    */
   public int getTokenFreq( final String token ) {
	   int ans=0;
	   AggregateIterable<Document> iterable = db.getCollection(TOKEN_COLL_NAME).aggregate(asList(
			   new Document( "$match", new Document("token", token) ),
		        new Document("$group", new Document("_id", "$token").append("count", new Document("$sum", "$frequency")))));
	   /*
	   iterable.forEach(new Block<Document>() {
		    public void apply(final Document document) {
		    	System.out.println(document.toJson());
		    }
		});
		*/
	   return iterable.first().getInteger("count");
   }
   
   /**
    * List highest frequency ranks of tokens. If num=10, list top 10 of tokens.
    * @param num
    * @return
    */
   public List<Map.Entry<String,Integer>> getHighestFreq_Token( int num ) {
	   //final?...
	   final ArrayList<Map.Entry<String, Integer>> ans = new ArrayList<Map.Entry<String,Integer>>();
	   AggregateIterable<Document> iterable = db.getCollection(TOKEN_COLL_NAME).aggregate(asList(
			   new Document( "$sort", new Document( "count", -1 ) ),
			   new Document( "$limit", num ),
		        new Document("$group", new Document("_id", "$token").append("count", new Document("$sum", 1)))));
	   iterable.forEach(new Block<Document>() {
		    public void apply(final Document document) {
		    	//Map.Entry<String, Integer> tmp = new AbstractMap.SimpleEntry<String, Integer>();
		    	//HashMap<String, Integer> tmpHash = new HashMap<String, Integer>();
		    	
		    	ans.add( new AbstractMap.SimpleEntry<String, Integer>
		    		( document.getString("_id"), document.getInteger("count") ) 
		    			);
		    }
		});
	   return ans;
   }
   
   //Three gram
   
   /**
    * Insert 3G into DB.
    * @param p 
    * @param URL
    * @return True = succeed, False = duplicated (token, URL) or something wrong (could be ignore)
    */
   public boolean insert3G( List<Pair> p, String URL ) {
	   for ( int i=0; i<p.size(); i++ ) {
		   insert3G( p.get(i).getT(), p.get(i).getE(), URL );
	   }
	   return true;
   }
   
   /**
    * Insert three-gram into DB. 
    * @param token
    * @param frequency Should be total count of "this URL(page)"
    * @param URL
    * @return True = succeed, False = duplicated (token, URL) or something wrong (could be ignore)
    */
   public boolean insert3G( String token, int frequency, String URL ) {
	   try{
		   db.getCollection(TGRAM_COLL_NAME).insertOne( 
				   new Document("token", token).append("frequency", frequency).append("URL", URL)
				   );
	   } catch( Exception e ) {
		   return false;	//if duplicate or something wrong
	   }
	   return true;
   }
   
   /**
    * Get count of specific token in collections
    * @param token
    * @return
    */
   public int get3GFreq( String token ) {
	   AggregateIterable<Document> iterable = db.getCollection(TGRAM_COLL_NAME).aggregate(asList(
			   new Document( "$match", new Document("token", token) ),
		        new Document("$group", new Document("_id", "$token").append("count", new Document("$sum", "$frequency")))));
	   
	   return iterable.first().getInteger("count");
   }
   
   /**
    * List highest frequency ranks of tokens. If num=10, list top 10 of tokens.
    * @param num
    * @return 
    */
   public List<Map.Entry<String,Integer>> getHighestFreq_3G( int num ) {
	 //final?...
	   final ArrayList<Map.Entry<String, Integer>> ans = new ArrayList<Map.Entry<String,Integer>>();
	   AggregateIterable<Document> iterable = db.getCollection(TGRAM_COLL_NAME).aggregate(asList(
			   new Document( "$sort", new Document( "count", -1 ) ),
			   new Document( "$limit", num ),
		        new Document("$group", new Document("_id", "$token").append("count", new Document("$sum", 1)))));
	   iterable.forEach(new Block<Document>() {
		    public void apply(final Document document) {
		    	//Map.Entry<String, Integer> tmp = new AbstractMap.SimpleEntry<String, Integer>();
		    	//HashMap<String, Integer> tmpHash = new HashMap<String, Integer>();
		    	
		    	ans.add( new AbstractMap.SimpleEntry<String, Integer>
		    		( document.getString("_id"), document.getInteger("count") ) 
		    			);
		    }
		});
	   return ans;
   }
}
