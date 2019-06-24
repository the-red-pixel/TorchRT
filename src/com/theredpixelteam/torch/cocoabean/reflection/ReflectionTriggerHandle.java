package com.theredpixelteam.torch.cocoabean.reflection;

import com.theredpixelteam.cocoabean.trigger.Trigger;
import com.theredpixelteam.cocoabean.trigger.TriggerResult;
import com.theredpixelteam.torch.cocoabean.TorchCocoaBeanService.CocoaBeanEntityContext.TriggerHandle;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Reflection implementation of trigger-type CocoaBean entity handle.
 */
public class ReflectionTriggerHandle extends TriggerHandle {
    public ReflectionTriggerHandle(@Nonnull String identity)
    {
        super(identity);
    }

    /**
     * @see TriggerHandle#createTrigger(Object)
     * @param instance Instance
     * @return {@link Trigger} instance
     */
    @Override
    public @Nonnull Trigger createTrigger(Object instance)
    {
        return null;
    }

    public class ReflectionTrigger implements Trigger
    {
        // TODO

        @Override
        public @Nonnull TriggerResult trigger(@Nonnull Cause cause)
        {
            return null;
        }

        @Override
        public @Nonnull List<Text> getDisplayTexts()
        {
            return null;
        }

        @Override
        public boolean available()
        {
            return false;
        }
    }
}
