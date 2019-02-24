package com.theredpixelteam.torch;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.profile.GameProfileManager;
import org.spongepowered.api.profile.ProfileNotFoundException;
import org.spongepowered.api.service.user.UserStorageService;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
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
     * @param manager {@link GameProfileManager} instance
     * @param name Player name
     * @throws SpongeExecutionException when other exceptions are thrown from sponge api
     * @return The result of this operation
     */
    public static @Nonnull CompletableFuture<Optional<GameProfile>> getProfileByName(
            @Nonnull GameProfileManager manager,
            @Nonnull String name)
        throws SpongeExecutionException
    {
        return manager.get(name).handle((profile, e) -> {
            if (e != null)
            {
                // mute ProfileNotFoundException
                if (e.getCause() instanceof ProfileNotFoundException)
                    return Optional.empty();

                throw new SpongeExecutionException(e);
            }

            return Optional.of(profile);
        });
    }

    /**
     * Get {@link GameProfile} instance by player name instantly.
     *
     * This method will ensure that the process of acquiring {@link GameProfile} instance
     * will instantly return. If no specified {@link GameProfile} instance available,
     * this method will simply return {@code Optional.empty()}. <b>This method is not
     * creating any fake {@link GameProfile} instance.</b>
     *
     * @param manager {@link GameProfileManager} instance
     * @param name Player name
     * @return The result of this operation
     */
    public static @Nonnull Optional<GameProfile> getProfileByNameInstantly(
            @Nonnull GameProfileManager manager,
            @Nonnull String name)
    {
        GameProfile profile = null;

        // Acquire user storage service for specified GameProfile instance first.
        Optional<UserStorageService> userStorage = Sponge.getServiceManager().provide(UserStorageService.class);

        if (userStorage.isPresent())
        {
            Optional<User> user = userStorage.get().get(name);

            if (user.isPresent())
                profile = user.get().getProfile();
        }

        // Acquire profile from Game Profile Manager without delay.
        // Theoretically, Game Profile Manager could provide the GameProfile instance
        // instantly if the server is under offline mode.
        if (profile == null)
            profile = manager.get(name).getNow(null);

        return Optional.ofNullable(profile);
    }

    /**
     * A UUID filled with zero, (00000000-0000-0000-0000-000000000000) can be used
     * to initialize (fake) game profile.
     */
    public static final UUID EMPTY_UUID = new UUID(0, 0);
}
