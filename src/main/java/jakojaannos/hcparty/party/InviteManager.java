package jakojaannos.hcparty.party;

import com.google.common.collect.ImmutableList;
import jakojaannos.api.lib.IApiInstance;
import jakojaannos.hcparty.api.IInviteManager;
import jakojaannos.hcparty.api.IParty;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class InviteManager extends IForgeRegistryEntry.Impl<IApiInstance> implements IInviteManager {

    private List<Proposal> proposals = new ArrayList<>();

    @Override
    public boolean hasPendingInvites(UUID playerUuid) {
        return proposals.stream()
                .filter(Proposal::isInvite)
                .anyMatch(proposal -> proposal.getPlayer().equals(playerUuid));
    }

    @Override
    public boolean hasPendingRequests(IParty party) {
        return proposals.stream()
                .filter(Proposal::isRequest)
                .anyMatch(proposal -> proposal.getParty().equals(party));
    }

    @Override
    public ImmutableList<IParty> getPendingInvites(UUID playerUuid) {
        return ImmutableList.copyOf(proposals.stream()
                .filter(Proposal::isInvite)
                .filter(proposal -> proposal.getPlayer().equals(playerUuid))
                .map(Proposal::getParty)
                .collect(Collectors.toList()));
    }

    @Override
    public ImmutableList<UUID> getPendingRequests(IParty party) {
        return ImmutableList.copyOf(proposals.stream()
                .filter(Proposal::isRequest)
                .filter(proposal -> proposal.getParty().equals(party))
                .map(Proposal::getPlayer)
                .collect(Collectors.toList()));
    }

    @Override
    public void addProposal(UUID player, IParty party, boolean isInvite) {
        // Ignore duplicate proposals, accept symmetric proposals
        if (proposals.stream().anyMatch(p -> p.getPlayer().equals(player) && p.getParty().equals(party) && p.isInvite() != isInvite)) {
            acceptProposal(player, party);
            return;
        } else if (proposals.stream().anyMatch(p -> p.getPlayer().equals(player) && p.getParty().equals(party) && p.isInvite() == isInvite)) {
            return;
        }

        proposals.add(new Proposal(player, party, isInvite));
    }

    @Override
    public void acceptProposal(UUID player, IParty party) {
        Proposal proposal = proposals.stream()
                .filter(p -> p.getPlayer().equals(player) && p.getParty().equals(party))
                .findAny()
                .orElse(null);

        if (proposal == null) {
            return;
        }

        // Remove all entries with player in them
        proposals = proposals.stream()
                .filter(p -> !p.getPlayer().equals(player))
                .collect(Collectors.toList());

        party.addMember(player);
    }

    @Override
    public void rejectProposal(UUID player, IParty party) {
        Proposal proposal = proposals.stream()
                .filter(p -> p.getPlayer().equals(player) && p.getParty().equals(party))
                .findAny()
                .orElse(null);

        if (proposal == null) {
            return;
        }

        proposals.remove(proposal);
    }

    @Override
    public void reset() {
        proposals.clear();
    }
}
