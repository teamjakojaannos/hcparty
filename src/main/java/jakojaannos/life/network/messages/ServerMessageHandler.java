package jakojaannos.life.network.messages;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class ServerMessageHandler<TMessage extends IMessage> extends MessageHandler<TMessage> {
    protected final EntityPlayerMP getSenderEntity(MessageContext ctx) {
        return ctx.getServerHandler().player;
    }
}
