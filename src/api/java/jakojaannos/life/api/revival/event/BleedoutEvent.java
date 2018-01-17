package jakojaannos.life.api.revival.event;

import jakojaannos.life.api.revival.capabilities.IBleedoutHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class BleedoutEvent extends Event {
    private final EntityPlayer player;
    private final IBleedoutHandler bleedoutHandler;

    public EntityPlayer getPlayer() {
        return player;
    }

    public IBleedoutHandler getBleedoutHandler() {
        return bleedoutHandler;
    }

    private BleedoutEvent(EntityPlayer player, IBleedoutHandler bleedoutHandler) {

        this.player = player;
        this.bleedoutHandler = bleedoutHandler;
    }

    /**
     * Fired when damage for bleedout damage instance is calculated
     */
    public static class Damage extends BleedoutEvent {
        private final float originalDamage;
        private final float originalResistance;
        private float damage;
        private float resistance;

        public Damage(EntityPlayer player, IBleedoutHandler bleedoutHandler, float damage, float resistance) {
            super(player, bleedoutHandler);
            this.originalDamage = damage;
            this.originalResistance = resistance;

            this.damage = damage;
            this.resistance = resistance;
        }

        public float getOriginalDamage() {
            return originalDamage;
        }

        public float getOriginalResistance() {
            return originalResistance;
        }

        public float getDamage() {
            return damage;
        }

        public void setDamage(float damage) {
            this.damage = damage;
        }

        public float getResistance() {
            return resistance;
        }

        public void setResistance(float resistance) {
            this.resistance = resistance;
        }
    }

    /**
     * Fired when player enters bleedout
     */
    public static class Downed extends BleedoutEvent {
        public Downed(EntityPlayer player, IBleedoutHandler bleedoutHandler) {
            super(player, bleedoutHandler);
        }
    }
}
