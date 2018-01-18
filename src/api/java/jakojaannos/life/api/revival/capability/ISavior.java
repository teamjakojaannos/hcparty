package jakojaannos.life.api.revival.capability;

import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;

/**
 * Capability for entities that can revive IRevivable capable entities
 */
public interface ISavior {
    String REGISTRY_KEY = "life:savior";

    void startRevive(IRevivable target);

    void reviveSuccess();

    void reviveFailed();

    /**
     * Gets the speed the savior can revive the current target at. Calling this with a null target is considered an
     * illegal state.
     */
    float getRevivalSpeed();

    /**
     * Gets the current rescue target
     */
    @Nullable
    IRevivable getTarget();

    /**
     * Does this savior have a target set
     */
    default boolean isReviving() {
        return getTarget() != null;
    }

    /**
     * Can this savior revive the target
     */
    default boolean canRevive(IRevivable target) {
        return true;
    }

    /**
     * Is this savior actively trying to revive some target. e.g. is player input active
     */
    boolean isTryingToRevive();

    void setTryingToRevive(boolean state);

    /**
     * Gets the player entity this capability belongs to
     */
    EntityPlayer getPlayer();
}
