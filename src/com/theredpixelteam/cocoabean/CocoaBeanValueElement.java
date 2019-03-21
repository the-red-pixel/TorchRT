package com.theredpixelteam.cocoabean;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;

/**
 * Cocoa bean value entity.
 */
public class CocoaBeanValueElement extends CocoaBeanElement {
    public CocoaBeanValueElement(@Nonnull String identity, @Nonnull ValueAccessor valueAccessor)
    {
        super(CocoaBeanElementType.VALUE, identity);

        this.accessor = Objects.requireNonNull(valueAccessor, "accessor");
    }

    /**
     * @see CocoaBeanElement#getValueAccessor()
     * @return {@link CocoaBeanElement.ValueAccessor} instance
     */
    @Override
    public Optional<ValueAccessor> getValueAccessor()
    {
        return Optional.of(accessor);
    }

    private final ValueAccessor accessor;
}
