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
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class FallUnconsciousMessage implements IMessage {
    private int entityId;
    private int duration;

    public FallUnconsciousMessage() {
        this(-1, 0);
    }

    public FallUnconsciousMessage(int entityId, int duration) {
        this.entityId = entityId;
        this.duration = duration;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityId = buf.readInt();
        this.duration = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeInt(duration);
    }

    public static class Handler extends ClientMessageHandler<FallUnconsciousMessage> {
        @Override
        public IMessage onMessage(FallUnconsciousMessage message, MessageContext ctx) {
            getMainThread(ctx).addScheduledTask(() -> {
                Entity entity = getPlayerEntity().world.getEntityByID(message.entityId);
                if (entity != null && entity instanceof EntityPlayer) {
                    IUnconsciousHandler unconsciousHandler = entity.getCapability(ModCapabilities.UNCONSCIOUS_HANDLER, null);
                    if (unconsciousHandler != null) {
                        unconsciousHandler.setOrientation(entity.rotationYaw);
                        unconsciousHandler.setDuration(message.duration);
                        unconsciousHandler.resetTimer();
                        MinecraftForge.EVENT_BUS.post(new UnconsciousEvent.FallUnconscious((EntityPlayer) entity, unconsciousHandler));
                    }
                }
            });

            return null;
        }
    }
}
