package jakojaannos.hcparty.api.event;

import jakojaannos.hcparty.api.IParty;
import jakojaannos.hcparty.api.PartyAPI;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.UUID;

/**
 * Events fired on party actions. All child events are fired on {@link PartyAPI#API_EVENT_BUS}
 * This event is not {@link Cancelable} nor does it {@link HasResult}.
 */
public abstract class PartyEvent extends Event {

    private final IParty party;

    PartyEvent(IParty party) {
        this.party = party;
    }

    /**
     * Fired after a new party is created.
     * This event is not {@link Cancelable} nor does it {@link HasResult}.
     */
    public static class Created extends PartyEvent {
        public Created(IParty party) {
            super(party);
        }
    }

    /**
     * Fired after a party is removed. The party passed as argument is no longer recognized by PartyManager.
     * This event is not {@link Cancelable} nor does it {@link HasResult}.
     */
    public static class Removed extends PartyEvent {
        public Removed(IParty party) {
            super(party);
        }
    }

    public static abstract class Member extends PartyEvent {
        private final UUID player;

        Member(IParty party, UUID player) {
            super(party);
            this.player = player;
        }

        public static class Added extends Member {
            public Added(IParty party, UUID player) {
                super(party, player);
            }
        }

        public static class Removed extends Member {
            public Removed(IParty party, UUID player) {
                super(party, player);
            }
        }
    }
}
