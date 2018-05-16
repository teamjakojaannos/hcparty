package jakojaannos.life.api.revival.capability;

import net.minecraft.util.DamageSource;

/**
 * Capability for entities that do not die instantly when their health reaches zero, but rather enter a special
 * bleedout state
 */
public interface IBleedoutHandler {
    String REGISTRY_KEY = "life:bleedout";
    DamageSource DAMAGE_BLEEDOUT = new DamageSource("life_bleedout");


    /**
     * Number of times the entity has entered bleedout
     */
    int getBleedoutCount();

    /**
     * Resets the bleedout counter to the given value
     */
    void setBleedoutCount(int count);

    /**
     * Number of times the entity can enter bleedout until next death is instant
     */
    int getBleedoutCounterMax();


    /**
     * The maximum health the entity may have when bleeding out
     */
    float getMaxBleedoutHealth();

    /**
     * Current bleedout health
     */
    float getBleedoutHealth();

    /**
     * Set the current bleedout health
     */
    void setBleedoutHealth(float health);


    /**
     * Resistance to bleedout damage
     */
    float getBleedoutResistance();

    /**
     * Time the entity has spent bleeding out
     */
    int getBleedoutTime();

    void setBleedoutTime(int time);

    default void updateTimer() {
        setBleedoutTime(getBleedoutTime() + 1);
    }

    default void resetTimer() {
        setBleedoutTime(0);
    }

    default boolean hasBledOut() {
        return getBleedoutHealth() <= 0;
    }
}
