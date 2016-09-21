package com.az24.crawler.product;

import hdc.crawler.DocumentAnalyzer;
import hdc.crawler.AbstractCrawler.Url;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.A;
import hdc.util.html.HttpURL;
import hdc.util.io.FileUtil;
import hdc.util.lang.UriID;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import jdbm.PrimaryHashMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

import org.apache.http.HttpResponse;

import com.az24.crawler.config.JdbmXmlConfig;
import com.az24.crawler.fiter.AbstractFilter;
import com.az24.dao.ConnectionDataLog;
import com.az24.test.HttpClientUtil;
import com.az24.util.FileLog;

public class ProductCrawlerInjectUrl {
	private String fileJdbmConfig = "";
	private int fetch = 1;
	private PrimaryHashMap<String, Url> urlPrimary;
	private RecordManager urlManager = null;
	private Url url = null;

	private Connection connLog = null;
	private String fileBeanConfig = "";
	private int recollection = 0;
	
	//protected RecordManager urlImageManager;
	//protected PrimaryHashMap<String, ImageConfig> urlImagePrimary;

	public ProductCrawlerInjectUrl(Url url, String fileJdbmConfig,
			String fileBeanConfig, int fetch, int is_slow,int recollection) {
		this.url = url;
		this.fileJdbmConfig = fileJdbmConfig;
		this.fetch = fetch;
		this.fileBeanConfig = fileBeanConfig;
		this.recollection = recollection;
	}

	public void collectionUrl() {
		try {
			JdbmXmlConfig.parseConfig(fileJdbmConfig);
			// delete file url
			FileUtil.deleteFile(JdbmXmlConfig.url);
			connLog = ConnectionDataLog.connection(fileBeanConfig);
			urlManager = RecordManagerFactory
					.createRecordManager(JdbmXmlConfig.url + "/url");
			urlPrimary = urlManager.hashMap("url");
			
			/*urlImageManager = RecordManagerFactory
			.createRecordManager(JdbmXmlConfig.url_image + "/image");
			urlImagePrimary = urlImageManager.hashMap("image");
*/
			int totalUrl = 0;
			int i = 1;
			String strUrl = "";
			System.out.println("Collection:" + url);
			strUrl = url.url;
			i = url.fetchFrom;
			while (i <= url.fetchTo) {
				url.url = strUrl + "," + i + "_24";
				totalUrl += putLink(url, "http://www.vatgia.com",
						"http://www.vatgia.com", this.fetch);
				Thread.sleep(1000);
				i++;
			}

			urlManager.close();
			Calendar calendar = Calendar.getInstance();
			String log = calendar.getTime().toString() + "-->Tong Url:"
					+ totalUrl;
			FileLog.createFileLog(JdbmXmlConfig.file_log + "_log_"
					+ calendar.get(Calendar.DAY_OF_MONTH)
					+ calendar.get(Calendar.MONTH)
					+ calendar.get(Calendar.YEAR) + ".txt");
			FileLog.writer(log);
			closeConnection();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void closeConnection() {
		try {
			if (connLog != null)
				connLog.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean checkEntity(String url_id) {

		try {
			PreparedStatement ps = connLog
					.prepareStatement("select id from tbl_importer where url_id = ? ");
			ps.setString(1, url_id);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private int putLink(Url url, String baseUrl, String rewriterUrl, int fetch)
			throws Exception {
		String html = "";
		if (fetch == 1) {
			HttpClientImpl client = new HttpClientImpl();
			HttpResponse res = client.fetch(url.url);
			html = HttpClientUtil.getResponseBody(res);
		} else {
			html = HttpClientUtil.getHtml(url.url);
		}
		DocumentAnalyzer analyzer = new DocumentAnalyzer.DefaultDocumentAnayzer(
				baseUrl, rewriterUrl);
		System.out.println(url.url);
	
		List<A> list = analyzer.analyze(html);
		int i = 0;
		Url injectUrl = null;
		String id = "";
		int j = 0;
		if (list != null)
			for (A a : list) {

				id = new UriID(new HttpURL(a.getURL())).getIdAsString();
				if(this.recollection==0)
				{
					if (this.checkEntity(id))
						continue;
				}
				if (AbstractFilter.find((String) url.regex, a.getURL())) {
					injectUrl = new Url();
					injectUrl.id = id;
					injectUrl.cat_id = url.cat_id;
					String urlTem = a.getURL();
					String urlTems[] = urlTem.split("/");
					urlTem = urlTems[0] + "/" + urlTems[1] + "/" + urlTems[2]
							+ "/" + urlTems[3] + "/" + urlTems[4]
							+ "/thong_so_ky_thuat" + "/" + urlTems[5];
					injectUrl.url = urlTem;
					urlPrimary.put(id, injectUrl);
					System.out.println("Inject-->i=" + j + a.getURL());
					j++;
				}
				if (i % 30 == 0)
					urlManager.commit();

				i++;
			}
		urlManager.commit();
		return j;
	}
	

	public static void main(String[] args) {
		Url url = new Url();
		url.fetchFrom = 1;
		url.fetchTo=2;
		url.url="http://vatgia.com/568,month/oto-du-lich.html,2_20";
		url.regex="http://www.vatgia.com/568/\\d+/.*.html$";
		url.pagePara="";
		url.cat_id = 632;
	
		ProductCrawlerInjectUrl crawlerInjectUrl = new ProductCrawlerInjectUrl(
				url, "src/com/az24/crawler/config/jdbm.xml",
				"src/com/az24/crawler/config/beanProductVatGia.xml", 1, 1,1);
		crawlerInjectUrl.collectionUrl();

	}

}
