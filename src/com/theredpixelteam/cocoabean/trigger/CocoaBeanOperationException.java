package com.theredpixelteam.cocoabean.trigger;

import javax.annotation.Nullable;

/**
 * Represent the exception occurred during the operation
 * of a CocoaBean element.
 */
public class CocoaBeanOperationException extends Exception {
    public CocoaBeanOperationException()
    {
    }

    public CocoaBeanOperationException(@Nullable String msg)
    {
        super(msg);
    }

    public CocoaBeanOperationException(@Nullable Throwable cause)
    {
        super(cause);
    }

    public CocoaBeanOperationException(@Nullable String msg,
                                       @Nullable Throwable cause)
    {
        super(msg, cause);
    }
}
