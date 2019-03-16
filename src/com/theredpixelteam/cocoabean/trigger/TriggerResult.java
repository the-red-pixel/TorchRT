package com.theredpixelteam.cocoabean.trigger;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;

/**
 * Result of the trigger process.
 */
public class TriggerResult {
    TriggerResult(Type type, Throwable throwable)
    {
        this.type = type;
        this.throwable = throwable;
    }

    /**
     * Succeeded.
     *
     * @return {@link TriggerResult} instance
     */
    public static @Nonnull TriggerResult succeeded()
    {
        return SUCCEEDED;
    }

    /**
     * Ignored.
     *
     * @return {@link TriggerResult} instance
     */
    public static @Nonnull TriggerResult ignored()
    {
        return IGNORED;
    }

    /**
     * Failed with throwable.
     *
     * @param throwable Throwable
     * @return {@link TriggerResult} instance
     */
    public static @Nonnull TriggerResult thrown(@Nonnull Throwable throwable)
    {
        return new TriggerResult(Type.THROWN, Objects.requireNonNull(throwable));
    }

    /**
     * Whether succeeded.
     *
     * @return Result
     */
    public boolean isSucceeded()
    {
        return Type.SUCCEEDED.equals(Type.SUCCEEDED);
    }

    /**
     * Get the type of the result.
     *
     * @return Result type
     */
    public @Nonnull Type getType()
    {
        return type;
    }

    /**
     * Whether any exception thrown.
     *
     * @return Result
     */
    public boolean hasThrowable()
    {
        return throwable != null;
    }

    /**
     * Get the exception thrown.
     *
     * @return Thrown exception
     */
    public @Nonnull Optional<Throwable> getThrowable()
    {
        return Optional.ofNullable(throwable);
    }

    private static final TriggerResult SUCCEEDED = new TriggerResult(Type.SUCCEEDED, null);

    private static final TriggerResult IGNORED = new TriggerResult(Type.IGNORED, null);

    private final Throwable throwable;

    private final Type type;

    public static enum Type
    {
        SUCCEEDED,
        THROWN,
        IGNORED
    }
}
