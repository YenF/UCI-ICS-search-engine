package crawling;

import storage.interDocStore;

/**
 * Created by Frank on 16/1/27.
 */

public class myCrawlerParams {

    private String seedUrl;
    private interDocStore docStorage;

    public String getSeedUrl() {
        return this.seedUrl;
    }

    public void setSeedUrl(String url){
        this.seedUrl = url;
    }

    public interDocStore getDocumentStorage() {
        return this.docStorage;
    }

    public void setDocumentStorage(interDocStore docStorage) {
        this.docStorage = docStorage;
    }
}
