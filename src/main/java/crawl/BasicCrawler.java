/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package crawl;

import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Pattern;
import data.text_processing.*;
import org.apache.http.Header;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import storage.main.*;
import data.*;

/**
 * @author Yasser Ganjisaffar
 */

public class BasicCrawler extends WebCrawler {

        private tokenGen tokengen = null;
        private final static Pattern IMAGE_EXTENSIONS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpeg|png|tiff|mid|mp2" +
                "|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf|rm|smil|wmv|swf|wma|zip|rar|gz|ico|pfm|c|h|o))$");
        private int pagecount=0;
        private FileStorage filestorage;
        private TokenStorage tokenstore;
        private BasicCrawlStats visitStats;
  
<<<<<<< Updated upstream
  @Override
  public void onStart() {
	  filestorage = new FileStorage (FileStorage.LOCAL_URI);
	  tokenstore = new TokenStorage(TokenStorage.LOCAL_URI);
  }
=======
        @Override
        public void onStart() {
             filestorage = new FileStorage (FileStorage.MONGOLAB_URI);
             tokenstore = new TokenStorage(TokenStorage.MONGOLAB_URI);
             visitStats = new BasicCrawlStats();
        }
>>>>>>> Stashed changes
  
        /**
         * You should implement this function to specify whether the given url
         * should be crawled or not (based on your crawling logic).
         */
        @Override
        public boolean shouldVisit(Page referringPage, WebURL url) {
              String href = url.getURL().toLowerCase();
              // Ignore the url if it has an extension that matches our defined set of image extensions.
              if (IMAGE_EXTENSIONS.matcher(href).matches()) {
                return false;
              }
              // Don't crawl the same pages too many times

          try {
              if (!visitStats.intendToVisit(url.getURL())){
                return false;
              }
          } catch (URISyntaxException e) {
              e.printStackTrace();
          }

          // Only accept the url if it is in the "www.ics.uci.edu" domain and protocol is "http".
              return href.contains(".ics.uci.edu/");
        }

      /**
       * This function is called when a page is fetched and ready to be processed
       * by your program.
       */
      @Override
      public void visit(Page page) {

              pagecount++;
              if(tokengen==null) tokengen = new tokenGen();
              int docid = page.getWebURL().getDocid();
              String url = page.getWebURL().getURL();
              String domain = page.getWebURL().getDomain();
              String path = page.getWebURL().getPath();
              String subDomain = page.getWebURL().getSubDomain();
              String parentUrl = page.getWebURL().getParentUrl();
              String anchor = page.getWebURL().getAnchor();

              logger.debug("Docid: {}", docid);
              logger.info("URL: {}", url);
              logger.debug("Domain: '{}'", domain);
              logger.debug("Sub-domain: '{}'", subDomain);
              logger.debug("Path: '{}'", path);
              logger.debug("Parent page: {}", parentUrl);
              logger.debug("Anchor text: {}", anchor);

<<<<<<< Updated upstream
  /**
   * This function is called when a page is fetched and ready to be processed
   * by your program.
   */
  @Override
  public void visit(Page page) {
    pagecount++;
    //if(tokengen==null) tokengen = new tokenGen();
	int docid = page.getWebURL().getDocid();
    String url = page.getWebURL().getURL();
    String domain = page.getWebURL().getDomain();
    String path = page.getWebURL().getPath();
    String subDomain = page.getWebURL().getSubDomain();
    String parentUrl = page.getWebURL().getParentUrl();
    String anchor = page.getWebURL().getAnchor();
=======
              if (page.getParseData() instanceof HtmlParseData) {
>>>>>>> Stashed changes

                    HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
                    String text = htmlParseData.getText();
                    List<String> list = TextProcessor.tokenizeFile(text);
                    System.out.println("*****************************************");
                    filestorage.insertURLPage(url,text);
                    System.out.printf("pagecount is %d\n",pagecount);

<<<<<<< Updated upstream
    if (page.getParseData() instanceof HtmlParseData) {
      HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
      String text = htmlParseData.getText();
      //List<String> list = TextProcessor.tokenizeFile(text);
      System.out.println("*****************************************");
      filestorage.insertURLPage(url,text);
      System.out.printf("pagecount is %d\n",pagecount);
      /*
      if((pagecount%3)==0){
    	  System.out.println("the result from mongodb");
    	  List<Map.Entry<String,Integer>> wordlist = tokenstore.getHighestFreq_Token(20);
    	  int size = wordlist.size();
    	  for(int i=0;i<size;i++){
    		  Map.Entry<String,Integer> tempmap = wordlist.get(i);
    		  System.out.printf("%s %d\n",tempmap.getKey(),tempmap.getValue() );
    	  }
    	  System.out.printf("\n\n");
      }
      //TextProcessor.print(list);
      //TextProcessor.print(TextProcessor.computeWordFrequencies(list));
      //TextProcessor.print3g(TextProcessor.computeThreeGramFrequencies(list));
      tokengen.tokenAnd3gram(text,url);
      System.out.println("*****************************************");
      String html = htmlParseData.getHtml();
      Set<WebURL> links = htmlParseData.getOutgoingUrls();

      logger.debug("Text length: {}", text.length());
      logger.debug("Html length: {}", html.length());
      logger.debug("Number of outgoing links: {}", links.size());
      */
    }
=======
                    if((pagecount%3)==0){
                            System.out.println("the result from mongodb");
                            List<Map.Entry<String,Integer>> wordlist = tokenstore.getHighestFreq_Token(20);
                            int size = wordlist.size();
                            for(int i=0;i<size;i++){
                                Map.Entry<String,Integer> tempmap = wordlist.get(i);
                                System.out.printf("%s %d\n",tempmap.getKey(),tempmap.getValue() );
                        }
                        System.out.printf("\n\n");
                    }
                    //TextProcessor.print(list);
                    //TextProcessor.print(TextProcessor.computeWordFrequencies(list));
                    //TextProcessor.print3g(TextProcessor.computeThreeGramFrequencies(list));
                    tokengen.tokenAnd3gram(text,url);
                    System.out.println("*****************************************");
                    String html = htmlParseData.getHtml();
                    Set<WebURL> links = htmlParseData.getOutgoingUrls();

                    logger.debug("Text length: {}", text.length());
                    logger.debug("Html length: {}", html.length());
                    logger.debug("Number of outgoing links: {}", links.size());
              }
>>>>>>> Stashed changes

              Header[] responseHeaders = page.getFetchResponseHeaders();
              if (responseHeaders != null) {
                    logger.debug("Response headers:");
                    for (Header header : responseHeaders) {
                        logger.debug("\t{}: {}", header.getName(), header.getValue());
                    }
              }

              logger.debug("=============");
            }
    }