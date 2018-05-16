package jakojaannos.life.entity;

import jakojaannos.life.api.entity.capability.IHealthTracker;
import jakojaannos.life.init.ModCapabilities;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class HealthTrackerEventHandler {
    // TODO: Might be better performance-wise to track health at living damage events and reset at entity constructing
    //      + avoids getting tracking getting called for all living entities instead every tick
    //      - breaks if mods cause damage that isn't tracked by living damage event
    //          -> (could be "fixed" by providing configurable option to enable this)
    //          -> (works by conditionally registering two separate handlers directly at init => option requires MC restart)
    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (!entity.hasCapability(ModCapabilities.HEALTH_TRACKER, null)) {
            return;
        }

        IHealthTracker tracker = entity.getCapability(ModCapabilities.HEALTH_TRACKER, null);
        if (tracker != null) {
            tracker.tick();

            if (tracker.getTimer() % tracker.getUpdateInterval() == 0) {
                tracker.addEntry(entity.getHealth());
            }
        }
    }
}
