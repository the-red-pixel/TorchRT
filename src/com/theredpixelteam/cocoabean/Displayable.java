package com.theredpixelteam.cocoabean;

import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Displayable
 */
public interface Displayable {
    /**
     * Get the texts that displays on external user interfaces, splitted
     * by lines.
     *
     * @return Display text
     */
    public @Nonnull List<Text> getDisplayTexts();
}
