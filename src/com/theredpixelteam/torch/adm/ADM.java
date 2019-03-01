package com.theredpixelteam.torch.adm;

import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Advanced Debug Mode helper class.
 */
public class ADM {
    /**
     * Return whether the Advanced Debug Mode is enabled.
     *
     * @return Result
     */
    public static boolean enabled()
    {
        return initialized && advancedDebugMode;
    }

    /**
     * Enable Advanced Debug Mode
     */
    public static void enableADM()
    {
        advancedDebugMode = true;
    }

    /**
     * Disable Advanced Debug Mode
     */
    public static void disableADM()
    {
        advancedDebugMode = false;
    }

    /**
     * Get logger
     *
     * @return Logger instance
     */
    public static @Nonnull Logger logger()
    {
        checkInit();

        return logger;
    }

    static void checkInit()
    {
        if (!initialized)
            throw new IllegalStateException("Not initialized.");
    }

    /**
     * Initialize the Advanced Debug Mode helper, and the method can only
     * be called once.
     *
     * @param logger Logger instance
     */
    public static void initialize(@Nonnull Logger logger)
    {
        synchronized (initLock)
        {
            if (!initialized)
            {
                ADM.logger = Objects.requireNonNull(logger, "logger");
                initialized = true;
            }
            else
                throw new IllegalStateException("Duplicated singleton initialization.");
        }
    }

    private static final Object initLock = new Object();

    private static Logger logger;

    private static boolean initialized = false;

    private static volatile boolean advancedDebugMode;
}
