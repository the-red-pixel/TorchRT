package com.theredpixelteam.torch.cocoabean;

import com.theredpixelteam.cocoabean.CocoaBeanElement;
import com.theredpixelteam.cocoabean.CocoaBeanElementType;
import com.theredpixelteam.cocoabean.CocoaBeanService;
import com.theredpixelteam.cocoabean.trigger.Trigger;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TorchCocoaBeanService extends CocoaBeanService {
    public TorchCocoaBeanService(@Nonnull CocoaBeanEntityContextProvider contextProvider)
    {
        this.contextProvider = Objects.requireNonNull(contextProvider, "contextProvider");
    }

    /**
     * Register the instance as a CocoaBean entity with specified identity
     * and namespace.
     *
     * @param namespaceName Namespace name
     * @param identity Identity
     * @param instance Instance
     * @return {@link TorchCocoaBeanEntity} instance
     */
    public TorchCocoaBeanEntity register(@Nonnull String namespaceName,
                                         @Nonnull String identity,
                                         @Nonnull Object instance)
    {
        // TODO
    }

    /**
     * Create the context of specified type.
     *
     * @param type Type
     * @return CocoaBeanContext instance
     */
    public @Nonnull CocoaBeanEntityContext createContext(@Nonnull Class<?> type)
    {
        return contextProvider.createContext(type);
    }

    /**
     * Get the context of specified type from the cache or create
     * one if not cached.
     *
     * @param type Type
     * @return CocoaBeanContext instance
     */
    public @Nonnull CocoaBeanEntityContext getContext(@Nonnull Class<?> type)
    {
        return contextCache.computeIfAbsent(type, this::createContext);
    }

    private final CocoaBeanEntityContextProvider contextProvider;

    private final Map<Class<?>, CocoaBeanEntityContext> contextCache = new HashMap<>();

    /**
     * CocoaBean entity context provider.
     */
    public static interface CocoaBeanEntityContextProvider
    {
        /**
         * Provide a context of specified type.
         *
         * @param type Type
         * @return {@link CocoaBeanEntityContext} instance
         */
        public @Nonnull CocoaBeanEntityContext createContext(@Nonnull Class<?> type);
    }

    /**
     * Context of a CocoaBean entity.
     */
    public static class CocoaBeanEntityContext
    {
        // TODO

        private final Map<String, ElementHandle> handleMap = new HashMap<>();

        /**
         * Handle of a CocoaBean element, representing the structural
         * information of a specified element.
         */
        public static abstract class ElementHandle
        {
            protected ElementHandle(@Nonnull CocoaBeanElementType type, @Nonnull String identity)
            {
                this.type = Objects.requireNonNull(type, "type");
                this.identity = Objects.requireNonNull(identity, "identity");
            }

            public @Nonnull CocoaBeanElementType getType()
            {
                return type;
            }

            public @Nonnull String getIdentity()
            {
                return identity;
            }

            private final String identity;

            private final CocoaBeanElementType type;
        }

        /**
         * Handle of a value-type CocoaBean element.
         */
        public abstract static class ValueHandle extends ElementHandle
        {
            protected ValueHandle(@Nonnull CocoaBeanElementType type, @Nonnull String identity)
            {
                super(type, identity);
            }

            /**
             * Create a value accessor attached to the given instance.
             *
             * @param instance Instance
             * @return {@link CocoaBeanElement.ValueAccessor} instance
             */
            public abstract @Nonnull CocoaBeanElement.ValueAccessor createAccessor(Object instance);
        }

        /**
         * Handle of a trigger-type CocoaBean element.
         */
        public abstract static class TriggerHandle extends ElementHandle
        {
            protected TriggerHandle(@Nonnull CocoaBeanElementType type, @Nonnull String identity)
            {
                super(type, identity);
            }

            /**
             * Create a trigger attached to the given instance.
             *
             * @param instance Instance
             * @return {@link Trigger} instance
             */
            public abstract @Nonnull Trigger createTrigger(Object instance);
        }
    }
}
