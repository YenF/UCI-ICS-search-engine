package crawl;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Frank on 16/2/5.
 */

public class BasicCrawlStats {

    private HashMap<String, Integer> pagesToCrawl = new HashMap<String, Integer>();

    public boolean intendToVisit(String url) throws URISyntaxException {
    	String urlR;
    	if ( url.lastIndexOf("?")!=-1 ) {
    		urlR = url.substring(0, url.lastIndexOf("?"));
    	} else urlR = url;
        String str = "calendar";

        if (urlR.length() > 512){
            return false;
        }
/*
        if (urlR.toLowerCase().contains(str)){
            return false;
        }
*/
        /**
         * if (urlR == null) { return true; }
         */

        int count = 1;
        if (this.pagesToCrawl.containsKey(urlR))
            count = this.pagesToCrawl.get(urlR);

        if (count >= 10)
            return false;

        this.pagesToCrawl.put(urlR, count + 1);

        return true;
    }
}
