package crawling;

/**
 * Created by Frank on 16/1/26.
 */

import storage.docStore;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import storage.interDocStore;

public class crawler {
    /**
     * General code structure borrowed from:
     * http://code.google.com/p/crawler4j/source/browse/src/test/java/edu/uci/ics/crawler4j/examples/basic/BasicCrawlController.java
     */

    private static final int maxDepth = -1;

    public static Collection<String> crawl(String seedURL) {
        return crawl(seedURL, new docStore("docStorage"));
    }

    public static Collection<String> crawl(String seedURL, interDocStore docStorage) {
        return crawl(seedURL, "intermediateStorage", docStorage, maxDepth, maxDepth);

    }

    public static Collection<String> crawl(String seedURL, String intermediateStoragePath,
                        interDocStore documentStorage,int maxDepth, int maxPages) {
        HashSet<String> crawledUrls = new HashSet<String>();

        try {
            // Setup the crawler configuration
            CrawlConfig myConfig = new CrawlConfig();
            myConfig.setCrawlStorageFolder(intermediateStoragePath);
            myConfig.setPolitenessDelay(300);
            myConfig.setMaxDepthOfCrawling(maxDepth);
            myConfig.setMaxPagesToFetch(maxPages);
            myConfig.setResumableCrawling(true);

            /**
             * 把你们两个的Student ID写在我的ID后面, 空一格
             */
            myConfig.setUserAgentString("UCI IR crawler 18601447 64688315");
            myConfig.setIncludeBinaryContentInCrawling(false);
            
            myCrawlerParams params = new myCrawlerParams();
            params.setSeedUrl(seedURL);
            params.setDocumentStorage(documentStorage);

            // Instantiate controller
            PageFetcher pageFetch = new PageFetcher(myConfig);
            RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
            RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetch);
            CrawlController controller = new CrawlController(myConfig, pageFetch, robotstxtServer);

            controller.addSeed(seedURL);
            controller.setCustomData(params);

            // Start crawling
            controller.start(myCrawler.class, 5); // 5 threads

            // Get list of crawled URLs for each crawler
            List<Object> crawlersLocalData = controller.getCrawlersLocalData();

            for (Object localData : crawlersLocalData) {
                // Get data
                myCrawlerStats s = (myCrawlerStats) localData;
                crawledUrls.addAll(s.getUrlsCrawled());
            }
        }
        catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return crawledUrls;
    }
}

