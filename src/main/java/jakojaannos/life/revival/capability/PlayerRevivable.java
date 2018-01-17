package jakojaannos.life.revival.capability;

import jakojaannos.life.ModInfo;
import jakojaannos.life.api.entity.LIFePlayerAttributes;
import jakojaannos.life.api.revival.capabilities.IBleedoutHandler;
import jakojaannos.life.api.revival.capabilities.IRevivable;
import jakojaannos.life.api.revival.capabilities.ISavior;
import jakojaannos.life.config.ModConfig;
import jakojaannos.life.init.ModCapabilities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public class PlayerRevivable implements IRevivable {
    private static final Logger LOGGER = LogManager.getLogger(ModInfo.MODID);

    private final EntityPlayer player;
    private float progress;
    private ISavior savior;

    public PlayerRevivable(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public int getRescueDuration() {
        return ModConfig.revival.revival.baseRevivalDuration;
    }

    @Override
    public float getRescueProgress() {
        return progress;
    }

    @Override
    public void setRevivalProgress(float progress) {
        this.progress = progress;
    }

    @Nullable
    @Override
    public ISavior getSavior() {
        return savior;
    }

    @Override
    public void startRescue(ISavior savior) {
        this.savior = savior;
        this.progress = 0.0f;
    }

    @Override
    public void stopRescue() {
        this.savior = null;
        this.progress = 0.0f;
    }

    @Override
    public void finishRescue() {
        this.savior = null;
        this.progress = 0.0f;

        if (!player.world.isRemote) {
            doRespawn((EntityPlayerMP) player);
        }
    }

    @Override
    public EntityPlayer getPlayer() {
        return player;
    }

    private void doRespawn(EntityPlayerMP player) {
        // Cache old spawn-point information to avoid messing with vanilla respawn
        // logic if some filthy casual plays without hardcore mode
        final BlockPos oldSpawn = player.getBedLocation();
        final Integer oldSpawnDimension = player.hasSpawnDimension() ? player.getSpawnDimension() : null;
        final boolean oldSpawnForced = player.isSpawnForced(player.dimension);

        // HACK:
        // By setting spawn dimension via setter, we can force "respawning" to the dimension player is currently in,
        // even in dimensions where WorldProvider#canSpawnHere is overridden to return false. This works only because
        // WorldProvider#getRespawnDimension is calling EntityPlayer#getSpawnDimension to determine fallback spawn
        // dimension, but that might not be the case for modded dimensions, however.
        player.setSpawnDimension(player.dimension);
        player.setSpawnPoint(player.getPosition(), true);

        // Spawn new player entity for the player
        MinecraftServer server = player.getServer();
        if (server == null) {
            LOGGER.error("Server instance was null while rescuing player!");
            return;
        }
        EntityPlayerMP newEntity = server.getPlayerList().recreatePlayerEntity(player, player.dimension, false);

        // Switch connection to use the new entity
        player.connection.player = newEntity;

        // Set stats
        newEntity.setHealth(getSpawnHealth(player));
        newEntity.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel());
        newEntity.getFoodStats().setFoodSaturationLevel(player.getFoodStats().getSaturationLevel());
        newEntity.experienceLevel = player.experienceLevel;
        newEntity.experienceTotal = player.experienceTotal;
        newEntity.experience = player.experience;

        // Copy inventory
        newEntity.inventory.copyInventory(player.inventory);

        // Cancel active "status effects" (burning, glowing, etc.)
        // TODO: Do we need to update flags here? .extinguish() doesn't seem to set them?
        //      -   potentially dangerous? AFAIK flags is meant to be server authored and automagically synchronized
        //          using dataManger
        //      -   Does NOT setting the flags cause any negative side-effects like clientside rendering burning entity
        //          when the entity is already extinguished etc?
        newEntity.extinguish();
        newEntity.setGlowing(false);


        // Restore old spawn point info
        newEntity.setSpawnPoint(oldSpawn, oldSpawnForced);
        newEntity.setSpawnDimension(oldSpawnDimension);
    }

    private static float getSpawnHealth(EntityPlayer player) {
        final float scaledMaxHealth = calculateScaledMaxHealth(player);
        final float scaled = scaledMaxHealth * getClampedMaxHealthPercentage(player);

        final IBleedoutHandler bleedoutHandler = player.getCapability(ModCapabilities.BLEEDOUT_HANDLER, null);
        if (bleedoutHandler == null) {
            LOGGER.error("Could not find bleedout handler!");
        }
        final float timeScale = bleedoutHandler == null ? 1.0f : calculateTimeScale(bleedoutHandler);
        final float penalty = (bleedoutHandler != null && bleedoutHandler.getBleedoutHealth() <= 0.0f) ? ModConfig.revival.spawningHealth.unconsciousMultiplier : 1.0f;

        final float base = ModConfig.revival.spawningHealth.baseSpawnHealth;

        return base + (scaled * timeScale * penalty);
    }

    private static float calculateTimeScale(IBleedoutHandler handler) {
        final float min = ModConfig.revival.spawningHealth.timeScaleMin;
        final float max = ModConfig.revival.spawningHealth.timeScaleMax;
        final float percentage = handler.getBleedoutHealth() / handler.getMaxBleedoutHealth();

        return min + (max - min) * percentage;
    }

    private static float getClampedMaxHealthPercentage(EntityPlayer player) {
        final float raw = (float) player.getEntityAttribute(LIFePlayerAttributes.SPAWN_HEALTH_PERCENTAGE).getAttributeValue();
        return MathHelper.clamp(raw, ModConfig.revival.spawningHealth.maxHealthPercentageMin, ModConfig.revival.spawningHealth.maxHealthPercentageMax);
    }

    private static float calculateScaledMaxHealth(EntityPlayer player) {
        final float maxHealth = player.getMaxHealth();
        final float recentMaxHealth = maxHealth; // TODO: Calculate this

        return recentMaxHealth + (maxHealth - recentMaxHealth) * ModConfig.revival.spawningHealth.recentHealthScalingFactor;
    }
}
