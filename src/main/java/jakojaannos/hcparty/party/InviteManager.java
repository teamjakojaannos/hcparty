package jakojaannos.hcparty.party;

import com.google.common.collect.ImmutableList;
import jakojaannos.api.hcparty.IInviteManager;
import jakojaannos.api.hcparty.IParty;

import java.util.*;

public class InviteManager implements IInviteManager {

    private Map<UUID, List<IParty>> invites = new HashMap<>();
    private Map<IParty, List<UUID>> requests = new HashMap<>();

    @Override
    public void inviteToParty(UUID playerUuid, IParty party) {
        if (getInviteList(playerUuid).contains(party)) {
            return;
        }

        getInviteList(playerUuid).add(party);
    }

    @Override
    public void requestToJoinParty(UUID playerUuid, IParty party) {
        if (getRequestList(party).contains(playerUuid)) {
            return;
        }

        getRequestList(party).add(playerUuid);
    }

    @Override
    public void clearInvites(UUID playerUuid) {
        getInviteList(playerUuid).clear();
    }

    @Override
    public void clearRequests(UUID playerUuid) {
        for (IParty party : requests.keySet()) {
            getRequestList(party).remove(playerUuid);
        }
    }

    @Override
    public void removeInvite(UUID playerUuid, int index) {
        getInviteList(playerUuid).remove(index);
    }

    @Override
    public void removeRequest(IParty party, int index) {
        getRequestList(party).remove(index);
    }

    @Override
    public List<IParty> getPendingInvites(UUID playerUuid) {
        return ImmutableList.copyOf(getInviteList(playerUuid));
    }

    @Override
    public List<UUID> getPendingRequests(IParty party) {
        return ImmutableList.copyOf(getRequestList(party));
    }


    private List<IParty> getInviteList(UUID uuid) {
        if (!invites.containsKey(uuid)) {
            invites.put(uuid, new ArrayList<>());
        }

        return invites.get(uuid);
    }

    private List<UUID> getRequestList(IParty party) {
        if (!requests.containsKey(party)) {
            requests.put(party, new ArrayList<>());
        }

        return requests.get(party);
    }
}
