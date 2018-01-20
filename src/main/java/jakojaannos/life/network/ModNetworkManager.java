package jakojaannos.life.network;

import jakojaannos.life.network.messages.revival.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public final class ModNetworkManager {
    private final SimpleNetworkWrapper wrapper;

    public ModNetworkManager(String channelId) {
        this.wrapper = new SimpleNetworkWrapper(channelId);
        registerMessages();
    }

    private void registerMessages() {
        int index = 0;
        wrapper.registerMessage(RevivalProgressMessage.Handler.class, RevivalProgressMessage.class, index++, Side.CLIENT);
        wrapper.registerMessage(RevivalStartMessage.Handler.class, RevivalStartMessage.class, index++, Side.CLIENT);
        wrapper.registerMessage(RevivalFailedMessage.Handler.class, RevivalFailedMessage.class, index++, Side.CLIENT);
        wrapper.registerMessage(RevivalSuccessMessage.Handler.class, RevivalSuccessMessage.class, index++, Side.CLIENT);

        wrapper.registerMessage(DieMessage.Handler.class, DieMessage.class, index++, Side.CLIENT);
        wrapper.registerMessage(DownedMessage.Handler.class, DownedMessage.class, index++, Side.CLIENT);
        wrapper.registerMessage(FallUnconsciousMessage.Handler.class, FallUnconsciousMessage.class, index++, Side.CLIENT);

        wrapper.registerMessage(BleedoutHealthMessage.Handler.class, BleedoutHealthMessage.class, index++, Side.CLIENT);
        wrapper.registerMessage(BleedoutTimeMessage.Handler.class, BleedoutTimeMessage.class, index++, Side.CLIENT);

        wrapper.registerMessage(RevivalInputMessage.Handler.class, RevivalInputMessage.class, index++, Side.SERVER);
    }


    public void sendToAll(IMessage message) {
        wrapper.sendToAll(message);
    }

    public void sendTo(IMessage message, EntityPlayerMP player) {
        wrapper.sendTo(message, player);
    }

    public void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point) {
        wrapper.sendToAllAround(message, point);
    }

    public void sendToDimension(IMessage message, int dimensionId) {
        wrapper.sendToDimension(message, dimensionId);
    }

    public void sendToServer(IMessage message) {
        wrapper.sendToServer(message);
    }
}
