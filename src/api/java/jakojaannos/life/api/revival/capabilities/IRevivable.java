package jakojaannos.life.api.revival.capabilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;

/**
 * Capability for entities that can be revived by ISavior capable entities after they have died. Default implementation
 * requires the entity to have IBleedoutHandler and/or IUnconsciousHandler capabilities.
 *
 * Note that calling any ISavior methods in implementations has risk in resulting in recursion so don't do that.
 */
public interface IRevivable {
    String REGISTRY_KEY = "life:revive";


    /**
     * Total time it takes to revive this entity. Note that saviors define their own revival speed so this is not
     * necessarily measured in ticks.
     */
    int getRescueDuration();

    /**
     * Time spent rescuing this entity
     */
    float getRescueProgress();

    /**
     * Updates the rescue timer
     */
    void setRevivalProgress(float progress);


    /**
     * Get the savior who is currently rescuing this entity. Returns null if entity is currently not being rescued.
     */
    @Nullable
    ISavior getSavior();

    void startRescue(ISavior savior);

    void stopRescue();

    void finishRescue();


    /**
     * Can the given savior rescue this revivable
     */
    default boolean canBeRevivedBy(ISavior savior) {
        return true;
    }

    /**
     * Is this entity currently being revived
     */
    default boolean isBeingRevived() {
        return getSavior() != null;
    }


    /**
     * Gets the player entity this capability belongs to
     */
    EntityPlayer getPlayer();
}
