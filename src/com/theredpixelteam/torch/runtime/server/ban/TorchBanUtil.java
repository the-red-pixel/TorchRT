package com.theredpixelteam.torch.runtime.server.ban;

import org.bukkit.BanList;
import org.spongepowered.api.util.ban.BanType;
import org.spongepowered.api.util.ban.BanTypes;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;

/**
 * Utils for ban services.
 */
public class TorchBanUtil {
    private TorchBanUtil()
    {
    }

    /**
     * Convert bukkit ban type to sponge ban type.
     *
     * @param type Bukkit ban type
     * @return Result
     */
    public static @Nonnull Optional<BanType> fromBukkitType(@Nonnull BanList.Type type)
    {
        Objects.requireNonNull(type, "type");

        switch (type)
        {
            case IP:    return Optional.of(BanTypes.IP);
            case NAME:  return Optional.of(BanTypes.PROFILE);
            default:    return Optional.empty();
        }
    }

    /**
     * Convert sponge ban type to bukkit ban type.
     *
     * @param type Sponge ban type
     * @return Result
     */
    public static @Nonnull Optional<BanList.Type> fromSpongeType(@Nonnull BanType type)
    {
        Objects.requireNonNull(type, "type");

        if (BanTypes.PROFILE.equals(type))
            return Optional.of(BanList.Type.NAME);
        else if (BanTypes.IP.equals(type))
            return Optional.of(BanList.Type.IP);

        return Optional.empty();
    }
}
