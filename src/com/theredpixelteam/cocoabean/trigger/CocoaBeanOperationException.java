package com.theredpixelteam.cocoabean.trigger;

/**
 * Represent the exception occurred during the operation
 * of a CocoaBean element.
 */
public class CocoaBeanOperationException extends Exception {
    public CocoaBeanOperationException()
    {
    }

    public CocoaBeanOperationException(String msg)
    {
        super(msg);
    }

    public CocoaBeanOperationException(Throwable cause)
    {
        super(cause);
    }

    public CocoaBeanOperationException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
