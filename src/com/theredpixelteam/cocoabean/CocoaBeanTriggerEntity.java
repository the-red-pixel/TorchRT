package com.theredpixelteam.cocoabean;

import com.theredpixelteam.cocoabean.trigger.Trigger;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;

/**
 * Cocoa bean trigger entry.
 */
public class CocoaBeanTriggerEntity extends CocoaBeanEntity {
    public CocoaBeanTriggerEntity(@Nonnull String identity, @Nonnull Trigger trigger)
    {
        super(CocoaBeanEntityType.TRIGGER, identity);
        this.trigger = Objects.requireNonNull(trigger, "trigger");
    }

    /**
     * @see CocoaBeanTriggerEntity#getTrigger()
     * @return Trigger
     */
    @Override
    public Optional<Trigger> getTrigger()
    {
        return Optional.of(trigger);
    }

    private final Trigger trigger;
}
