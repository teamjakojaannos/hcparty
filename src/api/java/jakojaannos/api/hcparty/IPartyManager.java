package jakojaannos.api.hcparty;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * !!! DO NOT IMPLEMENT THIS INTERFACE !!!
 * <p>
 * Use provided static field {@link #INSTANCE} instead. That field gets
 * populated to a valid instance during pre-initialization.
 *
 * TODO: Investigate if creating custom registry for API instances would be feasible to implement
 *      - would allow @ObjectHolder("hcparty:apiPartyManager")
 *      - @ObjectHolders can be declared final...
 *      - ...which in turn would prevent anyone from fucking with the instance
 *      - ...unless they use ATs, reflection and/or black magic but that's just stupid
 */
public interface IPartyManager {
    IPartyManager INSTANCE = null;

    /**
     * Gets the invite manager
     */
    IInviteManager getInviteManager();

    /**
     * Returns true if the player is in a party
     */
    boolean isInParty(UUID playerUuid);

    /**
     * Creates a new party and assigns the given player as its leader.
     * Throws IllegalStateException if player is already in a party.
     */
    void createParty(UUID leaderUuid);

    /**
     * Gets the party player currently belongs to. null if player is not in a party
     */
    @Nullable
    IParty getCurrentParty(UUID playerUuid);

    /**
     * Adds a player to the party. Note: Does not remove invites/requests
     */
    void addPlayerToParty(UUID playerUuid, IParty party, boolean isLeader);

    /**
     * Removes target player from party it currently belongs to
     */
    void removePlayerFromParty(UUID playerUuid);
}
