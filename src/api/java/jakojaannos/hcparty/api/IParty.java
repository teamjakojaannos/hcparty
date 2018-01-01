package jakojaannos.hcparty.api;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.UUID;

public interface IParty {

    /**
     * Gets the party ID
     */
    byte getId();

    /**
     * Gets an immutable list of all party members
     */
    ImmutableList<UUID> getMembers();

    /**
     * Gets the party leader
     */
    UUID getLeader();

    /**
     * Sets the party leader
     */
    void setLeader(UUID playerUuid);

    /**
     * Adds a player to the party
     */
    void addMember(UUID playerUuid);

    /**
     * Removes a player from the party
     */
    void removeMember(UUID playerUuid);

    /**
     * Checks if given player is a member of this party
     */
    default boolean isMember(UUID playerUuid) {
        return getMembers().contains(playerUuid);
    }

    /**
     * Checks if given player is the leader of this party
     */
    default boolean isLeader(UUID playerUuid) {
        return getLeader().equals(playerUuid);
    }
}
