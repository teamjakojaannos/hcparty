package jakojaannos.life.network.messages.revival;

import io.netty.buffer.ByteBuf;
import jakojaannos.life.api.revival.capability.IBleedoutHandler;
import jakojaannos.life.init.ModCapabilities;
import jakojaannos.life.network.messages.ClientMessageHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BleedoutTimeMessage implements IMessage {
    private int time;

    public BleedoutTimeMessage() {
        this(0);
    }

    public BleedoutTimeMessage(int time) {
        this.time = time;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        time = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(time);
    }

    public static class Handler extends ClientMessageHandler<BleedoutTimeMessage> {
        @Override
        public IMessage onMessage(BleedoutTimeMessage message, MessageContext ctx) {
            getMainThread(ctx).addScheduledTask(() -> {
                EntityPlayerSP player = getPlayerEntity();
                if (player != null) {
                    IBleedoutHandler bleedoutHandler = player.getCapability(ModCapabilities.BLEEDOUT_HANDLER, null);
                    if (bleedoutHandler != null) {
                        bleedoutHandler.setBleedoutTime(message.time);
                    }
                }
            });
            return null;
        }
    }
}
