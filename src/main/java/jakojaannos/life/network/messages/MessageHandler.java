package jakojaannos.life.network.messages;

import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class MessageHandler<TMessage extends IMessage> implements IMessageHandler<TMessage, IMessage> {
    protected final IThreadListener getMainThread(MessageContext ctx) {
        return ctx.side == Side.SERVER ? getServerMainThread(ctx) : getClientMainThread();
    }

    private IThreadListener getServerMainThread(MessageContext ctx) {
        return ctx.getServerHandler().player.getServerWorld();
    }

    @SideOnly(Side.CLIENT)
    private IThreadListener getClientMainThread() {
        return net.minecraft.client.Minecraft.getMinecraft();
    }
}
