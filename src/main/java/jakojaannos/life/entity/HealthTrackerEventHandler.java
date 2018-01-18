package jakojaannos.life.entity;

import jakojaannos.life.api.entity.capability.IHealthTracker;
import jakojaannos.life.init.ModCapabilities;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class HealthTrackerEventHandler {
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
