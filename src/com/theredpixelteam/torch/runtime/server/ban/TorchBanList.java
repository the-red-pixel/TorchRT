package com.theredpixelteam.torch.runtime.server.ban;

import com.theredpixelteam.torch.adm.ADM;
import com.theredpixelteam.torch.GameProfileUtil;
import com.theredpixelteam.torch.adm.ADMLogging;
import com.theredpixelteam.torch.exception.ShouldNotReachHere;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.ban.BanService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.ban.Ban;
import org.spongepowered.api.util.ban.BanType;
import org.spongepowered.api.util.ban.BanTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class TorchBanList implements BanList {
    public TorchBanList(@Nonnull BanService service,
                        @Nonnull BanList.Type type)
    {
        this.service = Objects.requireNonNull(service, "service");
        this.type = Objects.requireNonNull(type, "type");
    }

    @Override
    public BanEntry getBanEntry(String target)
    {
        return null;
    }

    /**
     * Adds a ban to this ban list. If a ban already exists, this will update the previous ban.
     * If an unavailable or illegal target is provided, this method will return a constructed
     * {@link BanEntry} instance and simply ignore this ban. And you can still call the
     * {@link BanEntry#save()} to make an attempt to establish this ban.
     *
     * <b>Whether this method has an immediate effect depends on the actual implementation
     * of the BanService.</b>
     *
     * @see BanList#addBan(String, String, Date, String)
     * @param target Target
     * @param reason Reason, default if null
     * @param expiration Expiration, forever if null
     * @param source Source, default if null
     * @return Created {@link BanEntry} instance
     */
    @Override
    @ADMLogging
    public @Nonnull BanEntry addBan(@Nonnull String target,
                                    @Nullable String reason,
                                    @Nullable Date expiration,
                                    @Nullable String source)
    {
        Objects.requireNonNull(target, "target");

        Ban.Builder spongeBanBuilder = Ban.builder();
        BanType spongeType = getSpongeType();

        BanEntry banEntry;
        boolean flag = false;

        spongeBanBuilder.type(spongeType);

        if (BanTypes.IP.equals(spongeType))
        {
            try {
                spongeBanBuilder.address(InetAddress.getByName(target));

                flag = true;
            } catch (UnknownHostException e) {
                // illegal ip address, ignore this ban
                if (ADM.enabled())
                    ADM.logger().debug("Ignoring ban on ip/host [" + target + "]", e);
            }
        }
        else if (BanTypes.PROFILE.equals(spongeType))
        {
            Optional<GameProfile> profile =
                    GameProfileUtil.getProfileByNameInstantly(Sponge.getServer().getGameProfileManager(), target);

            if (!(flag = profile.isPresent())) // specified user not found, ignore this ban
                if (ADM.enabled())
                    ADM.logger().debug("Ignoring ban on player [" + target + "]");
                else;
            else
                spongeBanBuilder.profile(profile.get());
        }
        else
            throw new ShouldNotReachHere();

        if (flag)
        {
            if (reason != null)
                spongeBanBuilder.reason(Text.of(reason));

            if (expiration != null)
                spongeBanBuilder.expirationDate(expiration.toInstant());

            if (source != null)
                spongeBanBuilder.source(Text.of(source));

            Ban spongeBanInstance = spongeBanBuilder.build();
            banEntry = TorchBanEntry.constructSilenty(service, spongeBanInstance)
                    .orElseThrow(ShouldNotReachHere::new);

            service.addBan(spongeBanInstance);
        }
        else
            banEntry = new TorchBanEntry(service, type, target, new Date(), source, reason, expiration);

        return banEntry;
    }

    /**
     * Get all bans in this ban list.
     *
     * @see BanList#getBanEntries()
     * @return An immutable collection of ban entries
     */
    @Override
    public @Nonnull Set<BanEntry> getBanEntries()
    {
        Set<BanEntry> entries = new HashSet<>();
        Collection<? extends Ban> spongeBanInstances;

        switch (type)
        {
            case NAME:
                spongeBanInstances = service.getProfileBans();
                break;

            case IP:
                spongeBanInstances = service.getIpBans();
                break;

            default:
                throw new ShouldNotReachHere();
        }

        for (Ban spongeBanInstance : spongeBanInstances)
            TorchBanEntry.constructSilenty(service, spongeBanInstance).ifPresent(entries::add);

        return Collections.unmodifiableSet(entries);
    }

    /**
     * Query whether the target is banned.
     *
     * @see BanList#isBanned(String)
     * @param target Target
     * @return Query result
     */
    @Override
    @ADMLogging
    public boolean isBanned(@Nonnull String target)
    {
        Objects.requireNonNull(target);

        switch (type)
        {
            case NAME:
                Optional<GameProfile> profile =
                        GameProfileUtil.getProfileByNameInstantly(Sponge.getServer().getGameProfileManager(), target);

                if (!profile.isPresent())
                    return false;

                return service.isBanned(profile.get());

            case IP:
                try {
                    return service.isBanned(InetAddress.getByName(target));
                } catch (UnknownHostException e) {
                    // illegal ip address, ignore this query
                    if (ADM.enabled())
                        ADM.logger().debug("Unknown or illegal ip/host: " + target, e);

                    return false;
                }

            default:
                throw new ShouldNotReachHere();
        }
    }

    /**
     * Pardon the specified target.
     *
     * <b>Whether this method has an immediate effect depends on the actual implementation
     * of the BanService.</b>
     *
     * @see BanList#pardon(String)
     * @param target Target
     */
    @Override
    @ADMLogging
    public void pardon(@Nonnull String target)
    {
        Objects.requireNonNull(target, "target");

        switch (type)
        {
            case NAME:
                GameProfileUtil.getProfileByNameInstantly(Sponge.getServer().getGameProfileManager(), target)
                    .ifPresent(service::pardon);

                break;

            case IP:
                try {
                    service.pardon(InetAddress.getByName(target));
                } catch (Exception e) {
                    // illegal ip address, ignore this ban
                    if (ADM.enabled())
                        ADM.logger().debug("Unknown or illegal ip/host: " + target, e);
                }

                break;

            default:
                throw new ShouldNotReachHere();
        }
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

    private final BanList.Type type;

    private final BanService service;
}
