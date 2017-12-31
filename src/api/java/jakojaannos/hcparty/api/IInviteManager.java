package jakojaannos.hcparty.api;

import java.util.List;
import java.util.UUID;

public interface IInviteManager {
    /**
     * Invites the player to the party
     */
    void inviteToParty(UUID playerUuid, IParty party);

    /**
     * Sends the party a join request
     */
    void requestToJoinParty(UUID playerUuid, IParty party);

    /**
     * Removes all invites a player has received
     */
    void clearInvites(UUID playerUuid);

    /**
     * Removes all requests a player has sent
     */
    void clearRequests(UUID playerUuid);

    /**
     * Removes the invite at given index
     */
    void removeInvite(UUID playerUuid, int index);

    /**
     * Removes the request at given index
     */
    void removeRequest(IParty party, int index);

    /**
     * Gets a list of pending invites to join parties
     */
    List<IParty> getPendingInvites(UUID playerUuid);

    /**
     * Gets a list of pending requests to join the party
     */
    List<UUID> getPendingRequests(IParty party);
}
