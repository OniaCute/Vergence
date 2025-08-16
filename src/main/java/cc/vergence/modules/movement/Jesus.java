package cc.vergence.modules.movement;

import cc.vergence.Vergence;
import cc.vergence.features.event.events.BlockCollisionEvent;
import cc.vergence.features.event.events.PacketEvent;
import cc.vergence.features.event.events.PlayerJumpEvent;
import cc.vergence.features.event.events.PlayerUpdateEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.injections.accessors.player.KeyBindingAccessor;
import cc.vergence.injections.accessors.player.PlayerMoveC2SPacketAccessor;
import cc.vergence.modules.Module;
import cc.vergence.modules.client.AntiCheat;
import cc.vergence.util.player.MovementUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.fluid.Fluids;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class Jesus extends Module {
    public static Jesus INSTANCE;
    private int timer = 1000;
    private boolean fluidState;
    private double floatOffset;

    public Jesus() {
        super("Jesus", Category.MOVEMENT);
        INSTANCE = this;
    }

    public Option<Enum<?>> mode = addOption(new EnumOption("Mode", Modes.Solid));
    public Option<Double> fallDistance = addOption(new DoubleOption("FallDistance", 0, 7, 3));
    public Option<Boolean> debug = addOption(new BooleanOption("Debug", true));

    @Override
    public String getDetails() {
        return debug.getValue() ? mode.getValue().name() + (AntiCheat.INSTANCE.isNCP() ? " | NCP" : "") : "";
    }

    @Override
    public void onBlockCollisionEvent(BlockCollisionEvent event, Block block, BlockPos pos, BlockState state, VoxelShape shape) {
        if (isNull() || mc.player.isSpectator() || mc.player.isOnFire() || state.getFluidState().isEmpty()) {
            return ;
        }
        if (mode.getValue().equals(Modes.Dolphin) && ((state.getBlock() == Blocks.WATER | state.getFluidState().getFluid() == Fluids.WATER) || state.getBlock() == Blocks.LAVA)) {
            event.cancel();
            event.setVoxelShape(VoxelShapes.cuboid(new Box(0.0, 0.0, 0.0, 1.0, 0.99, 1.0)));
            if (mc.player.getVehicle() != null) {
                event.setVoxelShape(VoxelShapes.cuboid(new Box(0.0, 0.0, 0.0, 1.0, 0.949999988079071, 1.0)));
            } else if (mode.getValue().equals(Modes.Jump)) {
                event.setVoxelShape(VoxelShapes.cuboid(new Box(0.0, 0.0, 0.0, 1.0, 0.96, 1.0)));
            }
        }
    }

    @Override
    public void onTick() {
        if (isNull()) {
            return ;
        }
        if (mode.getValue().equals(Modes.Solid)) {
//            if (isInFluid() || mc.player.fallDistance > 3.0f || mc.player.isSneaking()) {
//            }
            if (!mc.options.sneakKey.isPressed() && !mc.options.jumpKey.isPressed()) {
                if (isInFluid()) {
                    timer = 0;
                    MovementUtil.setMotionY(0.11);
                    return;
                }
                if (timer == 0) {
                    MovementUtil.setMotionY(0.30);
                } else if (timer == 1) {
                    MovementUtil.setMotionY(0.0);
                }
                timer++;
            }
        } else if (mode.getValue().equals(Modes.Dolphin) && isInFluid() && !mc.options.sneakKey.isPressed() && !mc.options.jumpKey.isPressed()) {
            KeyBinding.setKeyPressed(((KeyBindingAccessor) mc.options.jumpKey).getBoundKey(), true);
        }
    }

    @Override
    public void onPlayerUpdateEvent(PlayerUpdateEvent event) {
        if (isNull()) {
            return ;
        }

        if (event.isPre() && mode.getValue().equals(Modes.Jump)) {
            boolean inFluid = getFluidBlockInBB(mc.player.getBoundingBox()) != null;
            if (inFluid && !mc.player.isSneaking()) {
                mc.player.setOnGround(false);
            }
            Block block = mc.world.getBlockState(new BlockPos((int) Math.floor(mc.player.getX()), (int) Math.floor(mc.player.getY()), (int) Math.floor(mc.player.getZ()))).getBlock();
            if (fluidState && !mc.player.getAbilities().flying && !mc.player.isTouchingWater()) {
                if (mc.player.getVelocity().y < -0.3 || mc.player.isOnGround() || mc.player.isHoldingOntoLadder()) {
                    fluidState = false;
                    return ;
                }
                MovementUtil.setMotionY(mc.player.getVelocity().y / 0.9800000190734863 + 0.08);
                MovementUtil.setMotionY(mc.player.getVelocity().y - 0.03120000000005);
            }
            if (isInFluid()) {
                MovementUtil.setMotionY(0.1);
                fluidState = false;
                return ;
            }
            if (!isInFluid() && block instanceof FluidBlock && mc.player.getVelocity().y < 0.2) {
                MovementUtil.setMotionY(AntiCheat.INSTANCE.isNCP() ? 0.184 : 0.5);
                fluidState = true;
            }
        }
    }

    @Override
    public void onPlayerJump(PlayerJumpEvent event) {
        if (!isInFluid() && isOnFluid()) {
            event.cancel();
        }
    }

    @Override
    public void onSendPacket(PacketEvent.Send event, Packet<?> packet) {
        if (Vergence.NETWORK.isCached(packet) || mc.player == null || mc.getNetworkHandler() == null || mc.player.age <= 20) {
            return;
        }
        if (packet instanceof PlayerMoveC2SPacket packet1 && packet1.changesPosition() && mode.getValue().equals(Modes.Solid) && !isInFluid() && isOnFluid() && mc.player.fallDistance <= fallDistance.getValue()) {
            double y = packet1.getY(mc.player.getY());
            if (!AntiCheat.INSTANCE.isNCP()) {
                floatOffset = mc.player.age % 2 == 0 ? 0.0 : 0.05;
            }
            ((PlayerMoveC2SPacketAccessor) packet1).setX(y - floatOffset);
            if (AntiCheat.INSTANCE.isNCP()) {
                floatOffset += 0.12;
                if (floatOffset > 0.4) {
                    floatOffset = 0.2;
                }
            }
        }
    }

    public boolean isInFluid() {
        return mc.player.isTouchingWater() || mc.player.isInLava();
    }

    public BlockState getFluidBlockInBB(Box box) {
        return getFluidBlockInBB(MathHelper.floor(box.minY - 0.2));
    }

    public BlockState getFluidBlockInBB(int minY) {
        for (int i = MathHelper.floor(mc.player.getBoundingBox().minX); i < MathHelper.ceil(mc.player.getBoundingBox().maxX); i++) {
            for (int j = MathHelper.floor(mc.player.getBoundingBox().minZ); j < MathHelper.ceil(mc.player.getBoundingBox().maxZ); j++) {
                BlockState state = mc.world.getBlockState(new BlockPos(i, minY, j));
                if (state.getBlock() instanceof FluidBlock) {
                    return state;
                }
            }
        }
        return null;
    }

    public boolean isOnFluid() {
        if (mc.player.fallDistance >= fallDistance.getValue()) {
            return false;
        }
        final Box bb = mc.player.getVehicle() != null ? mc.player.getVehicle().getBoundingBox().contract(0.0, 0.0, 0.0).offset(0.0, -0.05000000074505806, 0.0) : mc.player.getBoundingBox().contract(0.0, 0.0, 0.0).offset(0.0, -0.05000000074505806, 0.0);
        boolean onLiquid = false;
        int y = (int) bb.minY;
        for (int x = MathHelper.floor(bb.minX); x < MathHelper.floor(bb.maxX + 1.0); x++) {
            for (int z = MathHelper.floor(bb.minZ); z < MathHelper.floor(bb.maxZ + 1.0); z++) {
                final Block block = mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block != Blocks.AIR) {
                    if (!(block instanceof FluidBlock)) {
                        return false;
                    }
                    onLiquid = true;
                }
            }
        }
        return onLiquid;
    }

    private enum Modes {
        Solid,
        Dolphin,
        Jump
    }
}
