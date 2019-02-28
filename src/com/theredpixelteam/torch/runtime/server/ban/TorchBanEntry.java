package com.theredpixelteam.torch.runtime.server.ban;

import com.theredpixelteam.torch.ADM;
import com.theredpixelteam.torch.GameProfileUtil;
import com.theredpixelteam.torch.SpongeExecutionException;
import com.theredpixelteam.torch.exception.ShouldNotReachHere;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.ban.BanService;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.ban.Ban;
import org.spongepowered.api.util.ban.BanType;
import org.spongepowered.api.util.ban.BanTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class TorchBanEntry implements BanEntry {
    TorchBanEntry(@Nonnull BanService service,
                  @Nonnull BanList.Type type,
                  @Nonnull String target)
    {
        this.service = Objects.requireNonNull(service, "service");
        this.type = Objects.requireNonNull(type, "type");
        this.target = Objects.requireNonNull(target, "target");
    }

    TorchBanEntry(@Nonnull BanService service,
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

    TorchBanEntry(@Nonnull BanService service,
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
            Ban.Profile profileBan = (Ban.Profile) spongeBanInstance;

            this.type = BanList.Type.NAME;

            // It seems that CraftBukkit's BanList doesn't support UUID ban.
            // This will be a great conflict or issue under current UUID-based user management.
            this.target = profileBan.getProfile().getName().orElseGet(() -> {
                String playerName = null;
                UUID uuid = profileBan.getProfile().getUniqueId();

                // Trying to access User Storage Service and fetch player name
                Optional<UserStorageService> userStorage = Sponge.getServiceManager().provide(UserStorageService.class);

                if (userStorage.isPresent())
                {
                    Optional<User> user = userStorage.get().get(uuid);

                    if (user.isPresent())
                        playerName = user.get().getName();
                }

                if (playerName == null)
                {
                    if (ADM.enabled())
                        ADM.logger().debug("Failed to convert uuid {" + uuid + "} to an actual player");

                    throw new IllegalArgumentException("Historically reserved issue");
                }

                return playerName;
            });
        }
        else
            throw new ShouldNotReachHere();

        this.created = new Date(spongeBanInstance.getCreationDate().toEpochMilli());

        spongeBanInstance.getBanSource().ifPresent((source) -> this.source = source.toPlain());
        spongeBanInstance.getReason().ifPresent((reason) -> this.reason = reason.toPlain());
        spongeBanInstance.getExpirationDate().ifPresent((expiration) -> this.expiration = new Date(expiration.toEpochMilli()));
    }

    static Optional<BanEntry> constructSilenty(@Nonnull BanService service,
                                               @Nonnull Ban spongeBanInstance)
    {
        try {
            return Optional.of(new TorchBanEntry(service, spongeBanInstance));
        } catch (Exception e) {
            if (ADM.enabled())
                ADM.logger().debug("Exception occurred constructing ban entry instance. Ignored.", e);
            return Optional.empty();
        }
    }

    /**
     * Get target of this ban entry.
     *
     * @see BanEntry#getTarget()
     * @return Target string
     */
    @Override
    public @Nonnull String getTarget()
    {
        return this.target;
    }

    /**
     * Get creating date of this ban entry.
     *
     * @see BanEntry#getCreated()
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
     * @see BanEntry#setCreated(Date)
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
     * @see BanEntry#getSource()
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
     * @see BanEntry#setSource(String)
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
     * @see BanEntry#getExpiration()
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
     * @see BanEntry#setExpiration(Date)
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
     * @see BanEntry#getReason()
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
     * @see BanEntry#getReason()
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
                    // illegal ip address, ignore this ban
                    if (ADM.enabled())
                        ADM.logger().debug("Ignoring ban on ip/host [" + target + "]", e);
                }

                break;

            case NAME:
                builder.type(BanTypes.PROFILE);

                Optional<GameProfile> profile =
                        GameProfileUtil.getProfileByNameInstantly(Sponge.getServer().getGameProfileManager(), target);

                if (!profile.isPresent())
                {
                    // specified user not found, ignore this ban
                    if (ADM.enabled())
                        ADM.logger().debug("Ignoring ban on player [" + target + "], specified user not found.");

                    return;
                }

                builder.profile(profile.get());

                break;

            default:
                throw new ShouldNotReachHere();
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

    /**
     * Return the current ban service instance.
     *
     * @return {@link BanService} instance
     */
    public @Nonnull BanService getService()
    {
        return service;
    }

    /**
     * Return the bukkit ban list type.
     *
     * @return {@link BanList.Type} instance
     */
    public @Nonnull BanList.Type getBukkitType()
    {
        return type;
    }

    /**
     * Return the sponge ban type.
     *
     * @return {@link BanType} instance
     */
    public @Nonnull BanType getSpongeType()
    {
        return TorchBanUtil.fromBukkitType(type)
                .orElseThrow(ShouldNotReachHere::new);
    }

    private String reason;

    private Date expiration;

    private Date created;

    private final String target;

    private String source;

    private final BanService service;

    private final BanList.Type type;
}
