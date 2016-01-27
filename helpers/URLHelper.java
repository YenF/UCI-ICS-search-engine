package helpers;

import java.net.URI;
import java.net.URISyntaxException;

public class URLHelper {

	/**
	 * use string url to remove the query part and returns a string
	 * */

	public static String removeQuery(String url) {
		try {
			URI uri = new URI(url.replace(" ", "%20"));
			return uri.getScheme() + "://" + uri.getAuthority() + uri.getPath();
		}
		catch (URISyntaxException e) {
			System.out.println("Error removing query from: " + url);
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * use string url to remove the path and returns a string
	 * */

	public static String removePath(String url) {
		try {
			URI uri = new URI(url.replace(" ", "%20"));
			return uri.getScheme() + "://" + uri.getAuthority();
		}
		catch (URISyntaxException e) {
			System.out.println("Error removing path from: " + url);
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * simple operations with string url to
	 * get the domain and returns a string
	 * */

	public static String getDomain(String url) {
		try {
			URI uri = new URI(url.replace(" ", "%20"));
			String host = uri.getHost();
			if (host != null) {
				host = host.toLowerCase();
				if (host.startsWith("www."))
					host = host.substring("www.".length());

				return host;
			}
		}
		catch (Exception e) {
			System.out.println("Error on url: " + url);
			e.printStackTrace();
		}

		return null;
	}
}
