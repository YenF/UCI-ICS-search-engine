package storage.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class TokenStorageTest {

private static TokenStorage ts; 
	
	@BeforeClass
	public static void setupDB() throws InterruptedException {
		ts = new TokenStorage();
		//Thread.sleep(1000);
	}
	
	/**
	 * import some data, couldn't use everytime
	 */
	@Ignore
	@Test
	public void insertTokenTest() {
		System.out.println("---Testing insertToken()---");
		for ( int i=1; i<=10; i++) {
	    	ts.insertToken("hi"+i, i, "TESTURL"+i);
    	}
		System.out.println("---Complete Testing insertToken()---");
	}
	
	//@Ignore
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
	
	@Ignore
	@Test
	public void insert3GTest() {
		
	}
	
	@Ignore
	@Test
	public void get3GFreqTest() {
		
	}

}
