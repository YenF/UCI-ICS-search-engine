# CS221_Proj
Web Crawler

##Storage: 
Use MongoDB to store data. Every change will reflect to "online database", which means we are now using the same database. After the function is stable, I'll change it to local to run on ICS machine.
###Functions: 
  Class: FileStorage, TokenStorage
  
  FileStorage: store whole pages, mainly for 遠帆
  
  TokenStorage: store tokens and 3-grams, mainly for 建霖學長
  
  For function details, please generate javadoc.


##Tokenizer:
tokenize(text, URL)
divide URL into domain & subdomain


##Crawler:
domain_subdomain
shouldVisit() 添加：1. 不應訪問同一個page太多次(20次？)    OR
                                2. 不應訪問同一個page一次以上 
shouldVisit() 調用intendToVisit() under myCrawlerStats類，傳入url參數，返回boolean
新添myCrawlerStats類文件 myCrawlerParams類文件 


Crawler抓取URL暫時完成了，現在需要storage的代碼部分來存儲信息。



