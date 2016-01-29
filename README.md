# CS221_Proj
Web Crawler

DB: mongo or sqlite

Yen will provide:
bool insertPage(text): insert whole page for later use, so that we don’t need to crawl it every time.
bool insertToken( token | freq | URL ): insert tokens into DB,  (token | freq | URL)
insertThreeGram( 3G   | freq  | URL )

int getTokenFreq(string token): return freq of token in whole collection
int getThreeGramFreq(3G): return freq of 3G

filename(URL) = subdomain + path


Tokenizer:
tokenize(text, URL)
divide URL into domain & subdomain


Crawler:
domain_subdomain
shouldVisit() 添加：1. 不應訪問同一個page太多次(20次？)    OR
                                2. 不應訪問同一個page一次以上 
shouldVisit() 調用intendToVisit() under myCrawlerStats類，傳入url參數，返回boolean
新添myCrawlerStats類文件 myCrawlerParams類文件 

雁豐的storage：public class docStore implements interDocStore()
                          public interface interDocStore()
docStore函數：docStore(String storePath)      constructor
                         storeDocument(String url, String html)
                         getDocument(String url)
                         getCrawledUrls()

Crawler抓取URL暫時完成了，現在需要storage的代碼部分來存儲信息。



