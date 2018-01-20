package jakojaannos.life.network.messages.revival;

import io.netty.buffer.ByteBuf;
import jakojaannos.life.api.revival.capability.IBleedoutHandler;
import jakojaannos.life.init.ModCapabilities;
import jakojaannos.life.network.messages.ClientMessageHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BleedoutHealthMessage implements IMessage {
    private float health;

    public BleedoutHealthMessage() {
        this(0.0f);
    }

    public BleedoutHealthMessage(float health) {
        this.health = health;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        health = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat(health);
    }

    public static class Handler extends ClientMessageHandler<BleedoutHealthMessage> {
        @Override
        public IMessage onMessage(BleedoutHealthMessage message, MessageContext ctx) {
            getMainThread(ctx).addScheduledTask(() -> {
                EntityPlayerSP player = getPlayerEntity();
                if (player != null) {
                    IBleedoutHandler bleedoutHandler = player.getCapability(ModCapabilities.BLEEDOUT_HANDLER, null);
                    if (bleedoutHandler != null) {
                        bleedoutHandler.setBleedoutHealth(message.health);
                    }
                }
            });
            return null;
        }
    }
}
