/*******************************************************************************
 * File name: DBException.java
 * 
 * @author Nguyen Thien Phuong 
 * 
 * Copyright (c) 2007 iNET Media Co .Ltd 
 * Created on Feb 27, 2007
 ******************************************************************************/

package com.az24.db.pool;

import com.az24.util.HdcException;


public class DBException extends HdcException {
    
	static final long serialVersionUID = 0L;
    /***************************************************************
     * DBException's Constructor
     ********************************************************************************/
    public DBException() {
        super();
    }

    /***************************************************************
     * DBException's Constructor
     ********************************************************************************/
    public DBException(String s) {
        super(s);
    }
    
    /***************************************************************
     * DBException's Constructor
     ********************************************************************************/

    public DBException(int type) {
        super(type);

    }
    /***************************************************************
     * DBException's Constructor
     ********************************************************************************/

    public DBException(int type, Throwable rootCause) {
        super(type, rootCause);

    }
    /***************************************************************
     * DBException's Constructor
     ********************************************************************************/

    public DBException(String message, int type) {
        super(message, type);

    }
    /***************************************************************
     * DBException's Constructor
     ********************************************************************************/

    public DBException(String message, int type, Throwable rootCause) {
        super(message, type, rootCause);

    }
    /***************************************************************
     * DBException's Constructor
     ********************************************************************************/

    public DBException(String message, Throwable rootCause) {
        super(message, rootCause);

    }
}