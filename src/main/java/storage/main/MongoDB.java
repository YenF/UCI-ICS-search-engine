package storage.main;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class MongoDB implements InternalDB {
	public MongoDatabase db;
	
	public void reset() {
		// TODO Auto-generated method stub
		
	}
	
	public void init(String URI, String DB_Name) {
		// TODO Auto-generated method stub
		MongoClient client = new MongoClient( 
	      		 new MongoClientURI(
	      				URI) 
	      		 );
	       // Now connect to your databases
	    db = client.getDatabase(DB_Name);
	    System.out.println("---MongoDB initialized---");
	} 
	
}
