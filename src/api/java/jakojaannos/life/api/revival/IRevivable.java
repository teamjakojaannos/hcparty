package jakojaannos.life.api.revival;

import com.google.common.collect.Lists;
import net.minecraft.util.DamageSource;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Something that can be revived
 */
public interface IRevivable {
    String REGISTRY_NAME = "life:revivable";
    DamageSource DAMAGE_BLEEDOUT = new DamageSource("life_bleedout");

    /**
     * Damage sources that are considered instant-kill (vanilla death behavior)
     */
    List<DamageSource> INSTAKILL_SOURCES = Lists.newArrayList(DamageSource.OUT_OF_WORLD, DAMAGE_BLEEDOUT);


    /**
     * Describes the status of the entity
     */
    enum Status {
        ALIVE,
        BLEEDING_OUT,
        UNCONSCIOUS,
        DEAD
    }

    Status getStatus();

    void setStatus(Status status);

    default boolean isBleedingOut() {
        return getStatus() == Status.BLEEDING_OUT;
    }

    default boolean isUnconscious() {
        return getStatus() == Status.UNCONSCIOUS;
    }


    /**
     * Number of times the entity has entered bleedout
     */
    int getBleedoutCount();

    /**
     * Resets the bleedout counter to given value
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
     * Changes the status to BLEEDING_OUT
     */
    void enterBleedout();


    /**
     * Total duration the entity may spend unconscious before dying
     */
    int getUnconsciousDuration();

    /**
     * Time the entity has been unconscious
     */
    int getUnconsciousTimer();


    /**
     * Resistance to bleedout damage
     */
    float getBleedoutResistance();

    /**
     * Calculates the amount of bleedout damage the entity will receive
     */
    float calculateBleedoutDamage();

    /**
     * Time the entity has spent bleeding out
     */
    int getBleedoutTime();


    /**
     * Total time it takes to revive this entity
     */
    int getRescueDuration();

    /**
     * Time spent rescuing this entity
     */
    int getRescueTimer();

    @Nullable
    ISavior getSavior();

    default boolean isBeingRescued() {
        return getSavior() != null;
    }

    /**
     * Increases timer values by one. Called once per tick.
     */
    void updateTimers();

    void startRescue(ISavior savior);

    void stopRescue();
}
