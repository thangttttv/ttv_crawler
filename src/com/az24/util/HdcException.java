
/*******************************************************************************
 * File name: InetException.java
 * 
 * @author Nguyen Thien Phuong 
 * 
 * Copyright (c) 2007 iNET Media Co .Ltd 
 * Created on Mar 15, 2007 1:51:53 PM
 ******************************************************************************/


package com.az24.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.StringTokenizer;

public class HdcException extends Exception {
    
	static final long serialVersionUID = 0L;
	
    public static final int INET_OBJECT_DOES_NOT_EXIST = -6;

    public static final int INET_OBJECT_DUPLICATE = -5;

    /** Error code for execute sql command - successful */
    public static final int INET_EXECUTE_SQL = 1;
    
    /** Error code for error connect to database */
    public static final int INET_CONNECT_DB = 2;
    
    /** Stores the error code of the InetException */
    protected int m_type = 0;
    
    /** A string message describing the InetException */
    protected String m_message = "NO MESSAGE";

    /** Stores a forwared exception */
    protected Throwable m_rootCause = null;

    /** Default prefix for a InetException message */
    public static final String C_EXCEPTION_PREFIX = "osum.common.InetException";
    
    /***************************************************************
     * @return Returns the m_message.
     ***************************************************************/
    public String getMessage() {
        return m_message;
    }
    /***************************************************************
     * @param m_message The m_message to set.
     ***************************************************************/
    public void setMessage(String m_message) {
        this.m_message = m_message;
    }
    /***************************************************************
     * @return Returns the m_rootCause.
     ***************************************************************/
    public Throwable getRootCause() {
        return m_rootCause;
    }
    /***************************************************************
     * @param cause The m_rootCause to set.
     ***************************************************************/
    public void setRootCause(Throwable cause) {
        m_rootCause = cause;
    }
    /***************************************************************
     * @return Returns the m_type.
     ***************************************************************/
    public int getType() {
        return m_type;
    }
    /***************************************************************
     * @param m_type The m_type to set.
     ***************************************************************/
    public void setType(int m_type) {
        this.m_type = m_type;
    }
    
    public HdcException() {
        super();
    }
    /**
     * Contructs a InetException with the provided error code, 
     * The error codes used should be the constants from the DDBDEception class.
     *
     * @param i Exception error code
     */
    public HdcException(int type) {
        this("InetException ID: " + type, type, null);
    }

    /**
     * Contructs a InetException with the provided error code and
     * a given root cause.
     * The error codes used should be the constants from the DDBDEception class.
     *
     * @param i Exception code
     * @param e Forwarded root cause exception
     */
    public HdcException(int type, Throwable rootCause) {
        this("InetException ID: " + type, type, rootCause);
    }
    
    /**
     * Constructs a InetException with the provided description.
     *
     * @param s Exception message
     */
    public HdcException(String message) {
        this(message, 0, null);
    }

    /**
     * Contructs a InetException with the provided description and error code.
     * 
     * @param s Exception message
     * @param i Exception code
     */
    public HdcException(String message, int type) {
        this(message, type, null);
    }

    /**
     * Construtcs a InetException with a detail message and a forwarded 
     * root cause exception
     *
     * @param s Exception message
     * @param e Forwarded root cause exception
     */
    public HdcException(String message, Throwable rootCause) {
        this(message, 0, rootCause);
    }

    /**
     * Creates a InetException with a provided error code, 
     * a forwarded root cause exception and a detail message.
     * The further processing of the exception can be controlled 
     *
     * @param s Exception message
     * @param i Exception code
     * @param e Forwarded root cause exception
     */    
    public HdcException(String message, int type, Throwable rootCause) {
        super(C_EXCEPTION_PREFIX + ": " + message);
        this.m_message = message;
        this.m_type = type;
        this.m_rootCause = rootCause;
    }
        
    /**
     * Get the root cause Exception which was provided
     * when this exception was thrown.
     *
     * @return The root cause Exception.
     */
    public Exception getException() {        
        try {
            return (Exception)getRootCause();
        } catch (ClassCastException e) {
            return null;
        }
    }
    
    /**
	 * Return a string with the stacktrace. for this exception
	 * and for all encapsulated exceptions.
	 * Creation date: (10/23/00 %r)
	 * @return java.lang.String
	 */
	public String getStackTraceAsString() {
	    java.io.StringWriter sw = new java.io.StringWriter();
	    java.io.PrintWriter pw = new java.io.PrintWriter(sw);
	
        // use stack trace of this eception and add the root case 
        super.printStackTrace(pw);

        // if there are any encapsulated exceptions, write them also.
        if(m_rootCause != null) {
            StringWriter _sw = new StringWriter();
            PrintWriter _pw = new PrintWriter(_sw);
            _pw.println("-----------");                
            _pw.println("Root cause:");                
            m_rootCause.printStackTrace(_pw);
            _pw.close();
            try {
                _sw.close();
            }
            catch(Exception exc) {
    
            // ignore the exception
            }
            StringTokenizer st = new StringTokenizer(_sw.toString(), "\n");
            while(st.hasMoreElements()) {
                String s = ">" + (String)st.nextElement();
                while ( (s != null) && (! "".equals(s)) && ((s.endsWith("\r") || s.endsWith("\n") || s.endsWith(">"))) ) {
                    s = s.substring(0, s.length()-1);
                } 
                if ((s != null) && (! "".equals(s))) pw.println(s);
            }
        }
	    pw.close();
	    try {
	        sw.close();
	    } catch(Exception exc) {
	        // ignore the exception
	    }
	    return sw.toString();
	}    

    /**
     * Print the exception stack trace to System.out.
     */
    public void printStackTrace() {
        printStackTrace(System.out);
    }

    /**
     * Prints this <code>Throwable</code> and its backtrace to the
     * specified print stream.
     */
    public void printStackTrace(java.io.PrintStream s) {
        s.println(getStackTraceAsString());
    }

    /**
     * Prints this <code>Throwable</code> and its backtrace to the specified
     * print writer.
     */
    public void printStackTrace(java.io.PrintWriter s) {
        s.println(getStackTraceAsString());
    }
    
    /**
     * Overwrites the standart toString method.
     */
    public String toString() {
        StringBuffer output = new StringBuffer();
        output.append(C_EXCEPTION_PREFIX + ": ");
        output.append(m_type + " ");
        if (m_message != null && (!"".equals(m_message))) {
            output.append("Detailed error: ");
            output.append(m_message + ". ");
        }
        if(m_rootCause != null) {
            output.append("\nroot cause was ");
            output.append(m_rootCause);
        }
        return output.toString();
    }    
}

