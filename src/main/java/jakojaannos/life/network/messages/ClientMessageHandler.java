package jakojaannos.life.network.messages;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ClientMessageHandler<TMessage extends IMessage> extends MessageHandler<TMessage> {
    @SideOnly(Side.CLIENT)
    protected final net.minecraft.client.entity.EntityPlayerSP getPlayerEntity() {
        return net.minecraft.client.Minecraft.getMinecraft().player;
    }
}
