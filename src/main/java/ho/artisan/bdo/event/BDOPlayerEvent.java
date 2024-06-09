package ho.artisan.bdo.event;

import ho.artisan.bdo.BurstDoorOpen;
import ho.artisan.bdo.BurstDoorOpenConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;

import java.io.IOException;

public class BDOPlayerEvent {
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        try(Level level = player.level()) {
            if (player.isSprinting()) {
                BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player);
                BlockPos pos = blockhitresult.getBlockPos();
                BlockState state = level.getBlockState(pos);
                if (state.is(BurstDoorOpen.DOORS) && player.position().distanceTo(pos.getCenter()) < 0.65D) {
                    if (!state.getValue(BlockStateProperties.OPEN)) {
                        if (state.is(BurstDoorOpen.WOODEN_DOORS)) {
                            state.setValue(BlockStateProperties.OPEN, true);
                            level.playSound(null, pos, SoundEvents.ZOMBIE_ATTACK_WOODEN_DOOR, SoundSource.BLOCKS, 1.0F, 1.0F);
                        }else {
                            if (BurstDoorOpenConfig.IRON_DOOR_BREAK_FLAG.get())
                                state.setValue(BlockStateProperties.OPEN, true);
                            level.playSound(null, pos, SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.BLOCKS, 1.0F, 1.0F);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static BlockHitResult getPlayerPOVHitResult(Level level, Player player) {
        float f = player.getXRot();
        float f1 = player.getYRot();
        Vec3 vec3 = player.getEyePosition();
        float f2 = Mth.cos(-f1 * 0.017453292F - 3.1415927F);
        float f3 = Mth.sin(-f1 * 0.017453292F - 3.1415927F);
        float f4 = -Mth.cos(-f * 0.017453292F);
        float f5 = Mth.sin(-f * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d0 = player.getBlockReach();
        Vec3 vec31 = vec3.add((double)f6 * d0, (double)f5 * d0, (double)f7 * d0);
        return level.clip(new ClipContext(vec3, vec31, net.minecraft.world.level.ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
    }
}
