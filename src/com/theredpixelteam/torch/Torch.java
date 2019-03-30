package com.theredpixelteam.torch;

import com.google.inject.Inject;
import com.theredpixelteam.torch.cocoabean.TorchCocoaBeanService;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.plugin.Plugin;

import java.util.Optional;

@Plugin(id = "torch",
        name = "TorchRT",
        version = "1.0 Beta",
        description = "Torch Runtime Support for Spigot plugins",
        authors = {"KuCrO3 Studio", "TheRedPixel"})
public class Torch {
    @Listener
    public void onConstruction(GameConstructionEvent event)
    {
        logger.info("Starting server with Torch Runtime Subsystem");
    }

    /**
     * Get the Torch CocoaBean service instance.
     *
     * @return {@link TorchCocoaBeanService} instance, if enabled
     */
    public static Optional<TorchCocoaBeanService> getCocoaBeanService()
    {
        return Optional.ofNullable(cocoaBeanService);
    }

    /**
     * Get the Torch CocoaBean service instance directly and throw
     * {@link UnsupportedOperationException} when the service
     * is not available.
     *
     * @throws UnsupportedOperationException If not available
     * @return {@link TorchCocoaBeanService} instance
     */
    public static TorchCocoaBeanService requireCocoaBeanService()
        throws UnsupportedOperationException
    {
        return getCocoaBeanService()
                .orElseThrow(() -> new UnsupportedOperationException("CocoaBean service not available"));
    }

    /**
     * Whether the CocoaBean service is enabled.
     *
     * @return Result
     */
    public static boolean isCocoaBeanEnabled()
    {
        return cocoaBeanService != null;
    }

    public static final String VERSION = "1.0b";

    public static final String NAME = "TorchRuntime";

    private static TorchCocoaBeanService cocoaBeanService;

    @Inject
    private Logger logger;
}
