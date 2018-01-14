package jakojaannos.life.api.revival;

import javax.annotation.Nullable;

/**
 * Something that can revive others
 */
public interface ISavior {
    /**
     * Can this savior revive the target at (x,y,z)?
     */
    boolean canRevive(float x, float y, float z, IRevivable target);

    /**
     * Sets the current rescue target
     */
    void setTarget(IRevivable target);

    /**
     * Gets the current rescue target
     */
    @Nullable
    IRevivable getTarget();
}
