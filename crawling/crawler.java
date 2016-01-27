package crawling;

/**
 * Created by Frank on 16/1/26.
 */

import crawling.myCrawler;
import storage.DocumentStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class crawler {
    /**
     * General code structure borrowed from:
     * http://code.google.com/p/crawler4j/source/browse/src/test/java/edu/uci/ics/crawler4j/examples/basic/BasicCrawlController.java
     */

    public static Collection<String> crawl(String seedURL, String intermediateStoragePath, int maxDepth, int maxPages) {
        HashSet<String> crawledUrls = new HashSet<String>();

        try {
            // Setup the crawler configuration
            CrawlConfig config = new CrawlConfig();
            config.setCrawlStorageFolder(intermediateStoragePath);
            config.setPolitenessDelay(300);
            config.setMaxDepthOfCrawling(maxDepth);
            config.setMaxPagesToFetch(maxPages);
            config.setResumableCrawling(true);

            /**
             * 把你们两个的Student ID写在我的ID后面, 空一格
             */
            config.setUserAgentString("UCI IR crawler 18601447");
            config.setIncludeBinaryContentInCrawling(false);

            // Instantiate controller
            PageFetcher pageFetcher = new PageFetcher(config);
            RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
            RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
            CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

            controller.addSeed(seedURL);

            // Start crawling
            controller.start(myCrawler.class, 5); // 5 threads

            // Get list of crawled URLs for each crawler
            List<Object> crawlersLocalData = controller.getCrawlersLocalData();

            for (Object localData : crawlersLocalData) {
                /**
                 * Get data
                 */
            }
        }
        catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return crawledUrls;
    }
}

