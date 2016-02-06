package crawl;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Frank on 16/2/5.
 */
public class URLhelper {
    public static String removeQuery(String url) throws URISyntaxException {
        URI uri = new URI(url.replace(" ", "%20"));
        return uri.getScheme() + "://" + uri.getAuthority() + uri.getPath();
    }
}
