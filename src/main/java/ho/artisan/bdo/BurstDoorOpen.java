package ho.artisan.bdo;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(BurstDoorOpen.MODID)
public final class BurstDoorOpen {
    public static final String MODID = "bdo";
    private static final Logger LOGGER = LogUtils.getLogger();

    public BurstDoorOpen() {
        // MinecraftForge.EVENT_BUS.addListener(BDOPlayerEvent::playerTick);
        MinecraftForge.EVENT_BUS.register(this);
        LOGGER.info("It's a dream~");

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BurstDoorOpenConfig.COMMON_CONFIG);
        LOGGER.info("BDO has set up!");
    }
}
