package storage;

/**
 * Created by Frank on 16/1/26.
 */

import com.sun.tools.doclets.formats.html.markup.HtmlDocument;

import java.io.File;
import java.io.IOException;

     /**
     * 这一部分雁丰再考虑一下吧
     * storage这一整个文件夹都交给你
     */

    public class docStore implements interDocStore {

        public docStore(String storePath) {
            // Make sure storage directory exists
            // A constructor
        }

        public void storeDocument(String url, String html) {
            // Don't forget counting
        }

        public String getDocument(String url) {
            // return this database's url;
            return null;
        }

        public Iterable<String> getCrawledUrls() {
            // The keys are all URLs
            return null;
        }

        public Iterable<HtmlDocument> getAll() {
            // The values are raw HTML, return an iterator that returns HtmlDocuments (which parse the HTML)
            return null;
        }

        public int getSize() {
            // return this database's size
            return 0;
        }

        public void close() {

        }
    }