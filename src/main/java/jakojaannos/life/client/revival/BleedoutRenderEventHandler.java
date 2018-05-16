package jakojaannos.life.client.revival;

import jakojaannos.life.api.revival.capability.IUnconsciousHandler;
import jakojaannos.life.init.ModCapabilities;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BleedoutRenderEventHandler {
    private static Map<UUID, Orientation> orientation = new HashMap<>();

    /**
     * Locks the render yaw of unconscious players so that they can't visually spin around while unconscious.
     * Sets the player yaw/head yaw before rendering to a fixed value from {@link IUnconsciousHandler} capability
     */
    @SubscribeEvent
    public static void onPreRender(RenderPlayerEvent.Pre event) {
        final EntityPlayer player = event.getEntityPlayer();
        final IUnconsciousHandler unconsciousHandler = player.getCapability(ModCapabilities.UNCONSCIOUS_HANDLER, null);
        if (unconsciousHandler == null) {
            return;
        }

        final UUID key = player.getUniqueID();
        if (!orientation.containsKey(key)) {
            orientation.put(key, new Orientation());
        }

        orientation.get(key).yaw = player.rotationYaw;
        orientation.get(key).yawCam = player.cameraYaw;
        orientation.get(key).yawHead = player.rotationYawHead;
        orientation.get(key).prevYaw = player.prevRotationYaw;
        orientation.get(key).prevYawCam = player.prevCameraYaw;
        orientation.get(key).prevYawHead = player.prevRotationYawHead;


        player.rotationYaw = unconsciousHandler.getOrientation();
        player.cameraYaw = unconsciousHandler.getOrientation();
        player.rotationYawHead = 0f;
        player.prevRotationYaw = unconsciousHandler.getOrientation();
        player.prevCameraYaw = unconsciousHandler.getOrientation();
        player.prevRotationYawHead = 0f;
    }

    /**
     * Restores the player yaw after rendering
     */
    @SubscribeEvent
    public static void onPostRender(RenderPlayerEvent.Post event) {
        final EntityPlayer player = event.getEntityPlayer();

        final UUID key = player.getUniqueID();
        if (!orientation.containsKey(key)) {
            return;
        }

        player.rotationYaw = orientation.get(key).yaw;
        player.cameraYaw = orientation.get(key).yawCam;
        player.rotationYawHead = orientation.get(key).yawHead;
        player.prevRotationYaw = orientation.get(key).prevYaw;
        player.prevRotationYawHead = orientation.get(key).prevYawHead;
        player.prevCameraYaw = orientation.get(key).prevYawCam;
    }

    private static class Orientation {
        float yaw;
        float yawCam;
        float yawHead;
        float prevYaw;
        float prevYawHead;
        float prevYawCam;
    }
}
