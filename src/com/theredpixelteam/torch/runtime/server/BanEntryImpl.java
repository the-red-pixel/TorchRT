package com.theredpixelteam.torch.runtime.server;

import com.theredpixelteam.torch.SpongeExecutionException;
import org.bukkit.BanEntry;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.ban.BanService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.ban.Ban;
import org.spongepowered.api.util.ban.BanTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Date;
import java.util.Objects;

public class BanEntryImpl implements BanEntry {
    BanEntryImpl(@Nonnull BanService service, @Nonnull String target)
    {
        this.target = target;
        this.service = service;
    }

    /**
     * Get target player of this ban entry.
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
        // Initialize Ban instance builder with type PROFILE
        Ban.Builder builder = Ban.builder().type(BanTypes.PROFILE);

        // Ban Target is the required argument, must be non-null
        Objects.requireNonNull(target, "Ban target not defined");

        GameProfile profile;

        try {
            // Getting GameProfile instance from sponge server, exception may occur
            // when calling CompletableFuture.get()
            profile = Sponge.getServer().getGameProfileManager().get(target).get();
        } catch (Exception e) {
            throw new SpongeExecutionException("Failed to get game profile \"" + target + "\"", e);
        }

        builder.profile(profile);

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
}
