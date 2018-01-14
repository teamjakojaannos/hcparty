package jakojaannos.life.revival;

import com.google.common.base.Preconditions;
import jakojaannos.life.ModInfo;
import jakojaannos.life.api.revival.IRevivable;
import jakojaannos.life.config.ModConfig;
import jakojaannos.life.init.ModCapabilities;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Handles player events relevant to bleedout states
 */
@EventBusSubscriber
public class BleedoutHandler {
    private static final Logger LOGGER = LogManager.getLogger(ModInfo.MODID);

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingDeath(LivingDeathEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (!(entity instanceof EntityPlayerMP) || IRevivable.INSTAKILL_SOURCES.contains(event.getSource())) {
            return;
        }

        // TODO: Requires a little tweaking to make player persist "alive" during the bleedout

        // Make sure player has the IRevivable capability
        IRevivable revivable = entity.getCapability(ModCapabilities.REVIVABLE, null);
        if (revivable == null) {
            LOGGER.error("IRevivable capability could not be found! Falling back to vanilla death behavior!");
            return;
        }

        // Cancel event to prevent the player from dying
        event.setCanceled(true);

        // Execute custom death behavior
        onPlayerDowned((EntityPlayerMP) entity, revivable);
    }

    private static void onPlayerDowned(EntityPlayerMP player, IRevivable revivable) {
        Preconditions.checkState(!player.getEntityWorld().isRemote, "Trying to run server logic on client!");

        // Handle "death" messages
        if (ModConfig.revival.sendVanillaDeathMessage) {
            sendDeathMessage(player, player.getCombatTracker().getDeathMessage());
        } else {
            sendDeathMessage(player, new TextComponentTranslation("downed.generic", player.getDisplayName()));
        }

        // Make stuff glow like Litvinenko's breakfast
        if (ModConfig.revival.renderOutlines) {
            player.setGlowing(true);
        }

        revivable.enterBleedout();
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

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (event.phase == TickEvent.Phase.START) {
            // Make sure the player has the IRevivable capability
            IRevivable revivable = player.getCapability(ModCapabilities.REVIVABLE, null);
            if (revivable == null) {
                LOGGER.error("IRevivable capability could not be found!");
                return;
            }

            final IRevivable.Status status = revivable.getStatus();

            // deathTime is increased by one each tick in EntityLivingBase#onDeathUpdate. When it reaches 20, entity is
            // marked dead and gets removed from the world. Limit deathTime to maximum of 19 in order to prevent the
            // entity from getting removed.
            // (Clamp the value to 18, vanilla increases it by one => value is 19 next time this executes)
            if (status != IRevivable.Status.ALIVE && status != IRevivable.Status.DEAD) {
                player.deathTime = Math.min(18, player.deathTime);
            }

            //
            if (!player.getEntityWorld().isRemote) {
                revivable.updateTimers();

                if (revivable.isBeingRescued()) {
                    handleRescue(player, revivable);
                }

                if (revivable.getStatus() == IRevivable.Status.BLEEDING_OUT) {
                    handleBleedingOut(player, revivable);
                } else if (revivable.getStatus() == IRevivable.Status.UNCONSCIOUS) {
                    handleUnconscious(player, revivable);
                }
            }
        }
    }

    private static void handleRescue(EntityPlayer player, IRevivable revivable) {

    }
}
