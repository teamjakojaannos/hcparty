package jakojaannos.life.api.revival.event;

import jakojaannos.life.api.revival.capability.IRevivable;
import jakojaannos.life.api.revival.capability.ISavior;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class RevivableEvent extends Event {
    private final ISavior savior;
    private final IRevivable revivable;

    public ISavior getSavior() {
        return savior;
    }

    public IRevivable getRevivable() {
        return revivable;
    }

    private RevivableEvent(ISavior savior, IRevivable revivable) {
        this.savior = savior;
        this.revivable = revivable;
    }

    public abstract static class Rescue extends RevivableEvent {
        private Rescue(ISavior savior, IRevivable revivable) {
            super(savior, revivable);
        }

        /**
         * Fired when a player begins rescuing a downed/unconscious player
         */
        public static class Start extends Rescue {
            public Start(ISavior savior, IRevivable revivable) {
                super(savior, revivable);
            }
        }

        /**
         * Fired when a player succeeds rescuing a downed/unconscious player
         */
        public static class Success extends Rescue {
            public Success(ISavior savior, IRevivable revivable) {
                super(savior, revivable);
            }
        }

        /**
         * Fired when rescuing is interrupted/stopped
         */
        public static class Interrupted extends Rescue {
            public Interrupted(ISavior savior, IRevivable revivable) {
                super(savior, revivable);
            }
        }
    }
}
