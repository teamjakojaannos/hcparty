package jakojaannos.hcparty.api;

import jakojaannos.api.lib.IApiInstance;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * !!! DO NOT IMPLEMENT THIS INTERFACE !!!
 * <p>
 * Use provided static field {@link #INSTANCE} instead. That field gets populated to a valid instance during
 * pre-initialization.
 */
public interface IPartyManager extends IApiInstance {
    // TODO: Investigate if this dies at runtime when API is in separate .jar
    @ObjectHolder("hcparty:partymanager")
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
