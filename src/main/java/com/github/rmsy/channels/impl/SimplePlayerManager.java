package com.github.rmsy.channels.impl;

import com.github.rmsy.channels.Channel;
import com.github.rmsy.channels.PlayerManager;
import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Simple implementation of {@link PlayerManager}.
 */
public class SimplePlayerManager implements PlayerManager {

    /**
     * Players mapped to their membership channels.
     */
    private final Map<Player, Channel> playerMembershipMap;
    /**
     * Players mapped to the channels they're listening to.
     */
    private final Map<Player, Set<Channel>> playerListeningMap;

    /**
     * Creates a new SimplePlayerManager.
     */
    public SimplePlayerManager() {
        this.playerMembershipMap = new HashMap<Player, Channel>();
        this.playerListeningMap = new HashMap<Player, Set<Channel>>();
    }

    /**
     * Gets the channel the player is a member of.
     *
     * @param player The player.
     * @return The channel the player is a member of.
     */
    @Override
    public Channel getMembershipChannel(@Nonnull Player player) {
        return this.playerMembershipMap.get(Preconditions.checkNotNull(player, "player"));
    }

    /**
     * Gets the channels the player is listening to.
     *
     * @param player The player.
     * @return The channels the player is listening to.
     */
    @Nonnull
    @Override
    public Set<Channel> getListeningChannels(@Nonnull Player player) {
        Set<Channel> channels = this.playerListeningMap.get(Preconditions.checkNotNull(player, "player"));
        if (channels == null) {
            channels = new HashSet<Channel>();
        }
        return new HashSet<Channel>(channels);
    }

    /**
     * Sets the channel the player is a member of. Removes the player from their old membership channel.
     *
     * @param player            The player.
     * @param membershipChannel The channel the player is a member of.
     */
    @Override
    public void setMembershipChannel(@Nonnull Player player, @Nonnull Channel membershipChannel) {
        this.playerMembershipMap.put(Preconditions.checkNotNull(player, "player"), Preconditions.checkNotNull(membershipChannel, "channel"));
        ((SimpleChannel) membershipChannel).addMember(player);
        SimpleChannel oldChannel = (SimpleChannel) this.playerMembershipMap.get(player);
        if (oldChannel != null) {
            oldChannel.removeMember(player);
        }
    }

    /**
     * Adds the player as a listener of the specified channel.
     *
     * @param player  The player.
     * @param channel The channel.
     */
    protected void addListener(@Nonnull final Player player, @Nonnull final Channel channel) {
        Set<Channel> channels = this.playerListeningMap.get(Preconditions.checkNotNull(player, "player"));
        if (channels == null) {
            channels = new HashSet<Channel>();
        }
        channels.add(channel);
        this.playerListeningMap.put(player, channels);
    }
}
