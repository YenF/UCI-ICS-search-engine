package crawl;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Frank on 16/2/5.
 */
public class URLhelper {
    /**
     * use string url to remove the query part and returns a string
     */

    public static String removeQuery(String url) throws URISyntaxException {
        URI uri = new URI(url.replace(" ", "%20"));
        return uri.getScheme() + "://" + uri.getAuthority() + uri.getPath();
    }

    /**
     * use string url to remove the path and returns a string
     */

    public static String removePath(String url) throws URISyntaxException {
        URI uri = new URI(url.replace(" ", "%20"));
        return uri.getScheme() + "://" + uri.getAuthority();
    }

    /**
     * simple operations with string url to
     * get the domain and returns a string
     */

    public static String getDomain(String url) throws URISyntaxException {

        URI uri = new URI(url.replace(" ", "%20"));
        String host = uri.getHost();
        if (host != null) {
            host = host.toLowerCase();
            if (host.startsWith("www."))
                host = host.substring("www.".length());
                return host;
            }

        return null;
    }
}
