package com.theredpixelteam.torch.cocoabean;

import com.theredpixelteam.cocoabean.*;
import com.theredpixelteam.cocoabean.trigger.CocoaBeanOperationException;
import com.theredpixelteam.cocoabean.trigger.Trigger;
import com.theredpixelteam.torch.adm.ADM;
import com.theredpixelteam.torch.adm.ADMLogging;
import com.theredpixelteam.torch.exception.ShouldNotReachHere;

import javax.annotation.Nonnull;
import java.util.*;

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
     * @return {@link CocoaBeanEntity} instance
     */
    @ADMLogging
    public CocoaBeanEntity register(@Nonnull String namespaceName,
                                         @Nonnull String identity,
                                         @Nonnull Object instance)
        throws CocoaBeanOperationException
    {
        CocoaBeanEntityContext context = getContext(instance.getClass());
        CocoaBeanNamespace namespace = getOrCreateNamespace(namespaceName);

        int eid = namespace.nextElementID();
        CocoaBeanEntity entity = new CocoaBeanEntity(eid, identity, instance);

        for (CocoaBeanEntityContext.ElementHandle handle : context.getHandles())
        {
            CocoaBeanElement element;

            switch (handle.getType())
            {
                case VALUE:
                    element = new CocoaBeanValueElement(handle.getIdentity(),
                            ((CocoaBeanEntityContext.ValueHandle) handle).createAccessor(instance));
                    break;

                case TRIGGER:
                    element = new CocoaBeanTriggerElement(handle.getIdentity(),
                            ((CocoaBeanEntityContext.TriggerHandle) handle).createTrigger(instance));
                    break;

                default:
                    throw new ShouldNotReachHere();
            }

            if (!entity.registerElement(element))
                throw new CocoaBeanOperationException("Duplicated element in entity context of <" + identity + ">");
        }

        if (!namespace.addEntity(entity))
            throw new CocoaBeanOperationException("Duplicated element ID");

        if (ADM.enabled())
            ADM.logger().info("CocoaBean entity <" + identity + "> with EID " + eid
                    + " registered to the namespace \"" + namespaceName + "\"");

        return entity;
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
        /**
         * Get all element handles in this context.
         *
         * @return Immutable collection of all element handles.
         */
        public @Nonnull Collection<ElementHandle> getHandles()
        {
            return Collections.unmodifiableCollection(handleMap.values());
        }

        /**
         * Get the element handle in this context by the identity.
         *
         * @param identity Identity of the element
         * @return Element handle, if any
         */
        public @Nonnull Optional<ElementHandle> getHandle(@Nonnull String identity)
        {
            return Optional.ofNullable(handleMap.get(Objects.requireNonNull(identity, "identity")));
        }

        /**
         * Seal this context, which means the completion of the construction
         * stage of this context. <b>Nothing will be done if already sealed.</b>
         */
        public void seal()
        {
//            if (!this.sealed)
            this.sealed = true;
        }

        /**
         * Whether this context is already sealed.
         *
         * @see #seal()
         * @return Result
         */
        public boolean isSealed()
        {
            return this.isSealed();
        }

        /**
         * Add handle to this context. This method will always
         * return false and do nothing if this context is already
         * sealed.
         *
         * @see #seal()
         * @param handle ElementHandle instance
         * @return True if added. Otherwise duplicated or context sealed.
         */
        public boolean addHandle(@Nonnull ElementHandle handle)
        {
            Objects.requireNonNull(handle, "handle");

            if (sealed)
                return false;

            return handleMap.putIfAbsent(handle.getIdentity(), handle) == null;
        }

        /**
         * Remove the handle by the identity. This method will always
         * return false and do nothing if this context is already
         * sealed.
         *
         * @see #seal()
         * @param identity Identity of the element
         * @return True if removed. Otherwise not exists or context sealed.
         */
        public boolean removeHandle(String identity)
        {
            Objects.requireNonNull(identity, "identity");

            if (sealed)
                return false;

            return handleMap.remove(identity) != null;
        }

        /**
         * Get all value-type element handles.
         *
         * @return Immutable collection of all value-type handles.
         */
        public @Nonnull Collection<ValueHandle> getValueHandles()
        {
            if (cachedValueHandles == null)
            {
                Set<ValueHandle> handles = new HashSet<>();

                for (ElementHandle handle : handleMap.values())
                    if (CocoaBeanElementType.VALUE.equals(handle.getType()))
                        handles.add((ValueHandle) handle);

                handles = Collections.unmodifiableSet(handles);

                if (sealed)
                    this.cachedValueHandles = handles;

                return handles;
            }
            else
                return cachedValueHandles;
        }

        /**
         * Get all trigger-type element handles.
         *
         * @return Immutable collection of all trigger-type handles.
         */
        public @Nonnull Collection<TriggerHandle> getTriggerHandles()
        {
            if (cachedTriggerHandles == null)
            {
                Set<TriggerHandle> handles = new HashSet<>();

                for (ElementHandle handle : handleMap.values())
                    if (CocoaBeanElementType.TRIGGER.equals(handle.getType()))
                        handles.add((TriggerHandle) handle);

                handles = Collections.unmodifiableSet(handles);

                if (sealed)
                    this.cachedTriggerHandles = handles;

                return handles;
            }
            else
                return cachedTriggerHandles;
        }

        private boolean sealed = false;

        private Collection<ValueHandle> cachedValueHandles;

        private Collection<TriggerHandle> cachedTriggerHandles;

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
            protected ValueHandle(@Nonnull String identity)
            {
                super(CocoaBeanElementType.VALUE, identity);
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
            protected TriggerHandle(@Nonnull String identity)
            {
                super(CocoaBeanElementType.TRIGGER, identity);
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
