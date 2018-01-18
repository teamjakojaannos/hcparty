package jakojaannos.life.revival;

import jakojaannos.life.LIFe;
import jakojaannos.life.api.revival.capability.IBleedoutHandler;
import jakojaannos.life.api.revival.capability.IRevivable;
import jakojaannos.life.api.revival.capability.IUnconsciousHandler;
import jakojaannos.life.api.revival.event.BleedoutEvent;
import jakojaannos.life.api.revival.event.UnconsciousEvent;
import jakojaannos.life.config.ModConfig;
import jakojaannos.life.init.ModCapabilities;
import jakojaannos.life.network.messages.revival.DieMessage;
import jakojaannos.life.network.messages.revival.FallUnconsciousMessage;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber
public class BleedoutEventHandler {
    /**
     * Handles redirecting damage taken to bleedout health
     * <p>
     * Receive at LOWEST as we apply the damage here to the bleedout health pool and want to be sure that all damage
     * modifications/cancellations are included
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingAttack(LivingAttackEvent event) {
        final EntityLivingBase entityLiving = event.getEntityLiving();
        if (entityLiving.world.isRemote) {
            return;
        }

        if (entityLiving instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) entityLiving;
            IBleedoutHandler bleedoutHandler = player.getCapability(ModCapabilities.BLEEDOUT_HANDLER, null);
            if (bleedoutHandler != null) {
                final DamageSource source = event.getSource();

                if (player.isEntityInvulnerable(source)) {
                    return;
                } else if (source.isFireDamage() && player.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
                    return;
                }

                float damageAmount = event.getAmount();
                damageAmount = ForgeHooks.onLivingHurt(player, event.getSource(), damageAmount);
                if (damageAmount <= 0) return;

                damageAmount = applyBleedoutResistance(damageAmount, bleedoutHandler.getBleedoutResistance());

                damageAmount = ForgeHooks.onLivingDamage(player, event.getSource(), damageAmount);

                if (damageAmount > 0.0f) {
                    float health = bleedoutHandler.getBleedoutHealth();
                    player.getCombatTracker().trackDamage(event.getSource(), health, damageAmount);
                    bleedoutHandler.setBleedoutHealth(health - event.getAmount());

                    if (bleedoutHandler.getBleedoutHealth() <= 0.0f) {
                        fallUnconscious(player);
                    }
                }
            }
        }
    }

    /**
     * Handles transition between bleedout, unconsciousness and death
     */
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START || event.side != Side.SERVER) {
            return;
        }
        EntityPlayer player = event.player;

        IBleedoutHandler bleedoutHandler = player.getCapability(ModCapabilities.BLEEDOUT_HANDLER, null);
        if (bleedoutHandler != null) {
            if (bleedoutHandler.getBleedoutHealth() <= 0.0f) {
                unconsciousTick(player);
                return;
            }

            bleedoutHandler.tickTimer();
            int timer = bleedoutHandler.getBleedoutTime();

            // Prevent applying damage if the config flag is set
            boolean canHurt = true;
            IRevivable revivable = player.getCapability(ModCapabilities.REVIVABLE, null);
            if (revivable != null && ModConfig.revival.revival.revivingPreventsBleedoutDamage) {
                canHurt = !revivable.isBeingRevived();
            }

            // Do damage instances at configured interval
            if (timer % ModConfig.revival.bleedout.damageInterval == 0 && canHurt) {
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
        IUnconsciousHandler unconsciousHandler = player.getCapability(ModCapabilities.UNCONSCIOUS_HANDLER, null);
        if (unconsciousHandler != null) {
            MinecraftForge.EVENT_BUS.post(new UnconsciousEvent.FallUnconscious(player, unconsciousHandler));
            LIFe.getNetman().sendToDimension(new FallUnconsciousMessage(player.getEntityId()), player.dimension);
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
                skipTick = !revivable.isBeingRevived();
            }

            if (!skipTick) {
                unconsciousHandler.tickTimer();
            }

            if (unconsciousHandler.getTimer() > unconsciousHandler.getDuration()) {
                player.setDead();
                MinecraftForge.EVENT_BUS.post(new UnconsciousEvent.Died(player, unconsciousHandler));
                LIFe.getNetman().sendToDimension(new DieMessage(player.getEntityId()), player.dimension);
            }
        }
    }
}
