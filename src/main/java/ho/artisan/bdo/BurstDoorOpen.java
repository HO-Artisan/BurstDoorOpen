package ho.artisan.bdo;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(BurstDoorOpen.MODID)
public final class BurstDoorOpen {
    public static final String MODID = "bdo";
    private static final Logger LOGGER = LogUtils.getLogger();

    public BurstDoorOpen(FMLJavaModLoadingContext context) {
        LOGGER.info("It's a dream~");
        context.registerConfig(ModConfig.Type.COMMON, BurstDoorOpenConfig.COMMON_CONFIG);
        LOGGER.info("BDO has set up!");
    }
}
