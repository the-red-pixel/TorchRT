package com.theredpixelteam.torch.runtime.server;

import com.theredpixelteam.torch.GameProfileUtil;
import com.theredpixelteam.torch.exception.ShouldNotReachHere;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.ban.BanService;

import javax.annotation.Nonnull;
import java.net.InetAddress;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class BanListImpl implements BanList {
    public BanListImpl(
            @Nonnull BanService service,
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

    @Override
    public Set<BanEntry> getBanEntries()
    {
        return null;
    }

    /**
     * Query whether the target is banned.
     *
     * @param target Target
     * @return Query result
     */
    @Override
    public boolean isBanned(String target)
    {
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
                } catch (Exception e) {
                    throw new IllegalArgumentException(e);
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
    public void pardon(String target)
    {
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
                    throw new IllegalArgumentException(e);
                }

                break;

            default:
                throw new ShouldNotReachHere();
        }
    }

    private final BanList.Type type;

    private final BanService service;
}
