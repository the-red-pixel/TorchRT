package com.theredpixelteam.cocoabean;

import com.theredpixelteam.torch.exception.ShouldNotReachHere;

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
     * Get next element ID.
     *
     * @return Next element id.
     */
    public int nextElementID()
    {
        int id = counter.getAndIncrement();

        if (entities.containsKey(id) || id < 0)
            throw new IllegalStateException("counter failure");

        return id;
    }

    /**
     * Add a CocoaBean entity to this namespace.
     *
     * @param entity CocoaBeanEntity instance
     * @return Whether this entity is added to this namespace,
     * otherwise element ID or identifier duplicated.
     */
    public boolean addEntity(@Nonnull CocoaBeanEntity entity)
    {
        Objects.requireNonNull(entity, "entity");

        Optional<Object> identifier = entity.getUniqueIdentifier();
        if (identifier.isPresent())
            if (identifiers.contains(identifier.get()))
                return false; // Unique identifier duplication

        if (entities.putIfAbsent(entity.getEntityID(), entity) != null)
            return false; // Element ID duplication

        getEntityListByIdentity(entity.getIdentity()).add(entity);

        if (identifier.isPresent())
            if (!registerIdentifier(identifier.get()))
                throw new ShouldNotReachHere();

        return true;
    }

    /**
     * Add a CocoaBean entity using the provided entity constructor
     * function.
     *
     * @param entityConstructor CocoaBean entity constructor
     * @return Entity id, -1 if identifier duplicated
     */
    public int addEntity(@Nonnull Function<Integer, CocoaBeanEntity> entityConstructor)
    {
        Objects.requireNonNull(entityConstructor, "constructor");

        int id = nextElementID();
        CocoaBeanEntity entity = entityConstructor.apply(id);

        Optional<Object> identifier = entity.getUniqueIdentifier();
        if (identifier.isPresent())
            if (identifiers.contains(identifier.get()))
                return -1; // Unique identifier duplication

        if (entities.putIfAbsent(id, entity) != null)
            throw new IllegalStateException("counter failure");

        getEntityListByIdentity(entity.getIdentity()).add(entity);

        if (identifier.isPresent())
            if (!registerIdentifier(identifier.get()))
                throw new ShouldNotReachHere();

        return id;
    }

    /**
     * Get entities by the identity.
     *
     * @param identity Identity of entities
     * @return Entity collection
     */
    public @Nonnull Collection<CocoaBeanEntity> getEntities(@Nonnull String identity)
    {
        return Collections.unmodifiableList(
                queryEntityListByIdentity(Objects.requireNonNull(identity, "identity")));
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
        {
            entities.remove(entity.getEntityID());

            entity.getUniqueIdentifier().ifPresent(this::unregisterIdentifier);
        }

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

        entity.getUniqueIdentifier().ifPresent(this::unregisterIdentifier);

        return true;
    }

    /**
     * Register the unique identifier to this namespace.
     *
     * @param identifier Unique identifier
     * @return Whether registered, false if duplicated
     */
    public boolean registerIdentifier(@Nonnull Object identifier)
    {
        return identifiers.add(Objects.requireNonNull(identifier, "identifier"));
    }

    /**
     * Check whether the unique identifier exists in this namespace.
     *
     * @param identifier Unique identifier
     * @return Whether exists
     */
    public boolean checkIdentifier(@Nonnull Object identifier)
    {
        return identifiers.add(Objects.requireNonNull(identifier));
    }

    /**
     * Unregister the unique identifier in this namespace.
     *
     * @param identifier Unique identifier
     * @return Whether exists and unregistered
     */
    public boolean unregisterIdentifier(@Nonnull Object identifier)
    {
        return identifiers.remove(Objects.requireNonNull(identifier));
    }

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

    private final Set<Object> identifiers = new HashSet<>();

    private final Map<String, List<CocoaBeanEntity>> entitiesByIdentity = new HashMap<>();

    private final Map<Integer, CocoaBeanEntity> entities = new HashMap<>();

    private final AtomicInteger counter = new AtomicInteger(0);

    private final String name;
}
