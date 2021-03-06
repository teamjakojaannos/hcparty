package jakojaannos.life.api.revival.capability;

/**
 * Capability for entities that do not die when their bleedout health reaches zero, but rather fall unconscious for a
 * set period of time before dying
 */
public interface IUnconsciousHandler {
    String REGISTRY_KEY = "life:unconscious";

    /**
     * Total time the entity may spend unconscious before dying (in ticks)
     */
    int getDuration();

    void setDuration(int duration);

    /**
     * Time the entity has been unconscious
     */
    int getTimer();

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

    /**
     * Increases timer value by one
     */
    default void updateTimer() {
        setTimer(getTimer() + 1);
    }

    default boolean shouldBeDead() {
        return getTimer() > getDuration();
    }


    /**
     * Returns the orientation (rotation around the "up" axis) the player was in when they fell unconscious.
     * Rendering locks the player to this orientation so that they cannot spin while unconscious.
     */
    float getOrientation();

    /**
     * Sets the orientation player will be rendered in while unconscious.
     */
    void setOrientation(float orientation);
}
