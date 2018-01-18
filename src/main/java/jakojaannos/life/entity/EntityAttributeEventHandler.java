package jakojaannos.life.entity;

import jakojaannos.life.api.entity.LIFePlayerAttributes;
import jakojaannos.life.config.ModConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class EntityAttributeEventHandler {
    @SubscribeEvent
    public static void onEntityConstructing(EntityEvent.EntityConstructing event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof EntityPlayer)) {
            return;
        }

        // Register attributes
        EntityPlayer player = (EntityPlayer) entity;
        player.getAttributeMap().registerAttribute(LIFePlayerAttributes.BLEEDOUT_MAX_HEALTH);
        player.getAttributeMap().registerAttribute(LIFePlayerAttributes.BLEEDOUT_COUNTER_MAX);
        player.getAttributeMap().registerAttribute(LIFePlayerAttributes.BLEEDOUT_RESISTANCE);

        player.getAttributeMap().registerAttribute(LIFePlayerAttributes.SPAWN_HEALTH_PERCENTAGE);

        player.getAttributeMap().registerAttribute(LIFePlayerAttributes.REVIVAL_SPEED);


        // Apply default values from configs
        player.getEntityAttribute(LIFePlayerAttributes.BLEEDOUT_MAX_HEALTH).setBaseValue(ModConfig.revival.bleedout.defaultMaxHealth);
        player.getEntityAttribute(LIFePlayerAttributes.BLEEDOUT_COUNTER_MAX).setBaseValue(ModConfig.revival.bleedout.defaultBleedoutCounterMax);
        player.getEntityAttribute(LIFePlayerAttributes.BLEEDOUT_RESISTANCE).setBaseValue(ModConfig.revival.bleedout.defaultBleedoutResistance);

        player.getEntityAttribute(LIFePlayerAttributes.SPAWN_HEALTH_PERCENTAGE).setBaseValue(ModConfig.revival.spawningHealth.defaultMaxHealthPercentage);

        player.getEntityAttribute(LIFePlayerAttributes.REVIVAL_SPEED).setBaseValue(ModConfig.revival.revival.defaultRevivalSpeed);
    }
}
