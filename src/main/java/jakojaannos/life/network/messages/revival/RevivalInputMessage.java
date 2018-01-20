package jakojaannos.life.network.messages.revival;

import io.netty.buffer.ByteBuf;
import jakojaannos.life.api.revival.capability.ISavior;
import jakojaannos.life.init.ModCapabilities;
import jakojaannos.life.network.messages.ServerMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RevivalInputMessage implements IMessage {
    private boolean tryingToInteract;

    public RevivalInputMessage() {
        this(false);
    }

    public RevivalInputMessage(boolean tryingToInteract) {
        this.tryingToInteract = tryingToInteract;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.tryingToInteract = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(tryingToInteract);
    }


    public static class Handler extends ServerMessageHandler<RevivalInputMessage> {
        @Override
        public IMessage onMessage(RevivalInputMessage message, MessageContext ctx) {
            getMainThread(ctx).addScheduledTask(() -> {
                ISavior savior = getSenderEntity(ctx).getCapability(ModCapabilities.SAVIOR, null);
                if (savior != null) {
                    savior.setTryingToRevive(message.tryingToInteract);
                }
            });

            return null;
        }
    }
}
