package ho.artisan.bdo;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class BurstDoorOpenConfig {
    public static ModConfigSpec COMMON_CONFIG;
    public static ModConfigSpec.BooleanValue IRON_DOOR_BREAK_FLAG;

    static {
        ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();
        COMMON_BUILDER.comment("General settings").push("general");
        IRON_DOOR_BREAK_FLAG = COMMON_BUILDER.comment("Determines if iron doors can be broken by sprinting.")
                .define("iron_door_break_flag", () -> false);
        COMMON_BUILDER.pop();
        COMMON_CONFIG = COMMON_BUILDER.build();
    }
}
