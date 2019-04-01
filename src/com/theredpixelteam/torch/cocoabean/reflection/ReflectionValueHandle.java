package com.theredpixelteam.torch.cocoabean.reflection;

import com.theredpixelteam.cocoabean.CocoaBeanElement;
import com.theredpixelteam.cocoabean.CocoaBeanElementType;
import com.theredpixelteam.torch.cocoabean.TorchCocoaBeanService.CocoaBeanEntityContext.ValueHandle;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Reflection implementation of value-type CocoaBean entity handle.
 */
public class ReflectionValueHandle extends ValueHandle {
    public ReflectionValueHandle(@Nonnull CocoaBeanElementType type,
                                 @Nonnull String identity,
                                 @Nullable Field field,
                                 @Nullable Method getter,
                                 @Nullable Method setter)
    {
        super(type, identity);

        this.field = field;
        this.getter = getter;
        this.setter = setter;

        this.readable = field != null || getter != null;
        this.writeable = field != null || setter != null;
    }

    /**
     * @see ValueHandle#createAccessor(Object)
     * @param instance Instance
     * @return {@link CocoaBeanElement.ValueAccessor} instance
     */
    @Override
    public @Nonnull CocoaBeanElement.ValueAccessor createAccessor(Object instance)
    {
        return null;
    }

    // TODO

    private final boolean readable;

    private final boolean writeable;

    private final Field field;

    private final Method getter;

    private final Method setter;
}
