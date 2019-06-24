package com.theredpixelteam.torch.cocoabean.reflection;

import com.theredpixelteam.cocoabean.CocoaBeanElement;
import com.theredpixelteam.cocoabean.trigger.CocoaBeanOperationException;
import com.theredpixelteam.torch.cocoabean.TorchCocoaBeanService.CocoaBeanEntityContext.ValueHandle;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Reflection implementation of value-type CocoaBean entity handle.
 */
public class ReflectionValueHandle extends ValueHandle {
    public ReflectionValueHandle(@Nonnull String identity,
                                 @Nonnull Class<?> valueType,
                                 @Nullable Field field,
                                 @Nullable Method getter,
                                 @Nullable Method setter)
    {
        // TODO lazy loading

        super(identity);

        this.valueType = Objects.requireNonNull(valueType, "valueType");
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
        return new ReflectionValueAccessor(instance);
    }

    /**
     * Reflection implementation of value accessor.
     */
    public class ReflectionValueAccessor implements CocoaBeanElement.ValueAccessor
    {
        public ReflectionValueAccessor(@Nonnull Object instance)
        {
            this.instance = Objects.requireNonNull(instance, "instance");
        }

        @Override
        public @Nullable Object getValue()
                throws CocoaBeanOperationException
        {
            if (!readable)
                return null;

            try {
                if (getter != null)
                    return getter.invoke(instance);

                return field.get(instance);
            } catch (ReflectiveOperationException e) {
                throw new CocoaBeanOperationException("reflection failure", e);
            }
        }

        @Override
        public boolean setValue(@Nonnull Object value)
                throws CocoaBeanOperationException
        {
            if (!writeable)
                return false;

            if (!valueType.isInstance(value))
                return false;

            try {
                if (setter != null)
                    setter.invoke(instance, value);
                else
                    field.set(instance, value);

                return true;
            } catch (ReflectiveOperationException e) {
                throw new CocoaBeanOperationException("reflection failure", e);
            }
        }

        @Override
        public boolean isModifiable()
        {
            return writeable;
        }

        @Override
        public boolean isReadable()
        {
            return readable;
        }

        @Override
        public @Nonnull Class<?> getType()
        {
            return valueType;
        }

        private final Object instance;
    }

    private final boolean readable;

    private final boolean writeable;

    private final Class<?> valueType;

    private final Field field;

    private final Method getter;

    private final Method setter;
}
