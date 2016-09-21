package com.az24.db.pool;
import java.io.*;
import java.util.*;

public class DBConfig {
   
	 //private static final String fileName = "config.conf";

    //========================================================================//
    //             Database Parameters
    //========================================================================//
   
    public static String db_driver_service   = "com.mysql.jdbc.Driver";
    public static String db_url_service      = "jdbc:mysql://localhost:3306/qadsd?characterEncoding=UTF-8";   
    public static String db_user_service     = "root";
    public static String db_pass_service     = "";
    
    
    public static String db_log_url_service      = "jdbc:mysql://localhost:3306/qadsd?characterEncoding=UTF-8";   
    public static String db_log_user_service     = "root";
    public static String db_log_pass_service     = "";
    
    public static String db_user_url_service      = "jdbc:mysql://localhost:3306/qadsd?characterEncoding=UTF-8";   
    public static String db_user_user_service     = "root";
    public static String db_user_pass_service     = "";
    
    public static String db_raovat_url_service ="jdbc:mysql://localhost:3306/az24?autoReconnect=true&characterEncoding=UTF-8";
    public static String db_raovat_user_service ="root";
    public static String db_raovat_pass_service ="";
    
    public static String db_raovatdes_url_service ="jdbc:mysql://localhost:3306/az24?autoReconnect=true&characterEncoding=UTF-8";
    public static String db_raovatdes_user_service ="root";
    public static String db_raovatdes_pass_service ="";
    
    public static String db_tinhhinh_url_service ="jdbc:mysql://localhost:3306/az24?autoReconnect=true&characterEncoding=UTF-8";
    public static String db_tinhhinh_user_service ="root";
    public static String db_tinhhinh_pass_service ="";
    
    public static String db_xoso_url_service ="jdbc:mysql://42.112.31.172:3306/vtc_10h_xs?autoReconnect=true&characterEncoding=UTF-8";
    public static String db_xoso_user_service ="thangtt";
    public static String db_xoso_pass_service ="thangtt123";
    
    public static String db_football_url_service ="jdbc:mysql://localhost:3306/vtc_bongda?autoReconnect=true&characterEncoding=UTF-8";
    public static String db_football_user_service ="root";
    public static String db_football_pass_service ="";
    
    public static String db_shop_url_service ="jdbc:mysql://localhost:3306/vtc_swaphub?autoReconnect=true&characterEncoding=UTF-8";
    public static String db_shop_user_service ="root";
    public static String db_shop_pass_service ="";
    
    public static String db_gamestore_url_service ="jdbc:mysql://localhost:3306/vtc_game_store?autoReconnect=true&characterEncoding=UTF-8";
    public static String db_gamestore_user_service ="ustoregame";
    public static String db_gamestore_pass_service ="pstoregame&*(";
    
    public static int db_connection = 3;
    public static String sms_send_queue = "SMS_SEND_QUEUE";
    public static String sms_send_log = "SMS_SEND_LOG";
    public static String sms_receive_queue = "SMS_RECEIVE_QUEUE";
    public static String sms_receive_log = "SMS_RECEIVE_LOG";
    
    public static String file_kqjson_mb = "C://Projects//kq_mb.txt";
    public static String file_kqjson_mn = "C://Projects//kq_mn.txt";
    public static String file_kqjson_mt = "C://Projects//kq_mt.txt";
    
    public static String file_kqjson_mb_2 = "C://Projects//kq_mb.txt";
    public static String file_kqjson_mn_2 = "C://Projects//kq_mn.txt";
    public static String file_kqjson_mt_2 = "C://Projects//kq_mt.txt";
    
    public static String file_feed = "C://Projects//boxnote.php";
    public static String file_feed_mobile = "C://Projects//boxnote2.php";
    
    public static String file_kqhtml_mb = "./conf/MienBacTT.html";
    public static String file_kqhtml_mn = "./conf/MienNamTT.html";
    public static String file_kqhtml_mt = "./conf/MienTrungTT.html";
    
    public static String file_kqhtml_mb_xosoplus = "./conf/MienBacTT.html";
    public static String file_kqhtml_mn_xosoplus = "./conf/MienNamTT.html";
    public static String file_kqhtml_mt_xosoplus = "./conf/MienTrungTT.html";
    
    public static String file_json_match_date = "C:/Projects/";
    public static String file_json_match_live = "C:/Projects/machlive.html";
    
    /**
     * Contains the parameters and default values for this gateway
     * such as system id, password, default npi, and ton of sender...
     */
    private static Properties properties = new Properties();

    /**
     * Loads configuration parameters from the file with the given name.
     * Sets private variable to the loaded values.
     */
    public static void loadProperties() throws IOException {
    	//URL url = DBConfig.class.getResource(fileName);   		
   		//String realPathFile = url.getFile().replaceAll("%20"," ");
   		//System.out.println(realPathFile);
   		//FileInputStream propsFile = new java.io.FileInputStream(realPathFile);
   		// link deploy
   		FileInputStream propsFile = new java.io.FileInputStream("./conf/config.conf"); 
   		properties.load(propsFile);
        propsFile.close();
        db_driver_service = properties.getProperty("db_driver_service", db_driver_service);
        db_url_service  = properties.getProperty("db_url_service", db_url_service);
        db_user_service = properties.getProperty("db_user_service", db_user_service);
        db_pass_service = properties.getProperty("db_pass_service", db_pass_service);
        
        db_log_url_service  = properties.getProperty("db_log_url_service", db_log_url_service);
        db_log_user_service = properties.getProperty("db_log_user_service", db_log_user_service);
        db_log_pass_service = properties.getProperty("db_log_pass_service", db_log_pass_service);
        
        db_user_url_service  = properties.getProperty("db_user_url_service", db_user_url_service);
        db_user_user_service = properties.getProperty("db_user_user_service", db_user_user_service);
        db_user_pass_service = properties.getProperty("db_user_pass_service", db_user_pass_service);
        
        db_raovat_url_service  = properties.getProperty("db_raovat_url_service", db_raovat_url_service);
        db_raovat_user_service = properties.getProperty("db_raovat_user_service", db_raovat_user_service);
        db_raovat_pass_service = properties.getProperty("db_raovat_pass_service", db_raovat_pass_service);
        
        db_raovatdes_url_service  = properties.getProperty("db_raovatdes_url_service", db_raovatdes_url_service);
        db_raovatdes_user_service = properties.getProperty("db_raovatdes_user_service", db_raovatdes_user_service);
        db_raovatdes_pass_service = properties.getProperty("db_raovatdes_pass_service", db_raovatdes_pass_service);
        
        db_tinhhinh_url_service  = properties.getProperty("db_tinhhinh_url_service", db_tinhhinh_url_service);
        db_tinhhinh_user_service = properties.getProperty("db_tinhhinh_user_service", db_tinhhinh_user_service);
        db_tinhhinh_pass_service = properties.getProperty("db_tinhhinh_pass_service", db_tinhhinh_pass_service);
        
        db_xoso_url_service  = properties.getProperty("db_xoso_url_service", db_xoso_url_service);
        db_xoso_user_service = properties.getProperty("db_xoso_user_service", db_xoso_user_service);
        db_xoso_pass_service = properties.getProperty("db_xoso_pass_service", db_xoso_pass_service);
        
        db_football_url_service  = properties.getProperty("db_football_url_service", db_football_url_service);
        db_football_user_service = properties.getProperty("db_football_user_service", db_football_user_service);
        db_football_pass_service = properties.getProperty("db_football_pass_service", db_football_pass_service);
        
        db_shop_url_service  = properties.getProperty("db_shop_url_service", db_shop_url_service);
        db_shop_user_service = properties.getProperty("db_shop_user_service", db_shop_user_service);
        db_shop_pass_service = properties.getProperty("db_shop_pass_service", db_shop_pass_service);
        
        db_gamestore_url_service  = properties.getProperty("db_gamestore_url_service", db_gamestore_url_service);
        db_gamestore_user_service = properties.getProperty("db_gamestore_user_service", db_gamestore_user_service);
        db_gamestore_pass_service = properties.getProperty("db_gamestore_pass_service", db_gamestore_pass_service);
   
        
        sms_send_queue = properties.getProperty("sms_send_queue", sms_send_queue);
        sms_send_log   = properties.getProperty("sms_send_log", sms_send_log);
        sms_receive_queue = properties.getProperty("sms_receive_queue", sms_receive_queue);
        sms_receive_log   = properties.getProperty("sms_receive_log", sms_receive_log);
        
        file_kqjson_mb   = properties.getProperty("file_kqjson_mb", file_kqjson_mb);
        file_kqjson_mn   = properties.getProperty("file_kqjson_mn", file_kqjson_mn);
        file_kqjson_mt   = properties.getProperty("file_kqjson_mt", file_kqjson_mt);
        
        file_kqhtml_mb   = properties.getProperty("file_kqhtml_mb", file_kqhtml_mb);
        file_kqhtml_mn   = properties.getProperty("file_kqhtml_mn", file_kqhtml_mn);
        file_kqhtml_mt   = properties.getProperty("file_kqhtml_mt", file_kqhtml_mt);
        
        file_kqhtml_mb_xosoplus   = properties.getProperty("file_kqhtml_mb_xosoplus", file_kqhtml_mb_xosoplus);
        file_kqhtml_mn_xosoplus   = properties.getProperty("file_kqhtml_mn_xosoplus", file_kqhtml_mn_xosoplus);
        file_kqhtml_mt_xosoplus   = properties.getProperty("file_kqhtml_mt_xosoplus", file_kqhtml_mt_xosoplus);
        
        file_kqjson_mb_2   = properties.getProperty("file_kqjson_mb_2", file_kqjson_mb_2);
        file_kqjson_mn_2   = properties.getProperty("file_kqjson_mn_2", file_kqjson_mn_2);
        file_kqjson_mt_2   = properties.getProperty("file_kqjson_mt_2", file_kqjson_mt_2);
        
        file_feed   = properties.getProperty("file_feed", file_feed);
        file_feed_mobile   = properties.getProperty("file_feed_mobile", file_feed_mobile);
        
        file_json_match_date   = properties.getProperty("file_json_match_date", file_json_match_date);
        file_json_match_live   = properties.getProperty("file_json_match_live", file_json_match_live);
    }

    // Gets a property and converts it into byte.
    static byte getByteProperty(String propName, byte defaultValue) {
        return Byte.parseByte(properties.getProperty(propName,
            Byte.toString(defaultValue)).trim());
    }

    // Gets a property and converts it into integer.
    static int getIntProperty(String propName, int defaultValue) {
        return Integer.parseInt(properties.getProperty(propName,
            Integer.toString(defaultValue)).trim());
    }

}
