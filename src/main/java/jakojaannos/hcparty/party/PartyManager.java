package jakojaannos.hcparty.party;

import com.google.common.base.Preconditions;
import jakojaannos.api.lib.IApiInstance;
import jakojaannos.hcparty.api.IInviteManager;
import jakojaannos.hcparty.api.IParty;
import jakojaannos.hcparty.api.IPartyManager;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PartyManager extends IForgeRegistryEntry.Impl<IApiInstance> implements IPartyManager {
    public static final PartyManager INSTANCE = (PartyManager) new PartyManager().setRegistryName(IPartyManager.REGISTRY_KEY);

    private List<IParty> parties = new ArrayList<>();

    @Override
    public boolean isInParty(UUID playerUuid) {
        return parties.stream().anyMatch(party -> party.isMember(playerUuid));
    }

    @Override
    public IParty createParty(UUID leaderUuid) {
        Preconditions.checkState(!isInParty(leaderUuid));

        Party party = new Party(findFreeId(), leaderUuid);
        parties.add(party);

        return party;
    }

    @Override
    @Nullable
    public IParty getParty(UUID playerUuid) {
        return parties.stream()
                .filter(party -> party.isMember(playerUuid))
                .findAny()
                .orElse(null);
    }

    @Override
    public void reset() {
        parties.clear();
    }

    void onPlayerAdded(UUID playerUuid, IParty party) {
        Preconditions.checkNotNull(party);
        // TODO: Fire events
    }

    void onPlayerRemoved(UUID playerUuid, IParty party, boolean wasLeader) {
        Preconditions.checkNotNull(party);

        // Remove the party from the manager if removed player was the last
        if (party.getMembers().isEmpty()) {
            parties.remove(party);
        }
        // Assign new leader if removed player used to be the leader
        else if (wasLeader) {
            party.setLeader(party.getMembers().get(0));
        }

        // TODO: Fire events
    }


    private byte findFreeId() {
        List<Byte> ids = parties.stream()
                .map(IParty::getId)
                .sorted(Byte::compareTo)
                .collect(Collectors.toList());

        if (ids.isEmpty()) {
            return 0;
        } else if (ids.size() == 1) {
            return 1;
        }

        for (int i = 0; i < ids.size() - 1; i++) {
            int dist = ids.get(i + 1) - ids.get(i);
            if (Math.abs(dist) > 1) {
                int foundId = ids.get(i) + 1;

                if (foundId <= Byte.MAX_VALUE) {
                    return (byte) foundId;
                }

                break;
            }
        }

        if (ids.get(ids.size() - 1) < Byte.MAX_VALUE) {
            return (byte)(ids.get(ids.size() - 1) + 1);
        }

        if (ids.get(0) > Byte.MIN_VALUE) {
            return (byte) (ids.get(0) - 1);
        }

        throw new IllegalStateException("Out of partyIds!");
    }
}
