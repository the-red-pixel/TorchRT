package com.theredpixelteam.cocoabean;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;

/**
 * Cocoa bean value entity.
 */
public class CocoaBeanValueEntity extends CocoaBeanEntity {
    public CocoaBeanValueEntity(@Nonnull String identity, @Nonnull ValueAccessor valueAccessor)
    {
        super(CocoaBeanEntityType.VALUE, identity);

        this.accessor = Objects.requireNonNull(valueAccessor, "accessor");
    }

    /**
     * @see CocoaBeanEntity#getValueAccessor()
     * @return {@link CocoaBeanEntity.ValueAccessor} instance
     */
    @Override
    public Optional<ValueAccessor> getValueAccessor()
    {
        return Optional.of(accessor);
    }

    private final ValueAccessor accessor;
}
