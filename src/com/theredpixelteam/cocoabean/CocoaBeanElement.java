package com.theredpixelteam.cocoabean;

import java.util.HashMap;
import java.util.Map;

public class CocoaBeanElement {
    public CocoaBeanElement(int id, String identity)
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

    private final Map<String, CocoaBeanEntity> entities = new HashMap<>();
}
