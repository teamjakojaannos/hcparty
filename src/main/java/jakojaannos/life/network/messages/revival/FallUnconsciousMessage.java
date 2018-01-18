package jakojaannos.life.network.messages.revival;

import io.netty.buffer.ByteBuf;
import jakojaannos.life.api.revival.capability.IUnconsciousHandler;
import jakojaannos.life.api.revival.event.UnconsciousEvent;
import jakojaannos.life.init.ModCapabilities;
import jakojaannos.life.network.messages.ClientMessageHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class FallUnconsciousMessage implements IMessage {
    private int entityId;

    public FallUnconsciousMessage() {
        this(-1);
    }

    public FallUnconsciousMessage(int entityId) {
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

    public static class Handler extends ClientMessageHandler<FallUnconsciousMessage> {
        @Override
        protected void onMessage(FallUnconsciousMessage message) {
            getMainThread().addScheduledTask(() -> {
                Entity entity = getPlayerEntity().world.getEntityByID(message.entityId);
                if (entity != null && entity instanceof EntityPlayer) {
                    IUnconsciousHandler unconsciousHandler = entity.getCapability(ModCapabilities.UNCONSCIOUS_HANDLER, null);
                    MinecraftForge.EVENT_BUS.post(new UnconsciousEvent.FallUnconscious((EntityPlayer) entity, unconsciousHandler));
                }
            });
        }
    }
}
