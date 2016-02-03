package storage.main;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
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
	private MongoClient client;
	private MongoDatabase db;
	private MongoCursor<Document> iter;
	
	public FileStorage() {
		client = new MongoClient( 
	      		 new MongoClientURI(
	      			"mongodb://UCI_Handsomes:UCI_Handsomes@ds051635.mongolab.com:51635/cs221_rawpages") 
	      		 );
	       // Now connect to your databases
	    db = client.getDatabase("cs221_rawpages");
	    System.out.println("---MongoDB initialized---");
	}
	
	/*	use mono instance will occur concurrency problem
	public static FileStorage newInstance() {
		if ( instance==null ) {
	        FileStorage fs = new FileStorage();
	        return fs;
		}
		return instance;
    }
	*/
	
	/**
	 * remove all elements in DB
	 */
	public void reset() {
		MongoCollection coll = db.getCollection("URL_Pages");
		coll.drop();
		IndexOptions IndOpt = new IndexOptions();
		IndOpt.unique(true);
		db.createCollection("URL_Pages");
		coll = db.getCollection("URL_Pages");
		coll.createIndex ( new Document("URL",1) , IndOpt);
	}
	
	/**
	* Insert an URL page into DB, uniqueness is determined by URL
	* @param URL
	* @param content
	* @return
	*/
   public boolean insertURLPage(String URL, String content) {
	   //get collection
   try{
	   db.getCollection("URL_Pages").insertOne( 
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
	   iter = db.getCollection("URL_Pages").find().iterator();
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
	   FindIterable<Document> it = db.getCollection("URL_Pages").find( new Document("URL",URL) );
	   return it.iterator().next().getString("content");
   }
	   
}
