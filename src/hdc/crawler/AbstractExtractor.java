package hdc.crawler;

import hdc.crawler.AbstractCrawler.Datum;
import hdc.crawler.AbstractCrawler.Url;
import hdc.crawler.classified.DownloadImageThread;
import hdc.crawler.fetcher.HttpClientFactory;
import hdc.crawler.fetcher.HttpClientImpl;
import hdc.util.html.NodeVisitor;
import hdc.util.html.parser.XPathReader;
import hdc.util.io.FileUtil;
import hdc.util.lang.MD5;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.imageio.ImageIO;

import jdbm.PrimaryHashMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ls.LSException;
import org.xml.sax.SAXParseException;

public abstract class AbstractExtractor {
  protected String dbDir ;
  private String baseUrl ;
  private String siteUrl ;
  private boolean reProcess = false ;
  protected boolean isOpen = false ;
  private DocumentFilter filter ;

  private RecordManager urlManager ;
  protected PrimaryHashMap<String, Url> urlPrimary ;

  private RecordManager datumManger ;
  protected PrimaryHashMap<String, Datum> datumPrimary ;

  private RecordManager extractManager ;
  protected PrimaryHashMap<String, ExtractEntity> extractPrimary ;
  
  private RecordManager thumbManager ;
  protected PrimaryHashMap<String, String> thumbPrimary;
  
  public AbstractExtractor(String dbDir, String baseUrl, String siteUrl) throws Exception {
    this.dbDir = dbDir ;
    this.baseUrl = baseUrl ;
    this.siteUrl = siteUrl ;
    open() ;
  }
  
  public synchronized void open() throws IOException {
    urlManager = RecordManagerFactory.createRecordManager(dbDir + "/url") ;
    urlPrimary = urlManager.hashMap("Url") ;

    datumManger = RecordManagerFactory.createRecordManager(dbDir + "/datum") ;
    datumPrimary = datumManger.hashMap("Datum") ;

    extractManager = RecordManagerFactory.createRecordManager(dbDir + "/extract") ;
    extractPrimary = extractManager.hashMap("Extract") ;
    
    thumbManager = RecordManagerFactory.createRecordManager(dbDir + "/thumb") ;
    thumbPrimary = thumbManager.hashMap("Thumb") ; 
    
    isOpen = true ;
  }
  
  public synchronized void close() throws IOException {
    urlManager.close() ;
    datumManger.close() ;
    extractManager.close() ;
    thumbManager.close() ;
    isOpen = false ;
  }

  public void setFilter(DocumentFilter filter) { this.filter = filter ; }
  public void setReProcess(boolean reProcess) { this.reProcess = reProcess ; }

  public abstract ExtractEntity extract(XPathReader reader, String docId) throws Exception ;

  public void process() throws Exception {
    if(!isOpen) open() ;
    List<String> holder = new ArrayList<String>() ;
    Iterator<Entry<String, Url>> i = urlPrimary.entrySet().iterator() ;
    while(i.hasNext()) {
      Entry<String, Url> entry = i.next() ;
      String id = entry.getKey() ;
      Url value = entry.getValue() ;

      if(reProcess) {

      } else if(extractPrimary.containsKey(id)) continue ;

      if(filter == null) throw new RuntimeException("DocumentFilter cannot null .... ") ;
      else if(filter.acceptProcess(value.url)) {
        holder.add(id) ;
      } 
    }

    if(holder.size() > 0) {
      ProcessThread thread = new ProcessThread(holder) ;
      thread.ids = holder ;
      thread.run() ;
    }
  }
  
  private class ProcessThread extends Thread {
    private List<String> ids ;

    public ProcessThread(List<String> ids) {
      this.ids = ids ;
    }

    public void run() {
      if(ids.size() == 0) return ;
      System.out.println(new Date() + " : total need extract " + ids.size());
      List<String> needRemove = new ArrayList<String>() ;
      for(int i = 0; i < ids.size(); i++) {
        try {
          Datum datum = datumPrimary.get(ids.get(i)) ;
          if(datum == null || datum.data == null) {
            System.out.println(ids.get(i) + " is null ..... ") ;
            needRemove.add(ids.get(i)) ;
            datumPrimary.remove(ids.get(i)) ;
            datumManger.commit() ;
            continue ;
          }
          
          XPathReader reader = null ;
          try {
            reader = CrawlerUtil.createXPathReaderByData(datum.data, siteUrl, baseUrl) ;
          } catch(LSException e) {
            e.printStackTrace() ;
          } catch(SAXParseException e) {
            e.printStackTrace() ;
          }
          if(reader == null) {
            System.out.println((datum.url + " reader null")) ;
            datumPrimary.remove(ids.get(i)) ;
            datumManger.commit() ;
            continue ;
          }
          ExtractEntity entity = extract(reader, datum.id) ;
          if(entity == null) {
            System.out.println((datum.url + " entity null")) ;
            datumPrimary.remove(ids.get(i)) ;
            datumManger.commit() ;
            continue ;
          }
          System.out.println(new Date() + ": (" + i + ") extracted " + datum.url);
          extractPrimary.put(datum.id, entity) ;
          if(i % 5000 == 0) extractManager.commit() ;
        } catch(Exception ex) {
          throw new RuntimeException(ex) ;
        }
      }

      for(int i = 0; i < needRemove.size(); i++) {
        urlPrimary.remove(needRemove.get(i)) ;
      }

      try {
        extractManager.commit() ;
        urlManager.commit() ;
        datumManger.commit() ;
      } catch (IOException e) {
        throw new RuntimeException(e) ;
      }
      
      try {
        FTPClient ftpClient = new FTPClient() ;
        ftpClient.connect("images.az24.vn") ;
        ftpClient.login("ftpHDC","FtpHdc@)!!") ;
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE) ;

        //TODO:begin implement code
        List<String> fileLocal = FileUtil.findFiles(new File("/data/modules/search/thumb"), new FileFilter() {
          public boolean accept(File child) {
            if(child.getName().endsWith(".PNG")) return true ;
            return false;
          }
        }) ;

        Map<String, String> fileHolder = new HashMap<String,String>() ;
        for(int i = 0; i < fileLocal.size(); i++) {
          File file = new File(fileLocal.get(i)) ;
          fileHolder.put(file.getName(), file.getAbsolutePath()) ;
        }
        System.out.println("File local: " + fileHolder.size());
        
        int count = 0 ;
        Iterator<String> i = extractPrimary.keySet().iterator() ;
        Set<String> keySet = new HashSet<String>() ;
        while(i.hasNext()) {
          String id = i.next() ;
          if(thumbPrimary.containsKey(id)) continue ;
          keySet.add(id) ;
        }
        System.out.println("Total need download : " + keySet.size());
        
        Calendar calendar = Calendar.getInstance() ;
        int month = calendar.get(Calendar.MONTH) + 1 ;
        int day = calendar.get(Calendar.DAY_OF_MONTH) ;
        StringBuilder b = new StringBuilder() ;
        if(month < 10) b.append(0) ;
        b.append(month) ;
        if(day < 10) b.append(0) ;
        b.append(day) ;
        String folderName = b.toString() ;
        
        File descFolder = new File(dbDir + "/image/" + folderName) ;
        if(!descFolder.exists()) descFolder.mkdir() ;
        if(!ftpClient.changeWorkingDirectory("/picture_auto/2011/thumb/" + folderName)) {
          while(!ftpClient.makeDirectory("/picture_auto/2011/thumb/" + folderName)) ;
          ftpClient.changeWorkingDirectory("/picture_auto/2011/thumb/" + folderName) ;
        }
        
        i = keySet.iterator() ;
        while(i.hasNext()) {
          String id = i.next() ;
          final String imageName = MD5.digest(id).toString() + ".PNG" ;
          String descPath = descFolder.getAbsolutePath() ;

          ExtractEntity entity = extractPrimary.get(id) ;
          String thumb = (String) entity.getProperty("thumb") ;
          if(thumb == null) {
            thumbPrimary.put(id, id) ;
            thumbManager.commit() ;
            continue ;
          }
          try {
            HttpClientImpl client = new HttpClientImpl() ;
            HttpResponse res = client.fetch(thumb) ;
            if(res.getStatusLine().getStatusCode() == 404) {
              System.out.println("image not found: " + thumb);
              continue ;
            }
            
            BufferedImage originalImage = ImageIO.read(new BufferedInputStream(res.getEntity().getContent())) ;
            
            int type = originalImage.getType() ;
            int width = originalImage.getWidth() ;
            int height = originalImage.getHeight() ;

            if(width > 250) {
              width = 250 ;
              height = height * 250 / width ;
            }
            
            BufferedImage resizeImage = new BufferedImage(120, 113, type) ;
            Graphics2D g = resizeImage.createGraphics() ;
            g.drawImage(originalImage, 0, 0, 120, 113, null) ;
            g.dispose() ;
            g.setComposite(AlphaComposite.Src) ;

            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            ImageIO.write(resizeImage, "PNG", new File(descPath + "/" + imageName)) ;

            boolean result = ftpClient.storeFile(imageName, new FileInputStream(descPath + "/" + imageName)) ;
            if(result) {
              count++ ;
              System.out.println(new Date() + " : upload successfully image " + imageName);
              System.out.println(new Date() + " : commit total " + count);
            } else continue ;
            thumbPrimary.put(id, id) ;
          } catch(ConnectionPoolTimeoutException e){
            e.printStackTrace() ;
            HttpClientFactory.reload() ;
            thumbPrimary.put(id, id) ;
            thumbManager.commit() ;
            continue ;
          } catch(Exception e) {
            System.out.println("image error: " + thumb);
            e.printStackTrace() ;
            thumbPrimary.put(id, id) ;
            thumbManager.commit() ;
            continue ;
          }

          finally {
            extractManager.commit() ;
            thumbManager.commit() ;
          }
        }
        
        //TODO:end 
        
        ftpClient.logout() ;
        ftpClient.disconnect() ;
      } catch(Exception e) { throw new RuntimeException(e) ; } 
    }
  }

  @Deprecated
  protected List<String> rewriteImageSource(final Node node, final String id, final String host) {
    final List<String> srcHolder = new ArrayList<String>() ;
    final List<String> fileNameHolder = new ArrayList<String>() ;
    NodeVisitor visitor = new NodeVisitor() {
      int counter = 0 ;
      public void preTraverse(Node node) {
        String tag = node.getNodeName() ;
        if(node instanceof Element && "img".equalsIgnoreCase(tag)) {
          Element a = (Element) node ;
          String src = a.getAttribute("src") ;
          srcHolder.add(src) ;
          String fileName = MD5.digest(id + ":" + counter).toString() + ".jpg" ;
          fileNameHolder.add(fileName) ;
          counter++ ;
          if(!host.endsWith("/")) a.setAttribute("src", host + "/" + fileName) ;
          else a.setAttribute("src", host + fileName) ;
        }
      }
      public void postTraverse(Node node) {}
    };
    visitor.traverse(node) ;

    try {
      DownloadImageThread thread = new DownloadImageThread(srcHolder, fileNameHolder, dbDir + "/image" ) ;
      thread.run() ;
    } catch (Exception e) {
      throw new RuntimeException(e) ;
    }
    return fileNameHolder ;
  }
  
  public void finalize() throws Throwable { super.finalize() ; }
}
