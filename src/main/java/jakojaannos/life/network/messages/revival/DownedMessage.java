package jakojaannos.life.network.messages.revival;

import io.netty.buffer.ByteBuf;
import jakojaannos.life.api.revival.capability.IBleedoutHandler;
import jakojaannos.life.api.revival.event.BleedoutEvent;
import jakojaannos.life.init.ModCapabilities;
import jakojaannos.life.network.messages.ClientMessageHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class DownedMessage implements IMessage {
    private int entityId;

    public DownedMessage() {
        this(-1);
    }

    public DownedMessage(int entityId) {
        this.entityId = entityId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
    }

    public static class Handler extends ClientMessageHandler<DownedMessage> {
        @Override
        public IMessage onMessage(DownedMessage message, MessageContext ctx) {
            getMainThread(ctx).addScheduledTask(() -> {
                Entity entity = getPlayerEntity().world.getEntityByID(message.entityId);
                if (entity != null && entity instanceof EntityPlayer) {
                    IBleedoutHandler bleedoutHandler = entity.getCapability(ModCapabilities.BLEEDOUT_HANDLER, null);
                    MinecraftForge.EVENT_BUS.post(new BleedoutEvent.Downed((EntityPlayer) entity, bleedoutHandler));
                }
            });

            return null;
        }
    }
}
