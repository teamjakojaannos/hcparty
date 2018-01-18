package jakojaannos.life.revival.capability;

import jakojaannos.life.api.entity.LIFePlayerAttributes;
import jakojaannos.life.api.revival.capability.IRevivable;
import jakojaannos.life.api.revival.capability.ISavior;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;

public class PlayerSavior implements ISavior {
    private final EntityPlayer player;

    private @Nullable IRevivable target;
    private boolean tryingToRevive;

    public PlayerSavior(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public void startRevive(IRevivable target) {
        this.target = target;
    }

    @Override
    public void reviveSuccess() {
        this.target = null;
    }

    @Override
    public void reviveFailed() {
        this.target = null;
    }

    @Override
    public float getRevivalSpeed() {
        return (float)player.getEntityAttribute(LIFePlayerAttributes.REVIVAL_SPEED).getAttributeValue();
    }

    @Nullable
    @Override
    public IRevivable getTarget() {
        return target;
    }

    @Override
    public boolean isTryingToRevive() {
        return tryingToRevive;
    }

    @Override
    public void setTryingToRevive(boolean state) {
        this.tryingToRevive = state;
    }

    @Override
    public EntityPlayer getPlayer() {
        return player;
    }
}
