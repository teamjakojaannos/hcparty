package jakojaannos.life.api.entity;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;

public final class LIFePlayerAttributes {
    public static final IAttribute BLEEDOUT_RESISTANCE = new RangedAttribute(null, "life.bleedoutResist", 0.0d, 0.0d, 1.0d).setDescription("Bleedout Resistance").setShouldWatch(true);
    public static final IAttribute BLEEDOUT_MAX_HEALTH = new RangedAttribute(null, "life.bleedoutMaxHealth", 10.0d, 0.0d, Double.MAX_VALUE).setDescription("Bleedout Max Health").setShouldWatch(true);
    public static final IAttribute BLEEDOUT_COUNTER_MAX = new RangedAttribute(null, "life.bleedoutCounterMax", 3, 0, Double.MAX_VALUE);
}