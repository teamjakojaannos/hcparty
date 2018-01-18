package jakojaannos.life.network.messages.revival;

import io.netty.buffer.ByteBuf;
import jakojaannos.life.api.revival.capability.IRevivable;
import jakojaannos.life.api.revival.capability.ISavior;
import jakojaannos.life.init.ModCapabilities;
import jakojaannos.life.network.messages.ClientMessageHandler;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class RevivalStartMessage implements IMessage {
    private int savior;
    private int target;

    public RevivalStartMessage() {
        this(-1, -1);
    }

    public RevivalStartMessage(int savior, int target) {
        this.savior = savior;
        this.target = target;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.savior = buf.readInt();
        this.target = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(savior);
        buf.writeInt(target);
    }

    public static class Handler extends ClientMessageHandler<RevivalStartMessage> {
        @Override
        protected void onMessage(RevivalStartMessage message) {
            getMainThread().addScheduledTask(() -> {
                final World world = getPlayerEntity().world;
                final Entity saviorEntity = world.getEntityByID(message.savior);
                final Entity targetEntity = world.getEntityByID(message.target);

                if (saviorEntity != null && targetEntity != null) {
                    final ISavior savior = saviorEntity.getCapability(ModCapabilities.SAVIOR, null);
                    final IRevivable target = targetEntity.getCapability(ModCapabilities.REVIVABLE, null);
                    if (savior != null && target != null) {
                        savior.startRevive(target);
                        target.startRescue(savior);
                    }
                }
            });
        }
    }
}
