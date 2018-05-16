package jakojaannos.life.client;

import jakojaannos.life.api.revival.capability.IBleedoutHandler;
import jakojaannos.life.api.revival.capability.IUnconsciousHandler;
import jakojaannos.life.init.ModCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
class GuiEventHandler {
    @SubscribeEvent
    public static void onOpenGui(GuiOpenEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (player != null && player.getHealth() <= 0.0f) {
            boolean isBleedingOut = false;
            boolean isUnconscious = false;
            boolean isDead = true;
            IBleedoutHandler bleedoutHandler = player.getCapability(ModCapabilities.BLEEDOUT_HANDLER, null);
            IUnconsciousHandler unconsciousHandler = player.getCapability(ModCapabilities.UNCONSCIOUS_HANDLER, null);
            if (bleedoutHandler != null && unconsciousHandler != null) {
                isBleedingOut = bleedoutHandler.getBleedoutHealth() > 0.0f;
                isUnconscious = !isBleedingOut && !unconsciousHandler.shouldBeDead();
                isDead = !isBleedingOut && !isUnconscious;
            }

            if (isDead && !isAllowedForDead(event.getGui())) {
                event.setGui(null);
            } else if (isBleedingOut && !isAllowedForBleedingOut(event.getGui())) {
                event.setGui(null);
            } else if (isUnconscious && !isAllowedForUnconscious(event.getGui())) {
                event.setGui(null);
            }
        }
    }

    private static boolean isAllowedForDead(Gui gui) {
        return gui instanceof GuiGameOver;
    }

    private static boolean isAllowedForBleedingOut(Gui gui) {
        return gui instanceof GuiChat || gui instanceof GuiIngame;
    }

    private static boolean isAllowedForUnconscious(Gui gui) {
        return gui instanceof GuiChat || gui instanceof GuiIngame;
    }
}
