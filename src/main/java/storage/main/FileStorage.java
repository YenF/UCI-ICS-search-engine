package storage.main;

import static java.util.Arrays.asList;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;

/**
 * Operation related to URL page could be found here.
 * @author Yen
 *
 */
public class FileStorage {
	
	private static final FileStorage instance = null;
	private MongoDB DB;
	private MongoDatabase db;
	private MongoCursor<Document> iter;
	public final static String RAWPAGE_DB_NAME = "cs221_rawpages";
	public final static String PAGE_COLL_NAME = "URL_Pages";
	public final static String MONGOLAB_URI = 
			"mongodb://UCI_Handsomes:UCI_Handsomes@ds051635.mongolab.com:51635/cs221_rawpages";
	public final static String ICS_URI = 
			"mongodb://UCI_Handsomes:UCI_Handsomes@ramon-limon.ics.uci.edu:8888/"+RAWPAGE_DB_NAME;
	public final static String LOCAL_URI = 
			"mongodb://UCI_Handsomes:UCI_Handsomes@127.0.0.1:8888/"+RAWPAGE_DB_NAME;
	/**
	 * connect to MONGOLAB_URI, ICS_URI or LOCAL_URI
	 * @param URI
	 */
	public FileStorage( String URI ) {
		
	    DB = new MongoDB();
		DB.init(URI, RAWPAGE_DB_NAME);
		
	       // Now connect to your databases
	    db = DB.db;
	}
	
	/**
	 * remove all stored pages in DB. Use it wisely.
	 */
	public void reset() {
		MongoCollection coll = db.getCollection(PAGE_COLL_NAME);
		coll.drop();
		IndexOptions IndOpt = new IndexOptions();
		IndOpt.unique(true);
		db.createCollection(PAGE_COLL_NAME);
		coll = db.getCollection(PAGE_COLL_NAME);
		coll.createIndex ( new Document("URL",1) , IndOpt);
	}
	
	/**
	* Insert an URL page into DB, uniqueness is determined by URL
	* @param URL
	* @param content
	* @return True for success, False for something wrong
	*/
   public boolean insertURLPage(String URL, String content) {
	   //get collection
   try{
	   db.getCollection(PAGE_COLL_NAME).insertOne( 
   new Document("URL", URL).append("content", content)
				   );
	   } catch ( Exception e ) {
		   return false;
	   }
	   return true;
   }
   
	/**
	* Set iterator of collection "URL_Pages". Use getNextPage() to retrieve page one by one.
	*/
   public void resetPagesIterator() {
	   iter = db.getCollection(PAGE_COLL_NAME).find().sort(
			   new Document("URL",1) ).iterator();
   }
   
   /**
	* Return HashMap type of (URL, text). Call it multiple times until it returns null;
	* If return null, means no more element.
	* @return Map.Entry of (URL, text)
	*/
   public Map.Entry<String,String> getNextPage() {
	   HashMap<String, String> tmp = null;
	   if ( !iter.hasNext() ) return null;
	   Document tmpDoc = iter.next();
	   tmp = new HashMap<String,String>();
	   tmp.put( tmpDoc.getString("URL"), tmpDoc.getString("content") );
	   Map.Entry<String, String> ret = tmp.entrySet().iterator().next();
	   return ret;
   }
   
   /**
	* Get page content by URL. Page will be in String format. Only one page will be returned.
	* @param URL
	* @return page content in String format
	*/
   public String getPageByURL(String URL) {
	   FindIterable<Document> it = db.getCollection(PAGE_COLL_NAME).find( new Document("URL",URL) );
	   return it.iterator().next().getString("content");
   }
	
   // functions below mainly for proj2
   
   public int getUniquePageNum() {
	   AggregateIterable<Document> iterable = db.getCollection(PAGE_COLL_NAME).aggregate(asList(
		        new Document("count", new Document("$sum", 1))
		        )
			   );
	   
	   return iterable.first().getInteger("count");
   }
   
   public List<Map.Entry<String, Integer>> getUniqueSubdomainList() {
	   
	   return new ArrayList();
   }
   
   public Map.Entry<String, Integer> getLongestPage() {
	   
	   return new AbstractMap.SimpleEntry<String, Integer>(null);
   }
   
   
}
