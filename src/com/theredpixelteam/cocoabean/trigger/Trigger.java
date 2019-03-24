package com.theredpixelteam.cocoabean.trigger;

import com.theredpixelteam.cocoabean.Displayable;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

/**
 * The interface that a bean trigger should implement.
 */
public interface Trigger extends Displayable {
    /**
     * Trigger process.
     *
     * @param cause Trigger source
     * @return Trigger result
     */
    public @Nonnull TriggerResult trigger(@Nonnull Cause cause);

    /**
     * Get the texts that displays on external user interfaces.
     *
     * @see Displayable#getDisplayTexts()
     * @return Display text
     */
    @Override
    public default @Nonnull List<Text> getDisplayTexts()
    {
        return Collections.emptyList();
    }

    /**
     * Check whether this trigger is triggerable currently.
     *
     * @return Whether triggerable
     */
    public default boolean available()
    {
        return true;
    }
}
