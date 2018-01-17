package jakojaannos.life.api.revival.capabilities;

/**
 * Capability for entities that do not die when their bleedout health reaches zero, but rather fall unconscious for a
 * set period of time before dying
 */
public interface IUnconsciousHandler {
    String REGISTRY_KEY = "life:unconscious";

    /**
     * Total time the entity may spend unconscious before dying (in ticks)
     */
    int getUnconsciousDuration();

    /**
     * Time the entity has been unconscious
     */
    int getUnconsciousTimer();

    /**
     * Increases timer value by one
     */
    void tickTimer();

    /**
     * Resets the timer to given value
     */
    void setTimer(int time);

    /**
     * Resets the timer to zero
     */
    default void resetTimer() {
        setTimer(0);
    }
}
