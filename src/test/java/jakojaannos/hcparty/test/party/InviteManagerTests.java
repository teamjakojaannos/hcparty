package jakojaannos.hcparty.test.party;

import jakojaannos.hcparty.api.IInviteManager;
import jakojaannos.hcparty.api.IParty;
import jakojaannos.hcparty.api.IPartyManager;
import jakojaannos.hcparty.party.InviteManager;
import jakojaannos.hcparty.party.PartyManager;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InviteManagerTests {
    private static IPartyManager partyManager;
    private static IInviteManager inviteManager;

    @BeforeAll
    static void initAll() {
        partyManager = new PartyManager();
        inviteManager = new InviteManager();
    }

    @BeforeEach
    void init() {
    }


    @Test
    void testHasInvitesWithoutInvites() {
        UUID player = UUID.randomUUID();
        IParty party = partyManager.createParty(UUID.randomUUID());
        assertFalse(inviteManager.hasPendingInvites(player));
        assertFalse(inviteManager.hasPendingRequests(party));
    }

    @Test
    void testHasInvites() {
        UUID player = UUID.randomUUID();
        IParty party = partyManager.createParty(UUID.randomUUID());

        inviteManager.addProposal(player, party, true);
        assertFalse(party.isMember(player));
        assertTrue(inviteManager.hasPendingInvites(player));
    }

    @Test
    void testHasInvitesWithoutInvitesWithRequests() {
        UUID player = UUID.randomUUID();
        IParty party = partyManager.createParty(UUID.randomUUID());

        inviteManager.addProposal(player, party, false);
        assertFalse(party.isMember(player));
        assertFalse(inviteManager.hasPendingInvites(player));
        assertTrue(inviteManager.hasPendingRequests(party));
    }

    /**
     * Tests that symmetrical invite/request results in accepted invite
     */
    @Test
    void testHasInviteAndRequest() {
        UUID player = UUID.randomUUID();
        IParty party = partyManager.createParty(UUID.randomUUID());

        inviteManager.addProposal(player, party, true);
        inviteManager.addProposal(player, party, false);
        assertFalse(inviteManager.hasPendingInvites(player));
        assertFalse(inviteManager.hasPendingRequests(party));

        assertTrue(party.isMember(player));
    }


    /**
     * Tests that all sent invites/requests are returned from getPending**
     */
    @Test
    void testGetPending() {
        UUID[] invited = new UUID[100];
        List<UUID> requested = new ArrayList<>(100);
        List<IParty> parties = new ArrayList<>(100);

        for (int i = 0; i < 100; i++) {
            invited[i] = UUID.randomUUID();
            requested.add(UUID.randomUUID());
            parties.add(partyManager.createParty(UUID.randomUUID()));
        }

        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                inviteManager.addProposal(invited[i], parties.get(j), true);
                inviteManager.addProposal(requested.get(i), parties.get(j), false);
            }
        }

        for (int i = 0; i < 100; i++) {
            assertTrue(inviteManager.getPendingInvites(invited[i]).containsAll(parties), "invites");
            assertTrue(inviteManager.getPendingRequests(parties.get(i)).containsAll(requested), "requests");
        }
    }

    @Test
    void testAcceptInvite() {
        UUID player = UUID.randomUUID();
        IParty party = partyManager.createParty(UUID.randomUUID());

        inviteManager.addProposal(player, party, true);
        inviteManager.acceptProposal(player, party);

        assertTrue(party.isMember(player));
        assertFalse(inviteManager.hasPendingInvites(player));
        assertFalse(inviteManager.hasPendingRequests(party));
    }

    @Test
    void testAcceptRequest() {
        UUID player = UUID.randomUUID();
        IParty party = partyManager.createParty(UUID.randomUUID());

        inviteManager.addProposal(player, party, false);
        inviteManager.acceptProposal(player, party);

        assertTrue(party.isMember(player));
        assertFalse(inviteManager.hasPendingInvites(player));
        assertFalse(inviteManager.hasPendingRequests(party));
    }

    @Test
    void testRejectInvite() {
        UUID player = UUID.randomUUID();
        IParty party = partyManager.createParty(UUID.randomUUID());

        inviteManager.addProposal(player, party, true);
        inviteManager.rejectProposal(player, party);

        assertFalse(party.isMember(player));
        assertFalse(inviteManager.hasPendingInvites(player));
        assertFalse(inviteManager.hasPendingRequests(party));
    }

    @Test
    void testRejectRequest() {
        UUID player = UUID.randomUUID();
        IParty party = partyManager.createParty(UUID.randomUUID());

        inviteManager.addProposal(player, party, false);
        inviteManager.rejectProposal(player, party);

        assertFalse(party.isMember(player));
        assertFalse(inviteManager.hasPendingInvites(player));
        assertFalse(inviteManager.hasPendingRequests(party));
    }


    @AfterEach
    void tearDown() {
        partyManager.reset();
        inviteManager.reset();
    }

    @AfterAll
    static void tearDownAll() {
        partyManager = null;
        inviteManager = null;
    }
}

