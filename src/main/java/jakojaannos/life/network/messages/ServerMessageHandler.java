package jakojaannos.life.network.messages;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class ServerMessageHandler<TMessage extends IMessage> extends MessageHandler<TMessage> {
    protected final EntityPlayerMP getSenderEntity() {
        return getContext().getServerHandler().player;
    }
}
