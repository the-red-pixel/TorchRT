package com.theredpixelteam.torch.cocoabean;

import com.theredpixelteam.cocoabean.CocoaBeanNamespace;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class TorchCocoaBeanNamespace extends CocoaBeanNamespace {
    public TorchCocoaBeanNamespace(@Nonnull String name)
    {
        super(name);
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

    private final Set<Object> identifiers = new HashSet<>();
}
