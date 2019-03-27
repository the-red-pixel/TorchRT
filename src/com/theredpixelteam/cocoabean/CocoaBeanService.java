package com.theredpixelteam.cocoabean;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * This object represents a CocoaBean service, providing bean
 * namespace management.
 */
public class CocoaBeanService {
    public CocoaBeanService()
    {
    }

    /**
     * Get namespace.
     *
     * @param name Name of the namespace
     * @return Namespace instance if any
     */
    public @Nonnull Optional<CocoaBeanNamespace> getNamespace(@Nonnull String name)
    {
        return Optional.ofNullable(namespaces.get(Objects.requireNonNull(name, "name")));
    }

    /**
     * Get namespace and create one if absent.
     *
     * @param name Name of the namespace
     * @return Existing or created namespace
     */
    public @Nonnull CocoaBeanNamespace getOrCreateNamespace(@Nonnull String name)
    {
        return namespaces.computeIfAbsent(Objects.requireNonNull(name, "name"), CocoaBeanNamespace::new);
    }

    /**
     * Remove namespace.
     *
     * @param name Name of the namespace
     * @return Whether the specified namespace exists and is removed
     */
    public boolean removeNamespace(@Nonnull String name)
    {
        return namespaces.remove(Objects.requireNonNull(name, "name")) != null;
    }

    /**
     * Get all namespaces.
     *
     * @return Immutable collection of all namespaces
     */
    public Collection<CocoaBeanNamespace> getAllNamespaces()
    {
        return Collections.unmodifiableCollection(namespaces.values());
    }

    protected Map<String, CocoaBeanNamespace> namespaces = new HashMap<>();
}
