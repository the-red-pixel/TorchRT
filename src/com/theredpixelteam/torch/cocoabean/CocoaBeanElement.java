package com.theredpixelteam.torch.cocoabean;

public class CocoaBeanElement {
    CocoaBeanElement(int id, String identity)
    {
        this.id = id;
        this.identity = identity;
    }

    public String getIdentity()
    {
        return identity;
    }

    public int getElementID()
    {
        return id;
    }

    private final int id;

    private final String identity;
}
