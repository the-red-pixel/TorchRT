package com.theredpixelteam.torch.cocoabean.reflection;

import com.theredpixelteam.torch.cocoabean.TorchCocoaBeanService;
import com.theredpixelteam.torch.cocoabean.TorchCocoaBeanService.CocoaBeanEntityContext;

import javax.annotation.Nonnull;

/**
 * Reflection implementation of CocoaBean entity context provider
 */
public class ReflectionCocoaBeanEntityContextProvider implements TorchCocoaBeanService.CocoaBeanEntityContextProvider {
    /**
     * Provide a CocoaBean entity context by the given type using
     * reflection.
     *
     * @param type Type
     * @return CocoaBean entity context
     */
    @Override
    public @Nonnull CocoaBeanEntityContext createContext(@Nonnull Class<?> type)
    {
        // TODO
        return null;
    }
}
