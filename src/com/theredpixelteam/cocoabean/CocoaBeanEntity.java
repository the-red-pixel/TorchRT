package com.theredpixelteam.cocoabean;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * This object represents a CocoaBean entity.
 */
public class CocoaBeanEntity {
    public CocoaBeanEntity(int id,
                           @Nonnull String identity,
                           @Nullable Object identifier)
    {
        this.id = id;
        this.identity = Objects.requireNonNull(identity, "identity");
        this.identifier = identifier;
    }

    /**
     * Get the source identity of the entity.
     *
     * @return Source identity
     */
    public @Nonnull String getIdentity()
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

    /**
     * Get all the elements in this cocoabean entity.
     *
     * @return An immutable that contains all elements
     */
    public @Nonnull Map<String, CocoaBeanElement> getElements()
    {
        return Collections.unmodifiableMap(elements);
    }

    /**
     * Get specified element by the given identity.
     *
     * @param elementIdentity Identity of the element
     * @return CocoaBean element if any
     */
    public @Nonnull Optional<CocoaBeanElement> getElement(String elementIdentity)
    {
        return Optional.ofNullable(elements.get(elementIdentity));
    }

    /**
     * Query whether this entity contains the specified element.
     *
     * @param elementIdentity Identity of the element
     * @return Result
     */
    public boolean hasElement(String elementIdentity)
    {
        return elements.containsKey(elementIdentity);
    }

    /**
     * Register the element to this entity. This method will do nothing
     * if a duplicated identity element found in this entity.
     *
     * @param element Element to register
     * @return Whether registered successfully, otherwise duplicated
     */
    public boolean registerElement(CocoaBeanElement element)
    {
        return elements.putIfAbsent(element.getIdentity(), element) == null;
    }

    /**
     * Get unique identifier. This identifier is only for preventing
     * multiple registration of a single entity instance. This identifier
     * should only be used to verify uniqueness.
     *
     * @return Unique identifier, if any
     */
    public @Nonnull Optional<Object> getUniqueIdentifier()
    {
        return Optional.ofNullable(identifier);
    }

    private final Object identifier;

    private final int id;

    private final String identity;

    private final Map<String, CocoaBeanElement> elements = new HashMap<>();
}
