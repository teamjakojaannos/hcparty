package jakojaannos.life.config;

import jakojaannos.life.ModInfo;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;

@Config(modid = ModInfo.MODID)
public class ModConfig {
    @Comment("The revival module configuration. Includes settings for bleedout and unconsciousness systems.")
    public static ConfigRevival revival = new ConfigRevival();

    //@Comment("The resurrection module configuration. Includes settings for defining resurrection rituals.")
    //public static ConfigResurrect resurrect = new ConfigResurrect();
}
