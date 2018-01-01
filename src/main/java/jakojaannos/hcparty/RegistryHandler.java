package jakojaannos.hcparty;

import jakojaannos.api.lib.IApiInstance;
import jakojaannos.hcparty.api.IInviteManager;
import jakojaannos.hcparty.api.IPartyManager;
import jakojaannos.hcparty.party.InviteManager;
import jakojaannos.hcparty.party.PartyManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class RegistryHandler {
    @SubscribeEvent
    public static void register(RegistryEvent.Register<IApiInstance> event) {
        event.getRegistry().register(PartyManager.INSTANCE);
        event.getRegistry().register(new InviteManager().setRegistryName(IInviteManager.REGISTRY_KEY));
    }
}
