package com.theredpixelteam.torch.cocoabean;

import com.theredpixelteam.torch.runtime.plugin.java.PluginClassTransformer;

import javax.annotation.Nonnull;

/**
 * Torch CocoaBean service class transformer.
 */
public class TorchCocoaBeanClassTransformer implements PluginClassTransformer {
    public TorchCocoaBeanClassTransformer(TorchCocoaBeanService service)
    {
        this.service = service;
    }

    /**
     * @see PluginClassTransformer#accept(byte[])
     *
     * @param byts Class bytecode
     * @return Class bytecode
     * @throws Exception Exception
     */
    @Override
    public @Nonnull byte[] accept(@Nonnull byte[] byts) throws Exception
    {
        // TODO

        return byts;
    }

    private final TorchCocoaBeanService service;
}
