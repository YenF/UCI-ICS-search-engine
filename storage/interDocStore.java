package storage;

import com.sun.tools.doclets.formats.html.markup.HtmlDocument;

/**
 * Created by Frank on 16/1/27.
 */

public interface interDocStore {
    void storeDocument(String url, String text);
    public Iterable<HtmlDocument> getAll();
    public Iterable<String> getCrawledUrls();
    void close();
}
