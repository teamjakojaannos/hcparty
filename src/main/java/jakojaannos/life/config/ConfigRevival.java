package jakojaannos.life.config;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.RangeDouble;

public class ConfigRevival {

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Revival
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Comment("Adjust the revival behavior")
    public Revival revival = new Revival();

    public static class Revival {
        @Comment("Should bleedout damage ticks be suppressed when player is being revived")
        public boolean revivingPreventsBleedoutDamage = true;

        @Comment("Should the unconsciousness death timer be paused while the player is being revived")
        public boolean revivingPausesUnconsciousTimer = true;

        @Comment("Range in blocks players can be revived from")
        public float maxRevivalRange = 3.0f;

        @Comment("Radius of the hitbox that must be interacted with around the downed player")
        public float revivalAimRadius = 1.0f;


        @Comment("Base duration in ticks how long it takes to revive a player")
        public int baseRevivalDuration = 40;
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Bleedout
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Comment("Adjust the bleedout-time behavior")
    public Bleedout bleedout = new Bleedout();

    public static class Bleedout {
        @Comment("Amount of damage dealt to the player per damage instance when bleeding out")
        @RangeDouble(min = 0.0)
        public float damagePerInstance = 0.05f;

        @Comment("Number of ticks between damage instances when bleeding out")
        public int damageInterval = 1;

        @Comment("Minimum amount of damage dealt per bleedout damage instance after the resistances are applied")
        public float minDamage = 0.001f;

        @Comment("Minimum amount of damage dealt per bleedout damage instance after the resistances are applied")
        public float maxDamage = 99999.0f;


        @Comment({
                "Amount of damage resistance against bleedout damage. This is applied against all damage taken while",
                "bleeding out, including mob damage. Will be clamped to range [0.0, maxBleedoutResistance]"
        })
        @RangeDouble(min = 0.0, max = 1.0)
        public float defaultBleedoutResistance = 0.0f;

        @Comment("Maximum amount of damage resistance against bleedout damage.")
        @RangeDouble(min = 0.0, max = 1.0)
        public float maxBleedoutResistance = 0.95f;



        @Comment("Default health player has when bleeding out")
        public float health = 100.0f;

        @Comment("Maximum value player bleedout health can increased to by modifiers")
        public float maxHealth = Float.MAX_VALUE;

        @Comment("Maximum value player bleedout health can decreased to by modifiers")
        public float minHealth = 0.001f;

        @Comment("Default mob aggro mode for mobs that are added by mods that don't add special aggro handlers for LIFe")
        public MobAggroMode mobAggroMode = MobAggroMode.LowPriority;

        public enum MobAggroMode {
            /**
             * Mob completely ignores bleeding out players. Mob will never purposely aggro the player.
             */
            Ignore,

            /**
             * Mob ignores bleeding out players unless the player has attacked the mob.
             */
            IgnoreUnlessAttacked,

            /**
             * There is high chance that the mob switches target when the targeted player is downed and the chance
             * bleeding out players are chosen as a target is very low.
             */
            VeryLowPriority,

            /**
             * Mob may switch target when the targeted player is downed and the chance bleeding out players are chosen
             * as targets is low.
             */
            LowPriority,

            /**
             * There is high chance the mob will not switch target when the targeted player is downed and there is
             * chance the mob will change target towards a player if they enter bleedout nearby.
             */
            HighPriority,

            /**
             * Mob will not change the target when the targeted player is downed and is likely to systematically target
             * bleeding out players.
             */
            VeryHighPriority,

            /**
             * Mob treats bleeding out players as normal players.
             */
            None
        }
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Unconsciousness
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Comment("Settings for unconscious players")
    public Unconscious unconscious = new Unconscious();

    public static class Unconscious {
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Spawning Health
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Comment({
            "Use following settings to fine-tune revival health calculation. (the health value the revived player gets)",
            "",
            "Spawning health is calculated using following formula:",
            "(1)    scaledMaxHealth = recentMaxHealth + (maxHealth - recentMaxHealth) * scalingFactor",
            "(2)    scaled = scaledMaxHealth * maxHealthPercentage",
            "(3)    timeScale = timeScaleMin + (timeScaleMax - timeScaleMin) * percentageOfBleedoutHealthRemaining",
            "(4)    finalHealth = base + (scaled * timeScale * unconsciousPenalty)",
            "",
            "Explanation of the above:",
            " 1. scaled max health value is calculated as weighted average between recent maximum health and player's real",
            "    maximum health. Scaling factor is defined in configs and cannot be modified by player attributes.",
            " 2. max health -based health modifier is calculated by multiplying the scaled max health by player's max health",
            "    percentage modifier -attribute. The modifier limits can be set in configs, while the modifier value may be",
            "    unique for each player.",
            " 3. bleedout-health scaling is calculated using percentage of bleedout health remaining. More bleedout health",
            "    the player has remaining, higher the scaling factor will be. If player has fallen unconscious, the value",
            "    is set to the minimum.",
            " 4. Finally, the actual spawning health is determined. Base health is applied without any scaling, thus always",
            "    being absolute and unmodified. Time scaling factor and unconscious penalty are applied to the max-health",
            "    modifier and the result added to base health to determine the final spawning health. As the name suggests,",
            "    unconscious penalty is only applied if the player has fallen unconscious."
    })
    public SpawningHealth spawningHealth = new SpawningHealth();

    public static class SpawningHealth {
        @Comment("The base spawn health the player always gets, no matter what.")
        public float baseSpawnHealth = 1.0f;


        @Comment("Minimum limit for players' maximum health percentage attribute. Attribute modifiers cannot modify the percentage below this limit.")
        @RangeDouble(min = 0.0, max = 1.0)
        public float maxHealthPercentageMin = 0.0f;

        @Comment("Maximum limit for players' maximum health percentage attribute. Attribute modifiers cannot modify the percentage above this limit.")
        @RangeDouble(min = 0.0, max = 1.0)
        public float maxHealthPercentageMax = 1.0f;

        @Comment("The default value for maximum health percentage attribute. (The default percentage of maximum health the player will get when revived)")
        @RangeDouble(min = 0.0, max = 1.0)
        public float maxHealthPercentageDefault = 0.5f;

        @Comment({
                "Scaling factor between max health and recent max health when calculating spawning health. e.g the value of",
                "1.0 means that the modifier is calculated using recent max health, the value of 0.0 means that the modifier",
                "is calculated using real max health and the value of 0.5 means that the modifier is the average of the two"
        })
        @RangeDouble(min = 0.0, max = 1.0)
        public float recentHealthScalingFactor = 0.75f;


        @Comment("The minimum time-based multiplier value")
        @RangeDouble(min = 0.0, max = 1.0)
        public float timeScaleMin = 0.5f;

        @Comment("The maximum time-based multiplier value")
        @RangeDouble(min = 0.0, max = 1.0)
        public float timeScaleMax = 1.0f;

        @Comment({
                "The multiplier value used when player has fallen unconscious. Note that the minimum time scale is applied",
                "before this multiplier."
        })
        @RangeDouble(min = 0.0, max = 1.0)
        public float unconsciousMultiplier = 0.75f;
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Visuals/Cosmetic
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Comment("Should the bleeding out players' outlines glow trough walls")
    public boolean renderOutlines = true;

    @Comment("Should the vanilla death cause be printed to chat when entering bleedout. Set to 'false' for generic \"is now bleeding out\" message")
    public boolean sendVanillaDeathMessage = true;
}
