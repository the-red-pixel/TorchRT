package com.theredpixelteam.cocoabean;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This object represents a CocoaBean namespace, providing
 * bean entity management, and managed directly by a
 * CocoaBean service.
 *
 * @see CocoaBeanService
 */
public class CocoaBeanNamespace {
    public CocoaBeanNamespace(@Nonnull String name)
    {
        this.name = Objects.requireNonNull(name, "name");
    }

    /**
     * Get name of this namespace.
     *
     * @return Name
     */
    public @Nonnull String getName()
    {
        return name;
    }

    // TODO

    private List<CocoaBeanEntity> queryEntityListByIdentity(String identity)
    {
        List<CocoaBeanEntity> list = entitiesByIdentity.get(identity);

        return list == null ? Collections.emptyList() : list;
    }

    private List<CocoaBeanEntity> getEntityListByIdentity(String identity)
    {
        return entitiesByIdentity.computeIfAbsent(identity, k -> new ArrayList<>());
    }

    private final Map<String, List<CocoaBeanEntity>> entitiesByIdentity = new HashMap<>();

    private final Map<Integer, CocoaBeanEntity> entities = new HashMap<>();

    private final AtomicInteger counter = new AtomicInteger(0);

    private final String name;
}
