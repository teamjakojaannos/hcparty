package jakojaannos.life.revival;

import jakojaannos.life.LIFe;
import jakojaannos.life.ModInfo;
import jakojaannos.life.api.entity.LIFePlayerAttributes;
import jakojaannos.life.api.revival.capability.IBleedoutHandler;
import jakojaannos.life.api.revival.capability.IRevivable;
import jakojaannos.life.api.revival.capability.IUnconsciousHandler;
import jakojaannos.life.api.revival.event.BleedoutEvent;
import jakojaannos.life.api.revival.event.UnconsciousEvent;
import jakojaannos.life.config.ModConfig;
import jakojaannos.life.init.ModCapabilities;
import jakojaannos.life.network.messages.revival.DieMessage;
import jakojaannos.life.network.messages.revival.FallUnconsciousMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@EventBusSubscriber
public class BleedoutEventHandler {
    private static final Logger LOGGER = LogManager.getLogger(ModInfo.MODID);

    /**
     * Handles redirecting damage taken to bleedout health
     * <p>
     * Receive at LOWEST as we apply the damage here to the bleedout health pool and want to be sure that all damage
     * modifications/cancellations are included
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.getEntityLiving().getHealth() <= 0 && event.getEntityLiving() instanceof EntityPlayerMP) {
            redirectDamage((EntityPlayerMP) event.getEntityLiving(), event.getAmount(), event.getSource());
        }
    }

    private static void redirectDamage(EntityPlayerMP player, float amount, DamageSource source) {
        IBleedoutHandler bleedoutHandler = player.getCapability(ModCapabilities.BLEEDOUT_HANDLER, null);
        if (bleedoutHandler == null || bleedoutHandler.getBleedoutHealth() <= 0.0f) {
            return;
        }

        if (player.isEntityInvulnerable(source)) {
            return;
        } else if (source.isFireDamage() && player.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
            return;
        } else if (source != IBleedoutHandler.DAMAGE_BLEEDOUT && player.hurtResistantTime > 0) {
            return;
        }

        float damageAmount = amount;
        damageAmount = ForgeHooks.onLivingHurt(player, source, damageAmount);
        if (damageAmount <= 0) {
            return;
        }

        damageAmount = applyBleedoutResistance(damageAmount, bleedoutHandler.getBleedoutResistance());
        damageAmount = ForgeHooks.onLivingDamage(player, source, damageAmount);

        if (damageAmount > 0.0f) {
            float health = bleedoutHandler.getBleedoutHealth();
            player.getCombatTracker().trackDamage(source, health, damageAmount);
            bleedoutHandler.setBleedoutHealth(health - damageAmount);
            player.hurtResistantTime = player.maxHurtResistantTime;

            if (bleedoutHandler.getBleedoutHealth() <= 0.0f) {
                fallUnconscious(player);
            }
        }
    }


    /**
     * Applies damage reduction to attacks done while bleeding out
     */
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.getSource().getTrueSource() instanceof EntityPlayer) {
            event.setAmount(applyBleedoutDamageReduction((EntityPlayer) event.getSource().getTrueSource(), event.getAmount()));
        }
    }

    private static float applyBleedoutDamageReduction(EntityPlayer player, float amount) {
        // Don't apply if the player is alive
        if (player.getHealth() > 0.0f) {
            return amount;
        }

        // Note: No need to clamp as reduction is always in range 0.0f...1.0f
        final float reduction = (float) player.getEntityAttribute(LIFePlayerAttributes.BLEEDOUT_DAMAGE_REDUCTION).getAttributeValue();
        return amount * (1.0f - reduction);
    }


    /**
     * Prevents players from damaging other entities while unconscious/dead
     */
    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        if (event.getEntityLiving().getHealth() <= 0.0f) {
            // Cancel the event if:
            //  1. bleedout attacks are disabled in configs
            //  2. bleedout handler cannot be found
            //  3. player has no bleedout health remaining
            IBleedoutHandler bleedoutHandler = event.getEntityPlayer().getCapability(ModCapabilities.BLEEDOUT_HANDLER, null);
            if (!ModConfig.revival.bleedout.allowAttackingWhileBleedingOut || (bleedoutHandler == null || bleedoutHandler.getBleedoutHealth() <= 0.0f)) {
                event.setCanceled(true);
            }
        }
    }


    /**
     * Handles transition between bleedout, unconsciousness and death
     */
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        // Only run during Phase.START on server-side
        if (event.side != Side.SERVER || event.phase != TickEvent.Phase.START) {
            return;
        }

        // Do nothing for alive players
        EntityPlayer player = event.player;
        if (player.getHealth() > 0.0f || player.isDead) {
            return;
        }

        IBleedoutHandler bleedoutHandler = player.getCapability(ModCapabilities.BLEEDOUT_HANDLER, null);
        if (bleedoutHandler != null) {
            // Run unconscious tick if player has fallen unconscious
            if (bleedoutHandler.getBleedoutHealth() <= 0.0f) {
                unconsciousTick(player);
                return;
            }

            bleedoutHandler.updateTimer();
            int timer = bleedoutHandler.getBleedoutTime();

            // Prevent applying damage if the config flag is set
            boolean canHurt = true;
            IRevivable revivable = player.getCapability(ModCapabilities.REVIVABLE, null);
            if (revivable != null && ModConfig.revival.revival.revivingPreventsBleedoutDamage) {
                canHurt = !revivable.isBeingRevived();
            }

            // Do damage instances at configured interval
            while (timer > ModConfig.revival.bleedout.damageInterval && canHurt) {
                timer -= ModConfig.revival.bleedout.damageInterval;

                float damage = ModConfig.revival.bleedout.damagePerInstance;
                float resistance = bleedoutHandler.getBleedoutResistance();
                BleedoutEvent.Damage damageEvent = new BleedoutEvent.Damage(player, bleedoutHandler, damage, resistance);
                MinecraftForge.EVENT_BUS.post(damageEvent);

                if (damageEvent.getDamage() > 0.0f) {
                    damage = applyBleedoutResistance(damageEvent.getDamage(), damageEvent.getResistance());
                    player.attackEntityFrom(IBleedoutHandler.DAMAGE_BLEEDOUT, damage);
                }
            }

            // If we have lost all of our bleedout health, fall unconscious
            if (bleedoutHandler.getBleedoutHealth() <= 0.0f) {
                fallUnconscious(player);
            }
        }
    }

    private static void fallUnconscious(EntityPlayer player) {
        LOGGER.info("{} is falling unconscious!", player.getDisplayNameString());

        IUnconsciousHandler unconsciousHandler = player.getCapability(ModCapabilities.UNCONSCIOUS_HANDLER, null);
        if (unconsciousHandler != null) {
            MinecraftForge.EVENT_BUS.post(new UnconsciousEvent.FallUnconscious(player, unconsciousHandler));
            LIFe.getNetman().sendToDimension(new FallUnconsciousMessage(player.getEntityId(), unconsciousHandler.getDuration()), player.dimension);
        }
    }

    private static float applyBleedoutResistance(float damage, float resistance) {
        final float clampedResistance = MathHelper.clamp(resistance, 0.0f, ModConfig.revival.bleedout.maxBleedoutResistance);
        final float actualDamage = damage * (1.0f - clampedResistance);

        return MathHelper.clamp(actualDamage, ModConfig.revival.bleedout.minDamage, ModConfig.revival.bleedout.maxDamage);
    }

    private static void unconsciousTick(EntityPlayer player) {
        IUnconsciousHandler unconsciousHandler = player.getCapability(ModCapabilities.UNCONSCIOUS_HANDLER, null);
        if (unconsciousHandler != null) {
            boolean skipTick = false;
            IRevivable revivable = player.getCapability(ModCapabilities.REVIVABLE, null);
            if (revivable != null && ModConfig.revival.revival.revivingPausesUnconsciousTimer) {
                skipTick = revivable.isBeingRevived();
            }

            if (!skipTick) {
                unconsciousHandler.updateTimer();
            }

            if (unconsciousHandler.getTimer() > unconsciousHandler.getDuration()) {
                LOGGER.info("{} is dying after being unconscious for too long!", player.getDisplayNameString());
                player.setDead();
                MinecraftForge.EVENT_BUS.post(new UnconsciousEvent.Died(player, unconsciousHandler));
                LIFe.getNetman().sendToDimension(new DieMessage(player.getEntityId()), player.dimension);
            }
        }
    }
}
