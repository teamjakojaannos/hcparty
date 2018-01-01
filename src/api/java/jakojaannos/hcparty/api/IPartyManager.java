package jakojaannos.hcparty.api;

import jakojaannos.api.lib.IApiInstance;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * <b>!!! DO NOT IMPLEMENT THIS INTERFACE !!!</b>
 * <p>
 * Use {@link ObjectHolder @ObjectHolder(IPartyManager.REGISTRY_KEY)} to get an instance instead.
 */
public interface IPartyManager extends IApiInstance {
    String REGISTRY_KEY = "hcparty:partymanager";
    @ObjectHolder(REGISTRY_KEY) IPartyManager INSTANCE = null; // TODO: Investigate if this dies at runtime when API is in separate .jar


    /**
     * Returns true if the player is in a party
     */
    boolean isInParty(UUID playerUuid);

    /**
     * Creates a new party and assigns the given player as its leader. Throws IllegalStateException if player already
     * belongs to a party.
     */
    IParty createParty(UUID leaderUuid);

    /**
     * Gets the party player currently belongs to. null if player is not in a party
     */
    @Nullable
    IParty getParty(UUID playerUuid);

    /**
     * Resets the party manager. Removes all parties and forgets invites/requests
     */
    void reset();
}
