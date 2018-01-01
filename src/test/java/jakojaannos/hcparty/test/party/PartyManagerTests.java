package jakojaannos.hcparty.test.party;

import jakojaannos.hcparty.api.IParty;
import jakojaannos.hcparty.api.IPartyManager;
import jakojaannos.hcparty.party.PartyManager;
import org.junit.jupiter.api.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests that IPartyManager methods work as intended
 */
public class PartyManagerTests {
    private static final int TEST_MULTIPLE_PLAYER_COUNT = 100;
    private static final int MAX_PARTIES = 255;
    private static IPartyManager INSTANCE;

    @BeforeAll
    static void initAll() {
        INSTANCE = new PartyManager();
    }

    @BeforeEach
    void init() {
    }


    /**
     * Tests that {@link IPartyManager#createParty(UUID)} works as intended.
     * Tests for one player.
     * <p>
     * 1. Creates a party
     * 2. Verifies that the player is in a party
     */
    @Test
    void testCreatePartySingle() {
        final UUID playerUuid = UUID.randomUUID();

        INSTANCE.createParty(playerUuid);
        assertTrue(INSTANCE.isInParty(playerUuid));
    }

    /**
     * Tests that {@link IPartyManager#createParty(UUID)} works as intended.
     * Tests for {@link #TEST_MULTIPLE_PLAYER_COUNT} players
     * <p>
     * 1. Creates a party for each player
     * 2. Verifies that all players are in a party
     * 3. Verifies that all parties are unique
     */
    @Test
    void testCreatePartyMultiple() {
        final UUID[] players = new UUID[TEST_MULTIPLE_PLAYER_COUNT];
        final IParty[] parties = new IParty[TEST_MULTIPLE_PLAYER_COUNT];
        for (int i = 0; i < players.length; i++) {
            players[i] = UUID.randomUUID();
            parties[i] = INSTANCE.createParty(players[i]);
        }

        for (UUID uuid : players) {
            assertTrue(INSTANCE.isInParty(uuid));
        }

        for (int i = 0; i < players.length; i++) {
            for (int j = 0; j < players.length; j++) {
                if (i == j) {
                    continue;
                }

                assertNotEquals(parties[i], parties[j]);
            }
        }
    }


    /**
     * Tests that getParty returns correct party
     * <p>
     * 1. Creates a party
     * 2. Tries to get a party the player belongs to
     * 3. Verifies that created party is same as the party player is in
     */
    @Test
    void testGetParty() {
        final UUID playerUuid = UUID.randomUUID();

        IParty created = INSTANCE.createParty(playerUuid);
        IParty get = INSTANCE.getParty(playerUuid);

        assertNotNull(get);
        assertEquals(created, get);
    }

    /**
     * Tests that getParty returns correct party even with large number of parties
     *
     * 1. Creates a party for each player
     * 2. Verifies that each player belongs to a non-null party
     * 3. Verifies that created parties are same parties the players are currently in
     */
    @Test
    void testGetPartyLotsOfParties() {
        final UUID[] players = new UUID[TEST_MULTIPLE_PLAYER_COUNT];
        final IParty[] parties = new IParty[TEST_MULTIPLE_PLAYER_COUNT];
        final IParty[] get = new IParty[TEST_MULTIPLE_PLAYER_COUNT];

        for (int i = 0; i < players.length; i++) {
            players[i] = UUID.randomUUID();
            parties[i] = INSTANCE.createParty(players[i]);
        }

        for (int i = 0; i < players.length; i++) {
            get[i] = INSTANCE.getParty(players[i]);
            assertNotNull(get[i]);
        }

        for (int i = 0; i < players.length; i++) {
            assertEquals(parties[i], get[i]);
        }
    }


    /**
     * Verifies that error throws properly when out of IDs.
     */
    @Test
    void testOutOfIDs() {
        // Fill IDs
        int iteration = 0;
        while (iteration <= MAX_PARTIES) {
            INSTANCE.createParty(UUID.randomUUID());
            iteration++;
        }

        assertThrows(IllegalStateException.class, () -> INSTANCE.createParty(UUID.randomUUID()));
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
