package jakojaannos.life.api.revival.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class BleedoutEvent extends Event {
    /**
     * Fired when damage for bleedout damage instance is calculated
     */
    public static class Damage extends BleedoutEvent {
        private final float originalDamage;
        private float newDamage;

        public float getOriginalDamage() {
            return originalDamage;
        }

        public float getNewDamage() {
            return newDamage;
        }

        public void setNewDamage(float newDamage) {
            this.newDamage = newDamage;
        }

        public Damage(float originalDamage) {
            this.originalDamage = originalDamage;
            this.newDamage = originalDamage;
        }
    }
}
