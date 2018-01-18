package jakojaannos.life.init;

import jakojaannos.life.api.entity.capability.IHealthTracker;
import jakojaannos.life.api.revival.capability.IBleedoutHandler;
import jakojaannos.life.api.revival.capability.IRevivable;
import jakojaannos.life.api.revival.capability.ISavior;
import jakojaannos.life.api.revival.capability.IUnconsciousHandler;
import jakojaannos.life.entity.capability.HealthTrackerProvider;
import jakojaannos.life.entity.capability.HealthTrackerStorage;
import jakojaannos.life.revival.capability.PlayerCapabilityProvider;
import jakojaannos.life.revival.capability.storage.BleedoutStorage;
import jakojaannos.life.revival.capability.storage.RevivableStorage;
import jakojaannos.life.revival.capability.storage.SaviorStorage;
import jakojaannos.life.revival.capability.storage.UnconsciousStorage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public final class ModCapabilities {
    @CapabilityInject(IRevivable.class)
    public static final Capability<IRevivable> REVIVABLE = null;

    @CapabilityInject(ISavior.class)
    public static final Capability<ISavior> SAVIOR = null;

    @CapabilityInject(IBleedoutHandler.class)
    public static final Capability<IBleedoutHandler> BLEEDOUT_HANDLER = null;

    @CapabilityInject(IUnconsciousHandler.class)
    public static final Capability<IUnconsciousHandler> UNCONSCIOUS_HANDLER = null;


    @CapabilityInject(IHealthTracker.class)
    public static final Capability<IHealthTracker> HEALTH_TRACKER = null;


    public static void initCapabilities() {
        CapabilityManager.INSTANCE.register(IRevivable.class, new RevivableStorage(), () -> null);
        CapabilityManager.INSTANCE.register(ISavior.class, new SaviorStorage(), () -> null);
        CapabilityManager.INSTANCE.register(IBleedoutHandler.class, new BleedoutStorage(), () -> null);
        CapabilityManager.INSTANCE.register(IUnconsciousHandler.class, new UnconsciousStorage(), () -> null);

        CapabilityManager.INSTANCE.register(IHealthTracker.class, new HealthTrackerStorage(), () -> null);
    }

    @SubscribeEvent
    public static void onCapabilityInject(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getObject();
            PlayerCapabilityProvider provider = new PlayerCapabilityProvider(player);
            event.addCapability(new ResourceLocation(IRevivable.REGISTRY_KEY), provider);
            event.addCapability(new ResourceLocation(ISavior.REGISTRY_KEY), provider);
            event.addCapability(new ResourceLocation(IBleedoutHandler.REGISTRY_KEY), provider);
            event.addCapability(new ResourceLocation(IUnconsciousHandler.REGISTRY_KEY), provider);

            event.addCapability(new ResourceLocation(IHealthTracker.REGISTRY_KEY), new HealthTrackerProvider());
        }
    }

}
