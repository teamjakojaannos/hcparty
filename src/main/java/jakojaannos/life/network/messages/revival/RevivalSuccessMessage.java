package jakojaannos.life.network.messages.revival;

import io.netty.buffer.ByteBuf;
import jakojaannos.life.api.revival.capability.ISavior;
import jakojaannos.life.init.ModCapabilities;
import jakojaannos.life.network.messages.ClientMessageHandler;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RevivalSuccessMessage implements IMessage {
    private int savior;

    public RevivalSuccessMessage() {
        this(-1);
    }

    public RevivalSuccessMessage(int savior) {
        this.savior = savior;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        savior = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(savior);
    }

    public static class Handler extends ClientMessageHandler<RevivalSuccessMessage> {
        @Override
        public IMessage onMessage(RevivalSuccessMessage message, MessageContext ctx) {
            getMainThread(ctx).addScheduledTask(() -> {
                Entity entity = getPlayerEntity().world.getEntityByID(message.savior);
                if (entity != null) {
                    ISavior savior = entity.getCapability(ModCapabilities.SAVIOR, null);
                    if (savior != null) {
                        if (savior.getTarget() != null) {
                            savior.getTarget().finishRescue();
                        }
                        savior.reviveSuccess();
                    }
                }
            });

            return null;
        }
    }
}
