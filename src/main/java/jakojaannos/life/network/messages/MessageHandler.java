package jakojaannos.life.network.messages;

import com.google.common.base.Preconditions;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class MessageHandler<TMessage extends IMessage> implements IMessageHandler<TMessage, IMessage> {
    private MessageContext ctx;

    protected MessageContext getContext() {
        Preconditions.checkNotNull(ctx);
        return ctx;
    }

    @Override
    public final IMessage onMessage(TMessage message, MessageContext ctx) {
        this.ctx = ctx;
        onMessage(message);
        this.ctx = null;
        return null;
    }

    protected abstract void onMessage(TMessage message);

    protected final IThreadListener getMainThread() {
        return this.ctx.side == Side.SERVER ? getServerMainThread() : getClientMainThread();
    }

    private IThreadListener getServerMainThread() {
        return this.ctx.getServerHandler().player.getServerWorld();
    }

    @SideOnly(Side.CLIENT)
    private IThreadListener getClientMainThread() {
        return net.minecraft.client.Minecraft.getMinecraft();
    }
}
