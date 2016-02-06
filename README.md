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


