package jakojaannos.life.client;

import com.google.common.base.Preconditions;
import jakojaannos.life.CommonProxy;
import jakojaannos.life.LIFe;
import jakojaannos.life.api.revival.capability.ISavior;
import jakojaannos.life.init.ModCapabilities;
import jakojaannos.life.network.messages.revival.RevivalInputMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber
public class ClientProxy extends CommonProxy {
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        Preconditions.checkState(event.side == Side.CLIENT, "Client tick executing on server!");
        if (event.phase != TickEvent.Phase.START) {
            return;
        }

        EntityPlayer player = Minecraft.getMinecraft().player;
        if (player.getHealth() <= 0.0f) {

        } else {
            ISavior savior = player.getCapability(ModCapabilities.SAVIOR, null);
            boolean tryingToInteract = Minecraft.getMinecraft().gameSettings.keyBindUseItem.isKeyDown();
            if (savior.isTryingToRevive() != tryingToInteract) {
                savior.setTryingToRevive(tryingToInteract);
                LIFe.getNetman().sendToServer(new RevivalInputMessage(tryingToInteract));
            }
        }


    }
}
