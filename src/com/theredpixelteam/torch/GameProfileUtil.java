package com.theredpixelteam.torch;

import com.theredpixelteam.redtea.util.Optional;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.profile.GameProfileManager;
import org.spongepowered.api.profile.ProfileNotFoundException;

import javax.annotation.Nonnull;
import java.util.concurrent.ExecutionException;

/**
 * Providing some common game profile operations.
 */
public class GameProfileUtil {
    private GameProfileUtil()
    {
    }

    /**
     * Get {@link GameProfile} instance by player name.
     *
     * When calling {@link GameProfileManager#get(String)},
     * if the player doesn't exist, it will throw an {@link ExecutionException} caused by
     * {@link ProfileNotFoundException}. In this method, <b>instead of throwing it</b>, will
     * mute {@link ProfileNotFoundException} when it occurs and simply return {@code Optional.empty()}.
     * Otherwise, it will return the non-null result wrapped in {@link Optional<GameProfile>}.
     *
     * @see GameProfileManager#get(String)
     * @param manager {@link GameProfile} instance
     * @param name Player name
     * @throws SpongeExecutionException when other exceptions are thrown from sponge api
     * @return The result of this operation
     */
    public static Optional<GameProfile> getProfileByName(@Nonnull GameProfileManager manager, @Nonnull String name)
        throws SpongeExecutionException
    {
        try {
            return Optional.of(manager.get(name).get());
        } catch (ExecutionException e) {
            if (e.getCause() instanceof ProfileNotFoundException)
                return Optional.empty();

            throw new SpongeExecutionException(e);
        } catch (Exception e) {
            throw new SpongeExecutionException(e);
        }
    }
}
