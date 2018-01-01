package jakojaannos.hcparty.api;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import java.util.UUID;

/**
 * <b>!!! DO NOT IMPLEMENT THIS INTERFACE !!!</b>
 * <p>
 * Use {@link ObjectHolder @ObjectHolder(IInviteManager.REGISTRY_KEY)} to get an instance instead.
 * <p>
 * Provides invite-handling for the IPartyManager.
 * <p>
 * Internally, the system calls invites and join requests "proposals". When player makes request to join a party, they
 * make proposal for them to join the party. When party leader invites someone to the party, they make proposal for the
 * target player to join the party. This removes the need for handling the invites/requests separately in most cases.
 * <p>
 */
public interface IInviteManager /* extends IApiInstance */ {
    String REGISTRY_KEY = "hcparty:invitemanager";
    @ObjectHolder(REGISTRY_KEY)  IInviteManager INSTANCE = null;


    boolean hasPendingInvites(UUID playerUuid);

    boolean hasPendingRequests(IParty party);

    ImmutableList<IParty> getPendingInvites(UUID playerUuid);

    ImmutableList<UUID> getPendingRequests(IParty party);


    /**
     * Adds proposal for adding player to the party. If proposals are made symmetrically for invite and request,
     * situation is counted as accepted proposal.
     */
    void addProposal(UUID player, IParty party, boolean isInvite);

    void acceptProposal(UUID player, IParty party);

    void rejectProposal(UUID player, IParty party);


    /**
     * Resets the invite manager. Clears all pending requests and invites
     */
    void reset();
}
