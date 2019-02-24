package com.theredpixelteam.torch.runtime.server;

import com.theredpixelteam.torch.GameProfileUtil;
import com.theredpixelteam.torch.SpongeExecutionException;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.ban.BanService;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.ban.Ban;
import org.spongepowered.api.util.ban.BanTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public class BanEntryImpl implements BanEntry {
    BanEntryImpl(
            @Nonnull BanService service,
            @Nonnull BanList.Type type,
            @Nonnull String target)
    {
        this.service = Objects.requireNonNull(service, "service");
        this.type = Objects.requireNonNull(type, "type");
        this.target = Objects.requireNonNull(target, "target");
    }

    BanEntryImpl(@Nonnull BanService service,
                 @Nonnull BanList.Type type,
                 @Nonnull String target,
                 @Nullable Date created,
                 @Nullable String source,
                 @Nullable String reason,
                 @Nullable Date expiration)
    {
        this(service, type, target);

        this.created = created;
        this.source = source;
        this.reason = reason;
        this.expiration = expiration;
    }

    BanEntryImpl(@Nonnull BanService service,
                 @Nonnull Ban spongeBanInstance)
    {
        this.service = Objects.requireNonNull(service, "service");

        if (BanTypes.IP.equals(spongeBanInstance.getType()))
        {
            this.type = BanList.Type.IP;
            this.target = ((Ban.Ip) spongeBanInstance).getAddress().toString();
        }
        else if (BanTypes.PROFILE.equals(spongeBanInstance.getType()))
        {
            this.type = BanList.Type.NAME;

            // It seems that CraftBukkit's BanList doesn't support UUID ban.
            // This will be a great conflict or issue under current UUID-based user management.
            this.target = ((Ban.Profile) spongeBanInstance).getProfile().getName().orElseGet(() -> {
                String playerName = null;

                // Trying to access User Storage Service and fetch player name
                Optional<UserStorageService> userStorage = Sponge.getServiceManager().provide(UserStorageService.class);

                if (userStorage.isPresent())
                {
                    Optional<User> user = userStorage.get().get(((Ban.Profile) spongeBanInstance).getProfile().getUniqueId());

                    if (user.isPresent())
                        playerName = user.get().getName();
                }

                if (playerName == null)
                    throw new IllegalArgumentException("Historically reserved issue");

                return playerName;
            });
        }
        else
            throw new IllegalArgumentException("Unknown sponge ban type");

        this.created = new Date(spongeBanInstance.getCreationDate().toEpochMilli());
    }

    /**
     * Get target of this ban entry.
     *
     * @return Target player name
     */
    @Override
    public @Nonnull String getTarget()
    {
        return this.target;
    }

    /**
     * Get creating date of this ban entry.
     *
     * @return Creating date
     */
    @Override
    public @Nullable Date getCreated()
    {
        return this.created;
    }

    /**
     * Set creating date of this ban entry.
     * <b>This operation won't have immediate effect on ban service.</b>
     *
     * @param date Creating date
     */
    @Override
    public void setCreated(@Nullable Date date)
    {
        this.created = date;
    }

    /**
     * Get source of this ban entry.
     *
     * @return Source
     */
    @Override
    public @Nullable String getSource()
    {
        return this.source;
    }

    /**
     * Set source of this ban entry.
     * <b>This operation won't have immediate effect on ban service.</b>
     *
     * @param source Source
     */
    @Override
    public void setSource(@Nullable String source)
    {
        this.source = source;
    }

    /**
     * Get expiration date of this ban entry.
     *
     * @return Expiration date
     */
    @Override
    public @Nullable Date getExpiration()
    {
        return this.expiration;
    }

    /**
     * Set expiration date of this ban entry.
     * <b>This operation won't have immediate effect on ban service.</b>
     *
     * @param date Expiration date
     */
    @Override
    public void setExpiration(@Nullable Date date)
    {
        this.expiration = date;
    }

    /**
     * Get reason of this ban entry.
     *
     * @return Banning reason
     */
    @Override
    public @Nullable String getReason()
    {
        return this.reason;
    }

    /**
     * Set reason of this ban entry.
     * <b>This operation won't have immediate effect on ban service.</b>
     *
     * @param reason Banning reason
     */
    @Override
    public void setReason(@Nullable String reason)
    {
        this.reason = reason;
    }

    /**
     * Save this ban entry to sponge server.
     * <b>Whether this method has an immediate effect depends on the actual implementation
     * of the BanService.</b>
     *
     * @see BanEntry#save()
     * @throws SpongeExecutionException when exceptions are thrown from sponge api
     */
    @Override
    public void save()
        throws SpongeExecutionException
    {
        Ban.Builder builder = Ban.builder();

        // Ban Target is the required argument, must be non-null
        Objects.requireNonNull(target, "Ban target not defined");

        switch (type)
        {
            case IP:
                builder.type(BanTypes.IP);

                try {
                    builder.address(InetAddress.getByName(target));
                } catch (UnknownHostException e) {
                    throw new IllegalArgumentException(e);
                }

                break;

            case NAME:
                builder.type(BanTypes.PROFILE);

                Optional<GameProfile> profile =
                        GameProfileUtil.getProfileByNameInstantly(Sponge.getServer().getGameProfileManager(), target);

                if (!profile.isPresent())
                    return; // specified user not found, ignore this ban

                builder.profile(profile.get());

                break;

            default:
                throw new UnsupportedOperationException("Unknown ban type");
        }

        if (source != null)
            builder.source(Text.of(source));

        if (reason != null)
            builder.reason(Text.of(reason));

        if (created != null)
            builder.startDate(created.toInstant());

        if (expiration != null)
            builder.expirationDate(expiration.toInstant());

        // Commit ban entry to BanService
        service.addBan(builder.build());
    }

    private String reason;

    private Date expiration;

    private Date created;

    private final String target;

    private String source;

    private final BanService service;

    private final BanList.Type type;
}
