package jakojaannos.life.init;

import jakojaannos.life.api.revival.IRevivable;
import jakojaannos.life.revival.capability.RevivableProvider;
import jakojaannos.life.revival.capability.RevivableStorage;
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

    public static void initCapabilities() {
        CapabilityManager.INSTANCE.register(
                IRevivable.class,
                new RevivableStorage(),
                () -> {
                    throw new IllegalStateException("Default instance factory not supported!");
                });
    }

    @SubscribeEvent
    public static void onCapabilityInject(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getObject();
            event.addCapability(new ResourceLocation(IRevivable.REGISTRY_NAME), new RevivableProvider(player));
        }
    }
}
