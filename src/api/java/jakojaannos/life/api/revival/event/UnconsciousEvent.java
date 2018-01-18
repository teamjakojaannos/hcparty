package jakojaannos.life.api.revival.event;

import jakojaannos.life.api.revival.capability.IUnconsciousHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class UnconsciousEvent extends Event {
    private final EntityPlayer player;
    private final IUnconsciousHandler unconsciousHandler;

    public EntityPlayer getPlayer() {
        return player;
    }

    public IUnconsciousHandler getUnconsciousHandler() {
        return unconsciousHandler;
    }

    private UnconsciousEvent(EntityPlayer player, IUnconsciousHandler unconsciousHandler) {
        this.player = player;
        this.unconsciousHandler = unconsciousHandler;
    }

    /**
     * Fired when player has about to fell unconscious
     */
    public static class FallUnconscious extends UnconsciousEvent {
        public FallUnconscious(EntityPlayer player, IUnconsciousHandler unconsciousHandler) {
            super(player, unconsciousHandler);
        }
    }

    /**
     * Fired when unconscious timer reaches zero and player dies
     */
    public static class Died extends UnconsciousEvent {
        public Died(EntityPlayer player, IUnconsciousHandler handler) {
            super(player, handler);
        }
    }
}
