package jakojaannos.life.revival;

import jakojaannos.life.LIFe;
import jakojaannos.life.api.revival.capability.IBleedoutHandler;
import jakojaannos.life.api.revival.capability.IUnconsciousHandler;
import jakojaannos.life.api.revival.event.BleedoutEvent;
import jakojaannos.life.config.ModConfig;
import jakojaannos.life.init.ModCapabilities;
import jakojaannos.life.network.messages.revival.DownedMessage;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

// TODO: Block GuiGameOver opening (may generate a ton of garbage as new GUI instances get spawned every frame, consider some dirty workaround)


@EventBusSubscriber
public class DeathEventHandler {

    /**
     * HACK: Prevents player entity from getting removed due to deathTime
     * <p>
     * Vanilla death behavior removes living entities from the world 20 ticks after their death. This behavior is
     * controlled by {@link EntityLivingBase#deathTime}. In {@link EntityLivingBase#onDeathUpdate()}, deathTime is first
     * incremented by one and if it then equals 20, the entity is removed. By ensuring deathTime is never 19 when
     * entering the method we can avoid the entity getting removed.
     * <br>
     * Additionally, as rendering code uses death time to make the corpse fall down, we fix the death time value to 20
     * during end tick. We do this on both the client and the server for consistency's sake.
     */
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player.isDead) {
            return;
        }

        if (event.phase == TickEvent.Phase.START && event.player.getHealth() <= 0) {
            // 19th tick is the last tick, by incrementing the value to 20 vanilla will set the value to 21 which makes
            // the removal condition (deathTime == 20) return false, preventing the removal of the entity.
            if (event.player.deathTime == 19) {
                event.player.deathTime = 20; // It's never 20 if it's greater than 20
            }
        }

        // Fix the deathTime after the tick
        if (event.phase == TickEvent.Phase.END && event.player.getHealth() <= 0) {
            // Skipping the 19th tick works great for preventing removal, but messes up the rendering (which uses
            // deathTime to calculate rotation for the death 'animation'). To fix that, clamp the value to 20 during
            // post-tick so that rendering will use value of 20, while vanilla will again increment the value to 21
            // during next tick, preventing entity removal.
            if (event.player.deathTime > 20) {
                event.player.deathTime = 20;
            }
        }
    }


    /**
     * Handles entering bleedout instead of dying
     * Resets the unconscious handler so that it kicks in when player bleeds out
     * Handles bleedout counter
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingDeath(LivingDeathEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (entity.world.isRemote) {
            return;
        }

        // Use vanilla behavior if entity is not player or damage was caused from falling into the void
        if (!(entity instanceof EntityPlayerMP) || event.getSource() == DamageSource.OUT_OF_WORLD) {
            return;
        }

        IBleedoutHandler bleedoutHandler = entity.getCapability(ModCapabilities.BLEEDOUT_HANDLER, null);
        IUnconsciousHandler unconsciousHandler = entity.getCapability(ModCapabilities.UNCONSCIOUS_HANDLER, null);

        // Let the event pass if we should be dead already
        if (bleedoutHandler == null || unconsciousHandler == null || unconsciousHandler.getTimer() == unconsciousHandler.getDuration()) {
            return;
        }

        int bleedoutCount = bleedoutHandler.getBleedoutCount() + 1;

        // If bleedout counter is capped out, die instantly instead of going down
        if (bleedoutCount > bleedoutHandler.getBleedoutCounterMax()) {
            bleedoutHandler.setBleedoutHealth(0);
            unconsciousHandler.setTimer(unconsciousHandler.getDuration());
        }
        // Enter bleedout
        else {
            // Increase counter and set health
            bleedoutHandler.setBleedoutCount(bleedoutCount);
            bleedoutHandler.setBleedoutHealth(bleedoutHandler.getMaxBleedoutHealth());
            bleedoutHandler.resetTimer();

            unconsciousHandler.resetTimer();

            // Cancel the event to prevent the player from dying
            event.setCanceled(true);

            // Handle "death" messages
            EntityPlayerMP player = (EntityPlayerMP) entity;
            if (ModConfig.revival.sendVanillaDeathMessage) {
                sendDeathMessage(player, player.getCombatTracker().getDeathMessage());
            } else {
                sendDeathMessage(player, new TextComponentTranslation("downed.generic", player.getDisplayName()));
            }

            // Make stuff glow like Litvinenko's breakfast
            if (ModConfig.revival.renderOutlines) {
                player.setGlowing(true);
            }

            MinecraftForge.EVENT_BUS.post(new BleedoutEvent.Downed(player, bleedoutHandler));
            LIFe.getNetman().sendToDimension(new DownedMessage(player.getEntityId()), player.dimension);
        }
    }

    private static void sendDeathMessage(EntityPlayerMP player, ITextComponent message) {
        final Team team = player.getTeam();
        final PlayerList playerList = player.mcServer.getPlayerList();
        if (team != null && team.getDeathMessageVisibility() != Team.EnumVisible.ALWAYS) {
            if (team.getDeathMessageVisibility() == Team.EnumVisible.HIDE_FOR_OTHER_TEAMS) {
                playerList.sendMessageToAllTeamMembers(player, message);
            } else if (team.getDeathMessageVisibility() == Team.EnumVisible.HIDE_FOR_OWN_TEAM) {
                playerList.sendMessageToTeamOrAllPlayers(player, message);
            }
        } else {
            playerList.sendMessage(message);
        }
    }
}
