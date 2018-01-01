package jakojaannos.hcparty.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Verifies that test suite is working as intended
 */
@DisplayName("Test suite validation")
@Tag("test-suite")
class TestSuiteTests {
    @Test
    void testSimpleAssert() {
        assertEquals(10, 7 + 3);
        assertNotEquals(10, 4 + 8);
    }
}
