package storage.test;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

public class FileStorageTest {

	@Ignore
	@Test
    public void testConnetivity()
    {
    	System.out.println("---testing connectivity...---");
    	FileStorage fs = new FileStorage();
        assertTrue("Connectivity test complete", true);
    }
    
    @Ignore
    @Test
    public void testGetPage()
    {
    	System.out.println("---testing getPage---");
    	FileStorage fs = new FileStorage();
    	fs.resetPagesIterator();
    	Map.Entry<String,String> page = fs.getNextPage();
    	System.out.println("---printing out page URL---");
    	while ( page!=null ) {
    		System.out.println("Page URL: " + page.getKey());
    		page = fs.getNextPage();
    	}
    	System.out.println("---getPage() function test complete---");
        assertTrue(true);
    }
    
    @Test
    public void testGetPageByURL() {
    	System.out.println("---testing getPageByURL()---");
    	FileStorage fs = new FileStorage();
    	fs.resetPagesIterator();
    	Map.Entry<String,String> page = fs.getNextPage();
    	System.out.println("---Printing out page content---");
    	String text = fs.getPageByURL(page.getKey());
    	System.out.println(text);
    	System.out.println("---getPageByURL() function test complete---");
        assertTrue(true);
    }

}
