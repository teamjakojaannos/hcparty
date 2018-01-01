package jakojaannos.hcparty.test.party;

import jakojaannos.hcparty.api.IParty;
import jakojaannos.hcparty.api.IPartyManager;
import jakojaannos.hcparty.party.PartyManager;
import org.junit.jupiter.api.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class IPartyTests {
    private static final int TEST_MULTIPLE_PLAYER_COUNT = 100;
    private static IPartyManager INSTANCE;

    @BeforeAll
    static void initAll() {
        INSTANCE = new PartyManager();
    }

    @BeforeEach
    void init() {
    }


    @Test
    void testAddMember() {
        final UUID playerUuid = UUID.randomUUID();
        final UUID otherUuid = UUID.randomUUID();

        IParty created = INSTANCE.createParty(playerUuid);
        created.addMember(otherUuid);

        assertEquals(created, INSTANCE.getParty(playerUuid));
        assertEquals(created, INSTANCE.getParty(otherUuid));
        assertEquals(INSTANCE.getParty(otherUuid), INSTANCE.getParty(playerUuid));
    }

    /**
     * Tries to remove a member from party
     */
    @Test
    void testRemoveMemberFromParty() {
        final UUID playerUuid = UUID.randomUUID();
        final UUID otherUuid = UUID.randomUUID();

        IParty created = INSTANCE.createParty(playerUuid);
        created.addMember(otherUuid);
        created.removeMember(otherUuid);

        assertTrue(created.isMember(playerUuid));
        assertFalse(created.isMember(otherUuid));
        assertTrue(created.isLeader(playerUuid));
    }

    /**
     * Tries to remove non-member player from party
     */
    @Test
    void testRemoveInvalidMemberFromParty() {
        final UUID playerUuid = UUID.randomUUID();
        final UUID otherUuid = UUID.randomUUID();

        IParty created = INSTANCE.createParty(playerUuid);
        created.removeMember(otherUuid);

        assertTrue(created.isMember(playerUuid));
        assertFalse(created.isMember(otherUuid));
        assertTrue(created.isLeader(playerUuid));
    }

    /**
     * Tries to remove the leader from party
     */
    @Test
    void testRemoveLeaderFromParty() {
        final UUID playerUuid = UUID.randomUUID();
        final UUID otherUuid = UUID.randomUUID();

        IParty created = INSTANCE.createParty(playerUuid);
        created.addMember(otherUuid);
        created.removeMember(playerUuid);

        assertTrue(created.isMember(otherUuid), "other is member");
        assertFalse(created.isMember(playerUuid), "original is member");
        assertTrue(created.isLeader(otherUuid), "other is new leader");
        assertFalse(created.isLeader(playerUuid), "original is new leader");
    }

    /**
     * Tries to remove the last member from party
     */
    @Test
    void testRemoveLastMemberFromParty() {
        final UUID playerUuid = UUID.randomUUID();

        IParty created = INSTANCE.createParty(playerUuid);
        created.removeMember(playerUuid);

        assertFalse(created.isMember(playerUuid));
        assertNull(INSTANCE.getParty(playerUuid));
    }

    /**
     * Creates a lot of parties, adds one additional player to each and removes them afterwards. Verifies that the
     * leader is still the party leader afterwards
     */
    @Test
    void testRemoveOtherPlayerFromPartyMultipleValid() {
        final UUID[] players = new UUID[TEST_MULTIPLE_PLAYER_COUNT];
        final UUID[] others = new UUID[TEST_MULTIPLE_PLAYER_COUNT];
        final IParty[] parties = new IParty[TEST_MULTIPLE_PLAYER_COUNT];

        for (int i = 0; i < players.length; i++) {
            players[i] = UUID.randomUUID();
            parties[i] = INSTANCE.createParty(players[i]);

            others[i] = UUID.randomUUID();
            parties[i].addMember(others[i]);
        }

        for (int i = 0; i < players.length; i++) {
            parties[i].removeMember(others[i]);
        }

        for (int i = 0; i < players.length; i++) {
            assertTrue(parties[i].isMember(players[i]));
            assertFalse(parties[i].isMember(others[i]));
            assertEquals(parties[i], INSTANCE.getParty(players[i]));
            assertTrue(parties[i].isLeader(players[i]));
            assertFalse(parties[i].isLeader(others[i]));
        }
    }


    /**
     * Tries to set the leader
     */
    @Test
    void testSetLeader() {
        UUID player = UUID.randomUUID();
        UUID other = UUID.randomUUID();

        IParty party = INSTANCE.createParty(player);
        party.addMember(other);
        party.setLeader(other);

        assertFalse(party.isLeader(player));
        assertTrue(party.isLeader(other));
    }

    /**
     * Tries to set leader to non-member
     */
    @Test
    void testSetLeaderToNonMember() {
        UUID player = UUID.randomUUID();
        UUID other = UUID.randomUUID();

        IParty party = INSTANCE.createParty(player);
        party.setLeader(other);

        assertTrue(party.isLeader(player));
        assertFalse(party.isLeader(other));
    }


    @AfterEach
    void tearDown() {
        INSTANCE.reset();
    }

    @AfterAll
    static void tearDownAll() {
        INSTANCE = null;
    }
}
