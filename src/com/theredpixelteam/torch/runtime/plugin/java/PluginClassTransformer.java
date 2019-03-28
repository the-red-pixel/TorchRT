package com.theredpixelteam.torch.runtime.plugin.java;

import javax.annotation.Nonnull;

/**
 * Plugin class transformer interface.
 */
public interface PluginClassTransformer {
    /**
     * Accept a class and transform the class if needed.
     * Any exception can be thrown out of this method and any
     * thrown exception will <b>interrupt the whole class transforming
     * process</b>.
     *
     * If the class remains non-transformed, this method should return
     * the original bytecode array.
     *
     * If the exception occurred during the transformation is not so
     * serious to have further influence and there is no need to interrupt
     * the class transforming process, this method shouldn't throw the exception
     * but catch and process it silently (e.g. logging it with normal loggers or
     * {@link com.theredpixelteam.torch.adm.ADM} logger) then return the original
     * bytecode array.
     *
     * <b>The returning bytecode array should NEVER be null, or it will crash
     * the class transformation process.</b>
     *
     * @param byts Class bytecode
     * @throws Exception Occurred exception when transforming class
     * @return Transformed or original class bytecode
     */
    public @Nonnull byte[] accept(@Nonnull byte[] byts) throws Exception;
}
