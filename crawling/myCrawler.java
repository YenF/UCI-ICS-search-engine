package crawling;

import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

/*
 * General code structure borrowed from:
 * http://code.google.com/p/crawler4j/source/browse/src/test/java/edu/uci/ics/crawler4j/examples/basic/BasicCrawler.java
*/
public class myCrawler extends WebCrawler {
	private final static Pattern FILTERS
			= Pattern.compile(".*(\\.(css|js|bmp|gif|jpeg|png|tiff|mid|mp2|mp3|mp4|" +
			"|wav|avi|mov|mpeg|ram|m4v|pdf|rm|smil|wmv|swf|wma|zip|rar|gz|ico|pfm|c|h|o))$");

	private myCrawlerStats stats = new myCrawlerStats();

	@Override
	public boolean shouldVisit(Page page, WebURL url) {

		// Don't crawl non-HTML pages
		String href = url.getURL().toLowerCase();
		if (FILTERS.matcher(href).matches()) // filter using file extension
			return false;

		// Don't crawl the same pages too many times
		if (!stats.intendToVisit(url.getURL()))
			return false;

        // Only accept the url if it is in the "www.ics.uci.edu" domain and protocol is "http".
        return href.startsWith("http://www.ics.uci.edu/");
	}

	@Override
	public void visit(Page page) {
		// Keep track of visited URLs
		String url = page.getWebURL().getURL();
		System.out.println("Crawled: " + url);

		// Get the page terms and store them locally
		if (page.getParseData() instanceof HtmlParseData) { // make sure document has HTML data
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String html = htmlParseData.getHtml();

			// Store the information by SQL goes here
			/**
			 * 这部分添加<存储入DB>的代码
			 * 这里存储的是HTML的数据
			 */

		}
	}
}
