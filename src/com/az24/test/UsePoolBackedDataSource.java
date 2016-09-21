package com.az24.test;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;
import com.mchange.v2.c3p0.DataSources;


/**
 *  This example shows how to programmatically get and directly use
 *  an pool-backed DataSource
 */
public final class UsePoolBackedDataSource
{

    public static void main(String[] argv)
    {
	try
	    {
		// Note: your JDBC driver must be loaded [via Class.forName( ... ) or -Djdbc.properties]
		// prior to acquiring your DataSource!

		// Acquire the DataSource... this is the only c3p0 specific code here
		DataSource unpooled = DataSources.unpooledDataSource("jdbc:mysql://210.211.97.11:3306/crawler?autoReconnect=true&characterEncoding=UTF-8",
								     "quangpn",
								     "QuangPN2011@!!!*^");
		Map overrides = new HashMap();
		overrides.put("maxStatements", "200");         //Stringified property values work
		overrides.put("minPoolSize", new Integer(5)); //"boxed primitives" also work
		overrides.put("maxPoolSize", new Integer(10)); //"boxed primitives" also work

		// create the PooledDataSource using the default configuration and our overrides
		 
		DataSource pooled = DataSources.pooledDataSource( unpooled,overrides );
		
		// get hold of a Connection an do stuff, in the usual way
		Connection con  = null;
		Statement  stmt = null;
		ResultSet  rs   = null;
		try
		    {
			con = pooled.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM crawler_tinhhinh_log where id = 1");
			while (rs.next())
			    System.out.println( rs.getString(2) );
		    }
		finally
		    {
			//i try to be neurotic about ResourceManagement,
			//explicitly closing each resource
			//but if you are in the habit of only closing
			//parent resources (e.g. the Connection) and
			//letting them close their children, all
			//c3p0 DataSources will properly deal.
			attemptClose(rs);
			attemptClose(stmt);
			attemptClose(con);
		
		    }
	    }
	catch (Exception e)
	    { e.printStackTrace(); }
    }

    static void attemptClose(ResultSet o)
    {
	try
	    { if (o != null) o.close();}
	catch (Exception e)
	    { e.printStackTrace();}
    }

    static void attemptClose(Statement o)
    {
	try
	    { if (o != null) o.close();}
	catch (Exception e)
	    { e.printStackTrace();}
    }

    static void attemptClose(Connection o)
    {
	try
	    { if (o != null) o.close();}
	catch (Exception e)
	    { e.printStackTrace();}
    }

    private UsePoolBackedDataSource()
    {}
}
