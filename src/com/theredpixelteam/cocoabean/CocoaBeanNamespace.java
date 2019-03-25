package com.theredpixelteam.cocoabean;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

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

    /**
     * Add a CocoaBean entity using the provided entity constructor
     * function.
     *
     * @param entityConstructor CocoaBean entity constructor
     * @return Entity id
     */
    public int addEntity(Function<Integer, CocoaBeanEntity> entityConstructor)
    {
        int id = counter.getAndIncrement();
        CocoaBeanEntity entity = entityConstructor.apply(id);

        if (entities.putIfAbsent(id, entity) != null)
            throw new IllegalStateException("counter failure");

        getEntityListByIdentity(entity.getIdentity()).add(entity);

        return id;
    }

    /**
     * Get entities by the identity.
     *
     * @param identity Identity of entities
     * @return Entity collection
     */
    public @Nonnull Collection<CocoaBeanEntity> getEntities(String identity)
    {
        return Collections.unmodifiableList(queryEntityListByIdentity(identity));
    }

    /**
     * Get entity by the entity ID.
     *
     * @param entityId Entity ID
     * @return Entity instance if any
     */
    public @Nonnull Optional<CocoaBeanEntity> getEntity(int entityId)
    {
        return Optional.ofNullable(entities.get(entityId));
    }

    /**
     * Remove entities by the identity.
     *
     * @param identity Identity of entities
     * @return Count of removed entities
     */
    public int removeEntities(String identity)
    {
        List<CocoaBeanEntity> list = removeEntityListByIdentity(identity);
        int count = list.size();

        for (CocoaBeanEntity entity : list)
            entities.remove(entity.getEntityID());

        return count;
    }

    /**
     * Remove entity by the entity ID.
     *
     * @param entityId Entity ID
     * @return Whether the entity exists and is removed
     */
    public boolean removeEntity(int entityId)
    {
        CocoaBeanEntity entity = entities.get(entityId);

        if (entity == null)
            return false;

        getEntityListByIdentity(entity.getIdentity()).remove(entity);

        return true;
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

    private List<CocoaBeanEntity> removeEntityListByIdentity(String identity)
    {
        List<CocoaBeanEntity> list = entitiesByIdentity.remove(identity);

        return list == null ? Collections.emptyList() : list;
    }

    private final Map<String, List<CocoaBeanEntity>> entitiesByIdentity = new HashMap<>();

    private final Map<Integer, CocoaBeanEntity> entities = new HashMap<>();

    private final AtomicInteger counter = new AtomicInteger(0);

    private final String name;
}
