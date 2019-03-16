package com.theredpixelteam.torch.cocoabean.trigger;

public interface Trigger {
    public TriggerResult trigger(TriggerSource source);

    public String getDisplayName();
}
