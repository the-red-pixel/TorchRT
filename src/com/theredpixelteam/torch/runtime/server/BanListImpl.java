package com.theredpixelteam.torch.runtime.server;

import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.spongepowered.api.service.ban.BanService;

import java.util.Date;
import java.util.Set;

public class BanListImpl implements BanList {
    public BanListImpl(BanService service)
    {
        this.service = service;
    }

    @Override
    public BanEntry getBanEntry(String playerName)
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

    @Override
    public boolean isBanned(String s)
    {
        return false;
    }

    /**
     * Pardon the actual game player.
     *
     * <b>Whether this method has an immediate effect depends on the actual implementation
     * of the BanService.</b>
     *
     * @param playerName Player name
     */
    @Override
    public void pardon(String playerName)
    {
        
    }

    private final BanService service;
}
