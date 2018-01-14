package jakojaannos.life.api.revival.event;

import jakojaannos.life.api.revival.IRevivable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class RevivalEvent extends Event {
    private final EntityPlayer player;
    private final IRevivable revivable;

    public EntityPlayer getPlayer() {
        return player;
    }

    public IRevivable getRevivable() {
        return revivable;
    }

    private RevivalEvent(EntityPlayer player, IRevivable revivable) {
        this.player = player;
        this.revivable = revivable;
    }

    /**
     * Fired when player health reaches zero and player would normally die
     */
    public static class Downed extends RevivalEvent {
        public Downed(EntityPlayer player, IRevivable revivable) {
            super(player, revivable);
        }
    }

    /**
     * Fired when player bleedout health reaches zero and player falls unconscious
     */
    public static class Unconscious extends RevivalEvent {
        public Unconscious(EntityPlayer player, IRevivable revivable) {
            super(player, revivable);
        }
    }

    /**
     * Fired when unconscious timer reaches zero and player dies
     */
    public static class Died extends RevivalEvent {
        public Died(EntityPlayer player, IRevivable revivable) {
            super(player, revivable);
        }
    }

    public abstract static class Rescue extends RevivalEvent {
        private final EntityPlayer savior;

        private Rescue(EntityPlayer player, IRevivable revivable, EntityPlayer savior) {
            super(player, revivable);
            this.savior = savior;
        }

        /**
         * Fired when a player begins rescuing a downed/unconscious player
         */
        public static class Start extends Rescue {
            public Start(EntityPlayer player, IRevivable revivable, EntityPlayer savior) {
                super(player, revivable, savior);
            }
        }

        /**
         * Fired when a player succeeds rescuing a downed/unconscious player
         */
        public static class Success extends Rescue {
            public Success(EntityPlayer player, IRevivable revivable, EntityPlayer savior) {
                super(player, revivable, savior);
            }
        }

        /**
         * Fired when rescuing is interrupted stopped
         */
        public static class Interrupted extends Rescue {
            private Interrupted(EntityPlayer player, IRevivable revivable, EntityPlayer savior) {
                super(player, revivable, savior);
            }
        }
    }
}
