package jakojaannos.life.revival.capability;

import jakojaannos.life.api.entity.LIFePlayerAttributes;
import jakojaannos.life.api.revival.capabilities.IBleedoutHandler;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerBleedoutHandler implements IBleedoutHandler {
    private final EntityPlayer player;

    private float health;
    private int bleedoutCounter;
    private int timer;

    public PlayerBleedoutHandler(EntityPlayer player) {
        this.player = player;
        this.health = 0.0f;
        this.bleedoutCounter = 0;
        this.timer = 0;
    }

    @Override
    public int getBleedoutCount() {
        return bleedoutCounter;
    }

    @Override
    public void setBleedoutCount(int count) {
        this.bleedoutCounter = Math.min(count, getBleedoutCounterMax());
    }

    @Override
    public int getBleedoutCounterMax() {
        return (int) player.getEntityAttribute(LIFePlayerAttributes.BLEEDOUT_COUNTER_MAX).getAttributeValue();
    }

    @Override
    public float getMaxBleedoutHealth() {
        return (float) player.getEntityAttribute(LIFePlayerAttributes.BLEEDOUT_MAX_HEALTH).getAttributeValue();
    }

    @Override
    public float getBleedoutHealth() {
        return health;
    }

    @Override
    public void setBleedoutHealth(float health) {
        this.health = health;
    }

    @Override
    public float getBleedoutResistance() {
        return (float) player.getEntityAttribute(LIFePlayerAttributes.BLEEDOUT_RESISTANCE).getAttributeValue();
    }

    @Override
    public int getBleedoutTime() {
        return timer;
    }

    @Override
    public void setBleedoutTime(int time) {
        this.timer = time;
    }
}
