package ho.artisan.bdo;

import com.mojang.logging.LogUtils;
import ho.artisan.bdo.event.BDOPlayerEvent;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(BurstDoorOpen.MODID)
public class BurstDoorOpen {
    public static final String MODID = "bdo";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final TagKey<Block> DOORS;
    public static final TagKey<Block> WOODEN_DOORS;

    static {
        DOORS = TagKey.create(Registries.BLOCK, new ResourceLocation("doors"));
        WOODEN_DOORS = TagKey.create(Registries.BLOCK, new ResourceLocation("wooden_doors"));
    }

    public BurstDoorOpen() {
        MinecraftForge.EVENT_BUS.addListener(BDOPlayerEvent::playerTick);
        MinecraftForge.EVENT_BUS.register(this);
        LOGGER.info("It's a dream~");

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BurstDoorOpenConfig.COMMON_CONFIG);
        LOGGER.info("BDO has set up!");
    }
}
