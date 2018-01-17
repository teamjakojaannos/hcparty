package jakojaannos.life.network.messages.revival;

import io.netty.buffer.ByteBuf;
import jakojaannos.life.api.revival.capabilities.IBleedoutHandler;
import jakojaannos.life.api.revival.capabilities.IUnconsciousHandler;
import jakojaannos.life.api.revival.event.BleedoutEvent;
import jakojaannos.life.api.revival.event.UnconsciousEvent;
import jakojaannos.life.init.ModCapabilities;
import jakojaannos.life.network.messages.ClientMessageHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

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
        protected void onMessage(DownedMessage message) {
            getMainThread().addScheduledTask(() -> {
                Entity entity = getPlayerEntity().world.getEntityByID(message.entityId);
                if (entity != null && entity instanceof EntityPlayer) {
                    IBleedoutHandler bleedoutHandler = entity.getCapability(ModCapabilities.BLEEDOUT_HANDLER, null);
                    MinecraftForge.EVENT_BUS.post(new BleedoutEvent.Downed((EntityPlayer) entity, bleedoutHandler));
                }
            });
        }
    }
}
