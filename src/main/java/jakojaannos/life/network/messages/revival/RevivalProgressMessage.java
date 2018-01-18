package jakojaannos.life.network.messages.revival;

import io.netty.buffer.ByteBuf;
import jakojaannos.life.api.revival.capability.IRevivable;
import jakojaannos.life.init.ModCapabilities;
import jakojaannos.life.network.messages.ClientMessageHandler;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class RevivalProgressMessage implements IMessage {
    private int targetEntityId;
    private float progress;

    public RevivalProgressMessage() {
        this(-1, 0.0f);
    }

    public RevivalProgressMessage(int targetEntityId, float progress) {
        this.targetEntityId = targetEntityId;
        this.progress = progress;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        targetEntityId = buf.readInt();
        progress = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(targetEntityId);
        buf.writeFloat(progress);
    }

    public static class Handler extends ClientMessageHandler<RevivalProgressMessage> {
        @Override
        public void onMessage(RevivalProgressMessage message) {
            getMainThread().addScheduledTask(() -> {
                if (message.targetEntityId != -1) {
                    final World world = getPlayerEntity().getEntityWorld();
                    final Entity target = world.getEntityByID(message.targetEntityId);
                    if (target != null) {
                        final IRevivable revivable = target.getCapability(ModCapabilities.REVIVABLE, null);
                        if (revivable != null) {
                            revivable.setRevivalProgress(message.progress);
                        }
                    }
                }
            });
        }
    }
}
