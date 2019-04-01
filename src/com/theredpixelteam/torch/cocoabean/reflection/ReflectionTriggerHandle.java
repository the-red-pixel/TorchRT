package com.theredpixelteam.torch.cocoabean.reflection;

import com.theredpixelteam.cocoabean.CocoaBeanElementType;
import com.theredpixelteam.cocoabean.trigger.Trigger;
import com.theredpixelteam.torch.cocoabean.TorchCocoaBeanService.CocoaBeanEntityContext.TriggerHandle;

import javax.annotation.Nonnull;

/**
 * Reflection implementation of trigger-type CocoaBean entity handle.
 */
public class ReflectionTriggerHandle extends TriggerHandle {
    public ReflectionTriggerHandle(@Nonnull CocoaBeanElementType type, @Nonnull String identity)
    {
        super(type, identity);
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

    // TODO
}
