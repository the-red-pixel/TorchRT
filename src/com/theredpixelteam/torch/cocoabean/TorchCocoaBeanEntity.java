package com.theredpixelteam.torch.cocoabean;

import com.theredpixelteam.cocoabean.CocoaBeanEntity;

import javax.annotation.Nonnull;
import java.util.Objects;

public class TorchCocoaBeanEntity extends CocoaBeanEntity {
    public TorchCocoaBeanEntity(int id, @Nonnull String identity, @Nonnull Object identifier)
    {
        super(id, identity);
        this.identifier = Objects.requireNonNull(identifier, "identifier");
    }

    /**
     * Get unique identifier. This identifier is only for preventing
     * multiple registration of a single entity instance. This identifier
     * should only be used to verify uniqueness.
     *
     * @return Unique identifier
     */
    public @Nonnull Object getUniqueIdentifier()
    {
        return identifier;
    }

    private final Object identifier;
}
