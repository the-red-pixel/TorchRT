package com.theredpixelteam.torch;

public class SpongeExecutionException extends RuntimeException {
    public SpongeExecutionException()
    {}

    public SpongeExecutionException(String msg)
    {
        super(msg);
    }

    public SpongeExecutionException(Throwable cause)
    {
        super(cause);
    }

    public SpongeExecutionException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
