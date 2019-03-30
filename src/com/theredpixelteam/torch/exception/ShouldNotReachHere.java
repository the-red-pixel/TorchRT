package com.theredpixelteam.torch.exception;

/**
 * Thrown when the flow entered an unexpected state.
 */
public class ShouldNotReachHere extends RuntimeException {
    public ShouldNotReachHere()
    {
    }

    public ShouldNotReachHere(String msg)
    {
        super(msg);
    }

    public ShouldNotReachHere(Throwable cause)
    {
        super(cause);
    }

    public ShouldNotReachHere(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
