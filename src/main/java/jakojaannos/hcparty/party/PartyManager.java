package jakojaannos.hcparty.party;

import com.google.common.base.Preconditions;
import jakojaannos.api.hcparty.IInviteManager;
import jakojaannos.api.hcparty.IParty;
import jakojaannos.api.hcparty.IPartyManager;

import javax.annotation.Nullable;
import java.util.*;

public class PartyManager implements IPartyManager {
    private List<Party> parties = new ArrayList<>();

    private IInviteManager inviteManager = new InviteManager();

    @Override
    public IInviteManager getInviteManager() {
        return inviteManager;
    }

    /**
     * Returns true if target player is in a party
     */
    @Override
    public boolean isInParty(UUID playerUuid) {
        return parties.stream().anyMatch(party -> party.isMember(playerUuid));
    }

    /**
     * Creates a new party, setting target player as the leader
     */
    @Override
    public void createParty(UUID leaderUuid) {
        Preconditions.checkState(!isInParty(leaderUuid));

        Party party = new Party();
        party.addMember(leaderUuid, true);

        parties.add(party);
    }

    /**
     * Gets the party the targeted player currently belongs to, null if player is not in a party
     */
    @Override
    @Nullable
    public IParty getCurrentParty(UUID playerUuid) {
        return parties.stream()
                .filter(party -> party.isMember(playerUuid))
                .findAny()
                .orElse(null);
    }

    /**
     * Adds a player to the party. Note: Does not remove invites/requests
     */
    @Override
    public void addPlayerToParty(UUID playerUuid, IParty party, boolean isLeader) {
        party.addMember(playerUuid, isLeader);
    }

    /**
     * Removes target player from party it currently belongs to
     */
    @Override
    public void removePlayerFromParty(UUID playerUuid) {
        parties.stream()
                .filter(party -> party.isMember(playerUuid))
                // There should only be one party at this point
                .forEach(party -> party.removeMember(playerUuid));
    }
}
