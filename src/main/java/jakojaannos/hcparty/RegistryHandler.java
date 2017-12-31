package jakojaannos.hcparty;

import jakojaannos.api.lib.IApiInstance;
import jakojaannos.hcparty.party.PartyManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class RegistryHandler {
    @SubscribeEvent
    public static void register(RegistryEvent.Register<IApiInstance> event) {
        event.getRegistry().register(new PartyManager().setRegistryName("hcparty:partymanager"));
    }
}
