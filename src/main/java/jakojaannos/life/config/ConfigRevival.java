package jakojaannos.life.config;

import net.minecraftforge.common.config.Config.Comment;

public class ConfigRevival {
    @Comment("Amount of damage dealt to the player per damage instance when bleeding out")
    public float bleedoutDamage = 0.5f;
    @Comment("Number of ticks between damage instances when bleeding out")
    public int bleedoutDamageInterval = 20;
    @Comment("Minimum amount of damage dealt per bleedout damage instance")
    public float minBleedoutDamage = 0.5f;

    @Comment("Maximum amount of damage resistance against bleedout damage")
    public float maxBleedoutResistance = 0.95f;

    @Comment("Default max health player has when bleeding out")
    public float bleedoutMaxHealth = 20.0f;


    @Comment("Should the bleeding out players' outlines glow trough walls")
    public boolean renderOutlines = true;

    @Comment("Should the vanilla death cause be printed to chat when entering bleedout. Set to 'false' for generic \"is now bleeding out\" message")
    public boolean sendVanillaDeathMessage = true;
}
