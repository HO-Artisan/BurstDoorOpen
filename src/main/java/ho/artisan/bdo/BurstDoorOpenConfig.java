package ho.artisan.bdo;

import net.minecraftforge.common.ForgeConfigSpec;

public class BurstDoorOpenConfig {
    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec.BooleanValue IRON_DOOR_BREAK_FLAG;

    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        COMMON_BUILDER.comment("General settings").push("general");
        IRON_DOOR_BREAK_FLAG = COMMON_BUILDER.comment("Test config value")
                .define("iron_doo_break_flag", () -> false);
        COMMON_BUILDER.pop();
        COMMON_CONFIG = COMMON_BUILDER.build();
    }
}
