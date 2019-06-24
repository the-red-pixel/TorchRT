package com.theredpixelteam.cocoabean;

import javax.annotation.Nullable;

/**
 * Thrown if the CocoaBean entity class does not match
 * the standard of CocoaBean entity declaration.
 */
public class CocoaBeanMalformationException extends Exception {
    public CocoaBeanMalformationException()
    {
    }

    public CocoaBeanMalformationException(@Nullable String msg)
    {
        super(msg);
    }

    public CocoaBeanMalformationException(@Nullable Throwable cause)
    {
        super(cause);
    }

    public CocoaBeanMalformationException(@Nullable String msg,
                                          @Nullable Throwable cause)
    {
        super(msg, cause);
    }
}
