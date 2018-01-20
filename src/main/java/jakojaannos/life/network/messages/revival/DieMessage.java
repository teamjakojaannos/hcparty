package jakojaannos.life.network.messages.revival;

import io.netty.buffer.ByteBuf;
import jakojaannos.life.api.revival.capability.IUnconsciousHandler;
import jakojaannos.life.api.revival.event.UnconsciousEvent;
import jakojaannos.life.init.ModCapabilities;
import jakojaannos.life.network.messages.ClientMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class DieMessage implements IMessage {
    private int entityId;

    public DieMessage() {
        this(-1);
    }

    public DieMessage(int entityId) {
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

    public static class Handler extends ClientMessageHandler<DieMessage> {
        @Override
        public IMessage onMessage(DieMessage message, MessageContext ctx) {
            getMainThread(ctx).addScheduledTask(() -> {
                Entity entity = getPlayerEntity().world.getEntityByID(message.entityId);
                if (entity != null && entity instanceof EntityPlayer) {
                    IUnconsciousHandler unconsciousHandler = entity.getCapability(ModCapabilities.UNCONSCIOUS_HANDLER, null);
                    if (unconsciousHandler != null) {
                        unconsciousHandler.setTimer(unconsciousHandler.getDuration());
                        entity.setDead();
                        MinecraftForge.EVENT_BUS.post(new UnconsciousEvent.Died((EntityPlayer) entity, unconsciousHandler));
                        Minecraft.getMinecraft().displayGuiScreen(new GuiGameOver(new TextComponentTranslation("yeee.yeee.asd")));
                    }
                }
            });

            return null;
        }
    }
}
