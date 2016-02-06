package crawl;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Frank on 16/2/5.
 */

public class BasicCrawlStats {

    private HashSet<String> urlCrawled = new HashSet<String>();
    private HashMap<String, Integer> pagesToCrawl = new HashMap<String, Integer>();

    public HashSet<String> getUrlsCrawled() {
        return this.urlCrawled;
    }

    public void addCrawledUrl(String url) {
        this.urlCrawled.add(url);
    }

    public boolean intendToVisit(String url) throws URISyntaxException {

        String urlR = URLhelper.removeQuery(url);
        if (urlR == null)
            return true;

        int count = 1;
        if (this.pagesToCrawl.containsKey(urlR))
            count = this.pagesToCrawl.get(urlR);

        if (count >= 10)
            return false;

        // Update with another intent
        this.pagesToCrawl.put(urlR, count + 1);

        return true;
    }
}
