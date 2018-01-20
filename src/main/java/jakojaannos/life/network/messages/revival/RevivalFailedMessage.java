package jakojaannos.life.network.messages.revival;

import io.netty.buffer.ByteBuf;
import jakojaannos.life.api.revival.capability.ISavior;
import jakojaannos.life.init.ModCapabilities;
import jakojaannos.life.network.messages.ClientMessageHandler;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RevivalFailedMessage implements IMessage {
    private int savior;

    public RevivalFailedMessage() {
        this(-1);
    }

    public RevivalFailedMessage(int savior) {
        this.savior = savior;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.savior = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(savior);
    }

    public static class Handler extends ClientMessageHandler<RevivalFailedMessage> {
        @Override
        public IMessage onMessage(RevivalFailedMessage message, MessageContext ctx) {
            getMainThread(ctx).addScheduledTask(() -> {
                final World world = getPlayerEntity().world;
                final Entity saviorEntity = world.getEntityByID(message.savior);

                if (saviorEntity != null) {
                    final ISavior savior = saviorEntity.getCapability(ModCapabilities.SAVIOR, null);
                    if (savior != null) {
                        if (savior.getTarget() != null) {
                            savior.getTarget().stopRescue();
                        }

                        savior.reviveFailed();
                    }
                }
            });

            return null;
        }
    }
}
