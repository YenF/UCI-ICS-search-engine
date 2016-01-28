package crawling;

/**
 * Created by Frank on 16/1/27.
 */

import helpers.URLHelper;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;

public class myCrawlerStats {

	private HashSet<String> urlsCrawled = new HashSet<String>();
	private HashMap<String, Integer> pagesToCrawl = new HashMap<String, Integer>();

	public HashSet<String> getUrlsCrawled() {
		return this.urlsCrawled;
	}

	public void addCrawledUrl(String url) {
		this.urlsCrawled.add(url);
	}

	public boolean intendToVisit(String url) {
		// Determine if the page has been visited too many times (might be infinite loop)
		String urlWithoutQuery = URLHelper.removeQuery(url);
		if (urlWithoutQuery == null)
			return true;

		// Get the current visit-intent count
		int count = 1;
		if (this.pagesToCrawl.containsKey(urlWithoutQuery))
			count = this.pagesToCrawl.get(urlWithoutQuery);
		
		if (count >= 20)
			return false; //visit single page at most 20 times (with different query strings)
		
		// Update with another intent
		this.pagesToCrawl.put(urlWithoutQuery, count + 1);

		return true;
	}
}