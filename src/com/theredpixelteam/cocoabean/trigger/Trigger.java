package com.theredpixelteam.cocoabean.trigger;

/**
 * The interface that a bean trigger should implement.
 */
public interface Trigger {
    /**
     * Trigger process.
     *
     * @param source Trigger source
     * @return Trigger result
     */
    public TriggerResult trigger(TriggerSource source);

    /**
     * Get the name that displays on external user interfaces.
     *
     * @return Display name
     */
    public String getDisplayName();
}
