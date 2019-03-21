package com.theredpixelteam.cocoabean;

import java.util.HashMap;
import java.util.Map;

public class CocoaBeanEntity {
    public CocoaBeanEntity(int id, String identity)
    {
        this.id = id;
        this.identity = identity;
    }

    /**
     * Get the source identity of the entity.
     *
     * @return Source identity
     */
    public String getIdentity()
    {
        return identity;
    }

    /**
     * Get the ID of this entity.
     *
     * @return Entity ID
     */
    public int getEntityID()
    {
        return id;
    }



    private final int id;

    private final String identity;

    private final Map<String, CocoaBeanElement> entities = new HashMap<>();
}
