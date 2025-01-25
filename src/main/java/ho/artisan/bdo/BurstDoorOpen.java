package ho.artisan.bdo;

import com.fizzware.dramaticdoors.fabric.tags.DDBlockTags;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BurstDoorOpen implements ModInitializer {
    public static final String MOD_ID = "bdo";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final boolean isDDLoaded = FabricLoader.getInstance().isModLoaded("dramaticdoors");

    public static void onPlayerTick(Player player) {
        Level level = player.level();
        if (!player.isSprinting()) return;
        BlockPos pos = getPlayerPOVHitResult(level, player).getBlockPos();
        BlockState state = level.getBlockState(pos);
        if (!state.is(BlockTags.DOORS) && !isDDDoors(state)) return;
        if (state.getValue(BlockStateProperties.OPEN)) return;
        var blockFace = state.getValue(DoorBlock.FACING);
        var newX = pos.getCenter().x() - blockFace.getStepX() * 0.5;
        var newZ = pos.getCenter().z() - blockFace.getStepZ() * 0.5;
        var doorTruePos = new Vec3(newX, pos.getCenter().y(), newZ);
        if (euclidean(doorTruePos, player.position()) > 0.65) return;
        if (state.is(BlockTags.WOODEN_DOORS) || isDDWoodenDoors(state)) {
            openDoor(level, pos, state, player, blockFace);
            level.playSound(null, pos, SoundEvents.ZOMBIE_ATTACK_WOODEN_DOOR, SoundSource.BLOCKS, 1.0F, 1.0F);
            return;
        }
        level.playSound(null, pos, SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.BLOCKS, 1.0F, 1.0F);
        if (!BurstDoorOpenConfig.IRON_DOOR_BREAK_FLAG.get()) return;
        openDoor(level, pos, state, player, blockFace);
    }

    private static void openDoor(Level level, BlockPos pos, BlockState state, Player player, Direction face) {
        state = state.cycle(DoorBlock.OPEN);
        level.setBlock(pos, state, 10);
        level.gameEvent(player, GameEvent.BLOCK_OPEN, pos);
        var side = state.getValue(DoorBlock.HINGE);
        var flag = side == DoorHingeSide.RIGHT;
        var reverse = (flag ? -1 : 1) * ((face == Direction.NORTH || face == Direction.SOUTH) ? -1 : 1);
        pos = pos.offset(face.getStepZ() * reverse, face.getStepY(), face.getStepX() * reverse);
        state = level.getBlockState(pos);
        if (!state.is(BlockTags.DOORS)) return;
        if (state.getValue(BlockStateProperties.OPEN)) return;
        if (state.getValue(DoorBlock.HINGE) != (flag ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT)) return;
        state = state.cycle(DoorBlock.OPEN);
        level.setBlock(pos, state, 10);
        level.gameEvent(player, GameEvent.BLOCK_OPEN, pos);
    }


    @SuppressWarnings("unused")
    private static double manhattanDistance(Vec3 pos1, Vec3 pos2) {
        double x = Math.abs(pos1.x - pos2.x);
        double z = Math.abs(pos1.z - pos2.z);
        return x + z;
    }

    private static double euclidean(Vec3 pos1, Vec3 pos2) {
        double x = Math.pow(pos2.x - pos1.x, 2);
        double z = Math.pow(pos2.z - pos1.z, 2);
        return Math.sqrt(x + z);
    }

    private static BlockHitResult getPlayerPOVHitResult(Level level, Player player) {
        float f = player.getXRot();
        float f1 = player.getYRot();
        Vec3 vec3 = player.getEyePosition();
        float f2 = Mth.cos(-f1 * 0.017453292F - 3.1415927F);
        float f3 = Mth.sin(-f1 * 0.017453292F - 3.1415927F);
        float f4 = -Mth.cos(-f * 0.017453292F);
        float f5 = Mth.sin(-f * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d0 = player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE);
        Vec3 vec31 = vec3.add((double) f6 * d0, (double) f5 * d0, (double) f7 * d0);
        return level.clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
    }

    public static boolean isDDDoors(BlockState state) {
        if (!isDDLoaded) return false;
        return state.is(DDBlockTags.TALL_DOORS) || state.is(DDBlockTags.SHORT_DOORS);
    }

    public static boolean isDDWoodenDoors(BlockState state) {
        if (!isDDLoaded) return false;
        return state.is(DDBlockTags.TALL_WOODEN_DOORS) || state.is(DDBlockTags.SHORT_WOODEN_DOORS);
    }

    @Override
    public void onInitialize() {
        LOGGER.info("It's a dream~");
        NeoForgeConfigRegistry.INSTANCE.register(MOD_ID, ModConfig.Type.COMMON, BurstDoorOpenConfig.COMMON_CONFIG);
        LOGGER.info("BDO has set up!");
    }
}