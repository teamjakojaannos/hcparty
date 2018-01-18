package jakojaannos.life.api.entity.capability;

/**
 * Capability for tracking the health of living entities
 */
public interface IHealthTracker {
    String REGISTRY_KEY = "life:healthtracker";

    /**
     * Number of recorded ticks to keep in memory
     */
    int getBacklogLength();

    void setBacklogLength(int ticks);

    void addEntry(float health);

    /**
     * Gets the tracked health of the entity given number of ticks ago. ticks must be positive and less than
     * {@link #getBacklogLength()}
     */
    float getHealth(int ticks);

    float getTrackedMaxHealth();

    int getTimer();

    void tick();

    int getUpdateInterval();
}
