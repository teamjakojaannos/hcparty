package jakojaannos.life.revival.capability;

import jakojaannos.life.LIFe;
import jakojaannos.life.api.entity.LIFePlayerAttributes;
import jakojaannos.life.api.revival.capability.IBleedoutHandler;
import jakojaannos.life.config.ModConfig;
import jakojaannos.life.network.messages.revival.BleedoutHealthMessage;
import jakojaannos.life.network.messages.revival.BleedoutTimeMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.MathHelper;

public class PlayerBleedoutHandler implements IBleedoutHandler {
    private final EntityPlayer player;

    private float health;
    private int bleedoutCounter;
    private int timer;

    PlayerBleedoutHandler(EntityPlayer player) {
        this.player = player;
        this.health = getMaxBleedoutHealth();
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
        return MathHelper.clamp(
                (float) player.getEntityAttribute(LIFePlayerAttributes.BLEEDOUT_MAX_HEALTH).getAttributeValue(),
                ModConfig.revival.bleedout.minHealth,
                ModConfig.revival.bleedout.maxHealth);
    }

    @Override
    public float getBleedoutHealth() {
        return health;
    }

    @Override
    public void setBleedoutHealth(float health) {
        this.health = health;

        if (!player.world.isRemote) {
            LIFe.getNetman().sendTo(new BleedoutHealthMessage(health), (EntityPlayerMP) player);
        }
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
        if (!player.world.isRemote) {
            LIFe.getNetman().sendTo(new BleedoutTimeMessage(this.timer), (EntityPlayerMP) player);
        }
    }
}
