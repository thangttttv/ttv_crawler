#!/bin/sh
cd /data/crawler
/usr/lib/jvm/jre-1.6.0-openjdk.x86_64/bin/java -Xms256M -Xmx512M -cp /data/crawler/:./lib/mysql.jar:./lib/jxl.jar:./lib/commons-codec-1.3.jar:./lib/commons-logging-1.1.1.jar:./lib/commons-net-2.2.jar:./lib/cron4j-2.2.3.jar:./lib/db4o-full-java5-8.1-SNAPSHOT.jar:./lib/hdcCrawler.jar:./lib/httpclient-4.0.1.jar:./lib/httpcore-4.0.1.jar:./lib/jericho-html-3.1.jar:./lib/json-simple-1.1.jar:./lib/junit-4.7.jar:./lib/nekohtml-1.9.14.jar:./lib/xercesImpl-2.9.1.jar com.az24.crawler.product.ProductCrawlerDaily
