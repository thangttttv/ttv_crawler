package com.az24.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import com.az24.util.io.IOUtil;

public class HttpClientUtil {
	static public void printRequestHeaders(HttpRequest req) {
		Header[] header = req.getAllHeaders();
		for (int i = 0; i < header.length; i++) {
			if (i > 0)
				System.out.print(" # ");
			System.out.print(header[i].getName() + ": " + header[i].getValue());
		}
		System.out.println();
	}

	static public void printResponseHeaders(HttpResponse res) {
		Header[] header = res.getAllHeaders();
		for (int i = 0; i < header.length; i++) {
			System.out.println(header[i].getName() + ": "
					+ header[i].getValue());
		}
		System.out.println();
	}

	static public String getResponseBody(HttpResponse res) throws Exception {
		//    HttpEntity entity = res.getEntity() ;
		//    InputStream is = null ;
		//    if ("gzip".equalsIgnoreCase(entity.getContentEncoding().getValue())) {
		//      is = new GZIPInputStream(entity.getContent()) ;
		//    } else if ("deflate".equals(entity.getContentEncoding().getValue())) {
		//      is = new InflaterInputStream(entity.getContent(), new Inflater(true));
		//    } else {
		//      is = entity.getContent() ;
		//    }
		InputStream is = res.getEntity().getContent();
		byte[] data = IOUtil.getStreamContentAsBytes(is);
		return new String(data, "UTF-8");
	}

	static public void printResponseBody(HttpResponse res) throws Exception {
		InputStream is = res.getEntity().getContent();
		byte[] data = IOUtil.getStreamContentAsBytes(is);
		System.out.println(new String(data, "UTF-8"));
	}

	static public void printCookiStore(CookieStore store) throws Exception {
		List<Cookie> cookies = store.getCookies();
		for (int i = 0; i < cookies.size(); i++) {
			Cookie cookie = cookies.get(i);
			System.out.println("Cookie " + cookie.getDomain() + ": "
					+ cookie.getName() + " = " + cookie.getValue());
		}
	}

	static public String getContentType(HttpResponse res) {
		Header header = res.getFirstHeader("Content-Type");
		if (header == null)
			return "unknown/unknown";
		String value = header.getValue();
		int idx = value.indexOf(";");
		if (idx > 0) {
			value = value.substring(0, idx).trim();
		}
		return value;
	}

	public static String getHtml(String urlToRead) {
		URL url; // The URL to read
		HttpURLConnection conn; // The actual connection to the web page
		BufferedReader rd; // Used to read results from the web page
		String line; // An individual line of the web page HTML
		String result = ""; // A long string containing all the HTML
		try {
			url = new URL(urlToRead);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(
					new InputStreamReader(conn.getInputStream(),"UTF8"));
			while ((line = rd.readLine()) != null) {
				result += line;
			}
			rd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static void main(String[] args) {
		System.out.println(HttpClientUtil.getHtml("http://taoviec.com"));
	}
}
