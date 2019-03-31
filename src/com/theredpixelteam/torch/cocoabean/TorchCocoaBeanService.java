package com.theredpixelteam.torch.cocoabean;

import com.theredpixelteam.cocoabean.CocoaBeanService;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class TorchCocoaBeanService extends CocoaBeanService {
    /**
     * Register the instance as a CocoaBean entity with specified identity
     * and namespace.
     *
     * @param namespaceName Namespace name
     * @param identity Identity
     * @param instance Instance
     * @return {@link TorchCocoaBeanEntity} instance
     */
    public TorchCocoaBeanEntity register(@Nonnull String namespaceName, @Nonnull String identity, @Nonnull Object instance)
    {
        // TODO
    }

    /**
     * Create the context of specified type.
     *
     * @param type Type
     * @return CocoaBeanContext instance
     */
    public static @Nonnull CocoaBeanContext createContext(@Nonnull Class<?> type)
    {
        // TODO
    }

    /**
     * Get the context of specified type from the cache or create
     * one if not cached.
     *
     * @param type Type
     * @return CocoaBeanContext instance
     */
    public static @Nonnull CocoaBeanContext getContext(@Nonnull Class<?> type)
    {
        return contextCache.computeIfAbsent(type, TorchCocoaBeanService::createContext);
    }

    private static final Map<Class<?>, CocoaBeanContext> contextCache = new HashMap<>();

    /**
     * Context of a CocoaBean entity.
     */
    public static class CocoaBeanContext
    {

    }
}
