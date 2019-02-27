package com.theredpixelteam.torch.runtime.server;

import com.theredpixelteam.torch.ADM;
import com.theredpixelteam.torch.GameProfileUtil;
import com.theredpixelteam.torch.exception.ShouldNotReachHere;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.ban.BanService;
import org.spongepowered.api.util.ban.Ban;

import javax.annotation.Nonnull;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class BanListImpl implements BanList {
    public BanListImpl(@Nonnull BanService service,
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

    @Override
    public BanEntry addBan(String s, String s1, Date date, String s2)
    {
        return null;
    }

    /**
     * Get all bans in this ban list.
     *
     * @return An immutable collection of ban entries
     */
    @Override
    public Set<BanEntry> getBanEntries()
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
            BanEntryImpl.constructSilenty(service, spongeBanInstance).ifPresent(entries::add);

        return Collections.unmodifiableSet(entries);
    }

    /**
     * Query whether the target is banned.
     *
     * @param target Target
     * @return Query result
     */
    @Override
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
     * @param target Target
     */
    @Override
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

    private final BanList.Type type;

    private final BanService service;
}
