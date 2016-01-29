package crawling;

import storage.docStore;

import java.util.Collection;

public class MainCrawler {
	public static void main(String[] args) {
		docStore docStore = new docStore("storage");
		try {
			Collection<String> crawledUrls = crawler.crawl("http://www.ics.uci.edu", docStore);
			System.out.println("Finished crawling " + crawledUrls.size() + " page(s)");
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		} finally {
			docStore.close();
		}
	}
}
