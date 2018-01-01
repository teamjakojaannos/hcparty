package jakojaannos.hcparty.party;

import jakojaannos.hcparty.api.IParty;

import java.util.UUID;

class Proposal {
    private final UUID playerUuid;
    private final IParty party;
    private final boolean isInvite;


    Proposal(UUID playerUuid, IParty party, boolean isInvite) {
        this.playerUuid = playerUuid;
        this.party = party;
        this.isInvite = isInvite;
    }


    UUID getPlayer() {
        return playerUuid;
    }

    IParty getParty() {
        return party;
    }


    boolean hasPlayerAccepted() {
        return !isInvite;
    }

    boolean hasPartyAccepted() {
        return isInvite;
    }

    boolean isInvite() {
        return isInvite;
    }

    boolean isRequest() {
        return !isInvite;
    }
}
