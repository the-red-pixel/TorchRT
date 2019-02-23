package com.theredpixelteam.torch;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.plugin.Plugin;

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

    @Inject
    private Logger logger;
}
