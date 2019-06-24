package com.theredpixelteam.torch.cocoabean.reflection;

import com.theredpixelteam.cocoabean.CocoaBeanMalformationException;
import com.theredpixelteam.torch.adm.ADM;
import com.theredpixelteam.torch.adm.ADMLogging;
import com.theredpixelteam.torch.cocoabean.TorchCocoaBeanService;
import com.theredpixelteam.torch.cocoabean.TorchCocoaBeanService.CocoaBeanEntityContext;
import com.theredpixelteam.torch.cocoabean.annotation.*;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Reflection implementation of CocoaBean entity context provider
 */
public class ReflectionCocoaBeanEntityContextProvider implements TorchCocoaBeanService.CocoaBeanEntityContextProvider {
    /**
     * Provide a CocoaBean entity context by the given type using
     * reflection.
     *
     * @see TorchCocoaBeanService.CocoaBeanEntityContextProvider#createContext(Class)
     * @param type Type
     * @return CocoaBean entity context
     */
    @ADMLogging
    @Override
    public @Nonnull CocoaBeanEntityContext createContext(@Nonnull Class<?> type)
            throws CocoaBeanMalformationException
    {
        Entity entityInfo = type.getAnnotation(Entity.class);

        if (entityInfo == null)
            throw new CocoaBeanMalformationException("The class " + type.getCanonicalName()
                    + " is not annotated with @Entity.");

        String namespaceName = entityInfo.namespace();
        String identity = entityInfo.identity();

        CocoaBeanEntityContext context;

        if ((context = preloaded.get(identity)) != null)
            return context;

        context = new CocoaBeanEntityContext();

        Map<String, ElementInfo> infoMap = new HashMap<>();

        for (Field field : type.getFields())
        {

        }

        if (ADM.enabled())
            ADM.logger().info("CocoaBean entity context of " + type.getCanonicalName() + " created.");

        return null;
    }

    /**
     * @see TorchCocoaBeanService.CocoaBeanEntityContextProvider#supportPreload()
     * @return True
     */
    @Override
    public boolean supportPreload()
    {
        return true;
    }

    /**
     * @see TorchCocoaBeanService.CocoaBeanEntityContextProvider#preloadContext(ClassNode)
     * @param classNode ClassNode instance
     */
    @ADMLogging
    @Override
    public @Nonnull void preloadContext(@Nonnull ClassNode classNode)
    {
        // TODO
    }

    private final Map<String, CocoaBeanEntityContext> preloaded = new HashMap<>();

    private static abstract class ElementInfo
    {
        private Element elementInfo;

        public abstract boolean isValue();
    }

    private static class ValueElementInfo extends ElementInfo
    {
        private Getter getterInfo;

        private Setter setterInfo;

        private Readability readabilityInfo;

        private Writability writabilityInfo;

        @Override
        public boolean isValue()
        {
            return true;
        }
    }

    public static class TriggerElementInfo extends ElementInfo
    {
        private Availability availabilityInfo;

        @Override
        public boolean isValue()
        {
            return false;
        }
    }
}
