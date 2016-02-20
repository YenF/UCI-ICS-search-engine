package storage.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoBulkWriteException;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;

import data.Pair.Pair;

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
	public final static String TOKEN_DB_NAME = "cs221_tokens";
	public final static String PAGE_COLL_NAME = "URL_Pages";
	public final static String MONGOLAB_URI = "mongodb://UCI_Handsomes:UCI_Handsomes@ds055535.mongolab.com:55535/cs221_tokens";
	public final static String ICS_URI = 
			"mongodb://UCI_Handsomes:UCI_Handsomes@ramon-limon.ics.uci.edu:8888/"+TOKEN_DB_NAME;
	public final static String LOCAL_URI = 
			"mongodb://127.0.0.1/";
	//private static final TokenStorage instance = null;
	//private MongoClient client;
	private MongoDB DB;
	private MongoDatabase db, dbPages;
	private MongoCursor<Document> iter;
	
	/**
	 * connect to MONGOLAB_URI, ICS_URI or LOCAL_URI
	 * @param URI
	 */
	public TokenStorage( String URI ) {
		DB = new MongoDB();
		DB.init(URI, TOKEN_DB_NAME);
		
	       // Now connect to your databases
	    db = DB.db;
	    dbPages = DB.dbPages;
	    //System.out.println("---MongoDB initialized---");
	}
	
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
		coll.createIndex ( new Document("token",1), IndOpt);
		
		db.getCollection(TGRAM_COLL_NAME).drop();
		db.createCollection(TGRAM_COLL_NAME);
		coll = db.getCollection(TGRAM_COLL_NAME);
		coll.createIndex ( new Document("token",1), IndOpt);
	}
	
	/**
	    * Insert token or tgram into DB. Mode could be ts.TOKEN_COLL_NAME
	    * @param p 
	    * @param URL
	    * @param mode 
	    * @return True = succeed, False = duplicated (token, URL) or something wrong (could be ignore)
	    */
	   public boolean insertToken( List<Pair> p, String URL, 
			   String mode ) {
		   //mode will be "TOKEN_COLL_NAME" or "TGRAM_COLL_NAME"
		   if ( p.isEmpty() ) return true;	//if no element in list, just return
		   List bulkList = new ArrayList();
		   for ( int i=0; i<p.size(); i++ ) {
			   //insertToken( p.get(i).getT(), p.get(i).getE(), URL );
			   bulkList.add( new UpdateOneModel( new Document( "token", p.get(i).getT() ),
					   new Document( "$push", new Document( "URLs", 
							   new Document("URL", URL)
							   		.append("position", p.get(i).getE())  
							   		) 
							   ), new UpdateOptions().upsert(true)
					   )	//UpdateOneModel
				);
		   }
		   BulkWriteOptions opt = new BulkWriteOptions();
		   try {
			   db.getCollection(mode).bulkWrite(bulkList, opt.ordered(false));
		   } catch ( MongoBulkWriteException e ) {
			   System.out.println(e.getMessage());
		   } catch ( Exception e ) {
			   System.out.println(e.getMessage());
			   return false;
		   }
		   return true;
	   }
	
	   public boolean computeTFIDF(String tokenCollName, String pageCollName) {
		   //tokenCollName: for retrieving tokens
		   //pageCollName: for total document number, in different db
		   List<Document> iter = db.getCollection(tokenCollName).find().into(new ArrayList<Document>());
		   long totalDocuments = dbPages.getCollection(pageCollName).count();
		   int countDoc=1;
		   List bulkList = new ArrayList();
		   
		   for ( Document token : iter ) {
			   List<Document> URLs = (List<Document>) token.get("URLs");
			   if ( URLs != null ) {	//some outdated entry may not have this field
				   countDoc++;
				   bulkList.add(
					   new UpdateOneModel(
						   new Document( "token", token.getString("token") ),
						   new Document( "$set", 
							   new Document( "DF", URLs.size() )
							   .append( "IDF", Math.log10( totalDocuments/URLs.size() ) )
						   )
						)
					);
			   }
			   //BulkWriteOptions opt = new BulkWriteOptions();
			   if ( countDoc % 10000 == 0 ) {
				   try {
					   db.getCollection(tokenCollName).bulkWrite(bulkList, new BulkWriteOptions().ordered(false));
				   } catch ( Exception e ) {
					   e.printStackTrace();
				   }
				   bulkList.clear();
				   countDoc=1;
			   }
		   }
		   if ( !bulkList.isEmpty() ) { 
			   try {
				   db.getCollection(tokenCollName).bulkWrite(bulkList, new BulkWriteOptions().ordered(false));
			   } catch ( Exception e ) {
				   e.printStackTrace();
			   }
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
	   /*
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
   */
	   
   @Deprecated
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
    * List highest frequency ranks of tokens & 3G. If num=10, list top 10 of tokens.
    * @param num
    * @return
    */
   public List<Map.Entry<String,Integer>> getHighestFreq( int num, String mode ) {
	   //final?...
	   final ArrayList<Map.Entry<String, Integer>> ans = new ArrayList<Map.Entry<String,Integer>>();
	   AggregateIterable<Document> iterable = db.getCollection(mode).aggregate(asList(
			   new Document("$unwind", "$URLs"),
			   new Document("$group", new Document("_id", "$token").append("count", 
					   new Document("$sum", new Document("$size","$URLs.position") ))),
				new Document( "$sort", new Document( "count", -1 ) ),
				new Document( "$limit", num )
		        )).allowDiskUse(true);
	   iterable.forEach(new Block<Document>() {
		    public void apply(final Document document) {
		    	//Map.Entry<String, Integer> tmp = new AbstractMap.SimpleEntry<String, Integer>();
		    	//HashMap<String, Integer> tmpHash = new HashMap<String, Integer>();
		    	//System.out.println(document.getString("_id"));
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
   /*
   public boolean insert3G( List<Pair> p, String URL ) {
	   if ( p.isEmpty() ) return true;	//if no element in list, just return
	   List bulkList = new ArrayList();
	   for ( int i=0; i<p.size(); i++ ) {
		   //insertToken( p.get(i).getT(), p.get(i).getE(), URL );
		   bulkList.add( new InsertOneModel( new Document( "token", p.get(i).getT() )
				   .append("frequency", p.get(i).getE() ) 
				   .append("URL", URL)
				   ));
	   }
	   BulkWriteOptions opt = new BulkWriteOptions();
	   try {
		   db.getCollection(TGRAM_COLL_NAME).bulkWrite(bulkList, opt.ordered(false) );
	   } catch ( MongoBulkWriteException e ) {
		   System.out.println(e.getMessage());
	   } catch ( Exception e ) {
		   System.out.println(e.getMessage());
		   return false;
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
   /*
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
    * Get count of specific 3-gram in collections
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
    * List highest frequency ranks of 3-grams. If num=10, list top 10 of 3-grams.
    * Need map reduce function to threeGramFreq collection
    * @param num
    * @return List of (token, frequency) 
    */
   /*
   public List<Map.Entry<String,Integer>> getHighestFreq_3G( int num ) {
	 //final?...
	   final ArrayList<Map.Entry<String, Integer>> ans = new ArrayList<Map.Entry<String,Integer>>();
	   AggregateIterable<Document> iterable = db.getCollection("threeGramFreq").aggregate(asList(
			   new Document("$group", new Document("_id", "$_id").append("count", new Document("$sum", "$value"))),
			   new Document( "$sort", new Document( "count", -1 ) ),
			   new Document( "$limit", num )
		        )
			   ).allowDiskUse(true);
	   iterable.forEach(new Block<Document>() {
		    public void apply(final Document document) {
		    	//Map.Entry<String, Integer> tmp = new AbstractMap.SimpleEntry<String, Integer>();
		    	//HashMap<String, Integer> tmpHash = new HashMap<String, Integer>();
		    	//System.out.println(document.getString("_id"));
		    	ans.add( new AbstractMap.SimpleEntry<String, Integer>
		    		( document.getString("_id"), document.getDouble("count").intValue() ) 
		    			);
		    }
		});
	   return ans;
   }
   */
}
