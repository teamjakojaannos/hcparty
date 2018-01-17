package jakojaannos.life.revival;

import com.google.common.base.Preconditions;
import jakojaannos.life.LIFe;
import jakojaannos.life.api.revival.capabilities.IRevivable;
import jakojaannos.life.api.revival.capabilities.ISavior;
import jakojaannos.life.api.revival.event.RevivableEvent;
import jakojaannos.life.config.ModConfig;
import jakojaannos.life.init.ModCapabilities;
import jakojaannos.life.network.messages.revival.RevivalFailedMessage;
import jakojaannos.life.network.messages.revival.RevivalProgressMessage;
import jakojaannos.life.network.messages.revival.RevivalStartMessage;
import jakojaannos.life.network.messages.revival.RevivalSuccessMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Optional;

/**
 * Handles processing rescue-states
 */
@EventBusSubscriber
public class RescueEventHandler {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.side == Side.SERVER) {
            onUpdate(event.player);
        }
    }

    private static void onUpdate(EntityPlayer entity) {
        Preconditions.checkState(entity.world.isRemote, "RescueEventHandler::onUpdate() should not execute on client!");

        ISavior savior = entity.getCapability(ModCapabilities.SAVIOR, null);
        if (savior != null) {
            final boolean revivalInputState = savior.isTryingToRevive();

            // Input active, reviving and eligible to continue
            if (revivalInputState && savior.isReviving() && canRevive(savior, savior.getTarget())) {
                handleRescueTick(savior, savior.getTarget());
            }
            // Input active but not currently reviving
            else if (revivalInputState && !savior.isReviving()) {
                // Find new target
                IRevivable target = findTargetFor(savior.getPlayer());
                if (target != null) {
                    savior.startRevive(target);
                    target.startRescue(savior);
                    LIFe.getNetman().sendToDimension(new RevivalStartMessage(savior.getPlayer().getEntityId(), target.getPlayer().getEntityId()), savior.getPlayer().dimension);

                    MinecraftForge.EVENT_BUS.post(new RevivableEvent.Rescue.Start(savior, target));
                    handleRescueTick(savior, target);
                }
            } else {
                if (savior.getTarget() != null) {
                    savior.getTarget().stopRescue();
                }
                savior.reviveFailed();

                MinecraftForge.EVENT_BUS.post(new RevivableEvent.Rescue.Interrupted(savior, savior.getTarget()));
                LIFe.getNetman().sendToDimension(new RevivalFailedMessage(savior.getPlayer().getEntityId()), savior.getPlayer().dimension);
            }
        }
    }

    @Nullable
    private static IRevivable findTargetFor(EntityPlayer player) {
        final Vec3d start = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        final Vec3d end = start.add(player.getLookVec().scale(ModConfig.revival.revival.maxRevivalRange));
        final RayTraceResult result = player.world.rayTraceBlocks(start, end, false, true, false);
        if (result == null) {
            return null;
        }

        Optional<EntityPlayer> target = player.world.getPlayers(EntityPlayer.class, p -> p != null && p != player && p.dimension == player.dimension)
                .stream()
                .filter(RescueEventHandler::isDeadAndRevivable)
                .filter(e -> player.getDistanceSq(e) < ModConfig.revival.revival.maxRevivalRange * ModConfig.revival.revival.maxRevivalRange)
                .sorted(Comparator.comparingDouble(player::getDistanceSq))
                .findFirst();

        return target.isPresent() ? target.get().getCapability(ModCapabilities.REVIVABLE, null) : null;
    }

    private static boolean isDeadAndRevivable(EntityPlayer player) {
        return player.getHealth() <= 0.0f && player.hasCapability(ModCapabilities.REVIVABLE, null);
    }

    private static boolean canRevive(ISavior savior, IRevivable target) {
        Preconditions.checkState(savior.isReviving() && savior.isTryingToRevive());
        Preconditions.checkNotNull(savior.getTarget());

        // Savior must be alive and the target dead
        if (!savior.getPlayer().isEntityAlive() || target.getPlayer().isEntityAlive()) {
            return false;
        }

        // Check that savior/revivable implementations don't impose any special rules that prevent revival
        if (savior.canRevive(target) && target.canBeRevivedBy(savior)) {
            // Make sure the target is in range
            final float rangeSq = ModConfig.revival.revival.maxRevivalRange;
            if (savior.getPlayer().getDistanceSq(target.getPlayer()) < rangeSq) {
                return true;
            }
        }

        return false;
    }

    private static void handleRescueTick(ISavior savior, IRevivable revivable) {
        Preconditions.checkState(savior.getTarget() == revivable, "Handling rescue tick for an invalid rescue target!");

        // Update progress
        revivable.setRevivalProgress(revivable.getRescueProgress() + savior.getRevivalSpeed());

        // Check if the timer has finished and the revivable should be rescued
        final EntityPlayer entity = savior.getPlayer();
        if (revivable.getRescueProgress() >= revivable.getRescueDuration()) {
            revivable.finishRescue();
            savior.reviveSuccess();
            MinecraftForge.EVENT_BUS.post(new RevivableEvent.Rescue.Success(savior, revivable));
            LIFe.getNetman().sendToDimension(new RevivalSuccessMessage(entity.getEntityId()), entity.dimension);
        } else {
            LIFe.getNetman().sendToDimension(new RevivalProgressMessage(entity.getEntityId(), revivable.getRescueProgress()), entity.dimension);
        }
    }
}
