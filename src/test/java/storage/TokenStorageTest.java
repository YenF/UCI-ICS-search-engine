package storage;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import data.Pair.Pair;
import storage.main.TokenStorage;

public class TokenStorageTest {

private static TokenStorage ts; 
	
	@BeforeClass
	public static void setupDB() throws InterruptedException {
		ts = new TokenStorage(TokenStorage.MONGOLAB_URI);
		ts.reset();
		//Thread.sleep(1000);
	}
	
	/**
	 * import some data, couldn't use everytime
	 */
	//@Ignore
	@Test
	public void insertTokenTest() {
		System.out.println("---Testing insertToken()---");
		for ( int i=1; i<=10; i++) {
	    	ts.insertToken("hi"+i, i, "TESTURL"+i);
    	}
		System.out.println("---Testing BULK insertToken()---");
		List l = new ArrayList();
		for ( int i=11; i<=20; i++) {
	    	l.add( new Pair("hi"+i,i) );
    	}
		try {
			ts.insertToken(l, "TESTURLBULK");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("---Complete Testing insertToken()---");
	}
	
	@Ignore
	@Test
	public void getTokenFreqTest() {
		//what if there no such token? test will FAIL~!
		System.out.println("---Testing getTokenFreq()---");
		for ( int i=1; i<=10; i++ ) {
			System.out.println( "hi" + i + ": " + ts.getTokenFreq("hi" + i) );
		}
		System.out.println("---Complete Testing getTokenFreq()---");
	}
	
	@Test
	public void getHighestFreq_TokenTest() {
		System.out.println("---Testing getHighestFreq_Token()---");
		System.out.println( ts.getHighestFreq_Token(10) );
		System.out.println("---Complete Testing getHighestFreq_Token()---");
	}
	
	
	@Test
	public void insert3GTest() {
		System.out.println("---Testing insert3G()---");
		for ( int i=1; i<=10; i++) {
	    	ts.insert3G("hi"+i+"hi"+(i+1)+"hi"+(i+2), i, "TESTURL"+i);
    	}
		List l = new ArrayList();
		for ( int i=11; i<=20; i++) {
	    	//l.add( new Pair("hi"+i+"hi"+(i+1)+"hi"+(i+2),i) );
    	}
		try {
			//if ( !l.isEmpty() )
				ts.insert3G(l, "TESTURLBULK");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("---Complete Testing insert3G()---");
	}
	
	@Ignore
	@Test
	public void get3GFreqTest() {
		//what if there no such token? test will FAIL~!
		System.out.println("---Testing get3GFreq()---");
		for ( int i=1; i<=10; i++ ) {
			System.out.println( "hi"+i+"hi"+(i+1)+"hi"+(i+2)+": " + ts.get3GFreq( "hi"+i+"hi"+(i+1)+"hi"+(i+2) ) );
		}
		System.out.println("---Complete Testing get3GFreq()---");
	}
	
	//@Ignore
	@Test
	public void getHighestFreq_3GTest() {
		System.out.println("---Testing getHighestFreq_3G()---");
		System.out.println( ts.getHighestFreq_3G(10) );
		System.out.println("---Complete Testing getHighestFreq_3G()---");
	}

}
