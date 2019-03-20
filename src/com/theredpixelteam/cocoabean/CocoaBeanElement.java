package com.theredpixelteam.cocoabean;

import com.theredpixelteam.cocoabean.trigger.Trigger;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * This object represents an element in a CocoaBean entity.
 * @see CocoaBeanEntity
 */
public abstract class CocoaBeanElement {
    public CocoaBeanElement(@Nonnull CocoaBeanElementType type, @Nonnull String identity)
    {
        this.type = type;
        this.identity = identity;
    }

    /**
     * Get the type of this bean element.
     * @return Entity type
     */
    public @Nonnull CocoaBeanElementType getType()
    {
        return type;
    }

    /**
     * Get the value accessor. If not available, {@code Optional.empty()}
     * will be returned.
     *
     * @return Value accessor
     */
    public @Nonnull Optional<ValueAccessor> getValueAccessor()
    {
        return Optional.empty();
    }

    /**
     * Get the value accessor. The specified exception will be thrown
     * if the value accessor is not available.
     *
     * @param exceptionSupplier Exception supplier
     * @param <E> Exception type
     * @return Value accessor
     * @throws E If value accessor is not available.
     */
    public @Nonnull <E extends Throwable> ValueAccessor requireValueAccessor(Supplier<E> exceptionSupplier) throws E
    {
        return getValueAccessor().orElseThrow(exceptionSupplier);
    }

    /**
     * Get the value accessor. A {@link UnsupportedOperationException} will
     * be thrown if the value accessor is not available.
     *
     * @return Value accessor
     */
    public ValueAccessor requireValueAccessor()
    {
        return requireValueAccessor(
                () -> new UnsupportedOperationException("Not a VALUE type element or value accessor not implemented"));
    }

    /**
     * Get the trigger. If not available, {@code Optional.empty()}
     * will be returned.
     *
     * @return Trigger
     */
    public @Nonnull Optional<Trigger> getTrigger()
    {
        return Optional.empty();
    }

    /**
     * Get the trigger. If not available, The specified exception
     * will be thrown.
     *
     * @param exceptionSupplier Exception supplier
     * @param <E> Exception type
     * @return Trigger
     * @throws E If trigger is not available.
     */
    public @Nonnull <E extends Throwable> Trigger requireTrigger(Supplier<E> exceptionSupplier) throws E
    {
        return getTrigger().orElseThrow(exceptionSupplier);
    }

    /**
     * Get the trigger. A {@link UnsupportedOperationException} will
     * be thrown if the trigger is not available.
     *
     * @return Trigger
     */
    public @Nonnull Trigger requireTrigger()
    {
        return requireTrigger(
                () -> new UnsupportedOperationException("Not a TRIGGER type element or trigger not implemented"));
    }

    /**
     * Get the identity of this element, which is provided by bean register
     * for internal use such as representing element itself in a {@link CocoaBeanEntity}
     * or storing elements in a map.
     *
     * @return Identity string
     */
    public @Nonnull String getIdentity()
    {
        return identity;
    }

    /**
     * Providing a hash code of this element, dominated by the identity of this element.
     *
     * @see Object#hashCode()
     * @return Hash value.
     */
    @Override
    public int hashCode()
    {
        return identity.hashCode();
    }

    /**
     * Compare object according to the identity of this element.
     *
     * @param object
     * @return
     */
    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof CocoaBeanElement))
            return false;

        CocoaBeanElement element = (CocoaBeanElement) object;

        return identity.equals(element.getIdentity());
    }

    private final CocoaBeanElementType type;

    private final String identity;

    /**
     * Value accessor. Value operations should be implemented through this object.
     */
    public interface ValueAccessor
    {
        /**
         * Get value by this value accessor.
         *
         * @return Value object
         */
        public @Nonnull Object getValue();

        /**
         * Set value through this value accessor.
         * If not modifiable, the method should simply return {@code false}.
         * This method should check whether the value object is compatible/legal
         * to this value entity. And the value should be set immediately calling
         * this method if the value is compatible and legal.
         *
         * @param value Value object
         * @return Whether the value object is compatible/legal to this value entity.
         */
        public boolean setValue(@Nonnull Object value);

        /**
         * Whether the {@link ValueAccessor#setValue(Object)} method is supported.
         *
         * @return Result
         */
        public boolean isModifiable();

        /**
         * Get the type of the accessing value.
         *
         * @return Value type
         */
        public Class<?> getType();
    }
}
